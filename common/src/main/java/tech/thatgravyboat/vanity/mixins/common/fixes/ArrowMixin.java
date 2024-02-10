package tech.thatgravyboat.vanity.mixins.common.fixes;

import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.thatgravyboat.vanity.common.util.EntityItemHolder;

@Mixin(Arrow.class)
public class ArrowMixin implements EntityItemHolder {

    @Unique
    private ItemStack vanity$arrowItem = ItemStack.EMPTY;

    @Inject(
            method = "setEffectsFromItem",
            at = @At("HEAD")
    )
    private void vanity$setItem(ItemStack stack, CallbackInfo ci) {
        this.vanity$arrowItem = stack.copyWithCount(1);
    }

    @Inject(
            method = "getPickupItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void vanity$getItem(CallbackInfoReturnable<ItemStack> cir) {
        if (this.vanity$arrowItem != null && !this.vanity$arrowItem.isEmpty()) {
            cir.setReturnValue(this.vanity$arrowItem);
        }
    }

    @Override
    public ItemStack vanity$getItem() {
        return this.vanity$arrowItem;
    }

    @Override
    public void vanity$setItem(ItemStack stack) {
        this.vanity$arrowItem = stack.copyWithCount(1);
    }
}
