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
import imgui.flag.ImGuiCol;
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

public class EventsView extends View {
    private final int viewWidth = 300;
    private final int viewHeight = 400;
    private final int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
    private final int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

    private List<CubeEvent> events = new ArrayList<>();
    private int selectedEvent = -1;

    private NbtList nbtEvents;

    public EventsView(NbtList events) {
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
                    ImGuiLoader.removeViews(ImGuiLoader.getView(EventSourceView.class), ImGuiLoader.getView(EventListView.class));
                })
                .callback(() -> {
                    ImGui.text("Используемые события");

                    ImGui.sameLine();

                    if (ImGui.imageButton(Icons.PLUS, 16, 16)) {
                        if (!ImGuiLoader.isOpenView(EventSourceView.class)) {
                            ImGuiLoader.pushView(new EventSourceView());
                        }
                    } else if (ImGui.isItemHovered()) {
                        ImGui.beginTooltip();
                        ImGui.text("Добавить событие");
                        ImGui.endTooltip();
                    }

                    CubeImGui.beginChild("Events", 0, 0, true, this::renderEvents);
                })
                .render(this);
    }

    public void renderEvents() {
        for (int i = 0; i < nbtEvents.size(); i++) {
            if (ImGui.selectable(Text.translatable("imgui.cubecode.windows.events."+nbtEvents.getString(i)+".title").getString(), selectedEvent == i, ImGuiSelectableFlags.None)) {
                selectedEvent = i;
            }

            if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
                selectedEvent = i;
                ImGui.openPopup("event_context_menu_" + i);
            }
        }

        runContextMenu();
    }

    public void runContextMenu() {
        if (ImGui.beginPopup("event_context_menu_" + selectedEvent)) {
            if (ImGui.menuItem("Редактировать")) {
                ImGuiLoader.pushView(new EventContentView(nbtEvents, selectedEvent));
            }

            if (ImGui.menuItem("Удалить")) {
                nbtEvents.remove(selectedEvent);
            }

            ImGui.endPopup();
        }
    }

    public static class EventContentView extends View {
        private final int viewWidth = 600;
        private final int viewHeight = 400;

        private final NbtList events;
        private final int index;

        public EventContentView(NbtList events, int index) {
            this.events = events;
            this.index = index;
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
            return String.format(Text.translatable("imgui.cubecode.windows.events." + events.getString(index) + ".title").getString()+"##%s", uniqueID);
        }

        @Override
        public void render() {
            Window.create()
                    .title(getName())
                    .callback(() -> {
                        EventListView.renderTooltipEvent(events.getString(index));
                    })
                    .render(this);
        }
    }

    public static class EventSourceView extends View {
        private final int viewWidth = 180;
        private final int viewHeight = 100;

        private int selectedSourceEvent = -1;
        private String sourceEvent = "";

        @Override
        public void init() {
            float posX = (windowWidth - viewWidth) * 0.5f;
            float posY = (windowHeight - viewHeight) * 0.5f;

            ImGui.setNextWindowPos(posX, posY);
            ImGui.setNextWindowSize(viewWidth, viewHeight);
        }

        @Override
        public String getName() {
            return String.format("Источники событий ##%s", uniqueID);
        }

        @Override
        public void render() {
            Window.create()
                    .flags(ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoDocking)
                    .title(getName())
                    .callback(() -> {
                        CubeImGui.beginChild("eventSource", 0, 0, true, this::renderEventsSource);
                    })
                    .render(this);
        }

        public void renderEventsSource() {
            for (int i = 0; i < CubeCode.eventManager.eventSource.size(); i++) {
                if (ImGui.selectable(CubeCode.eventManager.eventSource.get(i), selectedSourceEvent == i, ImGuiSelectableFlags.None)) {
                    selectedSourceEvent = i;
                    sourceEvent = CubeCode.eventManager.eventSource.get(i);

                    if (!ImGuiLoader.isOpenView(EventListView.class)) {
                        ImGuiLoader.pushView(new EventListView(sourceEvent));
                    }
                }
            }
        }
    }

    public static class EventListView extends View {
        private final int viewWidth = 475;
        private final int viewHeight = 250;

        private final String sourceEvent;

        private int selectedEvent = -1;
        private CubeEvent event = null;

        public EventListView(String sourceEvent) {
            this.sourceEvent = sourceEvent;
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
            return String.format("Доступные события ##%s", uniqueID);
        }

        @Override
        public void render() {
            Window.create()
                .title(getName())
                .callback(() -> {
                    if (sourceEvent.equals("CubeCode")) {
                        List<String> eventsName = CubeCode.eventManager.events.stream()
                                .map(event -> Text.translatable("imgui.cubecode.windows.events." + event.name + ".title").getString())
                                .toList();

                        ImVec2 maxTextSize  = ImGui.calcTextSize(eventsName.stream()
                                .max(Comparator.comparingInt(String::length))
                                .orElse(""));

                        EventsView eventsView = ImGuiLoader.getView(EventsView.class);

                        for (int i = 0; i < CubeCode.eventManager.events.size(); i++) {
                            CubeEvent currentEvent = CubeCode.eventManager.events.get(i);
                            String eventTitle = Text.translatable("imgui.cubecode.windows.events." + currentEvent.name + ".title").getString();

                            if (eventsView != null && NbtUtils.containsString(eventsView.nbtEvents, CubeCode.eventManager.events.get(i).name)) {
                                ImGui.pushStyleColor(ImGuiCol.Text, 0.4f, 0.4f, 0.4f, 1f);
                            }
                            if (ImGui.selectable(eventTitle, selectedEvent == i, ImGuiSelectableFlags.None, maxTextSize.x, 20)) {
                                selectedEvent = i;
                                event = currentEvent;
                            }
                            if (eventsView != null && NbtUtils.containsString(eventsView.nbtEvents, CubeCode.eventManager.events.get(i).name)) {
                                ImGui.popStyleColor();
                            }

                            handleRightClickMenu(i);
                            renderQuestionButton(i);
                        }

                        runEventContextMenu();
                    }
                }).render(this);
        }

        private void handleRightClickMenu(int index) {
            if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
                selectedEvent = index;
                ImGui.openPopup("event_list_context_menu_" + index);
            }
        }

        private void renderQuestionButton(int index) {
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
            CubeImGui.gif(String.format("https://github.com/Erfram/CubeCode/blob/main/gifs/%s.gif?raw=true", eventName), 512, 256);
        }

        private static void renderTooltipText(String key) {
            CubeImGui.mutableText(TextUtils.formatText(Text.translatable(key).getString()));
        }

        public void runEventContextMenu() {
            if (ImGui.beginPopup("event_list_context_menu_" + selectedEvent)) {
                EventsView eventsView = ImGuiLoader.getView(EventsView.class);
                if (eventsView != null && !NbtUtils.containsString(eventsView.nbtEvents, CubeCode.eventManager.events.get(selectedEvent).name)) {
                    if (ImGui.menuItem("Добавить")) {
                        eventsView.nbtEvents.add(NbtString.of(CubeCode.eventManager.events.get(selectedEvent).name));
                    }
                }
                ImGui.endPopup();
            }
        }
    }
}