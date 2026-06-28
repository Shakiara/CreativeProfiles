package com.shakiara.creativeprofiles.managers;

import com.shakiara.creativeprofiles.util.CommandUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class TeleportManager {
    public static void savePosition(ServerPlayer player, String key) {
        CompoundTag pos = new CompoundTag();

        pos.putString("dimension", player.level().dimension().location().toString());
        pos.putDouble("x", player.getX());
        pos.putDouble("y", player.getY());
        pos.putDouble("z", player.getZ());
        pos.putFloat("yaw", player.getYRot());
        pos.putFloat("pitch", player.getXRot());

        player.getPersistentData().put(key, pos);
    }

    public static void teleportToSavedPosition(ServerPlayer player, String key) {
        CompoundTag pos = player.getPersistentData().getCompound(key);

        ResourceLocation dimensionId = ResourceLocation.parse(pos.getString("dimension"));
        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, dimensionId);
        ServerLevel targetLevel = player.getServer().getLevel(dimensionKey);

        if (targetLevel != null) {
            player.getPersistentData().putBoolean(CommandUtil.INTERNAL_TELEPORT_KEY, true);

            try {
                player.teleportTo(
                        targetLevel,
                        pos.getDouble("x"),
                        pos.getDouble("y"),
                        pos.getDouble("z"),
                        pos.getFloat("yaw"),
                        pos.getFloat("pitch")
                );
            } finally {
                player.getPersistentData().remove(CommandUtil.INTERNAL_TELEPORT_KEY);
            }
        }
    }
}