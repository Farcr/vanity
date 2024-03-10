package tech.thatgravyboat.vanity.common.util.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class ModUtilsImpl {

    public static boolean isMixinModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }
}
