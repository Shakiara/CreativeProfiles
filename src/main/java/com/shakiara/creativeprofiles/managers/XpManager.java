package com.shakiara.creativeprofiles.managers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class XpManager {
    public static void saveXp(ServerPlayer player, String key) {
        CompoundTag xp = new CompoundTag();

        xp.putInt("level", player.experienceLevel);
        xp.putInt("total", player.totalExperience);
        xp.putFloat("progress", player.experienceProgress);

        player.getPersistentData().put(key, xp);
    }

    public static void loadXp(ServerPlayer player, String key) {
        CompoundTag xp = player.getPersistentData().getCompound(key);

        player.experienceLevel = xp.getInt("level");
        player.totalExperience = xp.getInt("total");
        player.experienceProgress = xp.getFloat("progress");

        player.onUpdateAbilities();
    }

    public static void clearXp(ServerPlayer player) {
        player.experienceLevel = 0;
        player.totalExperience = 0;
        player.experienceProgress = 0.0F;

        player.onUpdateAbilities();
    }
}