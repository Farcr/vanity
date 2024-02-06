package tech.thatgravyboat.vanity.common.network.packets.server;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.menu.StylingMenu;

import java.util.function.Consumer;

public record ServerboundSelectStylePacket(ResourceLocation design, @Nullable String style) implements Packet<ServerboundSelectStylePacket> {

    public static final ServerboundPacketType<ServerboundSelectStylePacket> TYPE = new Type();

    @Override
    public PacketType<ServerboundSelectStylePacket> type() {
        return TYPE;
    }

    private static class Type implements ServerboundPacketType<ServerboundSelectStylePacket> {

        public static final ResourceLocation ID = new ResourceLocation(Vanity.MOD_ID, "select_style");

        @Override
        public Class<ServerboundSelectStylePacket> type() {
            return ServerboundSelectStylePacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }

        @Override
        public void encode(ServerboundSelectStylePacket message, FriendlyByteBuf buffer) {
            buffer.writeResourceLocation(message.design());
            buffer.writeNullable(message.style(), FriendlyByteBuf::writeUtf);
        }

        @Override
        public ServerboundSelectStylePacket decode(FriendlyByteBuf buffer) {
            return new ServerboundSelectStylePacket(buffer.readResourceLocation(), buffer.readNullable(FriendlyByteBuf::readUtf));
        }

        @Override
        public Consumer<Player> handle(ServerboundSelectStylePacket message) {
            return (player) -> {
                if (player.containerMenu instanceof StylingMenu stylingMenu) {
                    stylingMenu.select(message.design(), message.style());
                }
            };
        }
    }
}
