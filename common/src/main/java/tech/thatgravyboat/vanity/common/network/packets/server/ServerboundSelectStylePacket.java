package tech.thatgravyboat.vanity.common.network.packets.server;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.bytecodecs.ExtraByteCodecs;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.menu.StylingMenu;

import java.util.Optional;
import java.util.function.Consumer;

public record ServerboundSelectStylePacket(ResourceLocation design, Optional<String> style) implements Packet<ServerboundSelectStylePacket> {

    public static final ServerboundPacketType<ServerboundSelectStylePacket> TYPE = new Type();

    public ServerboundSelectStylePacket(ResourceLocation design, @Nullable String style) {
        this(design, Optional.ofNullable(style));
    }

    @Override
    public PacketType<ServerboundSelectStylePacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ServerboundSelectStylePacket> implements ServerboundPacketType<ServerboundSelectStylePacket> {

        public Type() {
            super(
                ServerboundSelectStylePacket.class,
                new ResourceLocation(Vanity.MOD_ID, "select_style"),
                ObjectByteCodec.create(
                    ExtraByteCodecs.RESOURCE_LOCATION.fieldOf(ServerboundSelectStylePacket::design),
                    ByteCodec.STRING.optionalFieldOf(ServerboundSelectStylePacket::style),
                    ServerboundSelectStylePacket::new
                )
            );
        }

        @Override
        public Consumer<Player> handle(ServerboundSelectStylePacket message) {
            return (player) -> {
                if (player.containerMenu instanceof StylingMenu stylingMenu) {
                    stylingMenu.select(message.design(), message.style().orElse(null));
                }
            };
        }
    }
}
