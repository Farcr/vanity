package gg.moonflower.vanity.core.mixin.client;

import gg.moonflower.vanity.client.concept.ClientConceptArtManager;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;
    @Unique
    private ItemStack capturedStack;

    @Inject(method = "getModel", at = @At("HEAD"))
    public void capture(ItemStack itemStack, Level level, LivingEntity livingEntity, int i, CallbackInfoReturnable<BakedModel> cir) {
        this.capturedStack = itemStack;
    }

    @ModifyVariable(method = "getModel", ordinal = 0, at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/ItemModelShaper;getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;", shift = At.Shift.AFTER))
    public BakedModel getModel(BakedModel original) {
        ModelResourceLocation model = ClientConceptArtManager.INSTANCE.getHandModel(this.capturedStack);
        if (model == null)
            model = ClientConceptArtManager.INSTANCE.getModel(this.capturedStack);
        if (model == null)
            return original;

        return this.itemModelShaper.getModelManager().getModel(model);
    }
}
