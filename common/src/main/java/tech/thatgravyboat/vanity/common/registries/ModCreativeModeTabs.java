package tech.thatgravyboat.vanity.common.registries;

import com.teamresourceful.resourcefullib.common.item.tabs.ResourcefulCreativeTab;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import tech.thatgravyboat.vanity.api.design.DesignManager;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.item.DesignHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ModCreativeModeTabs {

    private static final ResourceKey<CreativeModeTab> FUNCTIONAL_BLOCKS = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation("functional_blocks"));

    public static void init() {
        new ResourcefulCreativeTab(new ResourceLocation(Vanity.MOD_ID, "designs"))
            .setItemIcon(VanityBlocks.STYLING_TABLE)
            .addContent(() -> {
                DesignManager manager = DesignManager.get(true);
                List<ResourceLocation> designs = new ArrayList<>();
                for (var entry : manager.getAllDesigns().entrySet()) {
                    if (!entry.getValue().type().hasItem()) continue;
                    designs.add(entry.getKey());
                }

                return designs
                        .stream()
                        .map(location -> {
                            ItemStack stack = new ItemStack(VanityItems.DESIGN.get());
                            DesignHelper.setDesign(stack, location);
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
