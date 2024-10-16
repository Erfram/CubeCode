package com.cubecode;

import com.cubecode.api.events.CubeEvent;
import com.cubecode.api.events.EventManager;
import com.cubecode.api.scripts.Properties;
import com.cubecode.api.scripts.ProjectManager;
import com.cubecode.api.scripts.code.ScriptVector;
import com.cubecode.api.scripts.code.blocks.ScriptBlockEntity;
import com.cubecode.api.scripts.code.entities.ScriptPlayer;
import com.cubecode.content.CubeCodeCommand;
import com.cubecode.state.PlayerState;
import com.cubecode.state.ServerState;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EventHandler {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CubeCodeCommand.init(dispatcher));

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            File worldDirectory = server.getSavePath(WorldSavePath.ROOT).getParent().toFile();

            CubeCode.cubeCodeDirectory = new File(worldDirectory, CubeCode.MOD_ID);
            CubeCode.factoryDirectory = new File(CubeCode.cubeCodeDirectory, "factory");
            CubeCode.contentDirectory = new File(CubeCode.factoryDirectory, "content");

            if (CubeCode.cubeCodeDirectory.mkdirs()) {
                CubeCode.LOGGER.info(String.format("#### Creating a mod directory %s for the world. ####", CubeCode.MOD_ID));
            }

            CubeCode.projectManager = new ProjectManager(new File(CubeCode.cubeCodeDirectory, "project"));
            CubeCode.eventManager = new EventManager(new File(CubeCode.cubeCodeDirectory, "events.json"));

            CubeCode.eventManager.register();

            CubeCode.eventManager.trigger("server_starting", null, null, null, server);
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerState serverState = ServerState.getServerState(handler.player.server);
            PlayerState playerState = ServerState.getPlayerState(handler.player);

            List<CubeEvent> usedEvents = EventManager.nbtListToCubeEvents(serverState.events);

            List<CubeEvent> events = new ArrayList<>();

            usedEvents.forEach(usedEvent ->
                CubeCode.eventManager.events.forEach(event -> {
                    if (usedEvent.name.equals(event.name)) {
                        events.add(event);
                    }
                })
            );

            serverState.events = EventManager.cubeEventsToNbtList(events);
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> CubeCode.eventManager.trigger("server_started", null, null, null, server));

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) ->
            CubeCode.eventManager.trigger("block_player_break_after", Properties.create(
                    null,
                    null,
                    player,
                    null,
                    world,
                    world.getServer()
            ).setValue("blockPos", new ScriptVector(pos)).setValue("blockState", state).setValue("blockEntity", new ScriptBlockEntity(blockEntity)))
        );

        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            CubeCode.eventManager.trigger("block_player_break_before", Properties.create(
                    null,
                    null,
                    player,
                    null,
                    world,
                    world.getServer()
            ).setValue("blockPos", new ScriptVector(pos)).setValue("blockState", state).setValue("blockEntity", new ScriptBlockEntity(blockEntity)));

            return true;
        });

        PlayerBlockBreakEvents.CANCELED.register((world, player, pos, state, blockEntity) -> {
            CubeCode.eventManager.trigger("block_player_break_canceled", Properties.create(
                    null,
                    null,
                    player,
                    null,
                    world,
                    world.getServer()
            ).setValue("blockPos", new ScriptVector(pos)).setValue("blockState", state).setValue("blockEntity", new ScriptBlockEntity(blockEntity)));
        });

        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            CubeCode.eventManager.trigger("entity_hurt", Properties.create(
                    null,
                    null,
                    source.getAttacker(),
                    source.getSource(),
                    entity.getWorld(),
                    entity.getServer()
            ).setValue("damageType", source.getType().msgId()).setValue("damage", amount));

            return true;
        });

        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) -> {
            CubeCode.eventManager.trigger("entity_before_death", Properties.create(
                    null,
                    null,
                    source.getAttacker(),
                    source.getSource(),
                    entity.getWorld(),
                    entity.getServer()
            ).setValue("damageType", source.getType().msgId()).setValue("damage", amount));

            return true;
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            CubeCode.eventManager.trigger("entity_after_death", Properties.create(
               null,
               null,
               entity,
               null,
               entity.getWorld(),
               entity.getServer()
            ).setValue("damageType", source.getType().msgId()));
        });

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(((world, entity, killedEntity) ->
            CubeCode.eventManager.trigger("entity_killed_by_entity", killedEntity, entity, world, world.getServer())
        ));

        ServerTickEvents.START_SERVER_TICK.register(server -> CubeCode.eventManager.trigger("server_tick_start", null, null, null, server));
        ServerTickEvents.END_SERVER_TICK.register(server -> CubeCode.eventManager.trigger("server_tick_end", null, null, null, server));

        ServerTickEvents.START_WORLD_TICK.register(world -> CubeCode.eventManager.trigger("world_tick_start", null, null, world, world.getServer()));
        ServerTickEvents.END_WORLD_TICK.register(world -> CubeCode.eventManager.trigger("world_tick_end", null, null, world, world.getServer()));

        ServerPlayerEvents.ALLOW_DEATH.register((player, source, amount) -> {
            CubeCode.eventManager.trigger("player_death", Properties.create(
                    null, null,
                    player,
                    source.getAttacker(),
                    player.getWorld(),
                    player.getServer()
            ).setValue("damageType", source.getType().msgId()).setValue("damage", amount));

            return true;
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            CubeCode.eventManager.trigger("player_respawn", Properties.create(
                    null,
                    null,
                    newPlayer,
                    null,
                    newPlayer.getWorld(),
                    newPlayer.getServer()
            ).setValue("oldPlayer", ScriptPlayer.create(oldPlayer)).setValue("alive", alive));
        });

        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (!world.isClient) {
                CubeCode.eventManager.trigger("player_attack_block", Properties.create(
                        null,
                        null,
                        player,
                        null,
                        world,
                        world.getServer()
                ).setValue("blockPos", new ScriptVector(pos)).setValue("direction", direction.getName()).setValue("hand", hand.name()));
            }

            return ActionResult.PASS;
        });

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient) {
                CubeCode.eventManager.trigger("player_attack_entity", Properties.create(
                        null,
                        null,
                        player,
                        entity,
                        world,
                        world.getServer()
                )
                .setValue("hand", hand.name()));
            }

            return ActionResult.PASS;
        });

        UseBlockCallback.EVENT.register((player, world, hand, blockHitResult) -> {
            if (!world.isClient) {
                CubeCode.eventManager.trigger("player_interact_block", Properties.create(
                        null,
                        null,
                        player,
                        null,
                        world,
                        world.getServer()
                )
                .setValue("blockPos", new ScriptVector(blockHitResult.getBlockPos()))
                .setValue("hand", hand.name())
                .setValue("isInsideBlock", blockHitResult.isInsideBlock())
                .setValue("direction", blockHitResult.getSide().getName()));
            }

            return ActionResult.PASS;
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient) {
                CubeCode.eventManager.trigger("player_interact_entity", Properties.create(
                        null,
                        null,
                        player,
                        entity,
                        world,
                        world.getServer()
                )
                .setValue("hand", hand.name()));
            }

            return ActionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!world.isClient) {
                CubeCode.eventManager.trigger("player_interact_item", Properties.create(
                        null,
                        null,
                        player,
                        null,
                        world,
                        world.getServer()
                )
                .setValue("hand", hand.name())
                .setValue("itemStack", player.getStackInHand(hand)));
            }

            return TypedActionResult.pass(player.getStackInHand(hand));
        });
    }
}