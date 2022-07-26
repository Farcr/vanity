package gg.moonflower.vanity.core;

import gg.moonflower.pollen.api.platform.Platform;
import gg.moonflower.pollen.api.registry.client.ModelRegistry;
import gg.moonflower.vanity.common.concept.ServerConceptArtManager;
import gg.moonflower.vanity.common.network.VanityMessages;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class Vanity {
    public static final String MOD_ID = "vanity";
    public static final Platform PLATFORM = Platform.builder(MOD_ID)
        .clientInit(() -> Vanity::onClientInit)
        .clientPostInit(() -> Vanity::onClientPostInit)
        .commonInit(Vanity::onCommonInit)
        .commonPostInit(Vanity::onCommonPostInit)
        .dataInit(Vanity::onDataInit)
        .build();

    public static void onClientInit() {
        ModelRegistry.registerFactory((resourceManager, out) -> {
            for (ResourceLocation location : resourceManager.listResources("models/item/concept_art/", name -> name.endsWith(".json"))) {
                out.accept(new ModelResourceLocation(new ResourceLocation(location.getNamespace(), location.getPath().substring(12, location.getPath().length() - 5)), "inventory"));
            }
        });
    }

    public static void onClientPostInit(Platform.ModSetupContext ctx) {
    }

    public static void onCommonInit() {
        VanityMessages.init();
        ServerConceptArtManager.init();
    }

    public static void onCommonPostInit(Platform.ModSetupContext ctx) {
    }

    public static void onDataInit(Platform.DataSetupContext ctx) {
    }
}
