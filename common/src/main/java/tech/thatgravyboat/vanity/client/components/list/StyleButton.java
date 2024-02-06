package tech.thatgravyboat.vanity.client.components.list;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.vanity.client.components.base.BaseAbstractWidget;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.item.DesignHelper;
import tech.thatgravyboat.vanity.common.menu.StylingMenu;
import tech.thatgravyboat.vanity.common.network.NetworkHandler;
import tech.thatgravyboat.vanity.common.network.packets.server.ServerboundSelectStylePacket;
import tech.thatgravyboat.vanity.common.util.ComponentConstants;

public class StyleButton extends BaseAbstractWidget {

    private static final ResourceLocation NORMAL = new ResourceLocation(Vanity.MOD_ID, "textures/gui/sprites/button/normal.png");
    private static final ResourceLocation HOVERED = new ResourceLocation(Vanity.MOD_ID, "textures/gui/sprites/button/hovered.png");
    private static final ResourceLocation SELECTED = new ResourceLocation(Vanity.MOD_ID, "textures/gui/sprites/button/selected.png");

    private final ResourceLocation design;
    private final String style;

    private final ItemStack stack;

    private boolean selected;

    public StyleButton(ResourceLocation design, String style, ItemStack inputStack) {
        super(18, 18);

        boolean shouldRemove = design.equals(StylingMenu.REMOVE_DESIGN);

        this.design = design;
        this.style = style;
        this.stack = inputStack.copyWithCount(1);
        DesignHelper.setDesignAndStyle(this.stack, shouldRemove ? null : design, style);

        if (shouldRemove) {
            this.setTooltip(Tooltip.create(ComponentConstants.ORIGINAL));
        } else {
            this.setTooltip(Tooltip.create(DesignHelper.getTranslationKey(design, style)));
        }
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation texture = texture(this.isHovered(), selected);
        graphics.blit(
            texture,
            this.getX(), this.getY(),
            0, 0,
            this.getWidth(), this.getHeight(),
            this.getWidth(), this.getHeight()
        );

        graphics.renderItem(this.stack, this.getX() + 1, this.getY() + 1);
    }

    @Override
    public void onClick(double d, double e) {
        SoundManager sounds = Minecraft.getInstance().getSoundManager();
        sounds.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        ServerboundSelectStylePacket packet = new ServerboundSelectStylePacket(this.design, this.style);
        NetworkHandler.CHANNEL.sendToServer(packet);
    }

    public void select(@Nullable ResourceLocation design, @Nullable String style) {
        this.selected = this.design.equals(design) && this.style.equals(style);
    }

    public static ResourceLocation texture(boolean hovered, boolean selected) {
        if (selected) return SELECTED;
        if (hovered) return HOVERED;
        return NORMAL;
    }
}
