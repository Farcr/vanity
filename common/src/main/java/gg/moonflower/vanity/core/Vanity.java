package gg.moonflower.vanity.core;

import gg.moonflower.pollen.api.itemgroup.v1.CreativeModeTabBuilder;
import gg.moonflower.vanity.api.concept.ConceptArtManager;
import gg.moonflower.vanity.common.concept.ServerConceptArtManager;
import gg.moonflower.vanity.common.item.ConceptArtItem;
import gg.moonflower.vanity.common.network.VanityMessages;
import gg.moonflower.vanity.core.registry.VanityBlocks;
import gg.moonflower.vanity.core.registry.VanityItems;
import gg.moonflower.vanity.core.registry.VanityMenuTypes;
import gg.moonflower.vanity.core.registry.VanityProfessions;
import gg.moonflower.vanity.core.registry.VanitySounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class Vanity {
    public static final String MOD_ID = "vanity";
    @SuppressWarnings("unused")
    public static final CreativeModeTab TAB = CreativeModeTabBuilder.builder(new ResourceLocation(Vanity.MOD_ID, "concept_art_tab"))
            .setIcon(() -> new ItemStack(VanityBlocks.STYLING_TABLE.get()))
            .setItems(list -> ConceptArtManager.get(true).getAllConceptArtIds().forEach(location -> {
                ItemStack stack = new ItemStack(VanityItems.CONCEPT_ART.get());
                ConceptArtItem.setConceptArt(stack, location);
                list.add(stack);
            })).build();

    public static void init() {
        VanityBlocks.BLOCKS.register();
        VanityBlocks.BLOCK_ENTITIES.register();

        VanityItems.REGISTRY.register();
        VanityMenuTypes.REGISTRY.register();
        VanitySounds.REGISTRY.register();

        VanityProfessions.REGISTRY.register();
        VanityProfessions.registerTrades();

        ServerConceptArtManager.init();
    }

    public static void postInit() {
        VanityMessages.init();
    }

    // TODO: pollen data gen
//    public static void onDataInit(Platform.DataSetupContext ctx) {
//        DataGenerator generator = ctx.getGenerator();
//        PollinatedModContainer container = ctx.getMod();
//
//        generator.addProvider(new VanityLanguageGenerator(generator, container));
//        generator.addProvider(new VanityTagGenerator(generator, container));
//        generator.addProvider(new VanityRecipeGenerator(generator));
//
//        PollinatedLootTableProvider lootProvider = new PollinatedLootTableProvider(generator);
//        lootProvider.add(LootContextParamSets.BLOCK, new VanityBlockLootGenerator(container));
//
//        generator.addProvider(lootProvider);
//    }
}
