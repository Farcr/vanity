package tech.thatgravyboat.vanity.api.style;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import tech.thatgravyboat.vanity.common.util.XorMapCodec;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public record Style(
        Either<TagKey<Item>, Item> item,
        Map<String, ResourceLocation> models
) {

    public static final Codec<Style> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            XorMapCodec.create(TagKey.codec(Registries.ITEM).fieldOf("tag"), BuiltInRegistries.ITEM.byNameCodec().fieldOf("item")).forGetter(Style::item),
            ResourceLocation.CODEC.optionalFieldOf("model").forGetter(CodecExtras.optionalFor(Style::model)),
            ResourceLocation.CODEC.optionalFieldOf("hand_model").forGetter(CodecExtras.optionalFor(Style::handModel)),
            Codec.unboundedMap(Codec.STRING, ResourceLocation.CODEC).optionalFieldOf("models", new HashMap<>()).forGetter(Style::models)
    ).apply(instance, Style::of));

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static Style of(Either<TagKey<Item>, Item> item, Optional<ResourceLocation> model, Optional<ResourceLocation> handModel, Map<String, ResourceLocation> tempModels) {
        Map<String, ResourceLocation> models = new HashMap<>(tempModels);
        model.ifPresent(id -> models.put("default", id));
        handModel.ifPresent(id -> models.put("hand", id));

        if (!models.containsKey("default")) {
            throw new IllegalArgumentException("Variant models must contain a default model");
        }
        return new Style(item, models);
    }

    public ResourceLocation model() {
        return Objects.requireNonNull(this.models.get("default"));
    }

    public ResourceLocation handModel() {
        return this.models.get("hand");
    }

    public ResourceLocation model(ModelType type) {
        if (type == null) return null;
        return this.models.get(type.id());
    }

    public boolean supportsItem(ItemStack stack) {
        return this.item.map(stack::is, stack::is);
    }
}
