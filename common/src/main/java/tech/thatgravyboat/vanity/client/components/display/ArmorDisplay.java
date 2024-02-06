package tech.thatgravyboat.vanity.client.components.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;
import tech.thatgravyboat.vanity.api.style.Style;

public class ArmorDisplay implements Display {
    private ArmorStand armorStand;

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height, float rotation) {
        if (armorStand == null) return;

        Quaternionf angle = new Quaternionf().rotateYXZ(rotation * 0.017453292F, 180 * 0.017453292F, 0);

        InventoryScreen.renderEntityInInventory(
                graphics,
                x + width / 2,
                y + height - 10,
                25,
                angle,
                null,
                armorStand
        );

    }

    @Override
    public void setValue(ItemStack stack, Style style) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        stack = stack.copyWithCount(1);
        ArmorStand armorStand = new ArmorStand(level, 0, 0, 0);
        armorStand.setInvisible(true);
        if (stack.getItem() instanceof Equipable equipable) {
            armorStand.setItemSlot(equipable.getEquipmentSlot(), stack);
        } else {
            armorStand.setItemInHand(InteractionHand.MAIN_HAND, stack);
        }
        this.armorStand = armorStand;
    }
}
