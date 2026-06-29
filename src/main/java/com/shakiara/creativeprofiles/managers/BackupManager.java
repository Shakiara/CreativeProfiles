package com.shakiara.creativeprofiles.managers;

import com.shakiara.creativeprofiles.compat.CosmeticArmorCompat;
import com.shakiara.creativeprofiles.compat.CuriosCompat;
import com.shakiara.creativeprofiles.util.MessageUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackupManager {
    public static final String BACKUP_PREFIX = "creativeprofiles_backup_";
    public static final String AUTO_BACKUP_PREFIX = "creativeprofiles_autobackup_";

    private static final String BACKUP_CREATED_AT = "creativeprofiles_backup_created_at";
    private static final String AUTO_BACKUP_CREATED_AT = "creativeprofiles_autobackup_created_at";
    private static final String CURRENT_PROFILE = "creativeprofiles_current_profile";
    private static final long BACKUP_COOLDOWN_MILLIS = 296 * 1000L;

    private static final Map<UUID, Long> LAST_BACKUP_TIME = new HashMap<>();

    private BackupManager() {}

    public static void createBackup(ServerPlayer player) {
        if (isBackupOnCooldown(player)) {
            MessageUtil.error(player, "Debes esperar " + getRemainingCooldownSeconds(player) + " segundos antes de crear otro backup.");
            return;
        }

        createBackupWithPrefix(player, BACKUP_PREFIX, BACKUP_CREATED_AT);
        LAST_BACKUP_TIME.put(player.getUUID(), System.currentTimeMillis());

        MessageUtil.success(player, "Backup manual creado para los perfiles survival y creative.");
    }

    public static void createAutoBackup(ServerPlayer player, String reason) {
        // Cambio 1.2.x: los backups automaticos se guardan separados de los backups manuales.
        createBackupWithPrefix(player, AUTO_BACKUP_PREFIX, AUTO_BACKUP_CREATED_AT);
    }

    public static void restoreProfile(CommandSourceStack source, ServerPlayer target, String profile) {
        restoreProfile(source, target, profile, BACKUP_PREFIX, "manual");
    }

    public static void restoreAutoProfile(CommandSourceStack source, ServerPlayer target, String profile) {
        restoreProfile(source, target, profile, AUTO_BACKUP_PREFIX, "automatico");
    }

    public static void showBackupStatus(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();

        MessageUtil.info(player, "Backup status");
        MessageUtil.info(player, "Backup manual survival: " + yesNo(hasManualProfileBackup(player, data, "survival")));
        MessageUtil.info(player, "Backup manual creative: " + yesNo(hasManualProfileBackup(player, data, "creative")));
        MessageUtil.info(player, "Backup automatico survival: " + yesNo(hasAutoProfileBackup(player, data, "survival")));
        MessageUtil.info(player, "Backup automatico creative: " + yesNo(hasAutoProfileBackup(player, data, "creative")));
        MessageUtil.info(player, "Perfil actual en backup manual: " + getBackedUpCurrentProfile(data, BACKUP_PREFIX));
        MessageUtil.info(player, "Perfil actual en backup automatico: " + getBackedUpCurrentProfile(data, AUTO_BACKUP_PREFIX));

        MessageUtil.info(player, "Backup manual timestamp: " + (data.contains(BACKUP_CREATED_AT) ? data.getLong(BACKUP_CREATED_AT) : "no disponible"));
        MessageUtil.info(player, "Backup automatico timestamp: " + (data.contains(AUTO_BACKUP_CREATED_AT) ? data.getLong(AUTO_BACKUP_CREATED_AT) : "no disponible"));

        if (isBackupOnCooldown(player)) {
            MessageUtil.info(player, "Cooldown de backup: " + getRemainingCooldownSeconds(player) + " segundos restantes.");
        } else {
            MessageUtil.info(player, "Cooldown de backup: disponible");
        }
    }

    public static boolean hasManualProfileBackup(ServerPlayer player, CompoundTag data, String profile) {
        return hasProfileBackup(player, data, BACKUP_PREFIX, profile);
    }

    public static boolean hasAutoProfileBackup(ServerPlayer player, CompoundTag data, String profile) {
        return hasProfileBackup(player, data, AUTO_BACKUP_PREFIX, profile);
    }

    private static void createBackupWithPrefix(ServerPlayer player, String prefix, String timestampKey) {
        CompoundTag data = player.getPersistentData();

        saveCurrentLiveProfile(player);

        backupKey(data, prefix, CURRENT_PROFILE);
        backupProfile(player, data, prefix, "survival");
        backupProfile(player, data, prefix, "creative");

        data.putLong(timestampKey, System.currentTimeMillis());
    }

    private static void restoreProfile(CommandSourceStack source, ServerPlayer target, String profile, String prefix, String backupType) {
        if (!isValidProfile(profile)) {
            sendError(source, "Perfil invalido. Usa survival o creative.");
            return;
        }

        CompoundTag data = target.getPersistentData();

        if (!hasProfileBackup(target, data, prefix, profile)) {
            sendError(source, "No hay backup " + backupType + " guardado para el perfil " + profile + " de " + target.getGameProfile().getName() + ".");
            return;
        }

        boolean restoredAny = restoreProfileKeys(target, data, prefix, profile);

        if (!restoredAny) {
            sendError(source, "No se pudo restaurar el backup " + backupType + " del perfil " + profile + ".");
            return;
        }

        // Cambio 1.2.x: el backup restaurado se consume para reducir duplicaciones repetidas.
        clearProfileBackup(target, data, prefix, profile);

        String currentProfile = getLiveProfile(target);
        data.putString(CURRENT_PROFILE, currentProfile);

        if (profile.equals(currentProfile)) {
            applyRestoredCurrentProfile(target, profile);
            MessageUtil.info(target, "Un administrador restauro tu backup " + backupType + " del perfil " + profile + ".");
        } else {
            MessageUtil.info(target, "Un administrador restauro tu backup " + backupType + " del perfil " + profile + ". Se cargara cuando cambies a ese perfil.");
        }

        sendSuccess(source, "Backup " + backupType + " del perfil " + profile + " restaurado para " + target.getGameProfile().getName() + ".");
    }

    private static void saveCurrentLiveProfile(ServerPlayer player) {
        // Cambio 1.2.x: usa la dimension real como fuente de verdad para evitar inventarios cruzados.
        saveLiveProfile(player, getLiveProfile(player));
    }

    private static String getLiveProfile(ServerPlayer player) {
        String dimension = player.level().dimension().location().toString();
        return ProfileManager.isCreativeDimension(dimension) ? "creative" : "survival";
    }

    private static void saveLiveProfile(ServerPlayer player, String profile) {
        TeleportManager.savePosition(player, "creativeprofiles_" + profile + "_position");
        InventoryManager.saveInventory(player, "creativeprofiles_" + profile + "_inventory");
        XpManager.saveXp(player, "creativeprofiles_" + profile + "_xp");
        InventoryManager.saveEnderChest(player, "creativeprofiles_" + profile + "_enderchest");
        CuriosCompat.saveCurios(player, "creativeprofiles_" + profile + "_curios");
        CosmeticArmorCompat.saveCosmeticArmor(player, "creativeprofiles_" + profile + "_cosmetic_armor");
        AdvancementManager.saveAdvancements(player, profile);
    }

    private static void backupProfile(ServerPlayer player, CompoundTag data, String prefix, String profile) {
        backupKey(data, prefix, "creativeprofiles_" + profile + "_inventory");
        backupKey(data, prefix, "creativeprofiles_" + profile + "_xp");
        backupKey(data, prefix, "creativeprofiles_" + profile + "_enderchest");
        backupKey(data, prefix, "creativeprofiles_" + profile + "_position");
        backupKey(data, prefix, "creativeprofiles_" + profile + "_curios");
        backupKey(data, prefix, "creativeprofiles_" + profile + "_cosmetic_armor");
        AdvancementManager.copyProfileAdvancementsToBackup(player, prefix, profile);
    }

    private static boolean restoreProfileKeys(ServerPlayer player, CompoundTag data, String prefix, String profile) {
        boolean restoredAny = false;

        restoredAny |= restoreKey(data, prefix, "creativeprofiles_" + profile + "_inventory");
        restoredAny |= restoreKey(data, prefix, "creativeprofiles_" + profile + "_xp");
        restoredAny |= restoreKey(data, prefix, "creativeprofiles_" + profile + "_enderchest");
        restoredAny |= restoreKey(data, prefix, "creativeprofiles_" + profile + "_position");
        restoredAny |= restoreKey(data, prefix, "creativeprofiles_" + profile + "_curios");
        restoredAny |= restoreKey(data, prefix, "creativeprofiles_" + profile + "_cosmetic_armor");
        restoredAny |= AdvancementManager.restoreBackupAdvancementsToProfile(player, prefix, profile);

        return restoredAny;
    }

    private static void applyRestoredCurrentProfile(ServerPlayer player, String profile) {
        player.getInventory().clearContent();

        InventoryManager.loadInventory(player, "creativeprofiles_" + profile + "_inventory");
        XpManager.loadXp(player, "creativeprofiles_" + profile + "_xp");
        InventoryManager.loadEnderChestOrEmpty(player, "creativeprofiles_" + profile + "_enderchest");
        CuriosCompat.loadCuriosOrEmpty(player, "creativeprofiles_" + profile + "_curios");
        CosmeticArmorCompat.loadCosmeticArmorOrEmpty(player, "creativeprofiles_" + profile + "_cosmetic_armor");
        AdvancementManager.loadAdvancementsOrEmpty(player, profile);
    }

    private static void backupKey(CompoundTag data, String prefix, String key) {
        if (!data.contains(key)) {
            return;
        }

        Tag tag = data.get(key);
        if (tag != null) {
            data.put(prefix + key, tag.copy());
        }
    }

    private static boolean restoreKey(CompoundTag data, String prefix, String key) {
        String backupKey = prefix + key;

        if (!data.contains(backupKey)) {
            return false;
        }

        Tag tag = data.get(backupKey);
        if (tag == null) {
            return false;
        }

        data.put(key, tag.copy());
        return true;
    }

    private static void clearProfileBackup(ServerPlayer player, CompoundTag data, String prefix, String profile) {
        data.remove(prefix + "creativeprofiles_" + profile + "_inventory");
        data.remove(prefix + "creativeprofiles_" + profile + "_xp");
        data.remove(prefix + "creativeprofiles_" + profile + "_enderchest");
        data.remove(prefix + "creativeprofiles_" + profile + "_position");
        data.remove(prefix + "creativeprofiles_" + profile + "_curios");
        data.remove(prefix + "creativeprofiles_" + profile + "_cosmetic_armor");
        AdvancementManager.clearBackupAdvancements(player, prefix, profile);
    }

    private static boolean hasProfileBackup(ServerPlayer player, CompoundTag data, String prefix, String profile) {
        return data.contains(prefix + "creativeprofiles_" + profile + "_inventory")
                || data.contains(prefix + "creativeprofiles_" + profile + "_xp")
                || data.contains(prefix + "creativeprofiles_" + profile + "_enderchest")
                || data.contains(prefix + "creativeprofiles_" + profile + "_position")
                || data.contains(prefix + "creativeprofiles_" + profile + "_curios")
                || data.contains(prefix + "creativeprofiles_" + profile + "_cosmetic_armor")
                || AdvancementManager.hasBackupAdvancements(player, prefix, profile);
    }

    private static boolean isBackupOnCooldown(ServerPlayer player) {
        Long lastBackupTime = LAST_BACKUP_TIME.get(player.getUUID());
        return lastBackupTime != null && System.currentTimeMillis() - lastBackupTime < BACKUP_COOLDOWN_MILLIS;
    }

    private static long getRemainingCooldownSeconds(ServerPlayer player) {
        Long lastBackupTime = LAST_BACKUP_TIME.get(player.getUUID());

        if (lastBackupTime == null) {
            return 0;
        }

        long remainingMillis = Math.max(0, BACKUP_COOLDOWN_MILLIS - (System.currentTimeMillis() - lastBackupTime));
        return (remainingMillis + 999L) / 1000L;
    }

    private static String getBackedUpCurrentProfile(CompoundTag data, String prefix) {
        if (!data.contains(prefix + CURRENT_PROFILE)) {
            return "no disponible";
        }

        return data.getString(prefix + CURRENT_PROFILE);
    }

    private static boolean isValidProfile(String profile) {
        return "survival".equals(profile) || "creative".equals(profile);
    }

    private static String yesNo(boolean value) {
        return value ? "si" : "no";
    }

    private static void sendSuccess(CommandSourceStack source, String message) {
        source.sendSuccess(() -> Component.literal(MessageUtil.PREFIX + message), false);
    }

    private static void sendError(CommandSourceStack source, String message) {
        source.sendFailure(Component.literal(MessageUtil.PREFIX + message));
    }
}