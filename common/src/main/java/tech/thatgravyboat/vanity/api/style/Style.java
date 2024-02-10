package tech.thatgravyboat.vanity.api.style;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import tech.thatgravyboat.vanity.common.util.XorMapCodec;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public record Style(
        Either<TagKey<Item>, Set<Item>> item,
        Map<String, ResourceLocation> assets
) {

    private static final MapCodec<Either<TagKey<Item>, Set<Item>>> ITEM_CODEC = XorMapCodec.create(
            TagKey.codec(Registries.ITEM).fieldOf("tag"),
            Codec.either(
                    CodecExtras.set(BuiltInRegistries.ITEM.byNameCodec()),
                    BuiltInRegistries.ITEM.byNameCodec()
            ).xmap(
                    either -> either.map(Function.identity(), Set::of),
                    set -> set.size() == 1 ? Either.right(set.iterator().next()) : Either.left(set)
            ).fieldOf("item")
    );

    public static final Codec<Style> CODEC = ExtraCodecs.validate(RecordCodecBuilder.create(instance -> instance.group(
            ITEM_CODEC.forGetter(Style::item),
            Codec.unboundedMap(Codec.STRING, ResourceLocation.CODEC).optionalFieldOf("assets", new HashMap<>()).forGetter(Style::assets)
    ).apply(instance, Style::of)), style -> {
        if (!style.assets.containsKey("default")) {
            return DataResult.error(() -> "Styles must contain a 'default' asset");
        }
        return DataResult.success(style);
    });

    public static Style of(Either<TagKey<Item>, Set<Item>> item, Map<String, ResourceLocation> tempModels) {
        return new Style(item, new HashMap<>(tempModels));
    }

    public boolean hasAsset(AssetType type) {
        return this.assets.containsKey(type.id());
    }

    public ResourceLocation asset(AssetType type) {
        if (type == null) return null;
        return this.assets.get(type.id());
    }

    public boolean supportsItem(ItemStack stack) {
        return this.item.map(stack::is, set -> set.contains(stack.getItem()));
    }
}
