package tech.thatgravyboat.vanity.client.compat.geckolib;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.texture.AnimatableTexture;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.util.RenderUtils;
import tech.thatgravyboat.vanity.mixins.client.armor.AgeableListModelAccessor;
import tech.thatgravyboat.vanity.mixins.client.armor.LevelRenderAccessor;

public class StyledGeoArmorRenderer extends HumanoidModel<LivingEntity> implements GeoRenderer<StyledArmorGeoAnimatable> {
    protected final GeoModel<StyledArmorGeoAnimatable> model;

    protected StyledArmorGeoAnimatable animatable;
    protected HumanoidModel<?> baseModel;
    protected float scaleWidth = 1;
    protected float scaleHeight = 1;

    protected Matrix4f entityRenderTranslations = new Matrix4f();
    protected Matrix4f modelRenderTranslations = new Matrix4f();

    protected BakedGeoModel lastModel = null;
    protected GeoBone head = null;
    protected GeoBone body = null;
    protected GeoBone rightArm = null;
    protected GeoBone leftArm = null;
    protected GeoBone rightLeg = null;
    protected GeoBone leftLeg = null;
    protected GeoBone rightBoot = null;
    protected GeoBone leftBoot = null;

    protected Entity currentEntity = null;
    protected ItemStack currentStack = null;
    protected EquipmentSlot currentSlot = null;

    public StyledGeoArmorRenderer(GeoModel<StyledArmorGeoAnimatable> model) {
        super(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));

