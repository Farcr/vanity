package gg.moonflower.vanity.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ConceptArtItem {

    @Nullable
    public static ResourceLocation getAppliedConceptArt(ItemStack stack) {
        if (stack == null)
            return null;

        CompoundTag tag = stack.getTag();
        if (tag == null)
            return null;
        if (!tag.contains("AppliedConceptArt", Tag.TAG_STRING))
            return null;

        return ResourceLocation.tryParse(tag.getString("AppliedConceptArt"));
    }
}
