package tech.thatgravyboat.vanity.client.components.display;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import tech.thatgravyboat.vanity.api.style.AssetTypes;
import tech.thatgravyboat.vanity.api.style.Style;
import tech.thatgravyboat.vanity.client.design.ClientDesignManager;
import tech.thatgravyboat.vanity.client.rendering.RenderingManager;

public class ItemDisplay implements Display {

    private ItemStack stack;
    private ModelResourceLocation model;
    private Type type;

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height, float rotation) {
        PoseStack stack = graphics.pose();
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        ModelManager manager = renderer.getItemModelShaper().getModelManager();
        RenderingManager.RENDERING.set(false);
        BakedModel model = manager.getModel(this.model);
        if (model == manager.getMissingModel()) return;

        stack.pushPose();
        stack.translate(x + width * 0.5, y + height * 0.625f, 150);
        stack.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        stack.scale(32, 32, 32);

        this.type.setupPoseStack(stack, rotation);

        renderer.render(
                this.stack,
                ItemDisplayContext.NONE, false,
                stack, graphics.bufferSource(),
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
                model
        );

        stack.popPose();
    }

    @Override
    public void setValue(ItemStack stack, Style style) {
        this.stack = stack;
        ResourceLocation model = style.asset(AssetTypes.HAND);
        if (model == null) {
            this.model = ClientDesignManager.getModelLocation(style.asset(AssetTypes.DEFAULT));
            this.type = Type.GUI;
        } else {
            this.model = ClientDesignManager.getModelLocation(model);
            this.type = Type.HAND;
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
