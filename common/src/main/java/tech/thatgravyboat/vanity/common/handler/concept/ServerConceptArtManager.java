package tech.thatgravyboat.vanity.common.handler.concept;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import tech.thatgravyboat.vanity.api.concept.ConceptArt;
import tech.thatgravyboat.vanity.common.network.NetworkHandler;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundConceptArtSyncPacket;
import tech.thatgravyboat.vanity.common.Vanity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ServerConceptArtManager extends ConceptArtManagerImpl implements PreparableReloadListener {

    public static final ServerConceptArtManager INSTANCE = new ServerConceptArtManager();
    private static final Logger LOGGER = LogUtils.getLogger();
    private final ResourceLoader resourceLoader = new ResourceLoader();

    public ClientboundConceptArtSyncPacket createPacket() {
        return new ClientboundConceptArtSyncPacket(this);
    }

    @Override
    public @NotNull CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        return this.resourceLoader.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor).thenRun(() -> {
            if (Vanity.server != null) {
                NetworkHandler.CHANNEL.sendToAllPlayers(createPacket(), Vanity.server);
            }
        });
    }

    private class ResourceLoader extends SimpleJsonResourceReloadListener {

        private ResourceLoader() {
            super(new Gson(), "concept_art");
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager resourceManager, ProfilerFiller profiler) {
            ServerConceptArtManager.this.clear();
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
            ServerConceptArtManager.this.setupDefaults();
        }
    }
}
