package com.shakiara.creativeprofiles.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.shakiara.creativeprofiles.Config;
import com.shakiara.creativeprofiles.CreativeProfiles;
import com.shakiara.creativeprofiles.compat.CosmeticArmorCompat;
import com.shakiara.creativeprofiles.compat.CuriosCompat;
import com.shakiara.creativeprofiles.managers.AdvancementManager;
import com.shakiara.creativeprofiles.managers.BackupManager;
import com.shakiara.creativeprofiles.managers.ProfileDataRepairManager;
import com.shakiara.creativeprofiles.managers.ProfileManager;
import com.shakiara.creativeprofiles.util.MessageUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

public class CpCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("cp")

                        .then(Commands.literal("status")
                                .requires(source -> source.hasPermission(2))
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    showStatus(player, player);
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.argument("player", EntityArgument.player())
                                        .requires(source -> source.hasPermission(4))
                                        .executes(context -> {
                                            ServerPlayer target = EntityArgument.getPlayer(context, "player");
                                            showStatus(context.getSource(), target);
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )

                        .then(Commands.literal("backup")
                                .requires(source -> source.hasPermission(2))
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    BackupManager.createBackup(player);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )

                        .then(Commands.literal("backupstatus")
                                .requires(source -> source.hasPermission(2))
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    BackupManager.showBackupStatus(player);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )

                        .then(Commands.literal("restore")
                                .requires(source -> source.hasPermission(4))
                                .then(Commands.literal("survival")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(context -> {
                                                    ServerPlayer target = EntityArgument.getPlayer(context, "player");
                                                    BackupManager.restoreProfile(context.getSource(), target, "survival");
                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                                .then(Commands.literal("creative")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(context -> {
                                                    ServerPlayer target = EntityArgument.getPlayer(context, "player");
                                                    BackupManager.restoreProfile(context.getSource(), target, "creative");
                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                        )

                        .then(Commands.literal("restoreauto")
                                .requires(source -> source.hasPermission(4))
                                .then(Commands.literal("survival")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(context -> {
                                                    ServerPlayer target = EntityArgument.getPlayer(context, "player");
                                                    BackupManager.restoreAutoProfile(context.getSource(), target, "survival");
                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                                .then(Commands.literal("creative")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(context -> {
                                                    ServerPlayer target = EntityArgument.getPlayer(context, "player");
                                                    BackupManager.restoreAutoProfile(context.getSource(), target, "creative");
                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                        )

                        // Cambio 1.2.x: herramienta nivel 4 para reparar datos survival/creative cruzados.
                        .then(Commands.literal("swapdata")
                                .requires(source -> source.hasPermission(4))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> {
                                            ServerPlayer target = EntityArgument.getPlayer(context, "player");
                                            ProfileDataRepairManager.swapProfileData(context.getSource(), target);
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )

                        .then(Commands.literal("fix")
                                .requires(source -> source.hasPermission(2))
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    repairProfile(player);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )

                        .then(Commands.literal("version")
                                .requires(source -> source.hasPermission(2))
                                .executes(context -> {
                                    showVersion(context.getSource());
                                    return Command.SINGLE_SUCCESS;
                                })
                        )

                        .then(Commands.literal("reload")
                                .requires(source -> source.hasPermission(4))
                                .executes(context -> {
                                    reloadConfig(context.getSource());
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
        );
    }

    private static void showStatus(ServerPlayer viewer, ServerPlayer target) {
        sendStatus(viewer.createCommandSourceStack(), target);
    }

    private static void showStatus(CommandSourceStack source, ServerPlayer target) {
        sendStatus(source, target);
    }

    private static void sendStatus(CommandSourceStack source, ServerPlayer target) {
        CompoundTag data = target.getPersistentData();

        String currentProfile = data.getString("creativeprofiles_current_profile");
        if (currentProfile.isEmpty()) {
            currentProfile = "survival";
        }

        String currentDimension = target.level().dimension().location().toString();
        boolean profileMatchesDimension = profileMatchesDimension(currentProfile, currentDimension);

        send(source, "Status de " + target.getGameProfile().getName());
        send(source, "Perfil actual: " + currentProfile);
        send(source, "Dimension actual: " + currentDimension);
        send(source, "Mundo creativo configurado: " + Config.CREATIVE_WORLD.get());
        send(source, "Mundo survival configurado: " + Config.SURVIVAL_WORLD.get());
        send(source, "Perfil coincide con dimension: " + yesNo(profileMatchesDimension));

        send(source, "Datos survival:");
        send(source, "- Inventario: " + yesNo(data.contains("creativeprofiles_survival_inventory")));
        send(source, "- XP: " + yesNo(data.contains("creativeprofiles_survival_xp")));
        send(source, "- Ender Chest: " + yesNo(data.contains("creativeprofiles_survival_enderchest")));
        send(source, "- Posicion: " + yesNo(data.contains("creativeprofiles_survival_position")));
        send(source, "- Curios: " + yesNo(data.contains("creativeprofiles_survival_curios")));
        send(source, "- Cosmetic Armor: " + yesNo(data.contains("creativeprofiles_survival_cosmetic_armor")));
        send(source, "- Advancements: " + yesNo(AdvancementManager.hasProfileAdvancements(target, "survival")));

        send(source, "Datos creative:");
        send(source, "- Inventario: " + yesNo(data.contains("creativeprofiles_creative_inventory")));
        send(source, "- XP: " + yesNo(data.contains("creativeprofiles_creative_xp")));
        send(source, "- Ender Chest: " + yesNo(data.contains("creativeprofiles_creative_enderchest")));
        send(source, "- Posicion: " + yesNo(data.contains("creativeprofiles_creative_position")));
        send(source, "- Curios: " + yesNo(data.contains("creativeprofiles_creative_curios")));
        send(source, "- Cosmetic Armor: " + yesNo(data.contains("creativeprofiles_creative_cosmetic_armor")));
        send(source, "- Advancements: " + yesNo(AdvancementManager.hasProfileAdvancements(target, "creative")));

        send(source, "Backups:");
        send(source, "- Manual survival: " + yesNo(BackupManager.hasManualProfileBackup(target, data, "survival")));
        send(source, "- Manual creative: " + yesNo(BackupManager.hasManualProfileBackup(target, data, "creative")));
        send(source, "- Automatico survival: " + yesNo(BackupManager.hasAutoProfileBackup(target, data, "survival")));
        send(source, "- Automatico creative: " + yesNo(BackupManager.hasAutoProfileBackup(target, data, "creative")));

        send(source, "Compatibilidad:");
        send(source, "- Curios cargado: " + yesNo(CuriosCompat.isLoaded()));
        send(source, "- Cosmetic Armor Reworked cargado: " + yesNo(CosmeticArmorCompat.isLoaded()));

        if (!profileMatchesDimension) {
            send(source, "Advertencia: el perfil actual no coincide con la dimension actual.");
        }
    }

    private static boolean profileMatchesDimension(String profile, String dimension) {
        boolean inCreativeDimension = ProfileManager.isCreativeDimension(dimension);

        if ("creative".equals(profile)) {
            return inCreativeDimension;
        }

        if ("survival".equals(profile)) {
            return !inCreativeDimension;
        }

        return false;
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

        send(source, "Creative Profiles: " + modVersion);
        send(source, "Minecraft: 1.21.1");
        send(source, "NeoForge: " + neoForgeVersion);
    }

    private static void reloadConfig(CommandSourceStack source) {
        send(source, "Config revisada.");
        send(source, "Mundo creativo actual: " + Config.CREATIVE_WORLD.get());
        send(source, "Mundo survival actual: " + Config.SURVIVAL_WORLD.get());
        send(source, "Nota: si editaste el TOML manualmente, reinicia el servidor para garantizar que todos los cambios se apliquen.");
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
        source.sendSuccess(() -> Component.literal(MessageUtil.PREFIX + message), false);
    }

    private static String yesNo(boolean value) {
        return value ? "si" : "no";
    }
}