package com.shakiara.creativeprofiles.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.shakiara.creativeprofiles.Config;
import com.shakiara.creativeprofiles.CreativeProfiles;
import com.shakiara.creativeprofiles.managers.ProfileManager;
import com.shakiara.creativeprofiles.managers.XpManager;
import com.shakiara.creativeprofiles.util.MessageUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

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

                        .then(Commands.literal("version")
                                .executes(context -> {
                                    showVersion(context.getSource());
                                    return Command.SINGLE_SUCCESS;
                                })
                        )

                        .then(Commands.literal("reload")
                                .executes(context -> {
                                    reloadConfig(context.getSource());
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

        MessageUtil.info(player, "Status");
        MessageUtil.info(player, "Perfil actual: " + currentProfile);
        MessageUtil.info(player, "Mundo creativo: " + Config.CREATIVE_WORLD.get());
        MessageUtil.info(player, "Mundo survival: " + Config.SURVIVAL_WORLD.get());

        MessageUtil.info(player, "Inventario survival guardado: " + yesNo(data.contains("creativeprofiles_survival_inventory")));
        MessageUtil.info(player, "XP survival guardada: " + yesNo(data.contains("creativeprofiles_survival_xp")));
        MessageUtil.info(player, "Ender Chest survival guardado: " + yesNo(data.contains("creativeprofiles_survival_enderchest")));
        MessageUtil.info(player, "Posición survival guardada: " + yesNo(data.contains("creativeprofiles_survival_position")));

        MessageUtil.info(player, "XP creative guardada: " + yesNo(data.contains("creativeprofiles_creative_xp")));
        MessageUtil.info(player, "Ender Chest creative guardado: " + yesNo(data.contains("creativeprofiles_creative_enderchest")));
        MessageUtil.info(player, "Posición creative guardada: " + yesNo(data.contains("creativeprofiles_creative_position")));
    }

    private static void showVersion(CommandSourceStack source) {
        String modVersion = ModList.get()
                .getModContainerById(CreativeProfiles.MODID)
                .map(container -> container.getModInfo().getVersion().toString())
                .orElse("unknown");

        String neoForgeVersion = ModList.get()
                .getModContainerById("neoforge")
                .map(container -> container.getModInfo().getVersion().toString())
                .orElse("unknown");

        send(source, "§eCreative Profiles: §f" + modVersion);
        send(source, "§eMinecraft: §f1.21.1");
        send(source, "§eNeoForge: §f" + neoForgeVersion);
    }

    private static void reloadConfig(CommandSourceStack source) {
        send(source, "§aConfig revisada.");
        send(source, "§eMundo creativo actual: §f" + Config.CREATIVE_WORLD.get());
        send(source, "§eMundo survival actual: §f" + Config.SURVIVAL_WORLD.get());
        send(source, "§eNota: si editaste el TOML manualmente, reinicia el servidor para garantizar que todos los cambios se apliquen.");
    }

    private static void repairProfile(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();

        String currentDimension = player.level().dimension().location().toString();

        if (ProfileManager.isCreativeDimension(currentDimension)) {
            data.putString("creativeprofiles_current_profile", "creative");
            MessageUtil.success(player, "Perfil actual reparado: creative");
        } else {
            data.putString("creativeprofiles_current_profile", "survival");
            MessageUtil.success(player, "Perfil actual reparado: survival");
        }

        MessageUtil.info(player, "No se borraron inventarios, XP, Ender Chests ni posiciones guardadas.");
    }

    private static void send(CommandSourceStack source, String message) {
        source.sendSuccess(
                () -> Component.literal(MessageUtil.PREFIX + message),
                false
        );
    }

    private static String yesNo(boolean value) {
        return value ? "sí" : "no";
    }
}