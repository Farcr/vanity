package tech.thatgravyboat.vanity.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import tech.thatgravyboat.vanity.client.design.ClientDesignManager;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundSyncConfigPacket;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundSyncDesignsPacket;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundSyncEntityItemPacket;
import tech.thatgravyboat.vanity.common.registries.ModGameRules;
import tech.thatgravyboat.vanity.common.util.EntityItemHolder;

public class VanityClientNetwork {

    public static void handleSyncEntityItem(ClientboundSyncEntityItemPacket packet) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        Entity entity = level.getEntity(packet.entityId());
        if (entity instanceof EntityItemHolder holder) {
            holder.vanity$setItem(packet.stack());
        }
    }

    public static void handleSyncDesigns(ClientboundSyncDesignsPacket packet) {
        ClientDesignManager.INSTANCE.readPacket(packet);
    }

    public static void handleSyncConfig(ClientboundSyncConfigPacket packet) {
        ModGameRules.UNLOCKABLE_DESIGNS.setValue(packet.unlockableDesigns());
        ModGameRules.LOCK_DESIGN_STORAGE.setValue(packet.lockDesignStorage());
    }
}
