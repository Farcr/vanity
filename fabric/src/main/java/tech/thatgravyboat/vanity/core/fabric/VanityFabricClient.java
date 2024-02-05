package tech.thatgravyboat.vanity.core.fabric;

import tech.thatgravyboat.vanity.client.VanityClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;

public class VanityFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModelLoadingRegistry.INSTANCE.registerModelProvider(VanityClient::registerModels);
        VanityClient.setup();
    }
}
