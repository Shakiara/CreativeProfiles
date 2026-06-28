package com.shakiara.creativeprofiles.managers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class InventoryManager {
    public static void saveInventory(ServerPlayer player, String key) {
        ListTag inventory = new ListTag();
        player.getInventory().save(inventory);
        player.getPersistentData().put(key, inventory);
    }

    public static void loadInventory(ServerPlayer player, String key) {
        ListTag inventory = player.getPersistentData().getList(key, 10);
        player.getInventory().load(inventory);
    }

    public static void saveEnderChest(ServerPlayer player, String key) {
        ListTag savedEnderChest = saveContainer(player.getEnderChestInventory());
        player.getPersistentData().put(key, savedEnderChest);
    }

    public static void loadEnderChestOrEmpty(ServerPlayer player, String key) {
        player.getEnderChestInventory().clearContent();

        if (player.getPersistentData().contains(key)) {
            ListTag savedEnderChest = player.getPersistentData().getList(key, 10);
            loadContainer(player.getEnderChestInventory(), savedEnderChest);
        }

        player.getEnderChestInventory().setChanged();
    }

    private static ListTag saveContainer(Container container) {
        ListTag list = new ListTag();

        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);

            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) slot);

                CompoundTag savedStack = (CompoundTag) stack.save(registryAccess(), itemTag);
                list.add(savedStack);
            }
        }

        return list;
    }

    private static void loadContainer(Container container, ListTag list) {
        container.clearContent();

        for (int i = 0; i < list.size(); i++) {
            CompoundTag itemTag = list.getCompound(i);
            int slot = itemTag.getByte("Slot") & 255;

            if (slot >= 0 && slot < container.getContainerSize()) {
                ItemStack stack = ItemStack.parseOptional(registryAccess(), itemTag);
                container.setItem(slot, stack);
            }
        }

        container.setChanged();
    }

    private static net.minecraft.core.HolderLookup.Provider registryAccess() {
        return ServerLifecycleHooks.getCurrentServer().registryAccess();
    }
}