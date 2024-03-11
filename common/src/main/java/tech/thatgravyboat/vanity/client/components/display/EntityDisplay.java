package tech.thatgravyboat.vanity.client.components.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import tech.thatgravyboat.vanity.api.style.Style;

public abstract class EntityDisplay implements Display {

    private LivingEntity entity;

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height, float rotation) {
        if (this.entity == null) return;

        Quaternionf angle = new Quaternionf().rotateYXZ(rotation * 0.017453292F, 180 * 0.017453292F, 0);

        InventoryScreen.renderEntityInInventory(
                graphics,
                x + width / 2f,
                y + height - 10,
                25,
                new Vector3f(),
                angle,
                null,
                this.entity
        );

    }

    @Override
    public void setValue(ItemStack stack, Style style) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        this.entity = setValue(level, stack.copyWithCount(1));
        if (this.entity == null) return;
        this.entity.setInvisible(true);
    }

    public abstract LivingEntity setValue(Level level, ItemStack stack);
}
