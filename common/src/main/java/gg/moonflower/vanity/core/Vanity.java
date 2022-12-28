package gg.moonflower.vanity.core;

import gg.moonflower.pollen.api.event.events.registry.client.RegisterAtlasSpriteEvent;
import gg.moonflower.pollen.api.platform.Platform;
import gg.moonflower.pollen.api.registry.client.ModelRegistry;
import gg.moonflower.pollen.api.registry.client.ScreenRegistry;
import gg.moonflower.vanity.client.screen.StylingScreen;
import gg.moonflower.vanity.common.concept.ServerConceptArtManager;
import gg.moonflower.vanity.common.menu.StylingMenu;
import gg.moonflower.vanity.common.network.VanityMessages;
import gg.moonflower.vanity.core.registry.VanityBlocks;
import gg.moonflower.vanity.core.registry.VanityItems;
import gg.moonflower.vanity.core.registry.VanityMenuTypes;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class Vanity {
    public static final String MOD_ID = "vanity";
    public static final Platform PLATFORM = Platform.builder(MOD_ID)
        .clientInit(() -> Vanity::onClientInit)
        .clientPostInit(() -> Vanity::onClientPostInit)
        .commonInit(Vanity::onCommonInit)
        .build();

    public static void onCommonInit() {
        VanityBlocks.REGISTRY.register(Vanity.PLATFORM);
        VanityItems.REGISTRY.register(Vanity.PLATFORM);

        VanityMessages.init();
        ServerConceptArtManager.init();
    }

    public static void onClientInit() {
        ModelRegistry.registerFactory((resourceManager, out) -> {
            for (ResourceLocation location : resourceManager.listResources("models/item/vanity_concept_art/", name -> name.endsWith(".json"))) {
                out.accept(new ModelResourceLocation(new ResourceLocation(location.getNamespace(), location.getPath().substring(12, location.getPath().length() - 5)), "inventory"));
            }
        });
    }

    public static void onClientPostInit(Platform.ModSetupContext ctx) {
        ctx.enqueueWork(() -> {
            RegisterAtlasSpriteEvent.event(InventoryMenu.BLOCK_ATLAS).register((atlas, registry) -> registry.accept(StylingMenu.EMPTY_CONCEPT_ART_SLOT));
        });
    }
}
