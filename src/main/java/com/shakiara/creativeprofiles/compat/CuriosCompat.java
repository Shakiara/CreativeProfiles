package com.shakiara.creativeprofiles.compat;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

public class CuriosCompat {
    private static final String CURIOS_MOD_ID = "curios";

    public static boolean isLoaded() {
        return ModList.get().isLoaded(CURIOS_MOD_ID);
    }

    public static void saveCurios(ServerPlayer player, String key) {
        if (!isLoaded()) {
            return;
        }

        CuriosCompatInternal.saveCurios(player, key);
    }

    public static void loadCuriosOrEmpty(ServerPlayer player, String key) {
        if (!isLoaded()) {
            return;
        }

        CuriosCompatInternal.loadCuriosOrEmpty(player, key);
    }
}