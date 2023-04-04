package gg.moonflower.vanity.api.concept.fabric;

import gg.moonflower.vanity.api.concept.ConceptArtManager;
import gg.moonflower.vanity.client.concept.ClientConceptArtManager;

public class ConceptArtManagerImpl {
    public static ConceptArtManager client() {
        return ClientConceptArtManager.INSTANCE;
    }
}
