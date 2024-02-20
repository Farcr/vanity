package tech.thatgravyboat.vanity.common.network.packets.server;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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

    private static class Type implements ServerboundPacketType<ServerboundOpenTabPacket> {

        public static final ResourceLocation ID = new ResourceLocation(Vanity.MOD_ID, "open_tab");

        @Override
        public Class<ServerboundOpenTabPacket> type() {
            return ServerboundOpenTabPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }

        @Override
        public void encode(ServerboundOpenTabPacket message, FriendlyByteBuf buffer) {
            buffer.writeBoolean(message.storage());
        }

        @Override
        public ServerboundOpenTabPacket decode(FriendlyByteBuf buffer) {
            return new ServerboundOpenTabPacket(buffer.readBoolean());
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
                        BlockState state = player.level().getBlockState(pos);
                        boolean canOpen = !state.hasProperty(StylingTableBlock.POWERED) || !state.getValue(StylingTableBlock.POWERED);
                        if (!canOpen) return;
                        table.storage().openMenu(serverPlayer);
                    } else {
                        table.styling().openMenu(serverPlayer);
                    }
                }
            };
        }
    }
}
