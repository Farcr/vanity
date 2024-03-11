package tech.thatgravyboat.vanity.mixins.compat.geckolib;

import com.llamalad7.mixinextras.sugar.Local;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.thatgravyboat.vanity.api.style.AssetTypes;
import tech.thatgravyboat.vanity.client.compat.geckolib.StyledArmorGeoAnimatable;
import tech.thatgravyboat.vanity.client.design.ClientDesignManager;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin<T extends LivingEntity, A extends HumanoidModel<T>> {

    @SuppressWarnings("unchecked")
    @ModifyArg(
            method = "renderArmorPiece",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/ArmorItem;Lnet/minecraft/client/model/HumanoidModel;ZFFFLjava/lang/String;)V"
            ),
            index = 4
    )
    @PlatformOnly("fabric")
    private A vanity$changeModel(A model, @Local(argsOnly = true) T livingEntity, @Local(argsOnly = true) EquipmentSlot equipmentSlot) {
        ItemStack stack = livingEntity.getItemBySlot(equipmentSlot);
        if (ClientDesignManager.INSTANCE.hasAsset(stack, AssetTypes.GECKOLIB_ARMOR)) {
            StyledArmorGeoAnimatable animatable = StyledArmorGeoAnimatable.get(stack);
            if (animatable != null) {
                HumanoidModel<LivingEntity> original = (HumanoidModel<LivingEntity>) model;
                HumanoidModel<LivingEntity> replacement = animatable.getModel(livingEntity, stack, equipmentSlot, original);
                if (replacement != original) {
                    original.copyPropertiesTo(replacement);
                    return (A) replacement;
                }
            }
        }
        return model;
    }

    @SuppressWarnings({"MixinAnnotationTarget", "UnresolvedMixinReference", "unchecked"})
    @Inject(method = "getArmorModelHook", remap = false, at = @At("HEAD"), cancellable = true)
    @PlatformOnly("neoforge")
    private void vanity$changeModel(T livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, A model, CallbackInfoReturnable<A> cir) {
        if (ClientDesignManager.INSTANCE.hasAsset(itemStack, AssetTypes.GECKOLIB_ARMOR)) {
            StyledArmorGeoAnimatable animatable = StyledArmorGeoAnimatable.get(itemStack);
            if (animatable != null) {
                HumanoidModel<LivingEntity> original = (HumanoidModel<LivingEntity>) model;
                HumanoidModel<LivingEntity> replacement = animatable.getModel(livingEntity, itemStack, equipmentSlot, original);
                if (replacement != model) {
                    original.copyPropertiesTo(replacement);
                    cir.setReturnValue((A) replacement);
                }
            }
        }
    }
}
