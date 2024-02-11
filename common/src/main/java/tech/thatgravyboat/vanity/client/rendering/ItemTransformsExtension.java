package tech.thatgravyboat.vanity.client.rendering;

import net.minecraft.client.renderer.block.model.ItemTransform;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface ItemTransformsExtension {

    @NotNull
    ItemTransform vanity$getTransform(String name);

    void vanity$putTransform(String name, @NotNull ItemTransform transform);

    Map<String, ItemTransform> vanity$getTransforms();
}
