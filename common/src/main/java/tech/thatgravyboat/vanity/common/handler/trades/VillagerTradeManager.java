package tech.thatgravyboat.vanity.common.handler.trades;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VillagerTradeManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new Gson();
    private static final String FOLDER = "vanity/trades";

    public static final VillagerTradeManager INSTANCE = new VillagerTradeManager();
    private static final Int2ObjectMap<List<VillagerTrade>> TRADES = new Int2ObjectOpenHashMap<>();
    private static final Logger LOGGER = LogUtils.getLogger();

    public VillagerTradeManager() {
        super(GSON, FOLDER);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objects, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        TRADES.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : objects.entrySet()) {
            try {
                VillagerTrade trade = VillagerTrade.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow(false, LOGGER::error);
                TRADES.computeIfAbsent(trade.tier(), integer -> new ArrayList<>()).add(trade);
            } catch (Exception e) {
                LOGGER.error("Failed to load trade: " + entry.getKey(), e);
            }
        }
    }

    public static List<VillagerTrade> getTrades(int tier) {
        return TRADES.getOrDefault(tier, List.of());
    }
}
