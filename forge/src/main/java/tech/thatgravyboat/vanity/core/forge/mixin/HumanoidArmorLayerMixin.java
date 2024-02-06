package tech.thatgravyboat.vanity.core.forge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.thatgravyboat.vanity.api.style.AssetType;
import tech.thatgravyboat.vanity.client.design.ClientDesignManager;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {

    @Inject(
            method = "renderArmorPiece",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;usesInnerModel(Lnet/minecraft/world/entity/EquipmentSlot;)Z"
            )
    )
    private void vanity$renderArmorPiece(
            PoseStack poseStack, MultiBufferSource multiBufferSource, T livingEntity, EquipmentSlot equipmentSlot, int i, A humanoidModel, CallbackInfo ci,
            @Local ItemStack stack, @Share("vanity$texture") LocalRef<ResourceLocation> texture
    ) {
        texture.set(ClientDesignManager.INSTANCE.getTexture(stack, AssetType.ARMOR));
    }

    @WrapOperation(
            method = "renderArmorPiece",
            constant = @Constant(classValue = DyeableLeatherItem.class, ordinal = 0)
    )
    private boolean vanity$isDyeableArmor(Object obj, Operation<Boolean> original, @Share("texture") LocalRef<ResourceLocation> texture) {
        if (texture.get() != null) {
            return false;
        }
        return original.call(obj);
    }

    @WrapOperation(
        method = "renderArmorPiece",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/ArmorItem;Lnet/minecraft/client/model/Model;ZFFFLnet/minecraft/resources/ResourceLocation;)V",
            ordinal = 2
        )
    )
    private void vantity$changeTexture(
        HumanoidArmorLayer<T, M, A> instance, PoseStack stack, MultiBufferSource source, int i, ArmorItem armorItem, Model model, boolean bl, float f, float g, float h, ResourceLocation string, Operation<Void> original,
        @Share("vanity$texture") LocalRef<ResourceLocation> texture
    ) {
        if (texture.get() != null) {
            VertexConsumer consumer = source.getBuffer(RenderType.armorCutoutNoCull(texture.get()));
            model.renderToBuffer(stack, consumer, i, OverlayTexture.NO_OVERLAY, f, g, h, 1.0F);
        } else {
            original.call(instance, stack, source, i, armorItem, model, bl, f, g, h, string);
        }
    }
}