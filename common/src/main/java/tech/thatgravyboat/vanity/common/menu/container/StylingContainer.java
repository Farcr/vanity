package tech.thatgravyboat.vanity.common.menu.container;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StylingContainer extends WorldlyContainer {

    int[] EMPTY = new int[0];

    NonNullList<ItemStack> items();

    @Override
    default int @NotNull [] getSlotsForFace(@NotNull Direction direction) {
        return EMPTY;
    }

    @Override
    default boolean canPlaceItemThroughFace(int i, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    default boolean canTakeItemThroughFace(int i, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        return false;
    }

    @Override
    default int getContainerSize() {
        return this.items().size();
    }

    @Override
    default boolean isEmpty() {
        for (ItemStack itemStack : this.items()) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    default @NotNull ItemStack getItem(int i) {
        return this.items().get(i);
    }

    @Override
    default @NotNull ItemStack removeItem(int i, int j) {
        return ContainerHelper.removeItem(this.items(), i, j);
    }

    @Override
    default @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items(), slot);
    }

    @Override
    default void setItem(int slot, @NotNull ItemStack stack) {
        this.items().set(slot, stack);
    }

    @Override
    default int getMaxStackSize() {
        return 1;
    }

    @Override
    default void clearContent() {
        this.items().clear();
    }
}
