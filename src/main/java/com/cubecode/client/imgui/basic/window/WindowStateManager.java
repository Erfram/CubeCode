package com.cubecode.client.imgui.basic.window;

import com.cubecode.CubeCodeClient;
import com.cubecode.client.views.ide.DocumentationView;
import com.cubecode.utils.Documentation;
import com.cubecode.utils.GsonManager;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.views.DashboardView;
import com.cubecode.client.views.EventsView;
import com.cubecode.client.views.SettingsView;
import com.cubecode.client.views.ide.core.CubeCodeIDEView;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.EventsRequestedPacket;
import com.cubecode.network.packets.all.IDERequestedPacket;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.internal.ImGuiDockNode;

import java.util.HashMap;
import java.util.Map;

public class WindowStateManager {
    private Map<Class<? extends View>, Runnable> windows = new HashMap<>();
    private Map<View, WindowData> sessionWindows = new HashMap<>();

    public WindowStateManager() {
        windows.put(CubeCodeIDEView.class, () -> Dispatcher.sendToServer(new IDERequestedPacket()));
        windows.put(EventsView.class, () -> Dispatcher.sendToServer(new EventsRequestedPacket()));
        windows.put(SettingsView.class, () -> ImGuiLoader.pushView(new SettingsView()));
        windows.put(DocumentationView.class, () -> ImGuiLoader.pushView(new DocumentationView(Documentation.parseDocs())));
    }

    public Map<View, WindowData> getSessionWindows() {
        return sessionWindows;
    }

    public WindowData getSessionWindowData(View view) {
        return sessionWindows.get(view);
    }

    public void addSessionWindow(View view, WindowData windowData) {
        sessionWindows.put(view, windowData);
    }

    public void removeSessionWindow(View view) {
        sessionWindows.remove(view);
    }

    public void clearSessionWindows() {
        sessionWindows.clear();
    }

    public Map<Class<? extends View>, Runnable> getWindows() {
        return windows;
    }

    public boolean hasWindow(Class<? extends View> view) {
        for (Class<? extends View> clazz : windows.keySet().stream().toList()) {
            if (clazz.isAssignableFrom(view)) {
                windows.remove(view);
                return true;
            }
        }

        return false;
    }

    public void saveWindowState() {
        JsonObject windows = new JsonObject();

        for (View view : ImGuiLoader.getViews()) {
            if (view instanceof DashboardView)
                continue;

            JsonObject properties = new JsonObject();

            JsonArray position = new JsonArray();

            position.add(view.windowPos.x);
            position.add(view.windowPos.y);

            JsonArray size = new JsonArray();

            size.add(view.windowSize.x);
            size.add(view.windowSize.y);
            properties.add("position", position);
            properties.add("size", size);
            properties.addProperty("collapsed", view.windowCollapsed);

//            int dockId = ImGui.getID(view.getName());
//            ImGui.dockSpace(dockId, 0, 0, ImGuiDockNodeFlags.None);
//            if (dockId != 0) {
//                ImGuiDockNode node = imgui.internal.ImGui.dockBuilderGetNode(dockId);
//                if (node != null && node.ptr != 0) {
//                    JsonObject dockingInfo = new JsonObject();
//                    dockingInfo.addProperty("isDocked", true);
//
//                    ImGuiDockNode parentNode = node.getParentNode();
//                    if (parentNode != null) {
//                        dockingInfo.addProperty("parentId", String.valueOf(parentNode.getID()));
//                        dockingInfo.addProperty("dockDir", getDockDirection(node));
//                        float ratio = node.getSizeRef().x / (parentNode.getSizeRef().x + parentNode.getSizeRef().y);
//                        dockingInfo.addProperty("dockRatio", ratio);
//                    }
//
//                    properties.add("docking", dockingInfo);
//                }
//            }

            windows.add(view.getClass().getName() + "#" + view.hashCode(), properties);
        }

        GsonManager.writeJSON(CubeCodeConfig.saveWindows.toFile(), windows);
    }

    private String getDockDirection(ImGuiDockNode node) {
        ImGuiDockNode parent = node.getParentNode();
        if (parent != null) {
            ImVec2 nodePos = node.getPos();
            ImVec2 parentPos = parent.getPos();

            if (nodePos.x < parentPos.x) return "LEFT";
            if (nodePos.x > parentPos.x) return "RIGHT";
            if (nodePos.y < parentPos.y) return "UP";
            if (nodePos.y > parentPos.y) return "DOWN";
        }
        return "NONE";
    }

    public void loadWindowState() {
        JsonObject jsonObject = GsonManager.readJSON(CubeCodeConfig.saveWindows.toFile(), JsonObject.class);

        if (jsonObject != null) {
            jsonObject.entrySet().forEach((entry) -> {
                try {
                    String view = entry.getKey().split("#")[0];

                    Runnable viewRunnable = CubeCodeClient.windowStateManager.getWindows().get(Class.forName(view));

                    if (viewRunnable != null) {
                        viewRunnable.run();
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void loadSessionWindowState() {
        sessionWindows.keySet().forEach(ImGuiLoader::pushView);
    }

    public void applyWindowState(Class<? extends View> view) {
        JsonObject jsonObject = GsonManager.readJSON(CubeCodeConfig.saveWindows.toFile(), JsonObject.class);

        JsonObject properties = jsonObject.getAsJsonObject(view.getName());

        if (properties != null) {
            JsonArray pos = properties.getAsJsonArray("position");
            JsonArray size = properties.getAsJsonArray("size");
            boolean collapsed = properties.get("collapsed").getAsBoolean();

            ImGui.setNextWindowPos(pos.get(0).getAsFloat(), pos.get(1).getAsFloat());
            ImGui.setNextWindowSize(size.get(0).getAsFloat(), size.get(1).getAsFloat());
            ImGui.setNextWindowCollapsed(collapsed);
        }
    }
}
