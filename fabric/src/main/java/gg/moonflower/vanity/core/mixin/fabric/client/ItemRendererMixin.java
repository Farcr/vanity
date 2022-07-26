package gg.moonflower.vanity.core.mixin.fabric.client;

import com.mojang.blaze3d.vertex.PoseStack;
import gg.moonflower.pollen.mixinextras.injector.ModifyExpressionValue;
import gg.moonflower.vanity.client.concept.ClientConceptArtManager;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Shadow @Final private ItemModelShaper itemModelShaper;
    @Unique
    private ItemStack capturedStack;
    @Unique
    private boolean gui;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/BakedModel;getTransforms()Lnet/minecraft/client/renderer/block/model/ItemTransforms;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void capture(ItemStack itemStack, ItemTransforms.TransformType transformType, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci, boolean gui) {
        this.capturedStack = itemStack;
        this.gui = gui;
    }

    @ModifyVariable(method = "render", ordinal = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/BakedModel;getTransforms()Lnet/minecraft/client/renderer/block/model/ItemTransforms;"), argsOnly = true)
    public BakedModel render(BakedModel original) {
        ModelResourceLocation model = null;
        if (this.gui)
            model = ClientConceptArtManager.INSTANCE.getModel(this.capturedStack);

        if (model == null)
            model = ClientConceptArtManager.INSTANCE.getHandModel(this.capturedStack);

        if (model == null)
            return original;

        return this.itemModelShaper.getModelManager().getModel(model);
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 2))
    public boolean shouldRender(boolean original) {
        ModelResourceLocation model = ClientConceptArtManager.INSTANCE.getHandModel(this.capturedStack);
        if (model == null)
            return original;

        return false;
    }
}
