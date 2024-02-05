package tech.thatgravyboat.vanity.common.menu.provider;

import com.teamresourceful.resourcefullib.common.menu.ContentMenuProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.vanity.common.block.StylingTableBlockEntity;
import tech.thatgravyboat.vanity.common.menu.StorageMenu;
import tech.thatgravyboat.vanity.common.menu.content.StorageMenuContent;
import tech.thatgravyboat.vanity.common.util.ConstantComponents;

public class StorageProvider implements ContentMenuProvider<StorageMenuContent> {

    private final StylingTableBlockEntity entity;

    public StorageProvider(StylingTableBlockEntity entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ConstantComponents.CONTAINER_TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        if (this.entity.getLevel() == null) return null;
        return new StorageMenu(i, inventory, this.entity, ContainerLevelAccess.create(this.entity.getLevel(), this.entity.getBlockPos()));
    }

    @Override
    public StorageMenuContent createContent(ServerPlayer player) {
        return new StorageMenuContent(this.entity.getBlockPos());
    }
}
