package gg.moonflower.vanity.common.network;

import gg.moonflower.pollen.api.network.v1.PollinatedLoginNetworkChannel;
import gg.moonflower.pollen.api.network.v1.PollinatedPlayNetworkChannel;
import gg.moonflower.pollen.api.network.v1.packet.PollinatedPacketDirection;
import gg.moonflower.pollen.api.network.v1.packet.login.ServerboundAckPacket;
import gg.moonflower.pollen.api.registry.network.v1.PollinatedNetworkRegistry;
import gg.moonflower.vanity.common.concept.ServerConceptArtManager;
import gg.moonflower.vanity.common.network.common.message.ClientboundConceptArtSyncPacket;
import gg.moonflower.vanity.core.Vanity;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;

public class VanityMessages {
    public static final String PROTOCOL_VERSION = "1";
    public static final PollinatedLoginNetworkChannel LOGIN = PollinatedNetworkRegistry.createLogin(new ResourceLocation(Vanity.MOD_ID, "login"), PROTOCOL_VERSION);
    public static final PollinatedPlayNetworkChannel PLAY = PollinatedNetworkRegistry.createPlay(new ResourceLocation(Vanity.MOD_ID, "play"), PROTOCOL_VERSION);

    public static void init() {
        // Register Play
        PLAY.register(ClientboundConceptArtSyncPacket.class, ClientboundConceptArtSyncPacket::new, PollinatedPacketDirection.PLAY_CLIENTBOUND);

        // Register Login
        LOGIN.register(ServerboundAckPacket.class, ServerboundAckPacket::new);
        LOGIN.registerLogin(ClientboundConceptArtSyncPacket.class, ClientboundConceptArtSyncPacket::new, isLocal -> Collections.singletonList(Pair.of(ClientboundConceptArtSyncPacket.class.getName(), ServerConceptArtManager.INSTANCE.createPacket())));
    }
}