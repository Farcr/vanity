package tech.thatgravyboat.vanity.common.registries;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import tech.thatgravyboat.vanity.common.item.DesignItem;
import tech.thatgravyboat.vanity.common.Vanity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class VanityItems {

    public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, Vanity.MOD_ID);

    public static final Supplier<Item> DESIGN = ITEMS.register("design", () -> new DesignItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> STYLING_TABLE = ITEMS.register("styling_table", () -> new BlockItem(VanityBlocks.STYLING_TABLE.get(), new Item.Properties()));
}
