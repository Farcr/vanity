package tech.thatgravyboat.vanity.api.concept;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.vanity.api.style.Style;

import java.util.*;
import java.util.function.Function;

public record ConceptArt(
    @Nullable ResourceLocation conceptArtModel,
    ConceptType type,
    Map<String, List<Style>> styles
) {

    public static final MapCodec<ConceptType> TYPE_CODEC = Codec.mapEither(
            ConceptType.CODEC.fieldOf("type"), Codec.BOOL.fieldOf("sold")
    ).xmap(either -> either.map(Function.identity(), ConceptType::fromBoolean), Either::left);

    public static final Codec<ConceptArt> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("concept_art_model").forGetter(CodecExtras.optionalFor(ConceptArt::conceptArtModel)),
            TYPE_CODEC.forGetter(ConceptArt::type),
            Codec.unboundedMap(Codec.STRING, Style.CODEC.listOf()).fieldOf("styles").forGetter(ConceptArt::styles)
    ).apply(instance, ConceptArt::new));

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private ConceptArt(Optional<ResourceLocation> model, ConceptType type, Map<String, List<Style>> variants) {
        this(model.orElse(null), type, variants);
    }

    @Nullable
    public Style getVariantForItem(String name, ItemStack stack) {
        List<Style> entries = this.styles.get(name);
        if (entries == null) {
            return null;
        }

        for (Style entry : entries) {
            if (entry.supportsItem(stack)) {
                return entry;
            }
        }

        return null;
    }

    public boolean canBeSold() {
        return this.type == ConceptType.SELLABLE;
    }

}
