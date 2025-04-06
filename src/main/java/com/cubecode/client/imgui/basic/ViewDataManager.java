package com.cubecode.client.imgui.basic;

import com.cubecode.CubeCodeClient;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.views.EventsView;
import com.cubecode.client.views.SettingsView;
import com.cubecode.client.views.StatesView;
import com.cubecode.client.views.ide.core.CubeCodeIDEView;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.EventsRequestedPacket;
import com.cubecode.network.packets.all.IDENodeRequestPacket;
import com.cubecode.network.packets.all.StatesRequestedPacket;
import com.cubecode.utils.CubeCodeException;
import com.cubecode.utils.GsonManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import imgui.ImGui;
import org.joml.Vector2f;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class ViewDataManager {
    private final Map<String, ViewData> viewsData = new HashMap<>();
    private final Map<Class<? extends View>, Runnable> viewsRunnable = new HashMap<>();
    private final Set<View> sessionViews = new HashSet<>();

    public File fileDataViews = CubeCodeConfig.configDir.resolve("data_views.json").toFile();

    public ViewDataManager() {
        this.viewsRunnable.put(CubeCodeIDEView.class, () -> Dispatcher.sendToServer(new IDENodeRequestPacket()));
        this.viewsRunnable.put(EventsView.class, () -> Dispatcher.sendToServer(new EventsRequestedPacket()));
        this.viewsRunnable.put(StatesView.class, () -> Dispatcher.sendToServer(new StatesRequestedPacket()));
        this.viewsRunnable.put(SettingsView.class, () -> ImGuiLoader.pushView(new SettingsView()));

        if (fileDataViews.exists() && GsonManager.isValidJSON(fileDataViews)) {
            try {
                JsonParser jsonParser = new JsonParser();
                JsonObject views = GsonManager.readJSON(fileDataViews, JsonObject.class);

                for (String viewId : views.keySet()) {
                    this.viewsData.put(viewId, GsonManager.readJSON(views.get(viewId).getAsJsonObject().toString(), ViewData.class));
                }
            } catch (JsonSyntaxException e) {
                CubeCodeClient.LOGGER.error("data_views.json - incorrect syntax.", e);
            }
        } else {
            try {
                fileDataViews.createNewFile();

                Files.writeString(fileDataViews.toPath(), "{\n\n}");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Runnable getRunnableView(Class<? extends View> viewClass) {
        return this.viewsRunnable.get(viewClass);
    }

    public Map<String, ViewData> getViewsData() {
        return this.viewsData;
    }

    public ViewData getViewData(String viewId) {
        return this.viewsData.get(viewId);
    }

    public UUID getViewUUID(String viewPackage) throws CubeCodeException {
        for (String viewId : this.viewsData.keySet()) {
            if (viewId.startsWith(viewPackage)) {
                return UUID.fromString(viewId.substring(viewPackage.length()+1));
            }
        }

        throw new CubeCodeException("[ViewDataManager] There is no window under package in json:" + viewPackage);
    }

    public void clearViewsData() {
        this.viewsData.clear();

        try {
            Files.writeString(fileDataViews.toPath(), "{\n\n}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addViewData(String viewId, ViewData viewData) {
        this.viewsData.put(viewId, viewData);

        JsonObject jsonViewsData = GsonManager.readJSON(fileDataViews, JsonObject.class);

        if (jsonViewsData == null)
            return;

        JsonObject jsonViewData = new JsonObject();

        JsonObject pos = new JsonObject();

        pos.addProperty("x", viewData.pos.x);
        pos.addProperty("y", viewData.pos.y);

        JsonObject size = new JsonObject();

        size.addProperty("x", viewData.size.x);
        size.addProperty("y", viewData.size.y);

        jsonViewData.add("pos", pos);
        jsonViewData.add("size", size);
        jsonViewData.addProperty("collapsed", viewData.collapsed);
        jsonViewData.add("data", viewData.data);

        jsonViewsData.add(viewId, jsonViewData);

        GsonManager.writeJSON(fileDataViews, jsonViewsData);
    }

    public void addSessionView(View view) {
        this.sessionViews.add(view);
    }

    public void addAllSessionView(List<View> views) {
        this.sessionViews.addAll(views);
    }

    public void loadSessionViews() {
        for (View sessionView : this.sessionViews) {
            ImGuiLoader.pushView(sessionView);

            ImGui.setNextWindowPos(sessionView.windowPos.x, sessionView.windowPos.y);
            ImGui.setNextWindowSize(sessionView.windowSize.x, sessionView.windowSize.y);
            ImGui.setNextWindowCollapsed(sessionView.windowCollapsed);
        }
    }

    public static class ViewData {
        Vector2f pos;
        Vector2f size;
        boolean collapsed;
        JsonObject data;

        public ViewData(float posX, float posY, float sizeX, float sizeY, boolean collapsed, JsonObject data) {
            this.pos = new Vector2f(posX, posY);
            this.size = new Vector2f(sizeX, sizeY);
            this.collapsed = collapsed;
            this.data = data;
        }
    }
}
