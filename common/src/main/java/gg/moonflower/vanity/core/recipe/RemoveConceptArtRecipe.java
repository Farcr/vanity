package gg.moonflower.vanity.core.recipe;

import gg.moonflower.pollen.api.crafting.grindstone.PollenGrindstoneRecipe;
import gg.moonflower.vanity.common.item.ConceptArtItem;
import gg.moonflower.vanity.core.registry.VanityItems;
import gg.moonflower.vanity.core.registry.VanityRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class RemoveConceptArtRecipe implements PollenGrindstoneRecipe {

    private final ResourceLocation id;

    public RemoveConceptArtRecipe(ResourceLocation resourceLocation) {
        this.id = resourceLocation;
    }

    @Override
    public int getResultExperience() {
        return 0;
    }

    @Override
    public boolean matches(Container container, Level level) {
        int result = 0;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(VanityItems.CONCEPT_ART.get()) || ConceptArtItem.getConceptArtId(stack) == null) {
                return false;
            }
            result++;
        }
        return result == 1;
    }

    @Override
    public ItemStack assemble(Container container) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (ConceptArtItem.getConceptArtId(stack) != null) {
                ItemStack result = stack.copy();
                result.setCount(1);
                ConceptArtItem.setConceptArt(result, null);
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return VanityRecipes.CLEAN_CONCEPT_ART.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
