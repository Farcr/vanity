package gg.moonflower.vanity.core.forge;

import gg.moonflower.vanity.core.Vanity;
import net.minecraftforge.fml.common.Mod;

@Mod(Vanity.MOD_ID)
public class VanityForge {
    public VanityForge() {
        Vanity.PLATFORM.setup();
    }
}
