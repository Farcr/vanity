package tech.thatgravyboat.vanity.common.registries;

import com.teamresourceful.resourcefullib.common.item.tabs.ResourcefulCreativeTab;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import tech.thatgravyboat.vanity.api.concept.ConceptArtManager;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.item.ConceptArtHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ModCreativeModeTabs {

    private static final ResourceKey<CreativeModeTab> FUNCTIONAL_BLOCKS = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation("functional_blocks"));

    public static void init() {
        new ResourcefulCreativeTab(new ResourceLocation(Vanity.MOD_ID, "concept_art_tab"))
            .setItemIcon(VanityBlocks.STYLING_TABLE)
            .addContent(() -> {
                ConceptArtManager manager = ConceptArtManager.get(true);
                List<ResourceLocation> availableArt = new ArrayList<>();
                for (var entry : manager.getAllConceptArt().entrySet()) {
                    if (!entry.getValue().type().hasItem()) continue;
                    availableArt.add(entry.getKey());
                }

                return availableArt
                        .stream()
                        .map(location -> {
                            ItemStack stack = new ItemStack(VanityItems.CONCEPT_ART.get());
                            ConceptArtHelper.setConceptArt(stack, location);
                            return stack;
                        });
            }).build();
    }

    /**
     * Adds the value after the value specified in the first parameter of the bi-consumer
     */
    public static void setupCreativeTab(ResourceKey<CreativeModeTab> tab, BiConsumer<ItemLike, ItemLike> adder) {
        if (tab.equals(FUNCTIONAL_BLOCKS)) {
            adder.accept(Items.LOOM, VanityBlocks.STYLING_TABLE.get());
        }
    }
}
