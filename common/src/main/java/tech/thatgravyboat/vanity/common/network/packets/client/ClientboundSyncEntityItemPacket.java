package tech.thatgravyboat.vanity.common.network.packets.client;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import tech.thatgravyboat.vanity.client.VanityClientNetwork;
import tech.thatgravyboat.vanity.common.Vanity;

public record ClientboundSyncEntityItemPacket(int entityId, ItemStack stack) implements Packet<ClientboundSyncEntityItemPacket> {

    public static final ClientboundPacketType<ClientboundSyncEntityItemPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundSyncEntityItemPacket> type() {
        return TYPE;
    }

    private static class Type implements ClientboundPacketType<ClientboundSyncEntityItemPacket> {

        public static final ResourceLocation ID = new ResourceLocation(Vanity.MOD_ID, "sync_entity_item");

        @Override
        public Class<ClientboundSyncEntityItemPacket> type() {
            return ClientboundSyncEntityItemPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }

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
        public Runnable handle(ClientboundSyncEntityItemPacket message) {
            return () -> VanityClientNetwork.handleSyncEntityItem(message);
        }
    }
}
