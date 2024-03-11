package tech.thatgravyboat.vanity.common.util.neoforge;

import net.neoforged.fml.loading.LoadingModList;

public class ModUtilsImpl {

    public static boolean isMixinModLoaded(String modid) {
        return LoadingModList.get().getModFileById(modid) != null;
    }
}
