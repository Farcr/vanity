package tech.thatgravyboat.vanity.common.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseContainerMenu extends AbstractContainerMenu {

    protected final ContainerLevelAccess access;

    protected BaseContainerMenu(int i, @Nullable MenuType<?> menuType, Inventory inventory, ContainerLevelAccess access) {
        super(menuType, i);
        this.access = access;

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 138 + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(inventory, x, 8 + x * 18, 196));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int i) {
        Slot slot = getSlot(i);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ItemStack result = stack.copy();

            if (i >= 36) {
                if (!this.moveItemStackTo(stack, 27, 36, false) && !this.moveItemStackTo(stack, 0, 27, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 36, this.slots.size(), false)) {
                if (i > 26 && !this.moveItemStackTo(stack, 0, 27, false)) {
                    return ItemStack.EMPTY;
                } else if (!this.moveItemStackTo(stack, 27, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            slot.onTake(player, stack);

            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.access.evaluate(
                (level, pos) -> {
                    if (level.getBlockEntity(pos) == null) return false;
                    return player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
                },
                true
        );
    }

    @Nullable
    public BlockPos getBlockPos() {
        return this.access.evaluate((world, pos) -> pos).orElse(null);
    }
}
