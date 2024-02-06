package tech.thatgravyboat.vanity.common.network;

import com.teamresourceful.resourcefullib.common.network.NetworkChannel;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundSyncDesignsPacket;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundSyncEntityItemPacket;
import tech.thatgravyboat.vanity.common.network.packets.server.ServerboundOpenTabPacket;
import tech.thatgravyboat.vanity.common.network.packets.server.ServerboundSelectStylePacket;

@SuppressWarnings("UnstableApiUsage")
public class NetworkHandler {
    public static final int PROTOCOL_VERSION = 1;

    public static final NetworkChannel CHANNEL = new NetworkChannel(Vanity.MOD_ID, PROTOCOL_VERSION, "main");

    public static void init() {
        CHANNEL.register(ClientboundSyncDesignsPacket.TYPE);
        CHANNEL.register(ClientboundSyncEntityItemPacket.TYPE);

        CHANNEL.register(ServerboundSelectStylePacket.TYPE);
        CHANNEL.register(ServerboundOpenTabPacket.TYPE);
    }
}