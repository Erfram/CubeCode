package com.cubecode.client.views;

import com.cubecode.api.events.CubeEvent;
import com.cubecode.api.events.EventManager;
import com.cubecode.api.scripts.Script;
import com.cubecode.client.gifs.GifManager;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.client.views.textEditor.RenameScriptView;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.server.DeleteScriptC2SPacket;
import com.cubecode.network.packets.server.EventsSyncC2SPacket;
import com.cubecode.utils.Icons;
import com.cubecode.utils.TextUtils;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.*;
import imgui.type.ImInt;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;
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

    List<CubeEvent> usedEvents;
    List<CubeEvent> events;
    List<Script> scripts;

    int selectedEvent = -1;

    public EventView(List<CubeEvent> usedEvents, List<CubeEvent> events, List<Script> scripts) {
        this.usedEvents = usedEvents;
        this.events = events;
        this.scripts = scripts;
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
                .onExit(this::onClose)
                .callback(() -> {
                    ImGui.text("Используемые события");

                    CubeImGui.beginChild("events", 0 ,0, true, this::renderUsedEvents);

                    if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
                        ImGuiLoader.pushView(new EventListView(events));
                    }
                })
                .render(this);
    }

    @Override
    public void onClose() {
        GifManager.clear();
        Dispatcher.sendToServer(new EventsSyncC2SPacket(EventManager.cubeEventsToNbtList(usedEvents)));
        ImGuiLoader.removeView(ImGuiLoader.getView(EventListView.class));
    }

    public void renderUsedEvents() {
        List<String> eventsName = new ArrayList<>();

        usedEvents.forEach((element) -> {
            eventsName.add(Text.translatable("imgui.cubecode.windows.events."+ element.name +".title").getString());
        });

        ImVec2 maxTextSize  = ImGui.calcTextSize(eventsName.stream()
                .max(Comparator.comparingInt(String::length))
                .orElse(""));

        for (int i = 0; i < usedEvents.size(); i++) {
            if (ImGui.selectable(Text.translatable("imgui.cubecode.windows.events." + usedEvents.get(i).name + ".title").getString(), selectedEvent == i, ImGuiSelectableFlags.AllowDoubleClick, maxTextSize.x, 20)) {
                selectedEvent = i;

                if (ImGui.isMouseDoubleClicked(ImGuiMouseButton.Left)) {
                    usedEvents.remove(i);
                }
            }

            String name = "";
            CubeEvent event = null;

            if (usedEvents.size() > i) {
                name = usedEvents.get(i).name;
            }

            if (usedEvents.size() > i) {
                event = usedEvents.get(i);
            }

            renderEditImage(event);
            renderQuestionImage(name);
        }
    }

    private void renderEditImage(CubeEvent event) {
        if (event != null) {
            ImGui.sameLine();
            ImGui.pushID("edit_image_" + event.name);

            ImGui.setCursorPosX(ImGui.getWindowWidth() - 64);
            if (ImGui.imageButton(Icons.FACTORY, 16, 16)) {
                ImGuiLoader.pushView(new EventEditView(event, scripts));
                //TODO Открываем окно
            }

            ImGui.popID();
        }
    }

    private void renderQuestionImage(String eventName) {
        ImGui.sameLine();
        ImGui.pushID("question_image_" + eventName);

        ImGui.setCursorPosX(ImGui.getWindowWidth() - 32);
        ImGui.image(Icons.QUESTION, 16, 16);
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            renderTooltipEvent(eventName);
            ImGui.endTooltip();
        }

        ImGui.popID();
    }

    private static void renderQuestionButton(List<CubeEvent> events, int index) {
        ImGui.sameLine();
        ImGui.pushID("question_" + index);

        ImGui.setCursorPosX(ImGui.getWindowWidth() - 42);
        if (ImGui.imageButton(Icons.QUESTION, 16, 16)) {
            ImGui.beginTooltip();
            renderTooltipEvent(events, index);
            ImGui.endTooltip();
        }

        ImGui.popID();
    }

    private static void renderQuestionButton(String eventName) {
        ImGui.sameLine();
        ImGui.pushID("question_" + eventName);

        ImGui.setCursorPosX(ImGui.getWindowWidth() - 32);
        ImGui.imageButton(Icons.QUESTION, 16, 16);
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            renderTooltipEvent(eventName);
            ImGui.endTooltip();
        }

        ImGui.popID();
    }

    private static void renderTooltipEvent(List<CubeEvent> events, int index) {
        renderTooltipEvent(events.get(index).name);
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

        List<CubeEvent> events;

        int selectedEvent = -1;

        private String searchText = "";

        List<String> eventsName;

        public EventListView(List<CubeEvent> events) {
            this.events = events;
            this.eventsName = events.stream()
                    .map(event -> Text.translatable("imgui.cubecode.windows.events." + event.name + ".title").getString())
                    .collect(Collectors.toList());
        }

        @Override
        public void init() {
            ImGui.setNextWindowPos(ImGui.getMousePos().x, ImGui.getMousePos().y);
            ImGui.setNextWindowSize(viewWidth, viewHeight);
        }

        @Override
        public String getName() {
            return String.format("Доступные события ##%s", uniqueID);
        }

        private void updateEventsList() {
            eventsName = events.stream()
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

            for (int i = 0; i < events.size(); i++) {
                String eventName = events.get(i).name;

                if (ImGui.selectable(Text.translatable("imgui.cubecode.windows.events."+ eventName +".title").getString(), selectedEvent == i, ImGuiSelectableFlags.None, maxTextSize.x, 20)) {
                    selectedEvent = i;
                    if (eventView.usedEvents.stream().noneMatch(event -> event.name.equals(eventName))) {
                        eventView.usedEvents.add(new CubeEvent(eventName, events.get(selectedEvent).getScripts()));
                    }
                }

                List<String> list = events.stream()
                        .map(event -> Text.translatable("imgui.cubecode.windows.events." + event.name + ".title").getString())
                        .toList();

                renderQuestionButton(events, list.indexOf(eventName));
            }
        }
    }

    public static class EventEditView extends View {
        private CubeEvent event;
        private final List<Script> scripts;
        private final List<String> scriptNames;

        private int selectedScript = -1;

        public EventEditView(CubeEvent event, List<Script> scripts) {
            this.event = event;
            this.scripts = scripts;

            System.out.println(this.event.getScripts());

            this.scriptNames = new ArrayList<>();

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
            return String.format("Редактирование события \""+Text.translatable("imgui.cubecode.windows.events." + event.name + ".title").getString()+"\" ##%s", uniqueID);
        }

        boolean flag = true;

        @Override
        public void render() {
            Window.create()
                .title(getName())
                .callback(() -> {
                    CubeImGui.beginChild("Scripts", ImGui.getWindowWidth() * 0.25f, 0, true, () -> {
                        for (int i = 0; i < this.event.getScripts().size(); i++) {
                            CubeEvent.EventScript script = this.event.getScripts().get(i);

                            if (ImGui.selectable(script.name + "##" + this.getUniqueID(), selectedScript == i)) {
                                selectedScript = i;
                            }

                            if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
                                selectedScript = i;
                                ImGui.openPopup("script_context_menu_of_script_"+selectedScript);

                                flag = false;
                            }
                        }
                        runContextMenuOfScript();
                    });

                    if (ImGui.isItemClicked(ImGuiMouseButton.Right) && flag) {
                        //Клик по компоненту
                        ImGui.openPopup("script_context_menu_of_event");
                    }
                    flag = true;

                    runContextMenuOfEvent();

                    ImGui.sameLine();

                    CubeImGui.beginChild("Content", 0, 0, this::renderContent);
                }).render(this);
        }

        public void renderContent() {
            if (selectedScript != -1) {
                ImGui.text("Выберите скрипт:");
                ImGui.sameLine();
                CubeImGui.combo(this, "##Scripts_"+selectedScript, scriptNames.indexOf(event.getScripts().get(selectedScript).name), scriptNames.toArray(new String[0]), (script) -> {
                    //CubeEvent.EventScript eventScript = this.event.getScripts().get(selectedScript); //= new CubeEvent.EventScript(scripts.get(script).name, "main");
                });

                ImGui.text("Функция:");
                ImGui.sameLine();
                CubeImGui.inputText(this, "##function", "main", (string) -> {

                });

                if (ImGui.imageButton(Icons.SAVE, 21, 21)) {

                }
            }
        }

        public void runContextMenuOfScript() {
            if (ImGui.beginPopup("script_context_menu_of_script_" + selectedScript)) {
                if (ImGui.menuItem("Удалить")) {
                    if (selectedScript != -1)
                        if (!this.event.getScripts().isEmpty()) {
                            this.event.getScripts().remove(selectedScript);
                            selectedScript = -1;
                        }
                }

                ImGui.endPopup();
            }
        }

        public void runContextMenuOfEvent() {
            if (ImGui.beginPopup("script_context_menu_of_event")) {
                if (ImGui.menuItem("Добавить")) {
                    this.event.getScripts().add(new CubeEvent.EventScript("Скрипт", "main"));
                    selectedScript = this.event.getScripts().size() - 1;
                }

                ImGui.endPopup();
            }
        }
    }
}