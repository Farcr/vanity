package tech.thatgravyboat.vanity.client.components.base;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.NotNull;

public abstract class BaseAbstractWidget extends AbstractWidget {

    public BaseAbstractWidget(int width, int height) {
        super(0, 0, width, height, CommonComponents.EMPTY);
    }

    @Override
    protected abstract void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick);

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {

    }

    @Override
    public void playDownSound(@NotNull SoundManager handler) {

    }
}
