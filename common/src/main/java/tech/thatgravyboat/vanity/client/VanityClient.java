package tech.thatgravyboat.vanity.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import tech.thatgravyboat.vanity.client.design.ClientDesignManager;
import tech.thatgravyboat.vanity.client.screen.StorageScreen;
import tech.thatgravyboat.vanity.client.screen.StylingScreen;
import tech.thatgravyboat.vanity.common.registries.VanityMenuTypes;

import java.util.function.Consumer;

public class VanityClient {

    public static void registerModels(ResourceManager resourceManager, Consumer<ResourceLocation> out) {
        for (ResourceLocation resource : resourceManager.listResources("models/item/" + ClientDesignManager.PATH, name -> name.getPath().endsWith(".json")).keySet()) {
            out.accept(new ModelResourceLocation(new ResourceLocation(resource.getNamespace(), resource.getPath().substring(12, resource.getPath().length() - 5)), "inventory"));
        }
    }

    public static void setup() {
        MenuScreens.register(VanityMenuTypes.STYLING.get(), StylingScreen::new);
        MenuScreens.register(VanityMenuTypes.STORAGE.get(), StorageScreen::new);
    }
}
