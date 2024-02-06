package tech.thatgravyboat.vanity.common.handler.design;

import tech.thatgravyboat.vanity.api.design.Design;
import tech.thatgravyboat.vanity.api.design.DesignManager;
import tech.thatgravyboat.vanity.api.style.Style;
import tech.thatgravyboat.vanity.common.item.DesignHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class DesignManagerImpl implements DesignManager {

    protected final Map<ResourceLocation, Design> designs = new HashMap<>();
    protected final Map<ResourceLocation, Design> defaultDesigns = new HashMap<>();

    public void clear() {
        this.designs.clear();
        this.defaultDesigns.clear();
    }

    public void setupDefaults() {
        this.designs.forEach((id, design) -> {
            if (!design.type().isDefault()) return;
            this.defaultDesigns.put(id, design);
        });
    }

    @Override
    public Optional<Design> getDesign(ResourceLocation location) {
        return Optional.ofNullable(this.designs.get(location));
    }

    @Override
    public Map<ResourceLocation, Design> getAllDesigns() {
        return Collections.unmodifiableMap(this.designs);
    }

    @Override
    public Map<ResourceLocation, Design> getDefaultDesigns() {
        return Collections.unmodifiableMap(this.defaultDesigns);
    }

    @Override
    @Nullable
    public Design getDesignFromItem(ItemStack stack) {
        return Optional.ofNullable(DesignHelper.getDesign(stack))
                .flatMap(this::getDesign)
                .orElse(null);
    }

    @Override
    @Nullable
    public Style getStyleFromItem(ItemStack stack) {
        String style = DesignHelper.getStyle(stack);
        if (style == null)
            return null;

        Design design = this.getDesignFromItem(stack);
        if (design == null)
            return null;

        return design.getStyleForItem(style, stack);
    }

    public boolean hasStyle(ItemStack stack) {
        return this.getStyleFromItem(stack) != null;
    }
}
