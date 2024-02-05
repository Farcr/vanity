package tech.thatgravyboat.vanity.common.menu.content;

import com.teamresourceful.resourcefullib.common.menu.MenuContent;
import com.teamresourceful.resourcefullib.common.menu.MenuContentSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public record StorageMenuContent(
        BlockPos position
) implements MenuContent<StorageMenuContent> {

    public static final MenuContentSerializer<StorageMenuContent> SERIALIZER = new Serializer();

    public static Function<StorageMenuContent, ContainerLevelAccess> access(Player player) {
        return content -> ContainerLevelAccess.create(player.level(), content.position());
    }

    @Override
    public MenuContentSerializer<StorageMenuContent> serializer() {
        return SERIALIZER;
    }

    private static class Serializer implements MenuContentSerializer<StorageMenuContent> {

        @Override
        public @Nullable StorageMenuContent from(FriendlyByteBuf buffer) {
            return new StorageMenuContent(buffer.readBlockPos());
        }

        @Override
        public void to(FriendlyByteBuf buffer, StorageMenuContent content) {
            buffer.writeBlockPos(content.position);
        }
    }
}
