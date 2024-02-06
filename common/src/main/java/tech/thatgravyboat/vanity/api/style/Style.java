package tech.thatgravyboat.vanity.api.style;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import java.util.function.Function;

public record Style(
        Either<TagKey<Item>, Item> item,
        Map<String, ResourceLocation> assets
) {

    private static final Codec<Style> UNVALIDATED_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            XorMapCodec.create(TagKey.codec(Registries.ITEM).fieldOf("tag"), BuiltInRegistries.ITEM.byNameCodec().fieldOf("item")).forGetter(Style::item),
            ResourceLocation.CODEC.optionalFieldOf("model").forGetter(ignored -> Optional.empty()),
            ResourceLocation.CODEC.optionalFieldOf("hand_model").forGetter(ignored -> Optional.empty()),
            Codec.unboundedMap(Codec.STRING, ResourceLocation.CODEC).optionalFieldOf("assets", new HashMap<>()).forGetter(Style::assets)
    ).apply(instance, Style::of));

    public static final Codec<Style> CODEC = UNVALIDATED_CODEC.comapFlatMap(style -> {
        if (!style.assets.containsKey("default")) {
            return DataResult.error(() -> "Styles must contain a 'default' asset");
        }
        return DataResult.success(style);
    }, Function.identity());

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static Style of(Either<TagKey<Item>, Item> item, Optional<ResourceLocation> model, Optional<ResourceLocation> handModel, Map<String, ResourceLocation> tempModels) {
        Map<String, ResourceLocation> models = new HashMap<>(tempModels);
        model.ifPresent(id -> models.put("default", id));
        handModel.ifPresent(id -> models.put("hand", id));

        return new Style(item, models);
    }

    public ResourceLocation model() {
        return Objects.requireNonNull(this.assets.get("default"));
    }

    public boolean hasAsset(AssetType type) {
        return this.assets.containsKey(type.id());
    }

    public ResourceLocation asset(AssetType type) {
        if (type == null) return null;
        return this.assets.get(type.id());
    }

    public boolean supportsItem(ItemStack stack) {
        return this.item.map(stack::is, stack::is);
    }
}
