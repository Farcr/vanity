package gg.moonflower.vanity.common.network.common.message;

import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import gg.moonflower.pollen.api.network.packet.login.SimplePollinatedLoginPacket;
import gg.moonflower.vanity.api.concept.ConceptArtManager;
import gg.moonflower.vanity.api.concept.ConceptArt;
import gg.moonflower.vanity.common.network.common.handler.VanityClientCommonHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClientboundConceptArtSyncPacket extends SimplePollinatedLoginPacket<VanityClientCommonHandler> {

    private final Map<ResourceLocation, ConceptArt> conceptArt = new HashMap<>();

    public ClientboundConceptArtSyncPacket(ConceptArtManager manager) {
        this.conceptArt.putAll(manager.getAllConceptArtIds()
            .filter(entry -> manager.getConceptArt(entry).isPresent())
            .collect(Collectors.toMap(Function.identity(), entry -> manager.getConceptArt(entry).orElseThrow(() -> new IllegalStateException("The concept art '" + entry + "' is not present")))));
    }

    public ClientboundConceptArtSyncPacket(FriendlyByteBuf buf) {
        try {
            int count = buf.readVarInt();
            for (int i = 0; i < count; i++) {
                this.conceptArt.put(buf.readResourceLocation(), buf.readWithCodec(ConceptArt.CODEC));
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read concept art", e);
        }
    }

    @Override
    public void writePacketData(FriendlyByteBuf buf) throws IOException {
        buf.writeVarInt(this.conceptArt.size());
        for (Map.Entry<ResourceLocation, ConceptArt> entry : this.conceptArt.entrySet()) {
            try {
                buf.writeResourceLocation(entry.getKey());
                buf.writeWithCodec(ConceptArt.CODEC, entry.getValue());
            } catch (Exception e) {
                throw new IOException("Failed to write concept art: " + entry.getKey());
            }
        }
    }

    @Override
    public void processPacket(VanityClientCommonHandler handler, PollinatedPacketContext ctx) {
        handler.handleConceptArtSync(this, ctx);
    }

    public Map<ResourceLocation, ConceptArt> getConceptArt() {
        return conceptArt;
    }
}
