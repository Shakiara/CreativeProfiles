package com.shakiara.creativeprofiles.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.shakiara.creativeprofiles.Config;
import com.shakiara.creativeprofiles.managers.ProfileManager;
import com.shakiara.creativeprofiles.managers.XpManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CpCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("cp")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("status")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    showStatus(player);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(Commands.literal("backup")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    ProfileManager.backupCurrentProfile(player);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(Commands.literal("fix")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    repairProfile(player);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
        );
    }

    private static void showStatus(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();

        String currentProfile = data.getString("creativeprofiles_current_profile");
        if (currentProfile.isEmpty()) {
            currentProfile = "survival";
        }

        player.sendSystemMessage(Component.literal("§6Creative Profiles Status"));
        player.sendSystemMessage(Component.literal("§ePerfil actual: §f" + currentProfile));
        player.sendSystemMessage(Component.literal("§eMundo creativo config: §f" + Config.CREATIVE_WORLD.get()));
        player.sendSystemMessage(Component.literal("§eMundo survival config: §f" + Config.SURVIVAL_WORLD.get()));
        player.sendSystemMessage(Component.literal("§eInventario survival guardado: §f" + yesNo(data.contains("creativeprofiles_survival_inventory"))));
        player.sendSystemMessage(Component.literal("§eXP survival guardada: §f" + yesNo(data.contains("creativeprofiles_survival_xp"))));
        player.sendSystemMessage(Component.literal("§eEnder Chest survival guardado: §f" + yesNo(data.contains("creativeprofiles_survival_enderchest"))));
        player.sendSystemMessage(Component.literal("§ePosición survival guardada: §f" + yesNo(data.contains("creativeprofiles_survival_position"))));
        player.sendSystemMessage(Component.literal("§eXP creative guardada: §f" + yesNo(data.contains("creativeprofiles_creative_xp"))));
        player.sendSystemMessage(Component.literal("§eEnder Chest creative guardado: §f" + yesNo(data.contains("creativeprofiles_creative_enderchest"))));
        player.sendSystemMessage(Component.literal("§ePosición creative guardada: §f" + yesNo(data.contains("creativeprofiles_creative_position"))));
    }

    private static void repairProfile(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();

        data.remove("creativeprofiles_current_profile");

        String currentDimension = player.level().dimension().location().toString();

        if (ProfileManager.isCreativeDimension(currentDimension)) {
            data.putString("creativeprofiles_current_profile", "creative");
            player.sendSystemMessage(Component.literal("§ePerfil actual reajustado a: creative"));
        } else {
            data.putString("creativeprofiles_current_profile", "survival");
            player.sendSystemMessage(Component.literal("§ePerfil actual reajustado a: survival"));
        }

        player.sendSystemMessage(Component.literal("§aEstado de Creative Profiles reparado."));
        player.sendSystemMessage(Component.literal("§eNo se borraron inventarios, XP, Ender Chests ni posiciones guardadas."));
    }

    private static String yesNo(boolean value) {
        return value ? "sí" : "no";
    }
}