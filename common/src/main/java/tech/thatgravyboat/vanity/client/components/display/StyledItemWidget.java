package tech.thatgravyboat.vanity.client.components.display;

import com.teamresourceful.resourcefullib.client.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.vanity.api.design.DesignManager;
import tech.thatgravyboat.vanity.api.style.AssetTypes;
import tech.thatgravyboat.vanity.api.style.Style;
import tech.thatgravyboat.vanity.client.components.base.BaseAbstractWidget;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.util.ComponentConstants;

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
    protected void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (!this.isAutoRotating) {
            boolean buttonHovered = isHoveringButton(mouseX, mouseY);
            ResourceLocation texture = buttonHovered ? HOVERED : NORMAL;
            graphics.blit(texture, getX() + 1, getY() + getHeight() - 6, 0, 0, 5, 5, 5, 5);
            if (buttonHovered) ScreenUtils.setTooltip(ComponentConstants.AUTO_ROTATE);
        }

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
        if (isHoveringButton(mouseX, mouseY) && !this.isAutoRotating) {
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
        Style style = DesignManager.client().getStyleFromItem(stack);
        if (style == null) {
            this.display = null;
        } else if (style.hasAsset(AssetTypes.ARMOR) || style.hasAsset(AssetTypes.GECKOLIB_ARMOR)) {
            if (stack.getItem() instanceof HorseArmorItem) {
                this.display = new HorseArmorDisplay();
            } else {
                this.display = new ArmorDisplay();
            }
        } else {
            this.display = new ItemDisplay();
        }

        if (this.display != null) {
            this.display.setValue(stack, style);
        }
    }
}
