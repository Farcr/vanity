package tech.thatgravyboat.vanity.client.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class StylingTabButton extends AbstractWidget {

    private final ItemStack stack;
    private final Runnable action;

    protected StylingTabButton(ItemStack stack, Component title, Runnable action) {
        super(0, 0, 26, 28, title);

        this.stack = stack;
        this.action = action;

        setTooltip(Tooltip.create(this.getMessage()));
    }

    public static StylingTabButton create(ItemLike item, Component title, Runnable action) {
        return new StylingTabButton(item.asItem().getDefaultInstance(), title, action);
    }

    public static StylingTabButton create(ItemLike item, Component title) {
        StylingTabButton button = new StylingTabButton(item.asItem().getDefaultInstance(), title, () -> {});
        button.active = false;
        return button;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.renderFakeItem(this.stack, this.getX() + 5, this.getY() + 8);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.action.run();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, this.createNarrationMessage());
        if (this.active) {
            if (this.isFocused()) {
                output.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.focused"));
            } else {
                output.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.hovered"));
            }
        }
    }

    @Override
    protected @NotNull ClientTooltipPositioner createTooltipPositioner() {
        return DefaultTooltipPositioner.INSTANCE;
    }
}
