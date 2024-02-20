package tech.thatgravyboat.vanity.mixins.client.transforms;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.thatgravyboat.vanity.client.rendering.ItemTransformsExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(BlockModel.class)
public class BlockModelMixin {

    @Inject(
            method = "getTransforms",
            at = @At("RETURN")
    )
    private void onDeserialize(CallbackInfoReturnable<ItemTransforms> cir) {
        ItemTransforms originalTransforms = cir.getReturnValue();
        ItemTransformsExtension newTransforms = (ItemTransformsExtension) originalTransforms;
        List<ItemTransformsExtension> transforms = new ArrayList<>();
        BlockModelAccessor model = (BlockModelAccessor) this;
        while (model != null) {
            transforms.add((ItemTransformsExtension) model.vanity$getTransforms());
            model = (BlockModelAccessor) model.vanity$getParent();
        }
        Collections.reverse(transforms);

        for (ItemTransformsExtension transform : transforms) {
            transform.vanity$getTransforms().forEach(newTransforms::vanity$putTransform);
        }
    }
}
