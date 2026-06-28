package com.shakiara.creativeprofiles.managers;

import com.shakiara.creativeprofiles.Config;
import com.shakiara.creativeprofiles.util.CommandUtil;
import com.shakiara.creativeprofiles.util.WorldUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

public class ProfileManager {
    public static void enterCreative(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();

        if ("creative".equals(data.getString("creativeprofiles_current_profile"))) {
            player.sendSystemMessage(Component.literal("Ya estás en el perfil creativo."));
            return;
        }

        String creativeWorldName = Config.CREATIVE_WORLD.get();
        ServerLevel creativeWorld = WorldUtil.findWorld(player, creativeWorldName);

        if (creativeWorld == null) {
            player.sendSystemMessage(Component.literal("No encontré el mundo creativo: " + creativeWorldName));
            player.sendSystemMessage(Component.literal("Créalo primero con Multiworld, por ejemplo: /mw create " + creativeWorldName + " NORMAL -g=FLAT"));
            return;
        }

        TeleportManager.savePosition(player, "creativeprofiles_survival_position");
        InventoryManager.saveInventory(player, "creativeprofiles_survival_inventory");
        XpManager.saveXp(player, "creativeprofiles_survival_xp");
        InventoryManager.saveEnderChest(player, "creativeprofiles_survival_enderchest");

        player.getInventory().clearContent();

        if (data.contains("creativeprofiles_creative_xp")) {
            XpManager.loadXp(player, "creativeprofiles_creative_xp");
        } else {
            XpManager.clearXp(player);
        }

        InventoryManager.loadEnderChestOrEmpty(player, "creativeprofiles_creative_enderchest");

        boolean teleportedToCreative = false;

        if (data.contains("creativeprofiles_creative_position")) {
            CompoundTag creativePos = data.getCompound("creativeprofiles_creative_position");
            String savedDimension = creativePos.getString("dimension");

            if (isCreativeDimension(savedDimension)) {
                TeleportManager.teleportToSavedPosition(player, "creativeprofiles_creative_position");
                teleportedToCreative = true;
            } else {
                data.remove("creativeprofiles_creative_position");
                player.sendSystemMessage(Component.literal("La posición creativa guardada era inválida. Se usará el spawn creativo."));
            }
        }

        if (!teleportedToCreative) {
            player.getPersistentData().putBoolean(CommandUtil.INTERNAL_TELEPORT_KEY, true);

            try {
                WorldUtil.teleportToWorldSpawn(player, creativeWorld);
            } finally {
                player.getPersistentData().remove(CommandUtil.INTERNAL_TELEPORT_KEY);
            }
        }

        player.setGameMode(GameType.CREATIVE);
        data.putString("creativeprofiles_current_profile", "creative");

        player.sendSystemMessage(Component.literal("Entraste al mundo creativo."));
    }

    public static void returnToSurvival(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();

        if (!"creative".equals(data.getString("creativeprofiles_current_profile"))) {
            player.sendSystemMessage(Component.literal("No estás en el perfil creativo. No se cambió nada."));
            return;
        }

        String survivalWorldName = Config.SURVIVAL_WORLD.get();
        ServerLevel survivalWorld = WorldUtil.findWorld(player, survivalWorldName);

        if (survivalWorld == null) {
            player.sendSystemMessage(Component.literal("No encontré el mundo survival: " + survivalWorldName));
            return;
        }

        TeleportManager.savePosition(player, "creativeprofiles_creative_position");
        XpManager.saveXp(player, "creativeprofiles_creative_xp");
        InventoryManager.saveEnderChest(player, "creativeprofiles_creative_enderchest");

        player.getInventory().clearContent();

        InventoryManager.loadEnderChestOrEmpty(player, "creativeprofiles_survival_enderchest");

        player.setGameMode(GameType.SURVIVAL);

        if (data.contains("creativeprofiles_survival_position")) {
            TeleportManager.teleportToSavedPosition(player, "creativeprofiles_survival_position");
            data.remove("creativeprofiles_survival_position");
        } else {
            player.getPersistentData().putBoolean(CommandUtil.INTERNAL_TELEPORT_KEY, true);

            try {
                WorldUtil.teleportToWorldSpawn(player, survivalWorld);
            } finally {
                player.getPersistentData().remove(CommandUtil.INTERNAL_TELEPORT_KEY);
            }
        }

        if (data.contains("creativeprofiles_survival_inventory")) {
            InventoryManager.loadInventory(player, "creativeprofiles_survival_inventory");
            data.remove("creativeprofiles_survival_inventory");
        }

        if (data.contains("creativeprofiles_survival_xp")) {
            XpManager.loadXp(player, "creativeprofiles_survival_xp");
        } else {
            XpManager.clearXp(player);
        }

        data.putString("creativeprofiles_current_profile", "survival");

        player.sendSystemMessage(Component.literal("Volviste al survival."));
    }

