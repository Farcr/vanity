package tech.thatgravyboat.vanity.mixins.client.transforms;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockModel.class)
public interface BlockModelAccessor {

    @Accessor("parent")
    BlockModel vanity$getParent();

    @Accessor("transforms")
    ItemTransforms vanity$getTransforms();
}
