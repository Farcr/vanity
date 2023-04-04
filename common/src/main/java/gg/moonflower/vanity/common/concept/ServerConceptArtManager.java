package gg.moonflower.vanity.common.concept;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.architectury.registry.ReloadListenerRegistry;
import gg.moonflower.pollen.api.platform.v1.Platform;
import gg.moonflower.vanity.api.concept.ConceptArt;
import gg.moonflower.vanity.common.network.VanityMessages;
import gg.moonflower.vanity.common.network.common.message.ClientboundConceptArtSyncPacket;
import gg.moonflower.vanity.core.Vanity;
import gg.moonflower.vanity.impl.concept.ConceptArtManagerImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ServerConceptArtManager extends ConceptArtManagerImpl implements PreparableReloadListener {

    public static final ServerConceptArtManager INSTANCE = new ServerConceptArtManager();
    private static final Logger LOGGER = LogUtils.getLogger();
    private final ResourceLoader resourceLoader = new ResourceLoader();

    public static void init() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, ServerConceptArtManager.INSTANCE, new ResourceLocation(Vanity.MOD_ID, "concept_art_manager"));
    }

    public ClientboundConceptArtSyncPacket createPacket() {
        return new ClientboundConceptArtSyncPacket(this);
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        return this.resourceLoader.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor).thenRun(() -> {
            LOGGER.info("Loaded " + this.conceptArt.size() + " concept art");
            Platform.getRunningServer().ifPresent(server -> VanityMessages.PLAY.sendToAll(server, this.createPacket()));
        });
    }

    private class ResourceLoader extends SimpleJsonResourceReloadListener {

        private ResourceLoader() {
            super(new Gson(), "concept_art");
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
