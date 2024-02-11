package tech.thatgravyboat.vanity.mixins.client.transforms;

import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import tech.thatgravyboat.vanity.client.rendering.ItemTransformsExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mixin(ItemTransforms.class)
public class ItemTransformsMixin implements ItemTransformsExtension {

    @Unique
    private Map<String, ItemTransform> vanity$transforms = null;

    @Override
    public @NotNull ItemTransform vanity$getTransform(String name) {
        if (vanity$transforms == null) return ItemTransform.NO_TRANSFORM;
        return vanity$transforms.getOrDefault(name, ItemTransform.NO_TRANSFORM);
    }

    @Override
    public void vanity$putTransform(String name, @NotNull ItemTransform transform) {
        if (vanity$transforms == null) vanity$transforms = new HashMap<>();
        vanity$transforms.put(name, transform);
    }

    @Override
    public Map<String, ItemTransform> vanity$getTransforms() {
        return Objects.requireNonNullElseGet(vanity$transforms, Map::of);
    }
}
