package tech.thatgravyboat.vanity.mixins.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Optionull;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
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
import tech.thatgravyboat.vanity.api.design.Design;
import tech.thatgravyboat.vanity.api.design.DesignManager;
import tech.thatgravyboat.vanity.api.style.AssetType;
import tech.thatgravyboat.vanity.client.design.ClientDesignManager;
import tech.thatgravyboat.vanity.client.rendering.RenderingManager;
import tech.thatgravyboat.vanity.common.registries.ModItems;


@Mixin(ItemRenderer.class)
public class ItemRendererMixin implements RenderingManager {

    @Unique
    private AssetType vanity$assetType;

    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;

    @Inject(method = "getModel", at = @At("HEAD"))
    private void vanity$captureModel(ItemStack itemStack, Level level, LivingEntity livingEntity, int i, CallbackInfoReturnable<BakedModel> cir, @Share("renderStack") LocalRef<ItemStack> renderStack) {
        renderStack.set(itemStack);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void vanity$captureRender(ItemStack itemStack, ItemDisplayContext context, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci, @Share("renderStack") LocalRef<ItemStack> renderStack, @Share("guiPerspective") LocalBooleanRef guiPerspective) {
        renderStack.set(itemStack);
        guiPerspective.set(context == ItemDisplayContext.GUI || context == ItemDisplayContext.GROUND || context == ItemDisplayContext.FIXED);
    }

    @ModifyVariable(
        method = "getModel",
        ordinal = 0,
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/ItemModelShaper;getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;", shift = At.Shift.AFTER)
    )
    private BakedModel vanity$replaceModel(BakedModel original, @Share("renderStack") LocalRef<ItemStack> renderStack) {
        ItemStack stack = renderStack.get();
        if (stack.is(ModItems.DESIGN.get())) {
            Design design = DesignManager.get(true).getDesignFromItem(stack);
            if (design != null && design.model() != null) {
                return this.itemModelShaper.getModelManager().getModel(ClientDesignManager.getModelLocation(design.model()));
            }
            return original;
        }

        return Optionull.mapOrDefault(
            ClientDesignManager.INSTANCE.getModel(stack, this.vanity$assetType, RenderingManager.IS_IN_GUI.get() ? null : AssetType.HAND),
            this.itemModelShaper.getModelManager()::getModel,
            original
        );
    }

    @ModifyVariable(
        method = "render",
        ordinal = 0,
        at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/resources/model/ModelManager;getModel(Lnet/minecraft/client/resources/model/ModelResourceLocation;)Lnet/minecraft/client/resources/model/BakedModel;",
                ordinal = 1,
                shift = At.Shift.BY,
                by = 3
        ),
        argsOnly = true
    )
    public BakedModel vanity$render(
        BakedModel original,
        @Share("renderStack") LocalRef<ItemStack> renderStack,
        @Share("guiPerspective") LocalBooleanRef guiPerspective
    ) {
        ItemStack stack = renderStack.get();
        if (stack.is(ModItems.DESIGN.get()) || RenderingManager.RENDERING.get())
            return original;

        ModelResourceLocation model = null;
        if (guiPerspective.get() && this.vanity$assetType == null) {
            model = ClientDesignManager.INSTANCE.getModel(stack, null);
        }

        if (model == null) {
            model = ClientDesignManager.INSTANCE.getModel(stack, this.vanity$assetType, AssetType.HAND);
        }

        return Optionull.mapOrDefault(
            model,
            this.itemModelShaper.getModelManager()::getModel,
            original
        );
    }

    // TODO: find a way to not use a redirect. Forge doesn't seem to like ModifyExpressionVariable.
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/BakedModel;isCustomRenderer()Z"))
    public boolean vanity$shouldRender(BakedModel instance, @Local(name = "stack", ordinal = 0, argsOnly = true) ItemStack stack) {
        boolean original = instance.isCustomRenderer();
        if (stack.is(ModItems.DESIGN.get()))
            return original;

        return !ClientDesignManager.INSTANCE.hasStyle(stack) && original;
    }

    @Redirect(method = "render", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 2
    ))
    public boolean vanity$shouldRender2(ItemStack instance, Item item) {
        return !ClientDesignManager.INSTANCE.hasStyle(instance) && instance.is(item);
    }

    @Override
    public void vanity$setModelType(AssetType type) {
        this.vanity$assetType = type;
    }
}
