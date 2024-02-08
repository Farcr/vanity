package tech.thatgravyboat.vanity.client.components.display;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ArmorDisplay extends EntityDisplay {

    @Override
    public LivingEntity setValue(Level level, ItemStack stack) {
        ArmorStand armorStand = new ArmorStand(level, 0, 0, 0);
        if (stack.getItem() instanceof Equipable equipable) {
            armorStand.setItemSlot(equipable.getEquipmentSlot(), stack);
        } else {
            armorStand.setItemInHand(InteractionHand.MAIN_HAND, stack);
        }
        return armorStand;
    }
}
