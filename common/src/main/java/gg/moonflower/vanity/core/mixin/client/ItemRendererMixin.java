package gg.moonflower.vanity.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import gg.moonflower.pollen.mixinextras.injector.ModifyExpressionValue;
import gg.moonflower.vanity.api.concept.ConceptArt;
import gg.moonflower.vanity.client.concept.ClientConceptArtManager;
import gg.moonflower.vanity.core.registry.VanityItems;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;
    @Unique
    private ItemStack vanity$capturedStack;
    @Unique
    private boolean vanity$gui;

    @Inject(method = "getModel", at = @At("HEAD"))
    private void vanity$captureModel(ItemStack itemStack, Level level, LivingEntity livingEntity, int i, CallbackInfoReturnable<BakedModel> cir) {
        this.vanity$capturedStack = itemStack;
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void vanity$captureRender(ItemStack itemStack, ItemTransforms.TransformType transformType, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
        this.vanity$capturedStack = itemStack;
        this.vanity$gui = transformType == ItemTransforms.TransformType.GUI || transformType == ItemTransforms.TransformType.GROUND || transformType == ItemTransforms.TransformType.FIXED;
    }

    @ModifyVariable(method = "getModel", ordinal = 0, at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/ItemModelShaper;getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;", shift = At.Shift.AFTER))
    private BakedModel vanity$replaceModel(BakedModel original) {
        if (this.vanity$capturedStack.is(VanityItems.CONCEPT_ART.get())) {
            ConceptArt art = ClientConceptArtManager.INSTANCE.getItemConceptArt(this.vanity$capturedStack);
            if (art != null && art.conceptArtModel() != null) {
                return this.itemModelShaper.getModelManager().getModel(ClientConceptArtManager.getModelLocation(art.conceptArtModel()));
            }
            return original;
        }

        ModelResourceLocation model = ClientConceptArtManager.INSTANCE.getHandModel(this.vanity$capturedStack);
        if (model == null)
            model = ClientConceptArtManager.INSTANCE.getModel(this.vanity$capturedStack);
        if (model == null)
            return original;

        return this.itemModelShaper.getModelManager().getModel(model);
    }

    @ModifyVariable(method = "render", ordinal = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelManager;getModel(Lnet/minecraft/client/resources/model/ModelResourceLocation;)Lnet/minecraft/client/resources/model/BakedModel;", ordinal = 1, shift = At.Shift.BY, by = 3), argsOnly = true)
    public BakedModel render(BakedModel original) {
        if (this.vanity$capturedStack.is(VanityItems.CONCEPT_ART.get()))
            return original;

        ModelResourceLocation model = null;
        if (this.vanity$gui)
            model = ClientConceptArtManager.INSTANCE.getModel(this.vanity$capturedStack);

        if (model == null)
            model = ClientConceptArtManager.INSTANCE.getHandModel(this.vanity$capturedStack);

        if (model == null)
            return original;

        return this.itemModelShaper.getModelManager().getModel(model);
    }

    // TODO: find a way to not use a redirect. Forge doesn't seem to like ModifyExpressionVariable.
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/BakedModel;isCustomRenderer()Z"))
    public boolean shouldRender(BakedModel instance) {
        boolean original = instance.isCustomRenderer();
        if (this.vanity$capturedStack.is(VanityItems.CONCEPT_ART.get()))
            return original;

        ModelResourceLocation model = ClientConceptArtManager.INSTANCE.getHandModel(this.vanity$capturedStack);
        if (model == null)
            return original;

        return false;
    }
}
