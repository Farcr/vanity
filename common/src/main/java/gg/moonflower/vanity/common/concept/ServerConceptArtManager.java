package gg.moonflower.vanity.common.concept;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import gg.moonflower.pollen.api.registry.resource.PollinatedPreparableReloadListener;
import gg.moonflower.pollen.api.registry.resource.ResourceRegistry;
import gg.moonflower.vanity.api.concept.ConceptArt;
import gg.moonflower.vanity.api.concept.ConceptArtManager;
import gg.moonflower.vanity.common.network.common.message.ClientboundConceptArtSyncPacket;
import gg.moonflower.vanity.core.Vanity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.IOException;
import java.util.Map;

public class ServerConceptArtManager extends ConceptArtManager {

    public static final ServerConceptArtManager INSTANCE = new ServerConceptArtManager();

    public static void init() {
        ResourceRegistry.registerReloadListener(PackType.SERVER_DATA, ServerConceptArtManager.INSTANCE.new ResourceLoader());
    }

    public ClientboundConceptArtSyncPacket createPacket() {
        return new ClientboundConceptArtSyncPacket(this);
    }

    private class ResourceLoader extends SimpleJsonResourceReloadListener implements PollinatedPreparableReloadListener {

        private ResourceLoader() {
            super(new Gson(), "concept_art");
        }

        @Override
        public ResourceLocation getPollenId() {
            return new ResourceLocation(Vanity.MOD_ID, "concept_art");
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager resourceManager, ProfilerFiller profiler) {
            ServerConceptArtManager.this.conceptArt.clear();
            for (Map.Entry<ResourceLocation, JsonElement> entry : elements.entrySet()) {
                try {
                    if (ServerConceptArtManager.this.conceptArt.containsKey(entry.getKey()))
                        throw new IllegalStateException("Duplicate concept art: " + entry.getKey());

                    DataResult<ConceptArt> result = ConceptArt.CODEC.parse(JsonOps.INSTANCE, entry.getValue());
                    if (result.error().isPresent() || result.result().isEmpty())
                        throw new IOException(result.error().get().message() + " " + entry.getValue());

                    ServerConceptArtManager.this.conceptArt.put(entry.getKey(), result.result().get());
                } catch (IOException e) {
                    LOGGER.error("Failed to load concept art: " + entry.getKey(), e);
                }
            }
        }
    }
}
