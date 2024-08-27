package com.cubecode.state;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import com.cubecode.CubeCode;

import java.util.HashMap;
import java.util.UUID;

public class ServerState extends PersistentState {

    public NbtCompound values = new NbtCompound();
    public NbtList events = new NbtList();
    public HashMap<UUID, PlayerState> players = new HashMap<>();

    public static ServerState createFromNbt(NbtCompound tag) {
        ServerState serverState = new ServerState();

        NbtList events = tag.getList("events", NbtList.COMPOUND_TYPE);
        NbtCompound statesTag = tag.getCompound("states");
        NbtCompound playersTag = tag.getCompound("players");

        playersTag.getKeys().forEach(key -> {
            PlayerState playerState = new PlayerState();

            playerState.setValues(playersTag.getCompound(key));

            serverState.players.put(UUID.fromString(key), playerState);
        });

        serverState.values.copyFrom(statesTag);
        serverState.events = events;

        return serverState;
    }

    public static ServerState getServerState(MinecraftServer server) {
        ServerWorld world = server.getWorld(World.OVERWORLD);
        PersistentStateManager persistentStateManager = world.getPersistentStateManager();

        Type<ServerState> type = new Type<>(
                ServerState::new,
                ServerState::createFromNbt,
                null
        );

        ServerState serverState = persistentStateManager.getOrCreate(
                type,
                CubeCode.MOD_ID);

        return serverState;
    }

    public static PlayerState getPlayerState(PlayerEntity player) {
        ServerState serverState = getServerState(player.getServer());

        PlayerState playerState = serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerState());

        return playerState;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playersNbtCompound = new NbtCompound();
        players.forEach((UUID, playerSate) -> {
            playersNbtCompound.put(String.valueOf(UUID), playerSate.getValues());
        });

        nbt.put("players", playersNbtCompound);
        nbt.put("states", this.values);
        nbt.put("events", this.events);

        return nbt;
    }
}
