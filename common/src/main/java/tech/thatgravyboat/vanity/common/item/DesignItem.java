package tech.thatgravyboat.vanity.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.vanity.common.handler.unlockables.UnlockableSaveHandler;
import tech.thatgravyboat.vanity.common.registries.ModGameRules;
import tech.thatgravyboat.vanity.common.registries.ModSounds;
import tech.thatgravyboat.vanity.common.util.ComponentConstants;

public class DesignItem extends Item {

    public DesignItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (ModGameRules.UNLOCKABLE_DESIGNS.getValue(level, level.isClientSide())) {
            ItemStack stack = player.getItemInHand(hand);
            ResourceLocation design = DesignHelper.getDesign(stack);
            if (design != null) {
                if (!level.isClientSide()) {
                    if (UnlockableSaveHandler.addUnlockable(level, player.getUUID(), design)) {
                        player.playNotifySound(
                                ModSounds.OPEN_DESIGN.get(), SoundSource.PLAYERS,
                                1.0F, 1.0F
                        );
                        stack.shrink(1);
                    } else {
                        player.displayClientMessage(ComponentConstants.DESIGN_OPEN_FAILURE, true);
                        return InteractionResultHolder.fail(stack);
                    }
                }
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
            }
            return InteractionResultHolder.fail(stack);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        ResourceLocation design = DesignHelper.getDesign(stack);
        if (design != null) {
            return DesignHelper.getTranslationKey(design, null);
        }
        return super.getName(stack);
    }
}
