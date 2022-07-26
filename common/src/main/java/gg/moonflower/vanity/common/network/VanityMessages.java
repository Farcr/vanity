package gg.moonflower.vanity.common.network;

import gg.moonflower.pollen.api.network.PollinatedLoginNetworkChannel;
import gg.moonflower.pollen.api.network.PollinatedPlayNetworkChannel;
import gg.moonflower.pollen.api.network.packet.PollinatedPacketDirection;
import gg.moonflower.pollen.api.network.packet.login.ServerboundAckPacket;
import gg.moonflower.pollen.api.registry.NetworkRegistry;
import gg.moonflower.vanity.common.concept.ServerConceptArtManager;
import gg.moonflower.vanity.common.network.common.message.ClientboundConceptArtSyncPacket;
import gg.moonflower.vanity.common.network.login.handler.VanityClientLoginHandlerImpl;
import gg.moonflower.vanity.common.network.play.handler.VanityClientPlayHandlerImpl;
import gg.moonflower.vanity.core.Vanity;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;

public class VanityMessages {
    public static final String PROTOCOL_VERSION = "1";
    public static final PollinatedLoginNetworkChannel LOGIN = NetworkRegistry.createLogin(new ResourceLocation(Vanity.MOD_ID, "login"), PROTOCOL_VERSION, VanityClientLoginHandlerImpl::new, Object::new);
    public static final PollinatedPlayNetworkChannel PLAY = NetworkRegistry.createPlay(new ResourceLocation(Vanity.MOD_ID, "play"), PROTOCOL_VERSION, VanityClientPlayHandlerImpl::new, Object::new);

    public static void init() {
        PLAY.register(ClientboundConceptArtSyncPacket.class, ClientboundConceptArtSyncPacket::new, PollinatedPacketDirection.PLAY_CLIENTBOUND);
        LOGIN.registerLogin(ClientboundConceptArtSyncPacket.class, ClientboundConceptArtSyncPacket::new, isLocal -> Collections.singletonList(Pair.of(ClientboundConceptArtSyncPacket.class.getName(), ServerConceptArtManager.INSTANCE.createPacket())));
        LOGIN.register(ServerboundAckPacket.class, ServerboundAckPacket::new);
    }
}