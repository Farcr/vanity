package tech.thatgravyboat.vanity.mixins.client.armor;

import net.minecraft.client.model.AgeableListModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AgeableListModel.class)
public interface AgeableListModelAccessor {

    @Accessor("scaleHead")
    boolean getScaleHead();

    @Accessor("babyYHeadOffset")
    float getBabyYHeadOffset();

    @Accessor("babyZHeadOffset")
    float getBabyZHeadOffset();

    @Accessor("babyHeadScale")
    float getBabyHeadScale();

    @Accessor("babyBodyScale")
    float getBabyBodyScale();

    @Accessor("bodyYOffset")
    float getBodyYOffset();
}
