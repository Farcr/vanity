package tech.thatgravyboat.vanity.common.item;

import net.minecraft.network.chat.CommonComponents;
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
import tech.thatgravyboat.vanity.common.registries.VanitySounds;
import tech.thatgravyboat.vanity.common.util.ComponentConstants;

public class ConceptArtItem extends Item {

    public ConceptArtItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ResourceLocation art = ConceptArtHelper.getArtId(stack);
        if (art != null) {
            if (!level.isClientSide()) {
                if (UnlockableSaveHandler.addUnlockable(level, player.getUUID(), art)) {
                    player.playNotifySound(
                        VanitySounds.OPEN_CONCEPT_ART.get(), SoundSource.PLAYERS,
                        1.0F, 1.0F
                    );
                    stack.shrink(1);
                } else {
                    player.displayClientMessage(ComponentConstants.CONCEPT_ART_OPEN_FAILURE, true);
                    return InteractionResultHolder.fail(stack);
                }
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        return super.use(level, player, hand);
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        Component itemName = super.getName(stack);
        ResourceLocation art = ConceptArtHelper.getArtId(stack);
        if (art != null) {
            return Component.empty()
                    .append(ConceptArtHelper.getTranslationKey(art, null))
                    .append(CommonComponents.SPACE)
                    .append(itemName);
        }
        return itemName;
    }
}
