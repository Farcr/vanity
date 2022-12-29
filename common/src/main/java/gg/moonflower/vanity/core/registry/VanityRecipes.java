package gg.moonflower.vanity.core.registry;

import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import gg.moonflower.vanity.core.Vanity;
import gg.moonflower.vanity.core.recipe.RemoveConceptArtRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

import java.util.function.Supplier;

public class VanityRecipes {

    public static final PollinatedRegistry<RecipeSerializer<?>> RECIPE_SERIALIZERS = PollinatedRegistry.create(Registry.RECIPE_SERIALIZER, Vanity.MOD_ID);

    public static final Supplier<RecipeSerializer<RemoveConceptArtRecipe>> CLEAN_CONCEPT_ART = RECIPE_SERIALIZERS.register("clean_concept_art", () -> new SimpleRecipeSerializer<>(RemoveConceptArtRecipe::new));
}
