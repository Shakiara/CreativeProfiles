package com.shakiara.creativeprofiles.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.shakiara.creativeprofiles.managers.ProfileManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class CommandRegistry {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("creative")
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            ProfileManager.enterCreative(player);
                            return Command.SINGLE_SUCCESS;
                        })
        );

        dispatcher.register(
                Commands.literal("survival")
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            ProfileManager.returnToSurvival(player);
                            return Command.SINGLE_SUCCESS;
                        })
        );

        CpCommand.register(dispatcher);
    }
}