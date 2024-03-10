package tech.thatgravyboat.vanity.common.util.forge;

import net.minecraftforge.fml.loading.LoadingModList;

public class ModUtilsImpl {

    public static boolean isMixinModLoaded(String modid) {
        return LoadingModList.get().getModFileById(modid) != null;
    }
}
