package gg.moonflower.vanity.core.fabric;

import gg.moonflower.vanity.core.VanityClient;
import net.fabricmc.api.ClientModInitializer;

public class VanityFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        VanityClient.init();
        VanityClient.postInit();
    }
}
