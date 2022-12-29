package gg.moonflower.vanity.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConceptArtItem extends Item {

    public static final String CONCEPT_ART_TAG = "ConceptArt";
    public static final String ID_TAG = "Id";
    public static final String VARIANT_TAG = "Variant";

    public ConceptArtItem(Properties properties) {
        super(properties);
    }

    @Nullable
    private static CompoundTag getConceptArt(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }

        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return null;
        }

        return tag.getCompound(CONCEPT_ART_TAG);
    }

    @Nullable
    public static ResourceLocation getConceptArtId(ItemStack stack) {
        CompoundTag artTag = ConceptArtItem.getConceptArt(stack);
        if (artTag == null || !artTag.contains(ID_TAG, Tag.TAG_STRING)) {
            return null;
        }

        return ResourceLocation.tryParse(artTag.getString(ID_TAG));
    }

    @Nullable
    public static String getVariantName(ItemStack stack) {
        CompoundTag artTag = ConceptArtItem.getConceptArt(stack);
        if (artTag == null || !artTag.contains(VARIANT_TAG, Tag.TAG_STRING)) {
            return null;
        }

        return artTag.getString(VARIANT_TAG);
    }

    public static void setConceptArt(ItemStack stack, @Nullable ResourceLocation id) {
        if (stack.isEmpty()) {
            return;
        }

        if (id == null) {
            stack.removeTagKey(CONCEPT_ART_TAG);
            return;
        }

        CompoundTag artTag = stack.getOrCreateTagElement(CONCEPT_ART_TAG);
        artTag.putString(ID_TAG, id.toString());
    }

    public static void setItemConceptArtVariant(ItemStack stack, @Nullable ResourceLocation id, @Nullable String variantName) {
        if (stack.isEmpty()) {
            return;
        }
        if (id == null || variantName == null) {
            stack.removeTagKey(CONCEPT_ART_TAG);
            return;
        }

        CompoundTag artTag = stack.getOrCreateTagElement(CONCEPT_ART_TAG);
        artTag.putString(ID_TAG, id.toString());
        artTag.putString(VARIANT_TAG, variantName);
    }

    public static TranslatableComponent getTranslationKey(ResourceLocation art, @Nullable String variant) {
        String artKey = "concept_art." + art.getNamespace() + "." + art.getPath();
        if (variant != null) {
            artKey += "." + variant;
        }

        return new TranslatableComponent(artKey);
    }

    @Override
    public Component getName(ItemStack stack) {
        Component itemName = super.getName(stack);
        ResourceLocation art = ConceptArtItem.getConceptArtId(stack);
        if (art != null) {
            return new TextComponent("")
                    .append(ConceptArtItem.getTranslationKey(art, null))
                    .append(new TextComponent(" ")
                    ).append(itemName);
        }
        return itemName;
    }
}
