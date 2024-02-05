package tech.thatgravyboat.vanity.api.concept;

import tech.thatgravyboat.vanity.api.style.Style;
import tech.thatgravyboat.vanity.client.concept.ClientConceptArtManager;
import tech.thatgravyboat.vanity.common.handler.concept.ServerConceptArtManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public interface ConceptArtManager {

    /**
     * Retrieves a sided concept art manager for the specified side.
     *
     * @param client Whether the client sided concept art manager should be returned
     * @return The sided concept art manager
     */
    static ConceptArtManager get(boolean client) {
        return client ? ConceptArtManager.client() : ConceptArtManager.server();
    }

    static ConceptArtManager client() {
        return ClientConceptArtManager.INSTANCE;
    }

    static ConceptArtManager server() {
        return ServerConceptArtManager.INSTANCE;
    }

    /**
     * Retrieves a concept art by the specified id.
     *
     * @param location The id of the concept art to retrieve
     * @return An optional of the concept art
     */
    Optional<ConceptArt> getConceptArt(ResourceLocation location);

    /**
     * Retrieves the id of the concept art.
     *
     * @param art The concept art to get the id for
     * @return An optional of the concept art id
     */
    Optional<ResourceLocation> getConceptArtId(ConceptArt art);

    /**
     * @return A map of all concept art by id
     */
    Map<ResourceLocation, ConceptArt> getAllConceptArt();

    /**
     * @return A map of all default concept art by id
     */
    Map<ResourceLocation, ConceptArt> getDefaultConceptArt();

    /**
     * Retrieves the concept art applied to an item.
     *
     * @param stack The item stack to get the concept art from
     * @return The applied concept art, null if there is none
     */
    @Nullable
    ConceptArt getItemConceptArt(ItemStack stack);

    /**
     * Retrieves the concept art variant applied to an item.
     *
     * @param stack The item stack to get the concept art from
     * @return The concept art variant, null if there is none
     */
    @Nullable
    Style getItemConceptArtVariant(ItemStack stack);
}
