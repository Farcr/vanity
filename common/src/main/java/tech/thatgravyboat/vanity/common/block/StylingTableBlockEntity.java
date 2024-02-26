package tech.thatgravyboat.vanity.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import tech.thatgravyboat.vanity.common.menu.container.StylingContainer;
import tech.thatgravyboat.vanity.common.menu.provider.StorageProvider;
import tech.thatgravyboat.vanity.common.menu.provider.StylingProvider;
import tech.thatgravyboat.vanity.common.registries.ModBlocks;

public class StylingTableBlockEntity extends BlockEntity implements StylingContainer {

    public static final int SIZE = 24;

    private final StylingProvider styling;
    private final StorageProvider storage;

    protected NonNullList<ItemStack> items;

    public StylingTableBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.STYLING_TABLE_BE.get(), blockPos, blockState);

        this.styling = new StylingProvider(this);
        this.storage = new StorageProvider(this);

        this.items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(Math.max(this.getContainerSize(), SIZE), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
    }

    @Override
    public NonNullList<ItemStack> items() {
        return this.items;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    public StylingProvider styling() {
        return this.styling;
    }

    public StorageProvider storage() {
        return this.storage;
    }
}
