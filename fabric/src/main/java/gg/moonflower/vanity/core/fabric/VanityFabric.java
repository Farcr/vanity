package gg.moonflower.vanity.core.fabric;

import gg.moonflower.vanity.core.Vanity;
import net.fabricmc.api.ModInitializer;

public class VanityFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Vanity.init();
    }
}
