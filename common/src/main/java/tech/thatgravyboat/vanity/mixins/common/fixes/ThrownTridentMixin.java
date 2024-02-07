package tech.thatgravyboat.vanity.mixins.common.fixes;

import tech.thatgravyboat.vanity.common.util.EntityItemHolder;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ThrownTrident.class)
public class ThrownTridentMixin implements EntityItemHolder {

    @Shadow private ItemStack tridentItem;

    @Override
    public ItemStack vanity$getItem() {
        return this.tridentItem;
    }

    @Override
    public void vanity$setItem(ItemStack stack) {
        this.tridentItem = stack;
    }
}
