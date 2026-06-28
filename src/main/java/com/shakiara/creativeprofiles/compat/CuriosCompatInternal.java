package com.shakiara.creativeprofiles.compat;

import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosCompatInternal {
    public static void saveCurios(ServerPlayer player, String key) {
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            ListTag curiosData = handler.saveInventory(false);
            player.getPersistentData().put(key, curiosData);
        });
    }

    public static void loadCuriosOrEmpty(ServerPlayer player, String key) {
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            clearCuriosSlots(player);

            if (player.getPersistentData().contains(key)) {
                ListTag curiosData = player.getPersistentData().getList(key, 10);
                handler.loadInventory(curiosData);
            } else {
                handler.loadInventory(new ListTag());
            }
        });
    }

    private static void clearCuriosSlots(ServerPlayer player) {
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            handler.getCurios().forEach((slotId, stacksHandler) -> {
                for (int slot = 0; slot < stacksHandler.getStacks().getSlots(); slot++) {
                    stacksHandler.getStacks().setStackInSlot(slot, ItemStack.EMPTY);
                }

                for (int slot = 0; slot < stacksHandler.getCosmeticStacks().getSlots(); slot++) {
                    stacksHandler.getCosmeticStacks().setStackInSlot(slot, ItemStack.EMPTY);
                }
            });
        });
    }
}