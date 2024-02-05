package tech.thatgravyboat.vanity.common.network;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import tech.thatgravyboat.vanity.common.entities.EntityItemHolder;
import tech.thatgravyboat.vanity.common.Vanity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public record ClientboundSyncEntityItemPacket(int entityId, ItemStack stack) implements Packet<ClientboundSyncEntityItemPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Vanity.MOD_ID, "sync_entity_item");
    public static final PacketHandler<ClientboundSyncEntityItemPacket> HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ClientboundSyncEntityItemPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ClientboundSyncEntityItemPacket> {

        @Override
        public void encode(ClientboundSyncEntityItemPacket message, FriendlyByteBuf buffer) {
            buffer.writeInt(message.entityId());
            buffer.writeItem(message.stack());
        }

        @Override
        public ClientboundSyncEntityItemPacket decode(FriendlyByteBuf buffer) {
            return new ClientboundSyncEntityItemPacket(buffer.readInt(), buffer.readItem());
        }

        @Override
        public PacketContext handle(ClientboundSyncEntityItemPacket message) {
            return (player, level) -> {
                Entity entity = level.getEntity(message.entityId());
                if (entity instanceof EntityItemHolder holder) {
                    holder.vanity$setItem(message.stack());
                }
            };
        }
    }
}
