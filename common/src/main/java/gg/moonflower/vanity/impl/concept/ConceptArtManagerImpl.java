package gg.moonflower.vanity.impl.concept;

import gg.moonflower.vanity.api.concept.ConceptArt;
import gg.moonflower.vanity.api.concept.ConceptArtManager;
import gg.moonflower.vanity.common.item.ConceptArtItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public abstract class ConceptArtManagerImpl implements ConceptArtManager {

    protected final Map<ResourceLocation, ConceptArt> conceptArt = new HashMap<>();

    @Override
    public Optional<ConceptArt> getConceptArt(ResourceLocation location) {
        return Optional.ofNullable(this.conceptArt.get(location));
    }

    @Override
    public Optional<ResourceLocation> getConceptArtId(ConceptArt art) {
        return this.conceptArt.entrySet().stream().filter(entry -> entry.getValue().equals(art)).map(Map.Entry::getKey).findFirst();
    }

    @Override
    public Stream<ResourceLocation> getAllConceptArtIds() {
        return this.conceptArt.keySet().stream();
    }

    @Override
    public Stream<ConceptArt> getAllConceptArt() {
        return this.conceptArt.values().stream();
    }

    @Override
    @Nullable
    public ConceptArt getItemConceptArt(ItemStack stack) {
        ResourceLocation location = ConceptArtItem.getConceptArtId(stack);
        if (location == null)
            return null;

        return this.getConceptArt(location).orElse(null);
    }

    @Override
    @Nullable
    public ConceptArt.Variant getItemConceptArtVariant(ItemStack stack) {
        String variant = ConceptArtItem.getVariantName(stack);
        if (variant == null)
            return null;

        ConceptArt art = this.getItemConceptArt(stack);
        if (art == null)
            return null;

        return art.getVariantForItem(variant, stack);
    }
}
