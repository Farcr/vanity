package tech.thatgravyboat.vanity.common.network.packets.client;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import tech.thatgravyboat.vanity.client.VanityClientNetwork;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.registries.ModGameRules;

public record ClientboundSyncConfigPacket(
        boolean unlockableDesigns,
        boolean lockDesignStorage
) implements Packet<ClientboundSyncConfigPacket> {

    public static final ClientboundPacketType<ClientboundSyncConfigPacket> TYPE = new Type();

    public ClientboundSyncConfigPacket(MinecraftServer server) {
        this(
            ModGameRules.UNLOCKABLE_DESIGNS.getValue(server, false),
            ModGameRules.LOCK_DESIGN_STORAGE.getValue(server, false)
        );
    }

    @Override
    public PacketType<ClientboundSyncConfigPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundSyncConfigPacket> implements ClientboundPacketType<ClientboundSyncConfigPacket> {

        public Type() {
            super(
                    ClientboundSyncConfigPacket.class,
                new ResourceLocation(Vanity.MOD_ID, "sync_config"),
                ObjectByteCodec.create(
                    ByteCodec.BOOLEAN.fieldOf(ClientboundSyncConfigPacket::unlockableDesigns),
                    ByteCodec.BOOLEAN.fieldOf(ClientboundSyncConfigPacket::lockDesignStorage),
                    ClientboundSyncConfigPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundSyncConfigPacket message) {
            return () -> VanityClientNetwork.handleSyncConfig(message);
        }
    }
}
