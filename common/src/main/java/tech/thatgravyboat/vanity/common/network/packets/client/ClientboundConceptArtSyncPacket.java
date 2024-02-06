package tech.thatgravyboat.vanity.common.network.packets.client;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import tech.thatgravyboat.vanity.api.concept.ConceptArt;
import tech.thatgravyboat.vanity.client.concept.ClientConceptArtManager;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.handler.concept.ConceptArtManagerImpl;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record ClientboundConceptArtSyncPacket(Map<ResourceLocation, ConceptArt> conceptArt) implements Packet<ClientboundConceptArtSyncPacket> {

    public static final ClientboundPacketType<ClientboundConceptArtSyncPacket> TYPE = new Type();

    public ClientboundConceptArtSyncPacket(ConceptArtManagerImpl manager) {
        this(manager.getAllConceptArt());
    }

    @Override
    public PacketType<ClientboundConceptArtSyncPacket> type() {
        return TYPE;
    }

    private static class Type implements ClientboundPacketType<ClientboundConceptArtSyncPacket> {

        public static final ResourceLocation ID = new ResourceLocation(Vanity.MOD_ID, "concept_art_sync");

        @Override
        public Class<ClientboundConceptArtSyncPacket> type() {
            return ClientboundConceptArtSyncPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }

        @Override
        public void encode(ClientboundConceptArtSyncPacket message, FriendlyByteBuf buffer) {
            buffer.writeMap(
                message.conceptArt,
                FriendlyByteBuf::writeResourceLocation,
                (buf, art) -> buf.writeWithCodec(NbtOps.INSTANCE, ConceptArt.CODEC, art)
            );
        }

        @Override
        public ClientboundConceptArtSyncPacket decode(FriendlyByteBuf buffer) {
            return new ClientboundConceptArtSyncPacket(
                buffer.readMap(
                    FriendlyByteBuf::readResourceLocation,
                    buf -> buf.readWithCodec(NbtOps.INSTANCE, ConceptArt.CODEC)
                )
            );
        }

        @Override
        public Runnable handle(ClientboundConceptArtSyncPacket message) {
            return () -> ClientConceptArtManager.INSTANCE.readPacket(message);
        }
    }
}
