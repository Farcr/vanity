package tech.thatgravyboat.vanity.common.network.packets.client;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import tech.thatgravyboat.vanity.api.design.Design;
import tech.thatgravyboat.vanity.client.design.ClientDesignManager;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.handler.design.DesignManagerImpl;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record ClientboundSyncDesignsPacket(Map<ResourceLocation, Design> designs) implements Packet<ClientboundSyncDesignsPacket> {

    public static final ClientboundPacketType<ClientboundSyncDesignsPacket> TYPE = new Type();

    public ClientboundSyncDesignsPacket(DesignManagerImpl manager) {
        this(manager.getAllDesigns());
    }

    @Override
    public PacketType<ClientboundSyncDesignsPacket> type() {
        return TYPE;
    }

    private static class Type implements ClientboundPacketType<ClientboundSyncDesignsPacket> {

        public static final ResourceLocation ID = new ResourceLocation(Vanity.MOD_ID, "sync_designs");

        @Override
        public Class<ClientboundSyncDesignsPacket> type() {
            return ClientboundSyncDesignsPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }

        @Override
        public void encode(ClientboundSyncDesignsPacket message, FriendlyByteBuf buffer) {
            buffer.writeMap(
                message.designs,
                FriendlyByteBuf::writeResourceLocation,
                (buf, design) -> buf.writeWithCodec(NbtOps.INSTANCE, Design.CODEC, design)
            );
        }

        @Override
        public ClientboundSyncDesignsPacket decode(FriendlyByteBuf buffer) {
            return new ClientboundSyncDesignsPacket(
                buffer.readMap(
                    FriendlyByteBuf::readResourceLocation,
                    buf -> buf.readWithCodec(NbtOps.INSTANCE, Design.CODEC)
                )
            );
        }

        @Override
        public Runnable handle(ClientboundSyncDesignsPacket message) {
            return () -> ClientDesignManager.INSTANCE.readPacket(message);
        }
    }
}
