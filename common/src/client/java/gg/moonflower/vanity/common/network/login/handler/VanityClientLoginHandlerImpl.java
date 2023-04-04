package gg.moonflower.vanity.common.network.login.handler;

import gg.moonflower.pollen.api.network.v1.packet.PollinatedPacketContext;
import gg.moonflower.pollen.api.network.v1.packet.login.ServerboundAckPacket;
import gg.moonflower.vanity.client.concept.ClientConceptArtManager;
import gg.moonflower.vanity.common.network.common.message.ClientboundConceptArtSyncPacket;

public class VanityClientLoginHandlerImpl implements VanityClientLoginHandler {

    @Override
    public void handleConceptArtSync(ClientboundConceptArtSyncPacket pkt, PollinatedPacketContext ctx) {
        ctx.enqueueWork(() -> ClientConceptArtManager.INSTANCE.readPacket(pkt));
        ctx.reply(new ServerboundAckPacket());
    }
}
