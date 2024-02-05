package tech.thatgravyboat.vanity.common.network.packets.client;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import tech.thatgravyboat.vanity.api.concept.ConceptArt;
import tech.thatgravyboat.vanity.client.concept.ClientConceptArtManager;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.handler.concept.ConceptArtManagerImpl;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record ClientboundConceptArtSyncPacket(Map<ResourceLocation, ConceptArt> conceptArt) implements Packet<ClientboundConceptArtSyncPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Vanity.MOD_ID, "concept_art_sync");
    public static final PacketHandler<ClientboundConceptArtSyncPacket> HANDLER = new Handler();

    public ClientboundConceptArtSyncPacket(ConceptArtManagerImpl manager) {
        this(manager.getAllConceptArt());
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ClientboundConceptArtSyncPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ClientboundConceptArtSyncPacket> {

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
        public PacketContext handle(ClientboundConceptArtSyncPacket message) {
            return (player, level) -> ClientConceptArtManager.INSTANCE.readPacket(message);
        }
    }
}
