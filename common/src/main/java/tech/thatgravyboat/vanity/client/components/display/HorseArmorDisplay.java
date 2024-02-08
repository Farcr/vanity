package tech.thatgravyboat.vanity.client.components.display;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import tech.thatgravyboat.vanity.mixins.common.AbstractHorseAccessor;

public class HorseArmorDisplay extends EntityDisplay {

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height, float rotation) {
        graphics.pose().pushPose();
        graphics.pose().translate(0, 7, 0);
        super.render(graphics, x, y, width, height, rotation);
        graphics.pose().popPose();
    }

    @Override
    public LivingEntity setValue(Level level, ItemStack stack) {
        Horse horse = EntityType.HORSE.create(level);
        if (horse != null) {
            AbstractHorseAccessor accessor = (AbstractHorseAccessor) horse;
            if (stack.getItem() instanceof HorseArmorItem) {
                horse.setItemSlot(EquipmentSlot.CHEST, stack);
                accessor.inventory().setItem(1, stack);
            }
        }
        return horse;
    }
}
