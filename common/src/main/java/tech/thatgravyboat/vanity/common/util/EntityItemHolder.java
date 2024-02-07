package tech.thatgravyboat.vanity.common.util;

import net.minecraft.world.item.ItemStack;

public interface EntityItemHolder {

    ItemStack vanity$getItem();

    void vanity$setItem(ItemStack stack);
}
