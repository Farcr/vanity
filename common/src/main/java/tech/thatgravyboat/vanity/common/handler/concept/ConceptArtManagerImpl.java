package tech.thatgravyboat.vanity.common.handler.concept;

import tech.thatgravyboat.vanity.api.concept.ConceptArt;
import tech.thatgravyboat.vanity.api.concept.ConceptArtManager;
import tech.thatgravyboat.vanity.api.style.Style;
import tech.thatgravyboat.vanity.common.item.ConceptArtHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class ConceptArtManagerImpl implements ConceptArtManager {

    protected final Map<ResourceLocation, ConceptArt> conceptArt = new HashMap<>();
    protected final Map<ResourceLocation, ConceptArt> defaultConceptArt = new HashMap<>();

    public void clear() {
        this.conceptArt.clear();
        this.defaultConceptArt.clear();
    }

    public void setupDefaults() {
        this.conceptArt.forEach((id, art) -> {
            if (!art.type().isDefault()) return;
            this.defaultConceptArt.put(id, art);
        });
    }

    @Override
    public Optional<ConceptArt> getConceptArt(ResourceLocation location) {
        return Optional.ofNullable(this.conceptArt.get(location));
    }

    @Override
    public Optional<ResourceLocation> getConceptArtId(ConceptArt art) {
        return this.conceptArt.entrySet().stream().filter(entry -> entry.getValue().equals(art)).map(Map.Entry::getKey).findFirst();
    }

    @Override
    public Map<ResourceLocation, ConceptArt> getAllConceptArt() {
        return Collections.unmodifiableMap(this.conceptArt);
    }

    @Override
    public Map<ResourceLocation, ConceptArt> getDefaultConceptArt() {
        return Collections.unmodifiableMap(this.defaultConceptArt);
    }

    @Override
    @Nullable
    public ConceptArt getItemConceptArt(ItemStack stack) {
        return Optional.ofNullable(ConceptArtHelper.getArtId(stack))
                .flatMap(this::getConceptArt)
                .orElse(null);
    }

    @Override
    @Nullable
    public Style getItemConceptArtVariant(ItemStack stack) {
        String variant = ConceptArtHelper.getStyle(stack);
        if (variant == null)
            return null;

        ConceptArt art = this.getItemConceptArt(stack);
        if (art == null)
            return null;

        return art.getVariantForItem(variant, stack);
    }

    public boolean hasVariant(ItemStack stack) {
        return this.getItemConceptArtVariant(stack) != null;
    }
}
