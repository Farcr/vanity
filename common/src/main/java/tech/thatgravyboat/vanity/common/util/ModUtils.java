package tech.thatgravyboat.vanity.common.util;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.Contract;

public class ModUtils {

    @Contract(pure = true)
    @ExpectPlatform
    public static boolean isMixinModLoaded(String modid) {
        throw new NotImplementedException();
    }
}
