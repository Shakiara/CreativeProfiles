package com.shakiara.creativeprofiles.managers;

import com.shakiara.creativeprofiles.Config;
import com.shakiara.creativeprofiles.compat.CosmeticArmorCompat;
import com.shakiara.creativeprofiles.compat.CuriosCompat;
import com.shakiara.creativeprofiles.util.CommandUtil;
import com.shakiara.creativeprofiles.util.MessageUtil;
import com.shakiara.creativeprofiles.util.WorldUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

public class ProfileManager {
    public static void enterCreative(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();

        if ("creative".equals(data.getString("creativeprofiles_current_profile"))) {
            MessageUtil.info(player, "Ya estas en el perfil creativo.");
            return;
        }

        String creativeWorldName = Config.CREATIVE_WORLD.get();
        ServerLevel creativeWorld = WorldUtil.findWorld(player, creativeWorldName);

        if (creativeWorld == null) {
            MessageUtil.error(player, "No encontre el mundo creativo: " + creativeWorldName);
            MessageUtil.info(player, "Crealo primero con Multiworld.");
            return;
        }

        TeleportManager.savePosition(player, "creativeprofiles_survival_position");
        InventoryManager.saveInventory(player, "creativeprofiles_survival_inventory");
        XpManager.saveXp(player, "creativeprofiles_survival_xp");
        InventoryManager.saveEnderChest(player, "creativeprofiles_survival_enderchest");
        CuriosCompat.saveCurios(player, "creativeprofiles_survival_curios");
        CosmeticArmorCompat.saveCosmeticArmor(player, "creativeprofiles_survival_cosmetic_armor");

        player.getInventory().clearContent();

        if (data.contains("creativeprofiles_creative_inventory")) {
            InventoryManager.loadInventory(player, "creativeprofiles_creative_inventory");
        }

        if (data.contains("creativeprofiles_creative_xp")) {
            XpManager.loadXp(player, "creativeprofiles_creative_xp");
        } else {
            XpManager.clearXp(player);
        }

        InventoryManager.loadEnderChestOrEmpty(player, "creativeprofiles_creative_enderchest");
        CuriosCompat.loadCuriosOrEmpty(player, "creativeprofiles_creative_curios");
        CosmeticArmorCompat.loadCosmeticArmorOrEmpty(player, "creativeprofiles_creative_cosmetic_armor");

        forceGameMode(player, GameType.CREATIVE);

        boolean teleportedToCreative = false;

        if (data.contains("creativeprofiles_creative_position")) {
            CompoundTag creativePos = data.getCompound("creativeprofiles_creative_position");
            String savedDimension = creativePos.getString("dimension");

            if (isCreativeDimension(savedDimension)) {
                TeleportManager.teleportToSavedPosition(player, "creativeprofiles_creative_position");
                teleportedToCreative = true;
            } else {
                data.remove("creativeprofiles_creative_position");
                MessageUtil.info(player, "La posicion creativa guardada era invalida. Se usara el spawn creativo.");
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

        forceGameMode(player, GameType.CREATIVE);
        data.putString("creativeprofiles_current_profile", "creative");

        MessageUtil.success(player, "Entraste al perfil creativo.");
    }

    public static void returnToSurvival(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();

        if (!"creative".equals(data.getString("creativeprofiles_current_profile"))) {
            MessageUtil.info(player, "Ya estas en el perfil survival. No se cambio nada.");
            return;
        }

        String survivalWorldName = Config.SURVIVAL_WORLD.get();
        ServerLevel survivalWorld = WorldUtil.findWorld(player, survivalWorldName);

        if (survivalWorld == null) {
            MessageUtil.error(player, "No encontre el mundo survival: " + survivalWorldName);
            return;
        }

        TeleportManager.savePosition(player, "creativeprofiles_creative_position");
        InventoryManager.saveInventory(player, "creativeprofiles_creative_inventory");
        XpManager.saveXp(player, "creativeprofiles_creative_xp");
        InventoryManager.saveEnderChest(player, "creativeprofiles_creative_enderchest");
        CuriosCompat.saveCurios(player, "creativeprofiles_creative_curios");
        CosmeticArmorCompat.saveCosmeticArmor(player, "creativeprofiles_creative_cosmetic_armor");

        player.getInventory().clearContent();

        InventoryManager.loadEnderChestOrEmpty(player, "creativeprofiles_survival_enderchest");
        CuriosCompat.loadCuriosOrEmpty(player, "creativeprofiles_survival_curios");
        CosmeticArmorCompat.loadCosmeticArmorOrEmpty(player, "creativeprofiles_survival_cosmetic_armor");

        forceGameMode(player, GameType.SURVIVAL);

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
        }

        if (data.contains("creativeprofiles_survival_xp")) {
            XpManager.loadXp(player, "creativeprofiles_survival_xp");
        } else {
            XpManager.clearXp(player);
        }

        forceGameMode(player, GameType.SURVIVAL);
        data.putString("creativeprofiles_current_profile", "survival");

        MessageUtil.success(player, "Volviste al perfil survival.");
    }

    public static void syncProfileOnLogin(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();

        data.remove(CommandUtil.INTERNAL_TELEPORT_KEY);

        String profile = data.getString("creativeprofiles_current_profile");

        if (profile.isEmpty()) {
            data.putString("creativeprofiles_current_profile", "survival");
            return;
        }

        switch (profile) {
            case "creative" -> syncCreativeProfile(player, data);
            case "survival" -> syncSurvivalProfile(player, data);
            default -> data.putString("creativeprofiles_current_profile", "survival");
        }
    }

    private static void syncCreativeProfile(ServerPlayer player, CompoundTag data) {
        player.getInventory().clearContent();

        if (data.contains("creativeprofiles_creative_inventory")) {
            InventoryManager.loadInventory(player, "creativeprofiles_creative_inventory");
        }

        if (data.contains("creativeprofiles_creative_xp")) {
            XpManager.loadXp(player, "creativeprofiles_creative_xp");
        } else {
            XpManager.clearXp(player);
        }

        InventoryManager.loadEnderChestOrEmpty(player, "creativeprofiles_creative_enderchest");
        CuriosCompat.loadCuriosOrEmpty(player, "creativeprofiles_creative_curios");
        CosmeticArmorCompat.loadCosmeticArmorOrEmpty(player, "creativeprofiles_creative_cosmetic_armor");
        forceGameMode(player, GameType.CREATIVE);

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

        forceGameMode(player, GameType.CREATIVE);
        MessageUtil.info(player, "Perfil creativo restaurado.");
    }

    private static void syncSurvivalProfile(ServerPlayer player, CompoundTag data) {
        if (data.contains("creativeprofiles_survival_inventory")) {
            InventoryManager.loadInventory(player, "creativeprofiles_survival_inventory");
        }

        if (data.contains("creativeprofiles_survival_xp")) {
            XpManager.loadXp(player, "creativeprofiles_survival_xp");
        }

        InventoryManager.loadEnderChestOrEmpty(player, "creativeprofiles_survival_enderchest");
        CuriosCompat.loadCuriosOrEmpty(player, "creativeprofiles_survival_curios");
        CosmeticArmorCompat.loadCosmeticArmorOrEmpty(player, "creativeprofiles_survival_cosmetic_armor");
        forceGameMode(player, GameType.SURVIVAL);

        MessageUtil.info(player, "Perfil survival restaurado.");
    }

    public static void saveCurrentProfileOnLogout(ServerPlayer player) {
        saveCurrentProfile(player);
    }

    public static void backupCurrentProfile(ServerPlayer player) {
        saveCurrentProfile(player);
        MessageUtil.success(player, "Backup del perfil actual guardado.");
    }

    private static void saveCurrentProfile(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        String profile = data.getString("creativeprofiles_current_profile");

        if ("creative".equals(profile)) {
            TeleportManager.savePosition(player, "creativeprofiles_creative_position");
            InventoryManager.saveInventory(player, "creativeprofiles_creative_inventory");
            XpManager.saveXp(player, "creativeprofiles_creative_xp");
            InventoryManager.saveEnderChest(player, "creativeprofiles_creative_enderchest");
            CuriosCompat.saveCurios(player, "creativeprofiles_creative_curios");
            CosmeticArmorCompat.saveCosmeticArmor(player, "creativeprofiles_creative_cosmetic_armor");
            return;
        }

        TeleportManager.savePosition(player, "creativeprofiles_survival_position");
        InventoryManager.saveInventory(player, "creativeprofiles_survival_inventory");
        XpManager.saveXp(player, "creativeprofiles_survival_xp");
        InventoryManager.saveEnderChest(player, "creativeprofiles_survival_enderchest");
        CuriosCompat.saveCurios(player, "creativeprofiles_survival_curios");
        CosmeticArmorCompat.saveCosmeticArmor(player, "creativeprofiles_survival_cosmetic_armor");
    }

    public static boolean isCreativeDimension(String dimension) {
        String creativeWorldName = Config.CREATIVE_WORLD.get();
        return dimension.equals(creativeWorldName) || dimension.endsWith(":" + creativeWorldName);
    }

    public static boolean isInternalTeleport(ServerPlayer player) {
        return player.getPersistentData().getBoolean(CommandUtil.INTERNAL_TELEPORT_KEY);
    }

    private static void forceGameMode(ServerPlayer player, GameType gameType) {
        player.setGameMode(gameType);

        MinecraftServer server = player.getServer();
        if (server != null) {
            server.execute(() -> player.setGameMode(gameType));
        }
    }
}