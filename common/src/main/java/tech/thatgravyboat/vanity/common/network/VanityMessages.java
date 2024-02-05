package tech.thatgravyboat.vanity.common.network;

import com.teamresourceful.resourcefullib.common.networking.NetworkChannel;
import com.teamresourceful.resourcefullib.common.networking.base.NetworkDirection;
import tech.thatgravyboat.vanity.common.Vanity;

public class VanityMessages {
    public static final int PROTOCOL_VERSION = 1;

    public static final NetworkChannel CHANNEL = new NetworkChannel(Vanity.MOD_ID, PROTOCOL_VERSION, "main");

    public static void init() {
        CHANNEL.registerPacket(NetworkDirection.SERVER_TO_CLIENT, ClientboundConceptArtSyncPacket.ID, ClientboundConceptArtSyncPacket.HANDLER, ClientboundConceptArtSyncPacket.class);
        CHANNEL.registerPacket(NetworkDirection.SERVER_TO_CLIENT, ClientboundSyncEntityItemPacket.ID, ClientboundSyncEntityItemPacket.HANDLER, ClientboundSyncEntityItemPacket.class);
    }
}