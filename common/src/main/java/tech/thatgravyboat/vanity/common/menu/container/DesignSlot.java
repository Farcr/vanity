package tech.thatgravyboat.vanity.common.menu.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import tech.thatgravyboat.vanity.common.registries.ModItems;

public class DesignSlot extends Slot {

    public DesignSlot(Container container, int id, int x, int y) {
        super(container, id, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(ModItems.DESIGN.get());
    }
}
