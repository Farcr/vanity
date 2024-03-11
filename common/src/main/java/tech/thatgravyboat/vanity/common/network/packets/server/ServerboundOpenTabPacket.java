package tech.thatgravyboat.vanity.common.network.packets.server;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.block.StylingTableBlock;
import tech.thatgravyboat.vanity.common.block.StylingTableBlockEntity;
import tech.thatgravyboat.vanity.common.menu.BaseContainerMenu;

import java.util.function.Consumer;

public record ServerboundOpenTabPacket(boolean storage) implements Packet<ServerboundOpenTabPacket> {

    public static final ServerboundPacketType<ServerboundOpenTabPacket> TYPE = new Type();

    @Override
    public PacketType<ServerboundOpenTabPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ServerboundOpenTabPacket> implements ServerboundPacketType<ServerboundOpenTabPacket> {

        public Type() {
            super(
                ServerboundOpenTabPacket.class,
                new ResourceLocation(Vanity.MOD_ID, "open_tab"),
                ByteCodec.BOOLEAN.map(ServerboundOpenTabPacket::new, ServerboundOpenTabPacket::storage)
            );
        }

        @Override
        public Consumer<Player> handle(ServerboundOpenTabPacket message) {
            return (player) -> {
                if (player.containerMenu instanceof BaseContainerMenu menu && player instanceof ServerPlayer serverPlayer) {
                    BlockPos pos = menu.getBlockPos();
                    if (pos == null) return;
                    BlockEntity entity = player.level().getBlockEntity(pos);
                    if (!(entity instanceof StylingTableBlockEntity table)) return;
                    if (message.storage()) {
                        if (!StylingTableBlock.canShowStorage(player.level(), pos)) return;
                        table.storage().openMenu(serverPlayer);
                    } else {
                        table.styling().openMenu(serverPlayer);
                    }
                }
            };
        }
    }
}
