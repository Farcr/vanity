package tech.thatgravyboat.vanity.mixins.client.fixes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.thatgravyboat.vanity.api.style.AssetTypes;
import tech.thatgravyboat.vanity.client.design.ClientDesignManager;
import tech.thatgravyboat.vanity.client.rendering.RenderingManager;
import tech.thatgravyboat.vanity.common.util.EntityItemHolder;

@Mixin(ArrowRenderer.class)
public class ArrowRendererMixin {

    @Unique
    private ItemRenderer vanity$itemRenderer;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void vanity$init(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.vanity$itemRenderer = context.getItemRenderer();
    }

    @Inject(
            method = "render(Lnet/minecraft/world/entity/projectile/AbstractArrow;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void vanity$renderArrow(AbstractArrow arrow, float f, float g, PoseStack stack, MultiBufferSource source, int i, CallbackInfo ci) {
        if (!(arrow instanceof EntityItemHolder holder)) return;
        ItemStack item = holder.vanity$getItem();
        boolean hasVanity = ClientDesignManager.INSTANCE.hasStyle(item);
        if (hasVanity && this.vanity$itemRenderer != null) {
            RenderingManager manager = (RenderingManager) this.vanity$itemRenderer;

            stack.pushPose();
            stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(g, arrow.yRotO, arrow.getYRot()) - 90.0F));
            stack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(g, arrow.xRotO, arrow.getXRot())));
            float s = arrow.shakeTime - g;
            if (s > 0.0F) {
                stack.mulPose(Axis.ZP.rotationDegrees(-Mth.sin(s * 3.0F) * s));
            }

            manager.vanity$setModelType(AssetTypes.PROJECTILE);
            this.vanity$itemRenderer.renderStatic(item, ItemDisplayContext.NONE, i, OverlayTexture.NO_OVERLAY, stack, source, arrow.level(), arrow.getId());
            manager.vanity$setModelType(null);
            stack.popPose();
            ci.cancel();
        }
    }
}
