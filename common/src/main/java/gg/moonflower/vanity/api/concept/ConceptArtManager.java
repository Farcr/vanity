package gg.moonflower.vanity.api.concept;

import com.mojang.logging.LogUtils;
import gg.moonflower.vanity.client.concept.ClientConceptArtManager;
import gg.moonflower.vanity.common.concept.ServerConceptArtManager;
import gg.moonflower.vanity.common.item.ConceptArtItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public abstract class ConceptArtManager {

    protected static final Logger LOGGER = LogUtils.getLogger();
    protected final Map<ResourceLocation, ConceptArt> conceptArt = new HashMap<>();

    /**
     * Retrieves a sided concept art manager for the specified side.
     *
     * @param client Whether the client sided concept art manager should be returned
     * @return The sided concept art manager
     */
    static ConceptArtManager get(boolean client) {
        return client ? ClientConceptArtManager.INSTANCE : ServerConceptArtManager.INSTANCE;
    }

    /**
     * Retrieves a concept art by the specified id.
     *
     * @param location The id of the concept art to retrieve
     * @return An optional of the concept art
     */
    public Optional<ConceptArt> getConceptArt(ResourceLocation location) {
        return Optional.ofNullable(this.conceptArt.get(location));
    }

    /**
     * Retrieves the id of the concept art.
     *
     * @param art The concept art to get the id for
     * @return An optional of the concept art id
     */
    public Optional<ResourceLocation> getConceptArtId(ConceptArt art) {
        return this.conceptArt.entrySet().stream().filter(entry -> entry.getValue().equals(art)).map(Map.Entry::getKey).findFirst();
    }

    /**
     * @return All ids of concept art that can be created
     */
    public Stream<ResourceLocation> getAllConceptArtIds() {
        return this.conceptArt.keySet().stream();
    }

    /**
     * @return All concept art that can be created
     */
    public Stream<ConceptArt> getAllConceptArt() {
        return this.conceptArt.values().stream();
    }

    /**
     * Retrieves the concept art applied to an item.
     *
     * @param stack The item stack to get the concept art from
     * @return The applied concept art, null if there is none
     */
    @Nullable
    public ConceptArt getAppliedConceptArt(ItemStack stack) {
        ResourceLocation location = ConceptArtItem.getAppliedConceptArt(stack);
        if (location == null)
            return null;

        return this.getConceptArt(location).orElse(null);
    }
}
