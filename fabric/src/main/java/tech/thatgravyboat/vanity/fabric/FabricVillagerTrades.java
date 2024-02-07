package tech.thatgravyboat.vanity.fabric;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.vanity.common.registries.ModTrades;

import java.util.*;
import java.util.stream.Collectors;

public class FabricVillagerTrades {

    private static final Map<VillagerProfession, Int2ObjectMap<VillagerTrades.ItemListing[]>> VANILLA_TRADES = new HashMap<>();

    static {
        VillagerTrades.TRADES.forEach((key, value) -> {
            Int2ObjectMap<VillagerTrades.ItemListing[]> copy = new Int2ObjectOpenHashMap<>();
            value.int2ObjectEntrySet().forEach(ent -> copy.put(ent.getIntKey(), Arrays.copyOf(ent.getValue(), ent.getValue().length)));
            VANILLA_TRADES.put(key, copy);
        });
    }

    public static void init() {
        for (VillagerProfession prof : BuiltInRegistries.VILLAGER_PROFESSION) {
            Map<Integer, VillagerTrades.ItemListing[]> vanillaTrades = VANILLA_TRADES.get(prof);
            Map<Integer, List<VillagerTrades.ItemListing>> newTrades = new Int2ObjectOpenHashMap<>();

            if (vanillaTrades != null) {
                // Create trades for each
                for (Map.Entry<Integer, VillagerTrades.ItemListing[]> entry : vanillaTrades.entrySet()) {
                    newTrades.put(entry.getKey(), new ArrayList<>(List.of(entry.getValue())));
                }
            } else {
                // There are no default trades to fill
                vanillaTrades = Collections.emptyMap();
                for (int i = 1; i <= 5; i++) {
                    newTrades.put(i, new ArrayList<>());
                }
            }

            // Remove old trades
            newTrades.values().forEach(list -> list.removeIf(listing -> listing instanceof Wrapped));

            int minTier = vanillaTrades.keySet().stream().mapToInt(Integer::intValue).min().orElse(1);
            int maxTier = vanillaTrades.keySet().stream().mapToInt(Integer::intValue).max().orElse(5);

            // Sanity check to make sure all tiers actually exist
            for (int i = minTier; i <= maxTier; i++) {
                if (!newTrades.containsKey(i)) {
                    newTrades.put(i, new ArrayList<>());
                }
            }

            ModTrades.registerTrades(prof, maxTier, minTier, (tier, listing) -> {
                Validate.inclusiveBetween(minTier, maxTier, tier, "Tier must be between " + minTier + " and " + maxTier);
                var registry = newTrades.get(tier);
                if (registry == null)
                    throw new IllegalStateException("No registered " + BuiltInRegistries.VILLAGER_PROFESSION.getKey(prof) + " Villager Trades for tier: " + tier + ". Valid tiers: " + newTrades.keySet().stream().sorted().map(i -> Integer.toString(i)).collect(Collectors.joining(", ")));
                registry.add(new Wrapped(listing));
            });

            Int2ObjectMap<VillagerTrades.ItemListing[]> modifiedTrades = new Int2ObjectOpenHashMap<>();
            newTrades.forEach((key, value) -> modifiedTrades.put(key.intValue(), value.toArray(new VillagerTrades.ItemListing[0])));
            VillagerTrades.TRADES.put(prof, modifiedTrades);
        }
    }

    private record Wrapped(VillagerTrades.ItemListing listing) implements VillagerTrades.ItemListing {

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
            return listing.getOffer(entity, randomSource);
        }
    }
}
