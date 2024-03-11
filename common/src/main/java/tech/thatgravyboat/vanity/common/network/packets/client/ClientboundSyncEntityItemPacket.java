package tech.thatgravyboat.vanity.common.network.packets.client;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.bytecodecs.ExtraByteCodecs;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
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

    private static class Type extends CodecPacketType<ClientboundSyncEntityItemPacket> implements ClientboundPacketType<ClientboundSyncEntityItemPacket> {

        public Type() {
            super(
                ClientboundSyncEntityItemPacket.class,
                new ResourceLocation(Vanity.MOD_ID, "sync_entity_item"),
                ObjectByteCodec.create(
                        ByteCodec.VAR_INT.fieldOf(ClientboundSyncEntityItemPacket::entityId),
                        ExtraByteCodecs.ITEM_STACK.fieldOf(ClientboundSyncEntityItemPacket::stack),
                        ClientboundSyncEntityItemPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundSyncEntityItemPacket message) {
            return () -> VanityClientNetwork.handleSyncEntityItem(message);
        }
    }
}
