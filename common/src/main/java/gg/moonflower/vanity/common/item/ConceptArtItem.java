package gg.moonflower.vanity.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ConceptArtItem extends Item {

    public static final String CONCEPT_ART_TAG = "ConceptArt";
    public static final String ID_TAG = "Id";
    public static final String VARIANT_TAG = "Variant";

    public ConceptArtItem(Properties properties) {
        super(properties);
    }

    @Nullable
    private static CompoundTag getConceptArt(ItemStack stack) {
        if (stack == null)
            return null;

        CompoundTag tag = stack.getTag();
        if (tag == null)
            return null;

        return tag.getCompound(CONCEPT_ART_TAG);
    }

    @Nullable
    public static ResourceLocation getConceptArtId(ItemStack stack) {
        CompoundTag artTag = ConceptArtItem.getConceptArt(stack);
        if (artTag == null || !artTag.contains(ID_TAG, Tag.TAG_STRING))
            return null;

        return ResourceLocation.tryParse(artTag.getString(ID_TAG));
    }

    @Nullable
    public static ResourceLocation getConceptArtVariantId(ItemStack stack) {
        CompoundTag artTag = ConceptArtItem.getConceptArt(stack);
        if (artTag == null || !artTag.contains(VARIANT_TAG, Tag.TAG_STRING))
            return null;

        return ResourceLocation.tryParse(artTag.getString(VARIANT_TAG));
    }

    public static void setConceptArt(ItemStack stack, @Nullable ResourceLocation id, @Nullable ResourceLocation variantId) {
        if (stack == null)
            return;
        if (id == null || variantId == null) {
            stack.removeTagKey(CONCEPT_ART_TAG);
            return;
        }

        CompoundTag artTag = stack.getOrCreateTagElement(CONCEPT_ART_TAG);
        artTag.putString(ID_TAG, id.toString());
        artTag.putString(VARIANT_TAG, variantId.toString());
    }
}
