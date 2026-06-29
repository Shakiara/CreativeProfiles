package com.shakiara.creativeprofiles.events;

import com.shakiara.creativeprofiles.managers.BackupManager;
import com.shakiara.creativeprofiles.managers.ProfileManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class PlayerEvents {
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ProfileManager.syncProfileOnLogin(player);

            // Cambio 1.2.x: backup automatico al entrar al servidor.
            BackupManager.createAutoBackup(player, "login");
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ProfileManager.saveCurrentProfileOnLogout(player);
        }
    }

    @SubscribeEvent
    public void onTravelToDimension(EntityTravelToDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (ProfileManager.isInternalTeleport(player)) {
            return;
        }

        String fromDimension = player.level().dimension().location().toString();
        String toDimension = event.getDimension().location().toString();

        boolean fromCreativeWorld = ProfileManager.isCreativeDimension(fromDimension);
        boolean toCreativeWorld = ProfileManager.isCreativeDimension(toDimension);

        if (fromCreativeWorld != toCreativeWorld) {
            event.setCanceled(true);
            player.sendSystemMessage(Component.literal("Usa /creative o /survival para cambiar entre perfiles."));
        }
    }
}