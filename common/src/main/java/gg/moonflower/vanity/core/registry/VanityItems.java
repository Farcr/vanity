package gg.moonflower.vanity.core.registry;

import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import gg.moonflower.vanity.common.item.ConceptArtItem;
import gg.moonflower.vanity.core.Vanity;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class VanityItems {

    public static final PollinatedRegistry<Item> REGISTRY = PollinatedRegistry.create(Registry.ITEM, Vanity.MOD_ID);

    public static final Supplier<Item> CONCEPT_ART = REGISTRY.register("concept_art", () -> new ConceptArtItem(new Item.Properties().stacksTo(1)));
}
