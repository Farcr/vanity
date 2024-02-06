package tech.thatgravyboat.vanity.client.components.display;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import tech.thatgravyboat.vanity.api.style.Style;

public interface Display {

    void render(GuiGraphics graphics, int x, int y, int width, int height, float rotation);

    void setValue(ItemStack stack, Style style);
}
