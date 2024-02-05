package tech.thatgravyboat.vanity.common.menu.content;

import com.teamresourceful.resourcefullib.common.menu.MenuContent;
import com.teamresourceful.resourcefullib.common.menu.MenuContentSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public record StylingMenuContent(
        BlockPos position,
        List<ResourceLocation> unlockables
) implements MenuContent<StylingMenuContent> {

    public static final MenuContentSerializer<StylingMenuContent> SERIALIZER = new Serializer();

    public static Function<StylingMenuContent, ContainerLevelAccess> access(Player player) {
        return content -> ContainerLevelAccess.create(player.level(), content.position());
    }

    @Override
    public MenuContentSerializer<StylingMenuContent> serializer() {
        return SERIALIZER;
    }

    private static class Serializer implements MenuContentSerializer<StylingMenuContent> {

        @Override
        public @Nullable StylingMenuContent from(FriendlyByteBuf buffer) {
            return new StylingMenuContent(
                    buffer.readBlockPos(),
                    buffer.readCollection(ArrayList::new, FriendlyByteBuf::readResourceLocation)
            );
        }

        @Override
        public void to(FriendlyByteBuf buffer, StylingMenuContent content) {
            buffer.writeBlockPos(content.position);
            buffer.writeCollection(content.unlockables, FriendlyByteBuf::writeResourceLocation);
        }
    }
}
