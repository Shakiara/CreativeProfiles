package com.shakiara.creativeprofiles.compat;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

public class CosmeticArmorCompat {
    private static final String MOD_ID = "cosmeticarmorreworked";

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }

    public static void saveCosmeticArmor(ServerPlayer player, String key) {
        if (!isLoaded()) {
            return;
        }

        CosmeticArmorCompatInternal.saveCosmeticArmor(player, key);
    }

    public static void loadCosmeticArmorOrEmpty(ServerPlayer player, String key) {
        if (!isLoaded()) {
            return;
        }

        CosmeticArmorCompatInternal.loadCosmeticArmorOrEmpty(player, key);
    }
}