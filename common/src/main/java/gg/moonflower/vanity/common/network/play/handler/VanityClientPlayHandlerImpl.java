package gg.moonflower.vanity.common.network.play.handler;

import gg.moonflower.pollen.api.network.packet.PollinatedPacketContext;
import gg.moonflower.vanity.client.concept.ClientConceptArtManager;
import gg.moonflower.vanity.common.network.common.message.ClientboundConceptArtSyncPacket;

public class VanityClientPlayHandlerImpl implements VanityClientPlayHandler {
    @Override
    public void handleConceptArtSync(ClientboundConceptArtSyncPacket pkt, PollinatedPacketContext ctx) {
        ctx.enqueueWork(() -> ClientConceptArtManager.INSTANCE.readPacket(pkt));
    }
}
