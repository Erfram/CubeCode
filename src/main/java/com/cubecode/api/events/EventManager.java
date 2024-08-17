package com.cubecode.api.events;

import com.cubecode.CubeCode;
import com.cubecode.api.scripts.Properties;
import com.cubecode.api.scripts.code.ScriptEvent;
import com.cubecode.api.scripts.code.ScriptFactory;
import com.cubecode.api.utils.DirectoryManager;
import com.cubecode.api.utils.GsonManager;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.utils.CubeCodeException;
import com.google.common.reflect.TypeToken;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EventManager extends DirectoryManager {
    public List<CubeEvent> events = new ArrayList<>();

    public LinkedList<String> eventSource = new LinkedList<>();

    public EventManager(File eventsDirectory) {
        super(eventsDirectory);

        eventSource.add("CubeCode");
    }

    public void addScriptToEvent(String cubeEvent, CubeEvent.EventScript eventScript) throws CubeCodeException {
        CubeEvent event = this.events.stream()
                .filter(e -> cubeEvent.equals(e.name))
                .findFirst()
                .orElse(null);

        if (event == null) {
            throw new CubeCodeException("Event not found " + cubeEvent);
        }

        event.addScript(eventScript);
    }

    public void trigger(String eventName, Entity subject, Entity object, World world, MinecraftServer server) {
        triggerInternal(eventName, Properties.create(
                "",
                "",
                subject,
                object,
                world,
                server
        ));
    }

    public <S extends Entity, O extends Entity> void trigger(String eventName, Properties properties) {
        triggerInternal(eventName, properties);
    }

    private void triggerInternal(String eventName, Properties properties) {
        CubeEvent event = CubeCode.eventManager.getEventByName(eventName);
        if (event == null) return;

        event.getScripts().forEach(script -> executeScriptEvent(script, properties));
    }

    private void executeScriptEvent(CubeEvent.EventScript eventScript, Properties properties) {
        try {
            ScriptEvent<?, ?> scriptEvent = (ScriptEvent<?, ?>) properties.get(CubeCodeConfig.getScriptConfig().contextName);

            ScriptEvent<?, ?> newScriptEvent = new ScriptEvent<>(
                    eventScript.name,
                    eventScript.function,
                    scriptEvent.getSubject(),
                    scriptEvent.getObject(),
                    scriptEvent.getWorld(),
                    scriptEvent.getServer()
            );

            newScriptEvent.setValues(scriptEvent.getValues());

            Properties newProperties = properties.copy();
            newProperties.put(CubeCodeConfig.getScriptConfig().contextName, newScriptEvent);
            newProperties.put("CubeCode", new ScriptFactory());

            CubeCode.scriptManager.getScript(eventScript.name).run(eventScript.function, eventScript.name, newProperties);
        } catch (CubeCodeException e) {
            CubeCode.LOGGER.error("Error executing script: {} - {}", eventScript.name, e.getMessage());
            //В консоль
        }
    }

    public boolean isEvent(String cubeEvent) {
        return this.events.stream().anyMatch(event -> cubeEvent.equals(event.name));
    }

    @Nullable
    public CubeEvent getEventByName(String eventName) {
        CubeEvent cubeEvent = null;

        for (CubeEvent event : this.events) {
            if (event.name.equals(eventName)) {
                cubeEvent = event;
            }
        }

        return cubeEvent;
    }

    public boolean add(List<CubeEvent> events, CubeEvent cubeEvent) {
        if (cubeEvent == null || events == null) {
            return false;
        }

        boolean eventExists = events.stream().anyMatch(event -> event.name.equals(cubeEvent.name));

        if (!eventExists) {
            events.add(cubeEvent);
            return true;
        }

        return false;
    }

    public void registerEvent(CubeEvent event) {
        this.events.add(event);
    }

    public void register() {
        File eventsFile = new File(CubeCode.cubeCodeDirectory, "events.json");

        if (!eventsFile.exists()) {
            try {
                eventsFile.createNewFile();
            } catch (IOException e) {
                CubeCode.LOGGER.error(e.getMessage());
            }
        }

        Type eventListType = new TypeToken<List<CubeEvent>>(){}.getType();
        List<CubeEvent> existingEvents = GsonManager.readJSON(eventsFile, eventListType);

        if (existingEvents == null) {
            existingEvents = new ArrayList<>();
        }

        this.add(existingEvents, new CubeEvent("server_starting", new ArrayList<>()));
        this.add(existingEvents, new CubeEvent("server_started", new ArrayList<>()));
        this.add(existingEvents, new CubeEvent("server_tick_start", new ArrayList<>()));
        this.add(existingEvents, new CubeEvent("server_tick_end", new ArrayList<>()));

        this.add(existingEvents, new CubeEvent("block_player_break_after", new ArrayList<>()));
        this.add(existingEvents, new CubeEvent("block_player_break_before", new ArrayList<>()));
        this.add(existingEvents, new CubeEvent("block_player_break_canceled", new ArrayList<>()));

        this.add(existingEvents, new CubeEvent("player_death", new ArrayList<>()));
        this.add(existingEvents, new CubeEvent("player_respawn", new ArrayList<>()));
        this.add(existingEvents, new CubeEvent("player_attack_block", new ArrayList<>()));
        this.add(existingEvents, new CubeEvent("player_attack_entity", new ArrayList<>()));
        this.add(existingEvents, new CubeEvent("player_interact_block", new ArrayList<>()));
        this.add(existingEvents, new CubeEvent("player_interact_entity", new ArrayList<>()));
        this.add(existingEvents, new CubeEvent("player_interact_item", new ArrayList<>()));

        this.add(existingEvents, new CubeEvent("entity_hurt", new ArrayList<>()));
        this.add(existingEvents, new CubeEvent("entity_before_death", new ArrayList<>()));
        this.add(existingEvents, new CubeEvent("entity_after_death", new ArrayList<>()));
        this.add(existingEvents, new CubeEvent("entity_killed_by_entity", new ArrayList<>()));

        this.add(existingEvents, new CubeEvent("world_tick_start", new ArrayList<>()));
        this.add(existingEvents, new CubeEvent("world_tick_end", new ArrayList<>()));

        for (CubeEvent event : existingEvents) {
            this.registerEvent(event);
        }

        GsonManager.writeJSON(eventsFile, existingEvents);
    }
}