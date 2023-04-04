package gg.moonflower.vanity.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import gg.moonflower.vanity.common.item.ConceptArtItem;
import gg.moonflower.vanity.core.Vanity;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class VanityItems {

    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Vanity.MOD_ID, Registry.ITEM_REGISTRY);

    public static final Supplier<Item> CONCEPT_ART = REGISTRY.register("concept_art", () -> new ConceptArtItem(new Item.Properties().stacksTo(1)));
}