        this.model = model;
        this.young = false;
    }

    @Override
    public GeoModel<StyledArmorGeoAnimatable> getGeoModel() {
        return this.model;
    }

    @Override
    public StyledArmorGeoAnimatable getAnimatable() {
        return this.animatable;
    }

    @Override
    public long getInstanceId(StyledArmorGeoAnimatable animatable) {
        return this.currentEntity.getId();
    }

    @Override
    public RenderType getRenderType(StyledArmorGeoAnimatable style, ResourceLocation texture, @Nullable MultiBufferSource source, float partialTick) {
        return RenderType.armorCutoutNoCull(texture);
    }

    @Override
    public void preRender(PoseStack stack, StyledArmorGeoAnimatable style, BakedGeoModel model, @Nullable MultiBufferSource source, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        this.entityRenderTranslations = new Matrix4f(stack.last().pose());

        applyBaseModel(this.baseModel);
        grabRelevantBones(getGeoModel().getBakedModel(getGeoModel().getModelResource(this.animatable)));
        applyBaseTransformations(this.baseModel);
        scaleModelForBaby(stack, isReRender);
        scaleModelForRender(this.scaleWidth, this.scaleHeight, stack, style, model, isReRender, partialTick, packedLight, packedOverlay);

        if (!(this.currentEntity instanceof GeoAnimatable)) {
            setAllVisible(true);
        }
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack stack, @NotNull VertexConsumer consumer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        Minecraft mc = Minecraft.getInstance();
        MultiBufferSource bufferSource = mc.renderBuffers().bufferSource();

        LevelRenderAccessor accessor = (LevelRenderAccessor) mc.levelRenderer;

        if (accessor.getShouldShowEntityOutlines() && mc.shouldEntityAppearGlowing(this.currentEntity)) {
            bufferSource = mc.renderBuffers().outlineBufferSource();
        }

        float partialTick = mc.getFrameTime();
        RenderType renderType = getRenderType(this.animatable, getTextureLocation(this.animatable), bufferSource, partialTick);
        consumer = ItemRenderer.getArmorFoilBuffer(bufferSource, renderType, false, this.currentStack.hasFoil());

        defaultRender(stack, this.animatable, bufferSource, null, consumer,
                0, partialTick, packedLight);
    }

    @Override
    public void actuallyRender(PoseStack stack, StyledArmorGeoAnimatable style, BakedGeoModel model, RenderType type, MultiBufferSource source, VertexConsumer consumer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        stack.pushPose();
        stack.translate(0, 24 / 16f, 0);
        stack.scale(-1, -1, 1);

        if (!isReRender) {
            AnimationState<StyledArmorGeoAnimatable> animationState = new AnimationState<>(style, 0, 0, partialTick, false);
            long instanceId = getInstanceId(style);

            animationState.setData(DataTickets.TICK, style.getTick(this.currentEntity));
            animationState.setData(DataTickets.ITEMSTACK, this.currentStack);
            animationState.setData(DataTickets.ENTITY, this.currentEntity);
            animationState.setData(DataTickets.EQUIPMENT_SLOT, this.currentSlot);
            this.model.addAdditionalStateData(style, instanceId, animationState::setData);
            this.model.handleAnimations(style, instanceId, animationState);
        }

        this.modelRenderTranslations = new Matrix4f(stack.last().pose());

        GeoRenderer.super.actuallyRender(stack, style, model, type, source, consumer, isReRender, partialTick, packedLight, packedOverlay, r, g, b, a);
        stack.popPose();
    }

    @Override
    public void renderRecursively(PoseStack stack, StyledArmorGeoAnimatable animatable, GeoBone bone, RenderType type, MultiBufferSource source, VertexConsumer consumer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        if (bone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(stack.last().pose());

            bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            bone.setLocalSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations));
        }

        GeoRenderer.super.renderRecursively(stack, animatable, bone, type, source, consumer, isReRender, partialTick, packedLight, packedOverlay, r, g, b, a);
    }

    protected void grabRelevantBones(BakedGeoModel model) {
        if (this.lastModel == model)
            return;

        this.lastModel = model;
        this.head = this.model.getBone("armorHead").orElse(null);
        this.body = this.model.getBone("armorBody").orElse(null);
        this.rightArm = this.model.getBone("armorRightArm").orElse(null);
        this.leftArm = this.model.getBone("armorLeftArm").orElse(null);
        this.rightLeg = this.model.getBone("armorRightLeg").orElse(null);
        this.leftLeg = this.model.getBone("armorLeftLeg").orElse(null);
        this.rightBoot = this.model.getBone("armorRightBoot").orElse(null);
        this.leftBoot = this.model.getBone("armorLeftBoot").orElse(null);
    }

    public void prepForRender(@Nullable Entity entity, ItemStack stack, @Nullable EquipmentSlot slot, @Nullable HumanoidModel<?> baseModel) {
        if (entity == null || slot == null || baseModel == null)
            return;

        this.baseModel = baseModel;
        this.currentEntity = entity;
        this.currentStack = stack;
        this.animatable = StyledArmorGeoAnimatable.get(stack);
        this.currentSlot = slot;
    }

    protected void applyBaseModel(HumanoidModel<?> baseModel) {
        this.young = baseModel.young;
        this.crouching = baseModel.crouching;
        this.riding = baseModel.riding;
        this.rightArmPose = baseModel.rightArmPose;
        this.leftArmPose = baseModel.leftArmPose;
    }

    protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
        if (this.head != null) {
            ModelPart headPart = baseModel.head;

            RenderUtils.matchModelPartRot(headPart, this.head);
            this.head.updatePosition(headPart.x, -headPart.y, headPart.z);
        }

        if (this.body != null) {
            ModelPart bodyPart = baseModel.body;

            RenderUtils.matchModelPartRot(bodyPart, this.body);
            this.body.updatePosition(bodyPart.x, -bodyPart.y, bodyPart.z);
        }

        if (this.rightArm != null) {
            ModelPart rightArmPart = baseModel.rightArm;

            RenderUtils.matchModelPartRot(rightArmPart, this.rightArm);
            this.rightArm.updatePosition(rightArmPart.x + 5, 2 - rightArmPart.y, rightArmPart.z);
        }

        if (this.leftArm != null) {
            ModelPart leftArmPart = baseModel.leftArm;

            RenderUtils.matchModelPartRot(leftArmPart, this.leftArm);
            this.leftArm.updatePosition(leftArmPart.x - 5f, 2f - leftArmPart.y, leftArmPart.z);
        }

        if (this.rightLeg != null) {
            ModelPart rightLegPart = baseModel.rightLeg;

            RenderUtils.matchModelPartRot(rightLegPart, this.rightLeg);
            this.rightLeg.updatePosition(rightLegPart.x + 2, 12 - rightLegPart.y, rightLegPart.z);

            if (this.rightBoot != null) {
                RenderUtils.matchModelPartRot(rightLegPart, this.rightBoot);
                this.rightBoot.updatePosition(rightLegPart.x + 2, 12 - rightLegPart.y, rightLegPart.z);
            }
        }

        if (this.leftLeg != null) {
            ModelPart leftLegPart = baseModel.leftLeg;

            RenderUtils.matchModelPartRot(leftLegPart, this.leftLeg);
            this.leftLeg.updatePosition(leftLegPart.x - 2, 12 - leftLegPart.y, leftLegPart.z);

            if (this.leftBoot != null) {
                RenderUtils.matchModelPartRot(leftLegPart, this.leftBoot);
                this.leftBoot.updatePosition(leftLegPart.x - 2, 12 - leftLegPart.y, leftLegPart.z);
            }
        }
    }

    @Override
    public void setAllVisible(boolean pVisible) {
        super.setAllVisible(pVisible);

        setBoneVisible(this.head, pVisible);
        setBoneVisible(this.body, pVisible);
        setBoneVisible(this.rightArm, pVisible);
        setBoneVisible(this.leftArm, pVisible);
        setBoneVisible(this.rightLeg, pVisible);
        setBoneVisible(this.leftLeg, pVisible);
        setBoneVisible(this.rightBoot, pVisible);
        setBoneVisible(this.leftBoot, pVisible);
    }

    public void scaleModelForBaby(PoseStack poseStack, boolean isReRender) {
        if (!this.young || isReRender)
            return;

        AgeableListModelAccessor accessor = (AgeableListModelAccessor) this.baseModel;

        if (this.currentSlot == EquipmentSlot.HEAD) {
            if (accessor.getScaleHead()) {
                float headScale = 1.5f / accessor.getBabyHeadScale();

                poseStack.scale(headScale, headScale, headScale);
            }

            poseStack.translate(0, accessor.getBabyYHeadOffset() / 16f, accessor.getBabyZHeadOffset() / 16f);
        } else {
            float bodyScale = 1 / accessor.getBabyBodyScale();

            poseStack.scale(bodyScale, bodyScale, bodyScale);
            poseStack.translate(0, accessor.getBodyYOffset() / 16f, 0);
        }
    }

    protected void setBoneVisible(@Nullable GeoBone bone, boolean visible) {
        if (bone == null)
            return;

        bone.setHidden(!visible);
    }

    @Override
    public void updateAnimatedTextureFrame(StyledArmorGeoAnimatable animatable) {
        if (this.currentEntity != null)
            AnimatableTexture.setAndUpdate(getTextureLocation(animatable), this.currentEntity.getId() + this.currentEntity.tickCount);
    }

    @Override
    public void fireCompileRenderLayersEvent() {
    }

    @Override
    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return true;
    }

    @Override
    public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
    }
}