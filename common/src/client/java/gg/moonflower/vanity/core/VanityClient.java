package gg.moonflower.vanity.core;

import dev.architectury.registry.menu.MenuRegistry;
import gg.moonflower.pollen.api.event.registry.v1.RegisterAtlasSpriteEvent;
import gg.moonflower.pollen.api.registry.render.v1.ModelRegistry;
import gg.moonflower.vanity.client.screen.StylingScreen;
import gg.moonflower.vanity.common.menu.StylingMenu;
import gg.moonflower.vanity.common.network.VanityMessages;
import gg.moonflower.vanity.common.network.login.handler.VanityClientLoginHandlerImpl;
import gg.moonflower.vanity.common.network.play.handler.VanityClientPlayHandlerImpl;
import gg.moonflower.vanity.core.registry.VanityMenuTypes;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.Map;

public class VanityClient {

    public static void init() {
        VanityMessages.PLAY.setClientHandler(new VanityClientPlayHandlerImpl());
        VanityMessages.LOGIN.setClientHandler(new VanityClientLoginHandlerImpl());
        ModelRegistry.registerFactory((resourceManager, out) -> {
            for (Map.Entry<ResourceLocation, Resource> resource : resourceManager.listResources("models/item/vanity_concept_art", name -> name.getPath().endsWith(".json")).entrySet()) {
                ResourceLocation location = resource.getKey();
                out.accept(new ModelResourceLocation(new ResourceLocation(location.getNamespace(), location.getPath().substring(12, location.getPath().length() - 5)), "inventory"));
            }
        });
    }

    public static void postInit() {
        RegisterAtlasSpriteEvent.event(InventoryMenu.BLOCK_ATLAS).register((atlas, registry) -> registry.accept(StylingMenu.EMPTY_CONCEPT_ART_SLOT));
        MenuRegistry.registerScreenFactory(VanityMenuTypes.STYLING_MENU.get(), StylingScreen::new);
    }
}
