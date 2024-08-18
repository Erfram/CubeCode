package com.cubecode.client.views;

import com.cubecode.CubeCode;
import com.cubecode.api.events.CubeEvent;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.utils.Icons;
import com.cubecode.utils.NbtUtils;
import com.cubecode.utils.TextUtils;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiSelectableFlags;
import imgui.flag.ImGuiWindowFlags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EventView extends View {
    final int viewWidth = 300;
    final int viewHeight = 400;

    final int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
    final int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

    NbtList nbtEvents;

    int selectedEvent = -1;

    public EventView(NbtList events) {
        this.nbtEvents = events;
    }

    @Override
    public void init() {
        float posX = (windowWidth - viewWidth) * 0.5f;
        float posY = (windowHeight - viewHeight) * 0.5f;

        ImGui.setNextWindowPos(posX, posY);
        ImGui.setNextWindowSize(viewWidth, viewHeight);
    }

    @Override
    public String getName() {
        return String.format(Text.translatable("imgui.cubecode.windows.events.title").getString()+" ##%s", uniqueID);
    }

    @Override
    public void render() {
        Window.create()
                .title(getName())
                .onExit(() -> {
                    ImGuiLoader.removeView(ImGuiLoader.getView(EventListView.class));
                })
                .callback(() -> {
                    ImGui.text("Используемые события");

                    CubeImGui.beginChild("events", 0 ,0, true, this::renderUsedEvents);

                    if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
                        ImGuiLoader.pushView(new EventListView(ImGui.getMousePos()));
                    }
                })
                .render(this);
    }

    public void renderUsedEvents() {
        List<String> eventsName = new ArrayList<>();

        nbtEvents.forEach((element) -> {
            eventsName.add(Text.translatable(element.asString()).getString());
        });

        ImVec2 maxTextSize  = ImGui.calcTextSize(eventsName.stream()
                .max(Comparator.comparingInt(String::length))
                .orElse(""));

        for (int i = 0; i < eventsName.size(); i++) {
            if (ImGui.selectable(eventsName.get(i), selectedEvent == i, ImGuiSelectableFlags.AllowDoubleClick, maxTextSize.x, 20)) {
                selectedEvent = i;

                if (ImGui.isMouseDoubleClicked(ImGuiMouseButton.Left)) {
                    nbtEvents.remove(i);
                }
            }

            renderQuestionButton(i);
        }
    }

    private static void renderQuestionButton(int index) {
        ImGui.sameLine();
        ImGui.pushID("question_" + index);

        ImGui.getStyle().setWindowRounding(0);
        if (!ImGui.imageButton(Icons.QUESTION, 16, 16) && ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            renderTooltipEvent(index);
            ImGui.endTooltip();
        }

        ImGui.popID();
    }

    private static void renderQuestionButton(String eventName) {
        ImGui.sameLine();
        ImGui.pushID("question_" + eventName);

        ImGui.getStyle().setWindowRounding(0);
        if (!ImGui.imageButton(Icons.QUESTION, 16, 16) && ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            renderTooltipEvent(eventName);
            ImGui.endTooltip();
        }

        ImGui.popID();
    }

    private static void renderTooltipEvent(int index) {
        renderTooltipEvent(CubeCode.eventManager.events.get(index).name);
    }

    private static void renderTooltipEvent(String eventName) {
        ImGui.textColored(255, 207, 64, 255, Text.translatable("imgui.cubecode.windows.events." + eventName + ".title").getString());
        ImGui.separator();
        renderTooltipText("imgui.cubecode.windows.events." + eventName + ".description");
        ImGui.separator();
        renderTooltipText("imgui.cubecode.windows.events." + eventName + ".variables");
        ImGui.separator();
        CubeImGui.gif(String.format("https://github.com/Erfram/CubeCode/blob/main/gifs/%s.gif?raw=true", eventName), 256, 256);
    }

    private static void renderTooltipText(String key) {
        CubeImGui.mutableText(TextUtils.formatText(Text.translatable(key).getString()));
    }

    public static class EventListView extends View {
        final int viewWidth = 475;
        final int viewHeight = 250;

        ImVec2 mousePos;

        int selectedEvent = -1;

        public EventListView(ImVec2 mousePos) {
            this.mousePos = mousePos;
        }

        @Override
        public void init() {
            ImGui.setNextWindowPos(mousePos.x, mousePos.y);
            ImGui.setNextWindowSize(viewWidth, viewHeight);
        }

        @Override
        public String getName() {
            return String.format("Доступные события ##%s", uniqueID);
        }

        List<String> eventsName = CubeCode.eventManager.events.stream()
                .map(event -> Text.translatable("imgui.cubecode.windows.events." + event.name + ".title").getString())
                .toList();

        @Override
        public void render() {
            Window.create()
                .flags(ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove)
                .title(getName())
                .callback(() -> {
                    if (!ImGui.isWindowFocused()) {
                        ImGuiLoader.removeView(this);
                    }

                    ImVec2 maxTextSize  = ImGui.calcTextSize(eventsName.stream()
                            .max(Comparator.comparingInt(String::length))
                            .orElse(""));


                    EventView eventView = ImGuiLoader.getView(EventView.class);

                    if (eventView == null)
                        return;

                    ImGui.image(Icons.SEARCH, 16, 16);
                    ImGui.sameLine();

                    CubeImGui.inputText(this, "##Search", (searchEvent) -> {
                        if (!searchEvent.isEmpty()) {
                            eventsName = CubeCode.eventManager.events.stream()
                                    .map(event -> Text.translatable("imgui.cubecode.windows.events." + event.name + ".title").getString())
                                    .filter(event -> {
                                String[] eventNameSeparator = event.split(":");

                                for (String word : eventNameSeparator) {
                                    if (word.toLowerCase().startsWith(searchEvent.toLowerCase())) {
                                        return true;
                                    }
                                }

                                return event.toLowerCase().contains(searchEvent.toLowerCase());
                            }).toList();
                        } else {
                            eventsName = CubeCode.eventManager.events.stream()
                                    .map(event -> Text.translatable("imgui.cubecode.windows.events." + event.name + ".title").getString())
                                    .toList();
                        }
                    });

                    for (int i = 0; i < eventsName.size(); i++) {
                        if (ImGui.selectable(eventsName.get(i), selectedEvent == i, ImGuiSelectableFlags.None, maxTextSize.x, 20)) {
                            selectedEvent = i;

                            if (!NbtUtils.containsString(eventView.nbtEvents, eventsName.get(i))) {
                                eventView.nbtEvents.add(NbtString.of(eventsName.get(i)));
                            }
                        }

                        renderQuestionButton(eventsName.get(i).substring(eventsName.get(i).indexOf("events.") + 7, eventsName.get(i).indexOf(".title")));
                    }
                })
                .render(this);
        }
    }
}