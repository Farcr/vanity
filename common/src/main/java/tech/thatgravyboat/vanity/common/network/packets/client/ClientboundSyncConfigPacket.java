package tech.thatgravyboat.vanity.common.network.packets.client;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import tech.thatgravyboat.vanity.client.VanityClientNetwork;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.registries.ModGameRules;

public record ClientboundSyncConfigPacket(
        boolean unlockableDesigns
) implements Packet<ClientboundSyncConfigPacket> {

    public static final ClientboundPacketType<ClientboundSyncConfigPacket> TYPE = new Type();

    public ClientboundSyncConfigPacket(MinecraftServer server) {
        this(
            ModGameRules.UNLOCKABLE_DESIGNS.getValue(server, false)
        );
    }

    @Override
    public PacketType<ClientboundSyncConfigPacket> type() {
        return TYPE;
    }

    private static class Type implements ClientboundPacketType<ClientboundSyncConfigPacket> {

        public static final ResourceLocation ID = new ResourceLocation(Vanity.MOD_ID, "sync_config");

        @Override
        public Class<ClientboundSyncConfigPacket> type() {
            return ClientboundSyncConfigPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }

        @Override
        public void encode(ClientboundSyncConfigPacket message, FriendlyByteBuf buffer) {
            buffer.writeBoolean(message.unlockableDesigns);
        }

        @Override
        public ClientboundSyncConfigPacket decode(FriendlyByteBuf buffer) {
            return new ClientboundSyncConfigPacket(buffer.readBoolean());
        }

        @Override
        public Runnable handle(ClientboundSyncConfigPacket message) {
            return () -> VanityClientNetwork.handleSyncConfig(message);
        }
    }
}
