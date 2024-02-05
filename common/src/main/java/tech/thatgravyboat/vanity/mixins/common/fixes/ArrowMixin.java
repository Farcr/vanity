package tech.thatgravyboat.vanity.mixins.common.fixes;

import tech.thatgravyboat.vanity.common.entities.EntityItemHolder;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Arrow.class)
public class ArrowMixin implements EntityItemHolder {

    @Unique
    private ItemStack vanity$arrowItem = new ItemStack(Items.ARROW);

    @Inject(
            method = "setEffectsFromItem",
            at = @At("HEAD")
    )
    private void vanity$setItem(ItemStack stack, CallbackInfo ci) {
        this.vanity$arrowItem = stack;
    }

    @Override
    public ItemStack vanity$getItem() {
        return this.vanity$arrowItem;
    }

    @Override
    public void vanity$setItem(ItemStack stack) {
        this.vanity$arrowItem = stack;
    }
}
