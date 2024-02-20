package tech.thatgravyboat.vanity.common.registries;

import com.teamresourceful.resourcefullib.common.collections.WeightedCollection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import tech.thatgravyboat.vanity.common.handler.design.ServerDesignManager;
import tech.thatgravyboat.vanity.common.handler.trades.VillagerTrade;
import tech.thatgravyboat.vanity.common.handler.trades.VillagerTradeManager;
import tech.thatgravyboat.vanity.common.handler.trades.WeightedTrade;
import tech.thatgravyboat.vanity.common.item.DesignHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModTrades {

    public static void registerTrades(VillagerProfession profession, int maxTier, int minTier, BiConsumer<Integer, VillagerTrades.ItemListing> adder) {
        if (profession != ModProfessions.STYLIST.get()) return;

        List<ResourceLocation> designs = new ArrayList<>();
        for (var entry : ServerDesignManager.INSTANCE.getAllDesigns().entrySet()) {
            if (!entry.getValue().canBeSold()) continue;
            designs.add(entry.getKey());
        }

        int basicTrades = Math.min(maxTier, designs.size() + 1);
        int index = 0;
        for (int i = minTier; i < basicTrades && !designs.isEmpty(); i++) {
            adder.accept(i, new DesignListing(designs, index++));
            if (i > 3) {
                adder.accept(i, new DesignListing(designs, index++));
            }
        }

        for (int i = minTier; i <= maxTier; i++) {
            Map<String, WeightedCollection<VillagerTrade>> weightedTrades = new HashMap<>();
            for (VillagerTrade trade : VillagerTradeManager.getTrades(i)) {
                if (trade.isDefaultGroup()) {
                    adder.accept(i, trade);
                } else {
                    weightedTrades.computeIfAbsent(trade.group(), s -> new WeightedCollection<>()).add(trade.chance(), trade);
                }
            }
            for (WeightedCollection<VillagerTrade> value : weightedTrades.values()) {
                adder.accept(i, new WeightedTrade(value));
            }
        }
    }

    private record DesignListing(List<ResourceLocation> designs, int offset) implements VillagerTrades.ItemListing {

        private static final int USES = 4;
        private static final int EMERALD_COST = 15;
        private static final int XP_GAIN = 24;
        private static final float PRICE_MULTIPLIER = 0.05F;

        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            ItemStack emeralds = new ItemStack(Items.EMERALD, EMERALD_COST + (random.nextInt(16)));
            int index = Mth.abs(entity.getUUID().hashCode() + this.offset) % this.designs.size();
            ResourceLocation design = this.designs.get(Mth.clamp(index, 0, this.designs.size() - 1));
            return new MerchantOffer(emeralds, DesignHelper.createDesignItem(design), USES, XP_GAIN, PRICE_MULTIPLIER);
        }
    }
}
