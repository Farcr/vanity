package tech.thatgravyboat.vanity.mixins.client.armor;

import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelRenderer.class)
public interface LevelRenderAccessor {

    @Invoker("shouldShowEntityOutlines")
    boolean getShouldShowEntityOutlines();
}
