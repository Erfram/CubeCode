package com.cubecode.api.events;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CubeEvent {
    public String name;
    private List<EventScript> scripts;

    public CubeEvent(String name, List<EventScript> scripts) {
        this.name = name;
        this.scripts = scripts;
    }

    public List<EventScript> getScripts() {
        return this.scripts;
    }

    public void setScripts(List<EventScript> scripts) {
        this.scripts = scripts;
    }

    public void addScript(EventScript scriptName) {
        this.scripts.add(scriptName);
    }

    @Nullable
    public EventScript getScript(EventScript searchScript) {
        EventScript script = null;
        for (EventScript eventScript : this.scripts) {
            if (eventScript.equals(searchScript)) {
                script = eventScript;
            }
        }

        return script;
    }

    public static List<CubeEvent> nbtListToCubeEvents(NbtList nbtList) {
        List<CubeEvent> cubeEvents = new ArrayList<>();

        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound eventNbt = nbtList.getCompound(i);
            String eventName = eventNbt.getString("name");
            NbtList scriptsList = eventNbt.getList("scripts", NbtList.COMPOUND_TYPE);

            List<EventScript> eventScripts = new ArrayList<>();
            for (int j = 0; j < scriptsList.size(); j++) {
                NbtCompound scriptNbt = scriptsList.getCompound(j);
                String script = scriptNbt.getString("script");
                String function = scriptNbt.getString("function");
                eventScripts.add(new EventScript(script, function));
            }

            cubeEvents.add(new CubeEvent(eventName, eventScripts));
        }

        return cubeEvents;
    }

    public static NbtList cubeEventsToNbtList(List<CubeEvent> cubeEvents) {
        NbtList eventsList = new NbtList();

        for (CubeEvent cubeEvent : cubeEvents) {
            NbtCompound eventNbt = new NbtCompound();
            NbtList scriptsList = new NbtList();

            for (EventScript script : cubeEvent.getScripts()) {
                NbtCompound scriptNbt = new NbtCompound();
                scriptNbt.putString("script", script.name);
                scriptNbt.putString("function", script.function);
                scriptsList.add(scriptNbt);
            }

            eventNbt.putString("name", cubeEvent.name);
            eventNbt.put("scripts", scriptsList);
            eventsList.add(eventNbt);
        }

        return eventsList;
    }

    public static class EventScript {
        public String name;
        public String function = "main";

        public EventScript(String name, String function) {
            this.name = name;
            this.function = function;
        }

        public EventScript(String name) {
            this.name = name;
        }
    }
}
