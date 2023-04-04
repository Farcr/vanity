package gg.moonflower.vanity.api.concept;

import dev.architectury.injectables.annotations.ExpectPlatform;
import gg.moonflower.pollen.core.Pollen;
import gg.moonflower.vanity.common.concept.ServerConceptArtManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

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

    @ExpectPlatform
    static ConceptArtManager client() {
        return Pollen.expect();
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
     * @return All ids of concept art that can be created
     */
    Stream<ResourceLocation> getAllConceptArtIds();

    /**
     * @return All concept art that can be created
     */
    Stream<ConceptArt> getAllConceptArt();

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
    ConceptArt.Variant getItemConceptArtVariant(ItemStack stack);
}
