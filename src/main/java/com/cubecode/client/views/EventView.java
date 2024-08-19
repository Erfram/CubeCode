package com.cubecode.client.views;

import com.cubecode.CubeCode;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.server.EventsSyncC2SPacket;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
                    Dispatcher.sendToServer(new EventsSyncC2SPacket(nbtEvents));
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

        for (int i = 0; i < nbtEvents.size(); i++) {
            List<String> list = CubeCode.eventManager.events.stream()
                    .map(event -> Text.translatable("imgui.cubecode.windows.events." + event.name + ".title").getString())
                    .toList();

            String eventId = CubeCode.eventManager.events.get(list.indexOf(nbtEvents.getString(i))).name;

            if (ImGui.selectable(Text.translatable("imgui.cubecode.windows.events." + eventId + ".title").getString(), selectedEvent == i, ImGuiSelectableFlags.AllowDoubleClick, maxTextSize.x, 20)) {
                selectedEvent = i;

                if (ImGui.isMouseDoubleClicked(ImGuiMouseButton.Left)) {
                    nbtEvents.remove(i);
                }
            }

            renderQuestionButton(eventId);
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
        CubeImGui.gif(
                String.format("https://github.com/Erfram/CubeCode/blob/main/gifs/%s.gif?raw=true", eventName),
                256, 256
        );
    }

    private static void renderTooltipText(String key) {
        CubeImGui.mutableText(TextUtils.formatText(Text.translatable(key).getString()));
    }

    public static class EventListView extends View {
        final int viewWidth = 475;
        final int viewHeight = 250;

        ImVec2 mousePos;

        int selectedEvent = -1;

        private String searchText = "";

        List<String> eventsName = CubeCode.eventManager.events.stream()
                .map(event -> Text.translatable("imgui.cubecode.windows.events." + event.name + ".title").getString())
                .collect(Collectors.toList());

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

        private void updateEventsList() {
            eventsName = CubeCode.eventManager.events.stream()
                    .map(event -> Text.translatable("imgui.cubecode.windows.events."+event.name+".title").getString())
                    .toList();
        }

        private void filterEvents(String searchEvent) {
            if (!searchEvent.isEmpty()) {
                eventsName = eventsName.stream()
                        .filter(event -> {
                            String[] eventNameSeparator = event.split(":");
                            return Arrays.stream(eventNameSeparator)
                                    .anyMatch(word -> word.toLowerCase().startsWith(searchEvent.toLowerCase()))
                                    || event.toLowerCase().contains(searchEvent.toLowerCase());
                        })
                        .toList();
            } else {
                updateEventsList();
            }
        }

        @Override
        public void render() {
            Window.create()
                .flags(ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove)
                .title(getName())
                .callback(this::renderContent)
                .render(this);
        }

        private void renderContent() {
            if (!ImGui.isWindowFocused()) {
                ImGuiLoader.removeView(this);
                return;
            }

            renderSearchBar();
            renderEventList();
        }

        private void renderSearchBar() {
            ImGui.image(Icons.SEARCH, 16, 16);
            ImGui.sameLine();

            CubeImGui.inputText(this, "##Search", (newSearchText) -> {
                if (!newSearchText.equals(searchText)) {
                    searchText = newSearchText;
                    filterEvents(searchText);
                }
            });
        }

        private void renderEventList() {
            ImVec2 maxTextSize = ImGui.calcTextSize(eventsName.stream()
                    .max(Comparator.comparingInt(String::length))
                    .orElse(""));

            EventView eventView = ImGuiLoader.getView(EventView.class);
            if (eventView == null) return;

            for (int i = 0; i < eventsName.size(); i++) {
                String eventName = eventsName.get(i);
                if (ImGui.selectable(eventName, selectedEvent == i, ImGuiSelectableFlags.None, maxTextSize.x, 20)) {
                    selectedEvent = i;
                    if (!NbtUtils.containsString(eventView.nbtEvents, eventName)) {
                        eventView.nbtEvents.add(NbtString.of(eventName));
                    }
                }

                List<String> list = CubeCode.eventManager.events.stream()
                        .map(event -> Text.translatable("imgui.cubecode.windows.events." + event.name + ".title").getString())
                        .toList();

                renderQuestionButton(list.indexOf(eventName));
            }
        }
    }
}