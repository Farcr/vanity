package gg.moonflower.vanity.core.mixin.client;

import gg.moonflower.vanity.common.item.ConceptArtItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;contains(Ljava/lang/String;I)Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void vanity$styleName(Player player, TooltipFlag isAdvanced, CallbackInfoReturnable<List<Component>> cir, List<Component> lines) {
        ResourceLocation art = ConceptArtItem.getConceptArtId((ItemStack) (Object) this);
        String variant = ConceptArtItem.getVariantName((ItemStack) (Object) this);
        if (art != null && variant != null) {
            lines.add(ConceptArtItem.getTranslationKey(art, variant).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }
}
