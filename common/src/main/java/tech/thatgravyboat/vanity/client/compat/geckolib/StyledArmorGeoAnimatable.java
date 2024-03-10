package tech.thatgravyboat.vanity.client.compat.geckolib;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.util.RenderUtils;
import tech.thatgravyboat.vanity.api.style.AssetTypes;
import tech.thatgravyboat.vanity.client.design.ClientDesignManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class StyledArmorGeoAnimatable implements SingletonGeoAnimatable {

    private static final Map<ResourceLocation, StyledArmorGeoAnimatable> INSTANCES = new HashMap<>();

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final ResourceLocation id;
    private StyledGeoArmorRenderer renderer;

    private StyledArmorGeoAnimatable(ResourceLocation id) {
        this.id = id;
    }

    public static StyledArmorGeoAnimatable get(ItemStack stack) {
        ResourceLocation id = ClientDesignManager.INSTANCE.getAsset(stack, AssetTypes.GECKOLIB_ARMOR);
        if (id == null) return null;
        return get(id);
    }

    public static StyledArmorGeoAnimatable get(ResourceLocation id) {
        return INSTANCES.computeIfAbsent(id, StyledArmorGeoAnimatable::new);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 20, (state) -> {
            state.getController().setAnimation(DefaultAnimations.IDLE);
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object o) {
        return RenderUtils.getCurrentTick();
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return null;
    }

    public HumanoidModel<LivingEntity> getModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<LivingEntity> original) {
        if(this.renderer == null) {
            this.renderer = new StyledGeoArmorRenderer(new DefaultedItemGeoModel<>(id));
        }

        this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

        return this.renderer;
    }
}
