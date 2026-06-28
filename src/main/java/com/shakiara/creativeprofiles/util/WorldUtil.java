package com.shakiara.creativeprofiles.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class WorldUtil {
    public static ServerLevel findWorld(ServerPlayer player, String worldName) {
        for (ServerLevel level : player.getServer().getAllLevels()) {
            String dimension = level.dimension().location().toString();

            if (dimension.equals(worldName) || dimension.endsWith(":" + worldName)) {
                return level;
            }
        }

        return null;
    }

    public static void teleportToWorldSpawn(ServerPlayer player, ServerLevel level) {
        BlockPos spawn = level.getSharedSpawnPos();

        player.teleportTo(
                level,
                spawn.getX() + 0.5,
                spawn.getY(),
                spawn.getZ() + 0.5,
                player.getYRot(),
                player.getXRot()
        );
    }
}