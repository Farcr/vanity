//package gg.moonflower.vanity.core.datagen;
//
//import gg.moonflower.pollen.api.datagen.provider.PollinatedRecipeProvider;
//import gg.moonflower.vanity.core.registry.VanityBlocks;
//import net.minecraft.data.DataGenerator;
//import net.minecraft.data.recipes.FinishedRecipe;
//import net.minecraft.data.recipes.ShapedRecipeBuilder;
//import net.minecraft.tags.ItemTags;
//import net.minecraft.world.item.Items;
//
//import java.util.function.Consumer;
//
//public class VanityRecipeGenerator extends PollinatedRecipeProvider {
//    public VanityRecipeGenerator(DataGenerator generator) {
//        super(generator);
//    }
//
//    @Override
//    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
//        ShapedRecipeBuilder.shaped(VanityBlocks.STYLING_TABLE.get())
//                .define('#', ItemTags.PLANKS)
//                .define('@', Items.LEATHER)
//                .pattern("@@")
//                .pattern("##")
//                .pattern("##")
//                .unlockedBy("has_leather", has(Items.LEATHER)).save(consumer);
//    }
//}
