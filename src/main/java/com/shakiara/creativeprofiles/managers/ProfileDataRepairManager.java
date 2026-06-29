package com.shakiara.creativeprofiles.managers;

import com.shakiara.creativeprofiles.compat.CosmeticArmorCompat;
import com.shakiara.creativeprofiles.compat.CuriosCompat;
import com.shakiara.creativeprofiles.util.MessageUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ProfileDataRepairManager {
    private ProfileDataRepairManager() {}

    public static void swapProfileData(CommandSourceStack source, ServerPlayer target) {
        CompoundTag data = target.getPersistentData();

        // Cambio 1.2.x: herramienta admin para reparar datos survival/creative intercambiados.
        swapKey(data, "creativeprofiles_survival_inventory", "creativeprofiles_creative_inventory");
        swapKey(data, "creativeprofiles_survival_xp", "creativeprofiles_creative_xp");
        swapKey(data, "creativeprofiles_survival_enderchest", "creativeprofiles_creative_enderchest");
        swapKey(data, "creativeprofiles_survival_position", "creativeprofiles_creative_position");
        swapKey(data, "creativeprofiles_survival_curios", "creativeprofiles_creative_curios");
        swapKey(data, "creativeprofiles_survival_cosmetic_armor", "creativeprofiles_creative_cosmetic_armor");

        AdvancementManager.swapProfileAdvancements(target, "survival", "creative");

        String currentProfile = data.getString("creativeprofiles_current_profile");
        applyCurrentProfileData(target, currentProfile);

        MessageUtil.info(target, "Un administrador intercambio tus datos guardados survival/creative.");
        sendSuccess(source, "Datos survival/creative intercambiados para " + target.getGameProfile().getName() + ".");
    }

    private static void swapKey(CompoundTag data, String leftKey, String rightKey) {
        boolean hasLeft = data.contains(leftKey);
        boolean hasRight = data.contains(rightKey);

        Tag left = hasLeft ? data.get(leftKey).copy() : null;
        Tag right = hasRight ? data.get(rightKey).copy() : null;

        if (hasRight && right != null) {
            data.put(leftKey, right);
        } else {
            data.remove(leftKey);
        }

        if (hasLeft && left != null) {
            data.put(rightKey, left);
        } else {
            data.remove(rightKey);
        }
    }

    private static void applyCurrentProfileData(ServerPlayer player, String currentProfile) {
        if ("creative".equals(currentProfile)) {
            applyProfileData(player, "creative");
            return;
        }

        if ("survival".equals(currentProfile)) {
            applyProfileData(player, "survival");
        }
    }

    private static void applyProfileData(ServerPlayer player, String profile) {
        CompoundTag data = player.getPersistentData();

        player.getInventory().clearContent();

        if (data.contains("creativeprofiles_" + profile + "_inventory")) {
            InventoryManager.loadInventory(player, "creativeprofiles_" + profile + "_inventory");
        }

        if (data.contains("creativeprofiles_" + profile + "_xp")) {
            XpManager.loadXp(player, "creativeprofiles_" + profile + "_xp");
        } else {
            XpManager.clearXp(player);
        }

        InventoryManager.loadEnderChestOrEmpty(player, "creativeprofiles_" + profile + "_enderchest");
        CuriosCompat.loadCuriosOrEmpty(player, "creativeprofiles_" + profile + "_curios");
        CosmeticArmorCompat.loadCosmeticArmorOrEmpty(player, "creativeprofiles_" + profile + "_cosmetic_armor");
        AdvancementManager.loadAdvancementsOrEmpty(player, profile);
    }

    private static void sendSuccess(CommandSourceStack source, String message) {
        source.sendSuccess(() -> Component.literal(MessageUtil.PREFIX + message), false);
    }
}