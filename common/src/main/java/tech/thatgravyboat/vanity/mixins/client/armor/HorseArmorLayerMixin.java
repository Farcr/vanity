package tech.thatgravyboat.vanity.mixins.client.armor;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tech.thatgravyboat.vanity.api.style.AssetTypes;
import tech.thatgravyboat.vanity.client.design.ClientDesignManager;

import java.util.Objects;

@Mixin(HorseArmorLayer.class)
public class HorseArmorLayerMixin {

    @WrapOperation(
        method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Horse;FFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/HorseArmorItem;getTexture()Lnet/minecraft/resources/ResourceLocation;"
        )
    )
    private ResourceLocation vanity$wrapArmorTexture(HorseArmorItem instance, Operation<ResourceLocation> original, @Local ItemStack stack) {
        ResourceLocation texture = ClientDesignManager.INSTANCE.getTexture(stack, AssetTypes.ARMOR);
        return Objects.requireNonNullElseGet(texture, () -> original.call(instance));
    }
}
