package tech.thatgravyboat.vanity.client.components.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import tech.thatgravyboat.vanity.api.style.AssetType;
import tech.thatgravyboat.vanity.api.style.Style;
import tech.thatgravyboat.vanity.client.components.base.BaseAbstractWidget;
import tech.thatgravyboat.vanity.client.design.ClientDesignManager;
import tech.thatgravyboat.vanity.common.Vanity;

public class StyledItemWidget extends BaseAbstractWidget {

    private static final ResourceLocation NORMAL = new ResourceLocation(Vanity.MOD_ID, "textures/gui/sprites/reset/normal.png");
    private static final ResourceLocation HOVERED = new ResourceLocation(Vanity.MOD_ID, "textures/gui/sprites/reset/hovered.png");

    private Display display;

    private boolean isAutoRotating = true;
    private float rotation = 45f;

    public StyledItemWidget() {
        super(54, 69);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation texture = isHoveringButton(mouseX, mouseY) ? HOVERED : NORMAL;
        graphics.blit(texture, getX() + 1, getY() + getHeight() - 6, 0, 0, 5, 5, 5, 5);

        if (this.display == null) return;

        graphics.enableScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());
        this.display.render(graphics, getX(), getY(), getWidth(), getHeight(), getRotation());
        graphics.disableScissor();
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        this.rotation = getRotation();
        this.isAutoRotating = false;
        this.rotation -= (float) dragX * 3;
        this.rotation = (this.rotation + 360) % 360;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (isHoveringButton(mouseX, mouseY)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            this.isAutoRotating = true;
        }
    }

    public boolean isHoveringButton(double mouseX, double mouseY) {
        int bottom = getY() + getHeight();
        return mouseY < bottom - 1 && mouseY > bottom - 6 && mouseX > getX() + 1 && mouseX < getX() + 6;
    }

    public float getRotation() {
        if (this.isAutoRotating) {
            long time = System.currentTimeMillis() / 20;
            return 45 + time % 360;
        }
        return this.rotation;
    }

    public void select(ItemStack stack) {
        Style style = ClientDesignManager.INSTANCE.getStyleFromItem(stack);
        if (style == null) {
            this.display = null;
        } else if (style.hasAsset(AssetType.ARMOR)) {
            this.display = new ArmorDisplay();
        } else {
            this.display = new ItemDisplay();
        }

        if (this.display != null) {
            this.display.setValue(stack, style);
        }
    }
}
