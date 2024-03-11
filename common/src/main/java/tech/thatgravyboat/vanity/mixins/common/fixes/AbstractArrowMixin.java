package tech.thatgravyboat.vanity.mixins.common.fixes;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import tech.thatgravyboat.vanity.common.util.EntityItemHolder;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin implements EntityItemHolder {


    @Shadow private ItemStack pickupItemStack;

    @Override
    public ItemStack vanity$getItem() {
        return this.pickupItemStack;
    }

    @Override
    public void vanity$setItem(ItemStack stack) {
        this.pickupItemStack = stack;
    }
}
