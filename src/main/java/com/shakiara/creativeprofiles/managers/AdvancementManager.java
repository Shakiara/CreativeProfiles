package com.shakiara.creativeprofiles.managers;

import com.shakiara.creativeprofiles.CreativeProfiles;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AdvancementManager {
    private static final String PROFILE_FILE_PREFIX = ".creativeprofiles_";
    private static final String ADVANCEMENTS_SUFFIX = "_advancements.json";

    private AdvancementManager() {}

    public static void saveAdvancements(ServerPlayer player, String profile) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }

        player.getAdvancements().save();

        Path activePath = activeAdvancementPath(player);
        Path profilePath = profileAdvancementPath(player, profile);

        try {
            Files.createDirectories(profilePath.getParent());

            if (Files.exists(activePath)) {
                Files.copy(activePath, profilePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(profilePath);
            }
        } catch (IOException exception) {
            CreativeProfiles.LOGGER.error("Could not save {} advancements for {}", profile, player.getGameProfile().getName(), exception);
        }
    }

    public static void loadAdvancementsOrEmpty(ServerPlayer player, String profile) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }

        Path activePath = activeAdvancementPath(player);
        Path profilePath = profileAdvancementPath(player, profile);

        try {
            Files.createDirectories(activePath.getParent());

            if (Files.exists(profilePath)) {
                Files.copy(profilePath, activePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(activePath);
            }

            player.getAdvancements().reload(server.getAdvancements());
            player.getAdvancements().flushDirty(player);
        } catch (IOException exception) {
            CreativeProfiles.LOGGER.error("Could not load {} advancements for {}", profile, player.getGameProfile().getName(), exception);
        }
    }

    public static void copyProfileAdvancementsToBackup(ServerPlayer player, String backupPrefix, String profile) {
        Path profilePath = profileAdvancementPath(player, profile);
        Path backupPath = backupAdvancementPath(player, backupPrefix, profile);

        try {
            Files.createDirectories(backupPath.getParent());

            if (Files.exists(profilePath)) {
                Files.copy(profilePath, backupPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(backupPath);
            }
        } catch (IOException exception) {
            CreativeProfiles.LOGGER.error("Could not backup {} advancements for {}", profile, player.getGameProfile().getName(), exception);
        }
    }

    public static boolean restoreBackupAdvancementsToProfile(ServerPlayer player, String backupPrefix, String profile) {
        Path backupPath = backupAdvancementPath(player, backupPrefix, profile);
        Path profilePath = profileAdvancementPath(player, profile);

        if (!Files.exists(backupPath)) {
            return false;
        }

        try {
            Files.createDirectories(profilePath.getParent());
            Files.copy(backupPath, profilePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException exception) {
            CreativeProfiles.LOGGER.error("Could not restore {} advancements backup for {}", profile, player.getGameProfile().getName(), exception);
            return false;
        }
    }

    public static void clearBackupAdvancements(ServerPlayer player, String backupPrefix, String profile) {
        try {
            Files.deleteIfExists(backupAdvancementPath(player, backupPrefix, profile));
        } catch (IOException exception) {
            CreativeProfiles.LOGGER.error("Could not clear {} advancements backup for {}", profile, player.getGameProfile().getName(), exception);
        }
    }

    public static void swapProfileAdvancements(ServerPlayer player, String firstProfile, String secondProfile) {
        // Cambio 1.2.x: permite reparar advancements survival/creative intercambiados.
        Path firstPath = profileAdvancementPath(player, firstProfile);
        Path secondPath = profileAdvancementPath(player, secondProfile);
        Path tempPath = advancementsDirectory(player).resolve(player.getUUID() + ".creativeprofiles_swap_tmp_advancements.json");

        try {
            Files.createDirectories(firstPath.getParent());

            boolean hasFirst = Files.exists(firstPath);
            boolean hasSecond = Files.exists(secondPath);

            if (hasFirst) {
                Files.copy(firstPath, tempPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(tempPath);
            }

            if (hasSecond) {
                Files.copy(secondPath, firstPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(firstPath);
            }

            if (hasFirst) {
                Files.copy(tempPath, secondPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.deleteIfExists(secondPath);
            }

            Files.deleteIfExists(tempPath);
        } catch (IOException exception) {
            CreativeProfiles.LOGGER.error("Could not swap advancements for {}", player.getGameProfile().getName(), exception);
        }
    }

    public static boolean hasProfileAdvancements(ServerPlayer player, String profile) {
        return Files.exists(profileAdvancementPath(player, profile));
    }

    public static boolean hasBackupAdvancements(ServerPlayer player, String backupPrefix, String profile) {
        return Files.exists(backupAdvancementPath(player, backupPrefix, profile));
    }

    private static Path activeAdvancementPath(ServerPlayer player) {
        return advancementsDirectory(player).resolve(player.getUUID() + ".json");
    }

    private static Path profileAdvancementPath(ServerPlayer player, String profile) {
        return advancementsDirectory(player).resolve(player.getUUID() + PROFILE_FILE_PREFIX + profile + ADVANCEMENTS_SUFFIX);
    }

    private static Path backupAdvancementPath(ServerPlayer player, String backupPrefix, String profile) {
        return advancementsDirectory(player).resolve(player.getUUID() + "." + backupPrefix + profile + ADVANCEMENTS_SUFFIX);
    }

    private static Path advancementsDirectory(ServerPlayer player) {
        return player.getServer().getWorldPath(LevelResource.PLAYER_ADVANCEMENTS_DIR);
    }
}