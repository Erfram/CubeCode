package com.cubecode.client.imgui.basic;

import com.cubecode.CubeCodeClient;
import com.cubecode.api.utils.GsonManager;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.views.DashboardView;
import com.cubecode.client.views.EventsView;
import com.cubecode.client.views.SettingsView;
import com.cubecode.client.views.idea.DocumentationView;
import com.cubecode.client.views.idea.core.CubeCodeIDEAView;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.EventsRequestedPacket;
import com.cubecode.network.packets.all.IDEARequestedPacket;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowStateManager {
    private Map<Class<? extends View>, Runnable> windows = new HashMap<>();
    private Map<View, WindowData> sessionWindows = new HashMap<>();

    public WindowStateManager() {
        windows.put(CubeCodeIDEAView.class, () -> Dispatcher.sendToServer(new IDEARequestedPacket()));
        windows.put(EventsView.class, () -> Dispatcher.sendToServer(new EventsRequestedPacket()));
        windows.put(SettingsView.class, () -> ImGuiLoader.pushView(new SettingsView()));
        windows.put(DocumentationView.class, () -> ImGuiLoader.pushView(new DocumentationView()));
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

            windows.add(view.getClass().getName(), properties);
        }

        GsonManager.writeJSON(CubeCodeConfig.saveWindows.toFile(), windows);
    }

    public void loadWindowState() {
        JsonObject jsonObject = GsonManager.readJSON(CubeCodeConfig.saveWindows.toFile(), JsonObject.class);

        if (jsonObject.entrySet() != null) {
            jsonObject.entrySet().forEach((entry) -> {
                try {
                    String view = entry.getKey();

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
