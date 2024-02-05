package tech.thatgravyboat.vanity.common.network.packets.server;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.block.StylingTableBlock;
import tech.thatgravyboat.vanity.common.block.StylingTableBlockEntity;
import tech.thatgravyboat.vanity.common.menu.BaseContainerMenu;

public record ServerboundOpenTabPacket(boolean storage) implements Packet<ServerboundOpenTabPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Vanity.MOD_ID, "open_tab");
    public static final PacketHandler<ServerboundOpenTabPacket> HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ServerboundOpenTabPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ServerboundOpenTabPacket> {

        @Override
        public void encode(ServerboundOpenTabPacket message, FriendlyByteBuf buffer) {
            buffer.writeBoolean(message.storage());
        }

        @Override
        public ServerboundOpenTabPacket decode(FriendlyByteBuf buffer) {
            return new ServerboundOpenTabPacket(buffer.readBoolean());
        }

        @Override
        public PacketContext handle(ServerboundOpenTabPacket message) {
            return (player, level) -> {
                if (player.containerMenu instanceof BaseContainerMenu menu && player instanceof ServerPlayer) {
                    BlockPos pos = menu.getBlockPos();
                    if (pos == null) return;
                    BlockEntity entity = level.getBlockEntity(pos);
                    if (!(entity instanceof StylingTableBlockEntity table)) return;
                    if (message.storage()) {
                        BlockState state = level.getBlockState(pos);
                        boolean canOpen = !state.hasProperty(StylingTableBlock.POWERED) || !state.getValue(StylingTableBlock.POWERED);
                        if (!canOpen) return;
                        table.storage().openMenu((ServerPlayer) player);
                    } else {
                        table.styling().openMenu((ServerPlayer) player);
                    }
                }
            };
        }
    }
}
