package tech.thatgravyboat.vanity.client.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import tech.thatgravyboat.vanity.api.style.ModelType;
import tech.thatgravyboat.vanity.api.style.Style;
import tech.thatgravyboat.vanity.client.components.base.BaseAbstractWidget;
import tech.thatgravyboat.vanity.client.concept.ClientConceptArtManager;
import tech.thatgravyboat.vanity.client.rendering.RenderingManager;
import tech.thatgravyboat.vanity.common.Vanity;

public class StyledItemWidget extends BaseAbstractWidget {

    private static final ResourceLocation NORMAL = new ResourceLocation(Vanity.MOD_ID, "textures/gui/sprites/reset/normal.png");
    private static final ResourceLocation HOVERED = new ResourceLocation(Vanity.MOD_ID, "textures/gui/sprites/reset/hovered.png");

    private ModelResourceLocation model;
    private Type type;
    private ItemStack stack;

    private boolean isAutoRotating = true;
    private float rotation = 45f;

    public StyledItemWidget() {
        super(54, 69);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (this.model == null || this.type == null) return;

        ResourceLocation texture = isHoveringButton(mouseX, mouseY) ? HOVERED : NORMAL;

        graphics.blit(texture, getX() + 1, getY() + getHeight() - 6, 0, 0, 5, 5, 5, 5);

        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        ModelManager manager = renderer.getItemModelShaper().getModelManager();
        RenderingManager.RENDERING.set(false);
        BakedModel model = manager.getModel(this.model);
        if (model == manager.getMissingModel()) return;
        PoseStack stack = graphics.pose();

        graphics.enableScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());

        stack.pushPose();
        stack.translate(getX() + getWidth() * 0.5, getY() + getHeight() * 0.625f, 150);
        stack.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        stack.scale(32, 32, 32);

        this.type.setupPoseStack(stack, getRotation());

        renderer.render(
            this.stack,
            ItemDisplayContext.NONE, false,
            stack, graphics.bufferSource(),
            LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
            model
        );

        stack.popPose();

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
        this.stack = stack;

        Style style = ClientConceptArtManager.INSTANCE.getItemConceptArtVariant(stack);
        if (style == null) {
            this.model = null;
        } else {
            ResourceLocation model = style.model(ModelType.HAND);
            if (model == null) {
                this.model = ClientConceptArtManager.getModelLocation(style.model());
                this.type = Type.GUI;
            } else {
                this.model = ClientConceptArtManager.getModelLocation(model);
                this.type = Type.HAND;
            }
        }
    }

    private enum Type {
        HAND,
        GUI;

        public void setupPoseStack(PoseStack stack, float rotation) {
            if (this == HAND) {
                stack.mulPose(Axis.YN.rotationDegrees(rotation));
                stack.mulPose(Axis.XP.rotationDegrees(22.5f));
            } else {
                stack.scale(1.25f, 1.25f, 1.25f);
                stack.translate(0, 0.25, 0);
                stack.mulPose(Axis.YN.rotationDegrees(rotation));
                stack.mulPose(Axis.ZP.rotationDegrees(90));
            }
        }
    }
}
