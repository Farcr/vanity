package tech.thatgravyboat.vanity.common.network;

import com.teamresourceful.resourcefullib.common.networking.NetworkChannel;
import com.teamresourceful.resourcefullib.common.networking.base.NetworkDirection;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundConceptArtSyncPacket;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundSyncEntityItemPacket;
import tech.thatgravyboat.vanity.common.network.packets.server.ServerboundOpenTabPacket;
import tech.thatgravyboat.vanity.common.network.packets.server.ServerboundSelectStylePacket;

public class NetworkHandler {
    public static final int PROTOCOL_VERSION = 1;

    public static final NetworkChannel CHANNEL = new NetworkChannel(Vanity.MOD_ID, PROTOCOL_VERSION, "main");

    public static void init() {
        CHANNEL.registerPacket(NetworkDirection.SERVER_TO_CLIENT, ClientboundConceptArtSyncPacket.ID, ClientboundConceptArtSyncPacket.HANDLER, ClientboundConceptArtSyncPacket.class);
        CHANNEL.registerPacket(NetworkDirection.SERVER_TO_CLIENT, ClientboundSyncEntityItemPacket.ID, ClientboundSyncEntityItemPacket.HANDLER, ClientboundSyncEntityItemPacket.class);

        CHANNEL.registerPacket(NetworkDirection.CLIENT_TO_SERVER, ServerboundSelectStylePacket.ID, ServerboundSelectStylePacket.HANDLER, ServerboundSelectStylePacket.class);
        CHANNEL.registerPacket(NetworkDirection.CLIENT_TO_SERVER, ServerboundOpenTabPacket.ID, ServerboundOpenTabPacket.HANDLER, ServerboundOpenTabPacket.class);
    }
}