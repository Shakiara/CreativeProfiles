package com.shakiara.creativeprofiles.compat;

import lain.mods.cos.api.CosArmorAPI;
import lain.mods.cos.api.inventory.CAStacksBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class CosmeticArmorCompatInternal {
    public static void saveCosmeticArmor(ServerPlayer player, String key) {
        CAStacksBase stacks = CosArmorAPI.getCAStacks(player.getUUID());

        if (stacks == null) {
            return;
        }

        CompoundTag tag = stacks.serializeNBT(registryAccess());
        player.getPersistentData().put(key, tag);
    }

    public static void loadCosmeticArmorOrEmpty(ServerPlayer player, String key) {
        CAStacksBase stacks = CosArmorAPI.getCAStacks(player.getUUID());

        if (stacks == null) {
            return;
        }

        clearCosmeticArmor(stacks);

        if (player.getPersistentData().contains(key)) {
            CompoundTag tag = player.getPersistentData().getCompound(key);
            stacks.deserializeNBT(registryAccess(), tag);
        } else {
            stacks.deserializeNBT(registryAccess(), new CompoundTag());
        }
    }

    private static void clearCosmeticArmor(CAStacksBase stacks) {
        for (int slot = 0; slot < stacks.getSlots(); slot++) {
            stacks.setStackInSlot(slot, ItemStack.EMPTY);
            stacks.setSkinArmor(slot, false);
        }
    }

    private static net.minecraft.core.HolderLookup.Provider registryAccess() {
        return ServerLifecycleHooks.getCurrentServer().registryAccess();
    }
}