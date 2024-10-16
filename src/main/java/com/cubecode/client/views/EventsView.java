package com.cubecode.client.views;

import com.cubecode.api.events.CubeEvent;
import com.cubecode.api.events.EventManager;
import com.cubecode.api.scripts.Script;
import com.cubecode.client.gifs.GifManager;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.server.EventsSyncC2SPacket;
import com.cubecode.utils.TextUtils;
import imgui.ImGui;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImInt;
import imgui.type.ImString;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedList;
import java.util.List;

public class EventsView extends View {
    List<CubeEvent> events;
    List<Script> scripts;
    LinkedList<String> scriptNames;

    int selectedScript = -1;
    int selectedEvent = -1;

    public EventsView(List<CubeEvent> events, List<Script> scripts) {
        this.events = events;
        this.scripts = scripts;

        this.scriptNames = new LinkedList<>();

        this.scripts.forEach(script -> {
            this.scriptNames.add(script.name);
        });
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public String getName() {
        return String.format(Text.translatable("imgui.cubecode.windows.events.title").getString()+" ##%s", uniqueID);
    }

    @Override
    public void render() {
        Window.create()
                .title(getName())
                .onExit(this::onClose)
                .callback(() -> {
                    CubeImGui.beginChild("events", ImGui.getWindowWidth() * 0.3f ,0, true, this::renderEvents);

                    ImGui.sameLine();

                    CubeImGui.beginChild("configuration event", 0, 0, false, this::renderContent);
                })
                .render(this);
    }

    @Override
    public void onClose() {
        GifManager.clear();
        Dispatcher.sendToServer(new EventsSyncC2SPacket(EventManager.cubeEventsToNbtList(events)));
    }

    public void renderEvents() {
        for (int i = 0; i < events.size(); i++) {
            CubeEvent event = events.get(i);
            String eventTitle = Text.translatable("imgui.cubecode.windows.events." + event.name + ".title").getString();

            MutableText mutableEventTitle = TextUtils.formatText(event.getScripts().isEmpty() ? eventTitle : "(&6"+ event.getScripts().size() +"&r) " + eventTitle);

            boolean treeNodeOpen = ImGui.treeNodeEx("##"+eventTitle, ImGuiTreeNodeFlags.SpanAvailWidth);

            renderTooltip(event.name);

            ImGui.sameLine();
            CubeImGui.textMutable(mutableEventTitle);
            renderCreateScript(i);

            if (treeNodeOpen) {
                runContextCreateScript();
                selectedEvent = i;
                for (int s = 0; s < event.getScripts().size(); s++) {
                    if (ImGui.selectable(event.getScripts().get(s).name, selectedScript == s)) {
                        selectedScript = s;
                        currentScript.set(scriptNames.indexOf(event.getScripts().get(selectedScript).name));
                        currentFunction.set(events.get(selectedEvent).getScripts().get(selectedScript).function);
                    }

                    if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
                        selectedScript = s;
                        ImGui.openPopup("delete_script");
                    }
                }
                runContextDeleteScript();
                ImGui.treePop();
            }
        }
        runContextCreateScript();
    }

    public void renderTooltip(String eventName) {
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.textColored(255, 207, 64, 255, Text.translatable("imgui.cubecode.windows.events." + eventName + ".title").getString());

            if (ImGui.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || ImGui.isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                ImGui.separator();
                renderTooltipText("imgui.cubecode.windows.events." + eventName + ".description");
                ImGui.separator();
                renderTooltipText("imgui.cubecode.windows.events." + eventName + ".variables");
                ImGui.separator();
                CubeImGui.gif(
                        String.format("https://github.com/Erfram/CubeCode/tree/1.20.4/gifs/%s.gif?raw=true", eventName),
                        256, 256
                );
            }
            ImGui.endTooltip();
        }
    }

    private static void renderTooltipText(String key) {
        CubeImGui.mutableText(TextUtils.formatText(Text.translatable(key).getString()));
    }

    int eventIndex = -1;

    public void renderCreateScript(int index) {
        if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
            eventIndex = index;
            System.out.println("1");
            ImGui.openPopup("create_script");
            System.out.println(index);
        }
    }

    public void runContextDeleteScript() {
        if (ImGui.beginPopup("delete_script")) {
            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.events.deleteScript").getString())) {
                events.get(selectedEvent).getScripts().remove(selectedScript);
                selectedScript = -1;
            }

            ImGui.endPopup();
        }
    }

    public void runContextCreateScript() {
        if (ImGui.beginPopup("create_script")) {
            if (ImGui.menuItem(Text.translatable("imgui.cubecode.windows.events.createScript").getString())) {
                if (eventIndex != -1) {
                    events.get(eventIndex).addScript(new CubeEvent.EventScript("Скрипт"));
                }
            }

            ImGui.endPopup();
        }
    }

    private final ImInt currentScript = new ImInt(0);
    private final ImString currentFunction = new ImString();

    public void renderContent() {
        if (selectedScript != -1) {
            ImGui.text(Text.translatable("imgui.cubecode.windows.events.selectionScript").getString()+": ");
            ImGui.sameLine();
            if (ImGui.combo("##Scripts", currentScript, scriptNames.toArray(new String[0]))) {
                CubeEvent.EventScript eventScript = events.get(selectedEvent).getScripts().get(selectedScript);

                eventScript.name = scriptNames.get(currentScript.get());

                events.get(selectedEvent).getScripts().set(selectedScript, eventScript);
            }

            ImGui.text(Text.translatable("imgui.cubecode.windows.events.function").getString()+": ");
            ImGui.sameLine();

            if (ImGui.inputText("##function", currentFunction)) {
                CubeEvent.EventScript eventScript = events.get(selectedEvent).getScripts().get(selectedScript);

                eventScript.function = currentFunction.get();

                events.get(selectedEvent).getScripts().set(selectedScript, eventScript);
            }
        }
    }
}
