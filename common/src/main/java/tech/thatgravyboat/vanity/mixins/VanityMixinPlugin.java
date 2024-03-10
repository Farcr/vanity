package tech.thatgravyboat.vanity.mixins;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import tech.thatgravyboat.vanity.common.util.ModUtils;

import java.util.List;
import java.util.Set;

public class VanityMixinPlugin implements IMixinConfigPlugin {

    private String packageName = null;

    @Override
    public void onLoad(String mixinPackage) {
        this.packageName = mixinPackage;
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (packageName == null) {
            throw new IllegalStateException("VanityMixinPlugin has not been loaded yet!");
        }
        String info = mixinClassName.substring(packageName.length() + 1);
        String[] parts = info.split("\\.");
        return switch (parts[0]) {
            case "common", "client" -> true;
            case "compat" -> ModUtils.isMixinModLoaded(parts[1]);
            default -> false;
        };
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
