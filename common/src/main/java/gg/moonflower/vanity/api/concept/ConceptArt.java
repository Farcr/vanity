package gg.moonflower.vanity.api.concept;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ConceptArt(Either<TagKey<Item>, ResourceLocation> item, @Nullable ResourceLocation model, @Nullable ResourceLocation handModel) {

    public static final Codec<ConceptArt> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.mapEither(TagKey.codec(Registry.ITEM.key()).fieldOf("tag"), ResourceLocation.CODEC.fieldOf("item")).forGetter(ConceptArt::item),
        ResourceLocation.CODEC.optionalFieldOf("model").forGetter(art -> Optional.ofNullable(art.model)),
        ResourceLocation.CODEC.optionalFieldOf("hand_model").forGetter(art -> Optional.ofNullable(art.handModel))
    ).apply(instance, (item, model, handModel) -> new ConceptArt(item, model.orElse(null), handModel.orElse(null))));

}
