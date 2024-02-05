package tech.thatgravyboat.vanity.common.network.packets.server;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.menu.StylingMenu;

public record ServerboundSelectStylePacket(ResourceLocation concept, @Nullable String style) implements Packet<ServerboundSelectStylePacket> {

    public static final ResourceLocation ID = new ResourceLocation(Vanity.MOD_ID, "select_style");
    public static final PacketHandler<ServerboundSelectStylePacket> HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ServerboundSelectStylePacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ServerboundSelectStylePacket> {

        @Override
        public void encode(ServerboundSelectStylePacket message, FriendlyByteBuf buffer) {
            buffer.writeResourceLocation(message.concept());
            buffer.writeNullable(message.style(), FriendlyByteBuf::writeUtf);
        }

        @Override
        public ServerboundSelectStylePacket decode(FriendlyByteBuf buffer) {
            return new ServerboundSelectStylePacket(buffer.readResourceLocation(), buffer.readNullable(FriendlyByteBuf::readUtf));
        }

        @Override
        public PacketContext handle(ServerboundSelectStylePacket message) {
            return (player, level) -> {
                if (player.containerMenu instanceof StylingMenu stylingMenu) {
                    stylingMenu.select(message.concept(), message.style());
                }
            };
        }
    }
}
