package com.shakiara.creativeprofiles.util;

import net.minecraft.server.level.ServerPlayer;

public class CommandUtil {
    public static final String INTERNAL_TELEPORT_KEY = "creativeprofiles_internal_teleport";

    public static void runCommand(ServerPlayer player, String command) {
        player.getPersistentData().putBoolean(INTERNAL_TELEPORT_KEY, true);

        try {
            player.getServer().getCommands().performPrefixedCommand(
                    player.createCommandSourceStack().withPermission(4),
                    command
            );
        } finally {
            player.getPersistentData().remove(INTERNAL_TELEPORT_KEY);
        }
    }
}