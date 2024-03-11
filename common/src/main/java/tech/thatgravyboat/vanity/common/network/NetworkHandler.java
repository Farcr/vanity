package tech.thatgravyboat.vanity.common.network;

import com.teamresourceful.resourcefullib.common.network.Network;
import net.minecraft.resources.ResourceLocation;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundSyncConfigPacket;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundSyncDesignsPacket;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundSyncEntityItemPacket;
import tech.thatgravyboat.vanity.common.network.packets.server.ServerboundOpenTabPacket;
import tech.thatgravyboat.vanity.common.network.packets.server.ServerboundSelectStylePacket;

public class NetworkHandler {
    public static final int PROTOCOL_VERSION = 1;

    public static final Network CHANNEL = new Network(new ResourceLocation(Vanity.MOD_ID, "main"), PROTOCOL_VERSION);

    public static void init() {
        CHANNEL.register(ClientboundSyncDesignsPacket.TYPE);
        CHANNEL.register(ClientboundSyncEntityItemPacket.TYPE);
        CHANNEL.register(ClientboundSyncConfigPacket.TYPE);

        CHANNEL.register(ServerboundSelectStylePacket.TYPE);
        CHANNEL.register(ServerboundOpenTabPacket.TYPE);
    }
}