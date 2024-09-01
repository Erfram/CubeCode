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
