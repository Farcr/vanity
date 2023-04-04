package gg.moonflower.vanity.common.network.common.handler;

import gg.moonflower.pollen.api.network.v1.packet.PollinatedPacketContext;
import gg.moonflower.vanity.common.network.common.message.ClientboundConceptArtSyncPacket;

public interface VanityClientCommonHandler {

    void handleConceptArtSync(ClientboundConceptArtSyncPacket pkt, PollinatedPacketContext ctx);
}
