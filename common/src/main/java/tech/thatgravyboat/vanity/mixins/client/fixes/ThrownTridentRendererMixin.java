package tech.thatgravyboat.vanity.mixins.client.fixes;

import com.mojang.blaze3d.vertex.PoseStack;
import tech.thatgravyboat.vanity.api.style.AssetType;
import tech.thatgravyboat.vanity.client.design.ClientDesignManager;
import tech.thatgravyboat.vanity.client.rendering.RenderingManager;
import tech.thatgravyboat.vanity.common.entities.EntityItemHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTridentRenderer.class)
public class ThrownTridentRendererMixin {

    @Unique
    private ItemRenderer vanity$itemRenderer;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void vanity$init(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.vanity$itemRenderer = context.getItemRenderer();
    }

    @Inject(
            method = "render(Lnet/minecraft/world/entity/projectile/ThrownTrident;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;getFoilBufferDirect(Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/renderer/RenderType;ZZ)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void vanity$renderTrident(ThrownTrident trident, float f, float g, PoseStack stack, MultiBufferSource source, int i, CallbackInfo ci) {
        ItemStack item = ((EntityItemHolder) trident).vanity$getItem();
        boolean hasVanity = ClientDesignManager.INSTANCE.hasStyle(item);
        if (hasVanity && this.vanity$itemRenderer != null) {
            RenderingManager manager = (RenderingManager) this.vanity$itemRenderer;

            //This injects after it has done the transformations of the stack
            manager.vanity$setModelType(AssetType.PROJECTILE);
            this.vanity$itemRenderer.renderStatic(item, ItemDisplayContext.NONE, i, OverlayTexture.NO_OVERLAY, stack, source, trident.level(), trident.getId());
            manager.vanity$setModelType(null);

            stack.popPose();
            ci.cancel();
        }
    }
}
