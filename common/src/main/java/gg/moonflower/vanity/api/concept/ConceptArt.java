package gg.moonflower.vanity.api.concept;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gg.moonflower.vanity.core.util.XorMapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class ConceptArt {

    public static final Codec<ConceptArt> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.optionalFieldOf("concept_art_model").forGetter(art -> Optional.ofNullable(art.conceptArtModel)),
        Codec.BOOL.optionalFieldOf("sold", false).forGetter(ConceptArt::sold),
        Codec.unboundedMap(ResourceLocation.CODEC, Variant.CODEC.listOf()).fieldOf("variants").forGetter(ConceptArt::variants)
    ).apply(instance, (conceptArtModel, sold, variants) -> new ConceptArt(conceptArtModel.orElse(null), sold, variants)));

    @Nullable
    private final ResourceLocation conceptArtModel;
    private final boolean sold;
    private final Map<ResourceLocation, List<Variant>> variants;

    public ConceptArt(@Nullable ResourceLocation conceptArtModel, boolean sold, Map<ResourceLocation, List<Variant>> variants) {
        this.conceptArtModel = conceptArtModel;
        this.sold = sold;
        this.variants = variants;
    }

    @Nullable
    public ConceptArt.Variant getVariantForItem(ResourceLocation id, Item item) {
        List<Variant> entries = this.variants.get(id);
        if (entries == null)
            return null;

        for (Variant entry : entries) {
            if (entry.supportsItem(item))
                return entry;
        }

        return null;
    }

    @Nullable
    public ResourceLocation conceptArtModel() {
        return conceptArtModel;
    }

    public boolean sold() {
        return sold;
    }

    public Map<ResourceLocation, List<Variant>> variants() {
        return variants;
    }

    public record Variant(Either<TagKey<Item>, ResourceLocation> item, ResourceLocation model,
                          @Nullable ResourceLocation handModel) {

        public static final Codec<Variant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            XorMapCodec.create(TagKey.codec(Registry.ITEM.key()).fieldOf("tag"), ResourceLocation.CODEC.fieldOf("item")).forGetter(Variant::item),
            ResourceLocation.CODEC.fieldOf("model").forGetter(Variant::model),
            ResourceLocation.CODEC.optionalFieldOf("hand_model").forGetter(variant -> Optional.ofNullable(variant.handModel))
        ).apply(instance, (item, model, handModel) -> new Variant(item, model, handModel.orElse(null))));

        public boolean supportsItem(Item item) {
            return this.item.right().isPresent() ? Registry.ITEM.get(this.item.right().get()).equals(item) : this.item.left().isPresent() && item.builtInRegistryHolder().is(this.item.left().get());
        }
    }
}
