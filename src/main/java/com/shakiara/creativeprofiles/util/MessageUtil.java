package com.shakiara.creativeprofiles.util;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class MessageUtil {
    public static final String PREFIX = "§6[Creative Profiles] §r";

    public static void success(ServerPlayer player, String message) {
        player.sendSystemMessage(Component.literal(PREFIX + "§a" + message));
    }

    public static void info(ServerPlayer player, String message) {
        player.sendSystemMessage(Component.literal(PREFIX + "§e" + message));
    }

    public static void error(ServerPlayer player, String message) {
        player.sendSystemMessage(Component.literal(PREFIX + "§c" + message));
    }
}