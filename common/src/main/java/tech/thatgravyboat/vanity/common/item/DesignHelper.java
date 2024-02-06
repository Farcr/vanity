package tech.thatgravyboat.vanity.common.item;

import net.minecraft.Optionull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class DesignHelper {

    public static final String DESIGN_TAG = "Design";
    public static final String ID_TAG = "Id";
    public static final String STYLE_TAG = "Style";

    private static final String TRANSLATION_KEY = "design";

    @Nullable
    private static CompoundTag getTag(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return Optionull.map(stack.getTag(), tag -> tag.getCompound(DESIGN_TAG));
    }

    @Nullable
    public static ResourceLocation getDesign(ItemStack stack) {
        CompoundTag tag = DesignHelper.getTag(stack);
        if (tag == null || !tag.contains(ID_TAG, Tag.TAG_STRING)) {
            return null;
        }

        return ResourceLocation.tryParse(tag.getString(ID_TAG));
    }

    @Nullable
    public static String getStyle(ItemStack stack) {
        CompoundTag tag = DesignHelper.getTag(stack);
        if (tag == null || !tag.contains(STYLE_TAG, Tag.TAG_STRING)) {
            return null;
        }

        return tag.getString(STYLE_TAG);
    }

    public static void setDesign(ItemStack stack, @Nullable ResourceLocation id) {
        if (stack.isEmpty()) return;
        if (id == null) {
            stack.removeTagKey(DESIGN_TAG);
            return;
        }

        CompoundTag tag = stack.getOrCreateTagElement(DESIGN_TAG);
        tag.putString(ID_TAG, id.toString());
    }

    public static void setDesignAndStyle(ItemStack stack, @Nullable ResourceLocation id, @Nullable String style) {
        if (stack.isEmpty()) return;
        if (id == null || style == null) {
            stack.removeTagKey(DESIGN_TAG);
            return;
        }

        CompoundTag tag = stack.getOrCreateTagElement(DESIGN_TAG);
        tag.putString(ID_TAG, id.toString());
        tag.putString(STYLE_TAG, style);
    }

    public static MutableComponent getTranslationKey(ResourceLocation design, @Nullable String style) {
        String key = style != null ? design.toLanguageKey(TRANSLATION_KEY, style) : design.toLanguageKey(TRANSLATION_KEY);
        return Component.translatable(key);
    }

}
