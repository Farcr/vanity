package tech.thatgravyboat.vanity.common.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;
import tech.thatgravyboat.vanity.common.block.StylingTableBlock;
import tech.thatgravyboat.vanity.common.block.StylingTableBlockEntity;
import tech.thatgravyboat.vanity.common.menu.container.DesignSlot;
import tech.thatgravyboat.vanity.common.menu.content.StorageMenuContent;
import tech.thatgravyboat.vanity.common.registries.ModMenuTypes;

import java.util.Optional;

public class StorageMenu extends BaseContainerMenu {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public StorageMenu(int i, Inventory inventory, Optional<StorageMenuContent> content) {
        this(i, inventory, new SimpleContainer(StylingTableBlockEntity.SIZE), content.map(StorageMenuContent.access(inventory.player)).orElse(ContainerLevelAccess.NULL));
    }

    public StorageMenu(int i, Inventory inventory, Container container, ContainerLevelAccess access) {
        super(i, ModMenuTypes.STORAGE.get(), inventory, access);

        checkContainerSize(container, StylingTableBlockEntity.SIZE);

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new DesignSlot(container, x + y * 9, 17 + x * 18, 67 + y * 18));
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (super.stillValid(player)) {
            return this.access.evaluate((level, pos) -> {
                BlockState state = level.getBlockState(pos);
                return !state.hasProperty(StylingTableBlock.POWERED) || !state.getValue(StylingTableBlock.POWERED);
            }, true);
        }
        return false;
    }
}
