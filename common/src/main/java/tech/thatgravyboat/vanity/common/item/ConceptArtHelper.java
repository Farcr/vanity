package tech.thatgravyboat.vanity.common.item;

import net.minecraft.Optionull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ConceptArtHelper {

    public static final String CONCEPT_ART_TAG = "ConceptArt";
    public static final String ID_TAG = "Id";
    public static final String VARIANT_TAG = "Variant";

    private static final String TRANSLATION_KEY = "concept_art";

    @Nullable
    private static CompoundTag getTag(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return Optionull.map(stack.getTag(), tag -> tag.getCompound(CONCEPT_ART_TAG));
    }

    @Nullable
    public static ResourceLocation getArtId(ItemStack stack) {
        CompoundTag tag = ConceptArtHelper.getTag(stack);
        if (tag == null || !tag.contains(ID_TAG, Tag.TAG_STRING)) {
            return null;
        }

        return ResourceLocation.tryParse(tag.getString(ID_TAG));
    }

    @Nullable
    public static String getStyle(ItemStack stack) {
        CompoundTag tag = ConceptArtHelper.getTag(stack);
        if (tag == null || !tag.contains(VARIANT_TAG, Tag.TAG_STRING)) {
            return null;
        }

        return tag.getString(VARIANT_TAG);
    }

    public static void setConceptArt(ItemStack stack, @Nullable ResourceLocation id) {
        if (stack.isEmpty()) return;
        if (id == null) {
            stack.removeTagKey(CONCEPT_ART_TAG);
            return;
        }

        CompoundTag tag = stack.getOrCreateTagElement(CONCEPT_ART_TAG);
        tag.putString(ID_TAG, id.toString());
    }

    public static void setItemConceptArtVariant(ItemStack stack, @Nullable ResourceLocation id, @Nullable String variantName) {
        if (stack.isEmpty()) return;
        if (id == null || variantName == null) {
            stack.removeTagKey(CONCEPT_ART_TAG);
            return;
        }

        CompoundTag tag = stack.getOrCreateTagElement(CONCEPT_ART_TAG);
        tag.putString(ID_TAG, id.toString());
        tag.putString(VARIANT_TAG, variantName);
    }

    public static MutableComponent getTranslationKey(ResourceLocation art, @Nullable String variant) {
        String key = variant != null ? art.toLanguageKey(TRANSLATION_KEY, variant) : art.toLanguageKey(TRANSLATION_KEY);
        return Component.translatable(key);
    }

}
