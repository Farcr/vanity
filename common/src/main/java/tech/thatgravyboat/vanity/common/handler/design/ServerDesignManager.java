package tech.thatgravyboat.vanity.common.handler.design;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import tech.thatgravyboat.vanity.api.condtional.Conditions;
import tech.thatgravyboat.vanity.api.condtional.conditions.Condition;
import tech.thatgravyboat.vanity.api.design.Design;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.network.NetworkHandler;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundSyncDesignsPacket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ServerDesignManager extends DesignManagerImpl implements PreparableReloadListener {

    public static final ServerDesignManager INSTANCE = new ServerDesignManager();
    private static final Logger LOGGER = LogUtils.getLogger();
    private final ResourceLoader resourceLoader = new ResourceLoader();

    public ClientboundSyncDesignsPacket createPacket() {
        return new ClientboundSyncDesignsPacket(this);
    }

    @Override
    public @NotNull CompletableFuture<Void> reload(@NotNull PreparationBarrier barrier, @NotNull ResourceManager manager, @NotNull ProfilerFiller preparationsProfiler, @NotNull ProfilerFiller reloadProfiler, @NotNull Executor backgroundExecutor, @NotNull Executor gameExecutor) {
        return this.resourceLoader.reload(barrier, manager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor).thenRun(() -> {
            if (Vanity.server != null) {
                NetworkHandler.CHANNEL.sendToAllPlayers(createPacket(), Vanity.server);
            }
        });
    }

    private class ResourceLoader extends SimpleJsonResourceReloadListener {

        private ResourceLoader() {
            super(new Gson(), "vanity/designs");
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> elements, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
            ServerDesignManager.this.clear();
            for (Map.Entry<ResourceLocation, JsonElement> entry : elements.entrySet()) {
                try {
                    if (ServerDesignManager.this.designs.containsKey(entry.getKey())) {
                        throw new IllegalStateException("Duplicate design: " + entry.getKey());
                    }

                    boolean hasConditions = entry.getValue().getAsJsonObject().has("condition");
                    if (hasConditions) {
                        Condition condition = Conditions.CODEC.parse(
                            JsonOps.INSTANCE,
                            entry.getValue().getAsJsonObject().get("condition")
                        ).getOrThrow(false, LOGGER::error);

                        if (!condition.test()) {
                            continue;
                        }
                    }


                    DataResult<Design> result = Design.CODEC.parse(JsonOps.INSTANCE, entry.getValue());
                    if (result.error().isPresent() || result.result().isEmpty()) {
                        throw new IOException(result.error().get().message() + " " + entry.getValue());
                    }

                    ServerDesignManager.this.designs.put(entry.getKey(), result.result().get());
                } catch (IOException e) {
                    LOGGER.error("Failed to load design: " + entry.getKey(), e);
                }
            }
            ServerDesignManager.this.setupDefaults();
        }
    }
}
