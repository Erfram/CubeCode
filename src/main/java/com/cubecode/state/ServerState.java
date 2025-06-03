package com.cubecode.state;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import com.cubecode.CubeCode;

import java.util.HashMap;
import java.util.UUID;

public class ServerState extends PersistentState {
    public HashMap<UUID, PlayerState> players = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playersNbtCompound = new NbtCompound();
        players.forEach((UUID, playerSate) -> {
            NbtCompound playerStateNbt = playerSate.serialize();

            playersNbtCompound.put(String.valueOf(UUID), playerStateNbt);
        });
        nbt.put("players", playersNbtCompound);
        return nbt;
    }

    public static ServerState createFromNbt(NbtCompound tag) {
        ServerState serverState = new ServerState();

        NbtCompound playersTag = tag.getCompound("players");
        playersTag.getKeys().forEach(key -> {
            PlayerState playerState = new PlayerState();

            playerState.deserialize(playersTag.getCompound(key));

            UUID uuid = UUID.fromString(key);
            serverState.players.put(uuid, playerState);
        });

        return serverState;
    }

    public static ServerState getServerState(MinecraftServer server) {
        ServerWorld world = server.getWorld(World.OVERWORLD);
        PersistentStateManager persistentStateManager = world.getPersistentStateManager();

        ServerState serverState = persistentStateManager.getOrCreate(
                ServerState::createFromNbt,
                ServerState::new,
                CubeCode.MOD_ID);

        return serverState;
    }

    public static PlayerState getPlayerState(LivingEntity player) {
        ServerState serverState = getServerState(player.getWorld().getServer());

        PlayerState playerState = serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerState((PlayerEntity) player));

        return playerState;
    }
}