    public static void syncProfileOnLogin(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();

        data.remove(CommandUtil.INTERNAL_TELEPORT_KEY);

        String profile = data.getString("creativeprofiles_current_profile");

        if (profile.isEmpty()) {
            data.putString("creativeprofiles_current_profile", "survival");
            return;
        }

        if ("creative".equals(profile)) {
            if (data.contains("creativeprofiles_creative_xp")) {
                XpManager.loadXp(player, "creativeprofiles_creative_xp");
            }

            InventoryManager.loadEnderChestOrEmpty(player, "creativeprofiles_creative_enderchest");
            player.setGameMode(GameType.CREATIVE);

            if (!isCreativeDimension(player.level().dimension().location().toString())) {
                ServerLevel creativeWorld = WorldUtil.findWorld(player, Config.CREATIVE_WORLD.get());

                if (data.contains("creativeprofiles_creative_position")) {
                    TeleportManager.teleportToSavedPosition(player, "creativeprofiles_creative_position");
                } else if (creativeWorld != null) {
                    player.getPersistentData().putBoolean(CommandUtil.INTERNAL_TELEPORT_KEY, true);

                    try {
                        WorldUtil.teleportToWorldSpawn(player, creativeWorld);
                    } finally {
                        player.getPersistentData().remove(CommandUtil.INTERNAL_TELEPORT_KEY);
                    }
                }
            }

            player.sendSystemMessage(Component.literal("Perfil creativo restaurado."));
            return;
        }

        if ("survival".equals(profile)) {
            if (data.contains("creativeprofiles_survival_xp")) {
                XpManager.loadXp(player, "creativeprofiles_survival_xp");
            }

            InventoryManager.loadEnderChestOrEmpty(player, "creativeprofiles_survival_enderchest");
            player.setGameMode(GameType.SURVIVAL);

            player.sendSystemMessage(Component.literal("Perfil survival restaurado."));
        }
    }

    public static void saveCurrentProfileOnLogout(ServerPlayer player) {
        saveCurrentProfile(player);
    }

    public static void backupCurrentProfile(ServerPlayer player) {
        saveCurrentProfile(player);
        player.sendSystemMessage(Component.literal("Backup del perfil actual guardado."));
    }

    private static void saveCurrentProfile(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        String profile = data.getString("creativeprofiles_current_profile");

        if ("creative".equals(profile)) {
            TeleportManager.savePosition(player, "creativeprofiles_creative_position");
            XpManager.saveXp(player, "creativeprofiles_creative_xp");
            InventoryManager.saveEnderChest(player, "creativeprofiles_creative_enderchest");
            return;
        }

        TeleportManager.savePosition(player, "creativeprofiles_survival_position");
        XpManager.saveXp(player, "creativeprofiles_survival_xp");
        InventoryManager.saveEnderChest(player, "creativeprofiles_survival_enderchest");
    }

    public static boolean isCreativeDimension(String dimension) {
        String creativeWorldName = Config.CREATIVE_WORLD.get();
        return dimension.equals(creativeWorldName) || dimension.endsWith(":" + creativeWorldName);
    }

    public static boolean isInternalTeleport(ServerPlayer player) {
        return player.getPersistentData().getBoolean(CommandUtil.INTERNAL_TELEPORT_KEY);
    }
}