package tech.thatgravyboat.vanity.client;

import tech.thatgravyboat.vanity.client.screen.StorageScreen;
import tech.thatgravyboat.vanity.client.screen.StylingScreen;
import tech.thatgravyboat.vanity.common.registries.VanityMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.Map;
import java.util.function.Consumer;

public class VanityClient {

    public static void registerModels(ResourceManager resourceManager, Consumer<ResourceLocation> out) {
        for (Map.Entry<ResourceLocation, Resource> resource : resourceManager.listResources("models/item/vanity_concept_art", name -> name.getPath().endsWith(".json")).entrySet()) {
            ResourceLocation location = resource.getKey();
            out.accept(new ModelResourceLocation(new ResourceLocation(location.getNamespace(), location.getPath().substring(12, location.getPath().length() - 5)), "inventory"));
        }
    }

    public static void setup() {
        MenuScreens.register(VanityMenuTypes.STYLING.get(), StylingScreen::new);
        MenuScreens.register(VanityMenuTypes.STORAGE.get(), StorageScreen::new);
    }
}
