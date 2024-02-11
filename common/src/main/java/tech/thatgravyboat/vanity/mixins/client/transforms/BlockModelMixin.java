package tech.thatgravyboat.vanity.mixins.client.transforms;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.thatgravyboat.vanity.client.rendering.ItemTransformsExtension;

@Mixin(BlockModel.class)
public class BlockModelMixin {

    @Shadow @Final private ItemTransforms transforms;

    @Inject(
            method = "getTransforms",
            at = @At("RETURN")
    )
    private void onDeserialize(CallbackInfoReturnable<ItemTransforms> cir) {
        ItemTransformsExtension newTransforms = (ItemTransformsExtension) cir.getReturnValue();
        ItemTransformsExtension oldTransforms = (ItemTransformsExtension) this.transforms;
        for (var entry : oldTransforms.vanity$getTransforms().entrySet()) {
            newTransforms.vanity$putTransform(entry.getKey(), entry.getValue());
        }
    }
}
