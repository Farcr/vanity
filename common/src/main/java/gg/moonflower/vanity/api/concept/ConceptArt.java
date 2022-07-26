package gg.moonflower.vanity.api.concept;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gg.moonflower.vanity.core.util.XorMapCodec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
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
        Entry.CODEC.listOf().fieldOf("entries").forGetter(ConceptArt::entries)
    ).apply(instance, (conceptArtModel, sold, entries) -> new ConceptArt(conceptArtModel.orElse(null), sold, entries)));

    private final Map<ResourceLocation, Entry> entryCache = new Object2ObjectArrayMap<>();

    @Nullable
    private final ResourceLocation conceptArtModel;
    private final boolean sold;
    private final List<Entry> entries;

    public ConceptArt(@Nullable ResourceLocation conceptArtModel, boolean sold, List<Entry> entries) {
        this.conceptArtModel = conceptArtModel;
        this.sold = sold;
        this.entries = entries;
    }

    @Nullable
    public Entry getEntryForItem(Item item) {
        return this.entryCache.computeIfAbsent(Registry.ITEM.getKey(item), key -> {
            for (Entry entry : this.entries) {
                if (entry.supportsItem(item))
                    return entry;
            }
            return null;
        });
    }

    @Nullable
    public ResourceLocation conceptArtModel() {
        return conceptArtModel;
    }

    public boolean sold() {
        return sold;
    }

    public List<Entry> entries() {
        return entries;
    }

    public record Entry(Either<TagKey<Item>, ResourceLocation> item, @Nullable ResourceLocation model,
                        @Nullable ResourceLocation handModel) {

        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            XorMapCodec.create(TagKey.codec(Registry.ITEM.key()).fieldOf("tag"), ResourceLocation.CODEC.fieldOf("item")).forGetter(Entry::item),
            ResourceLocation.CODEC.optionalFieldOf("model").forGetter(entry -> Optional.ofNullable(entry.model)),
            ResourceLocation.CODEC.optionalFieldOf("hand_model").forGetter(entry -> Optional.ofNullable(entry.handModel))
        ).apply(instance, (item, model, handModel) -> new Entry(item, model.orElse(null), handModel.orElse(null))));

        public boolean supportsItem(Item item) {
            return this.item.right().isPresent() ? Registry.ITEM.get(this.item.right().get()).equals(item) : this.item.left().isPresent() && item.builtInRegistryHolder().is(this.item.left().get());
        }
    }
}
