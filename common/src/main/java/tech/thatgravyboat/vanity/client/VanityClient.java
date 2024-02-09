package tech.thatgravyboat.vanity.client;

import net.minecraft.client.gui.screens.MenuScreens;
import tech.thatgravyboat.vanity.client.screen.StorageScreen;
import tech.thatgravyboat.vanity.client.screen.StylingScreen;
import tech.thatgravyboat.vanity.common.registries.ModMenuTypes;

public class VanityClient {

    public static void setup() {
        MenuScreens.register(ModMenuTypes.STYLING.get(), StylingScreen::new);
        MenuScreens.register(ModMenuTypes.STORAGE.get(), StorageScreen::new);
    }
}
