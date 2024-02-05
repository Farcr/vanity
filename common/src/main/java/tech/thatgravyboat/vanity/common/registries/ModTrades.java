package tech.thatgravyboat.vanity.common.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import tech.thatgravyboat.vanity.common.handler.concept.ServerConceptArtManager;
import tech.thatgravyboat.vanity.common.handler.trades.VillagerTrade;
import tech.thatgravyboat.vanity.common.handler.trades.VillagerTradeManager;
import tech.thatgravyboat.vanity.common.item.ConceptArtHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ModTrades {

    public static void registerTrades(VillagerProfession profession, int maxTier, int minTier, BiConsumer<Integer, VillagerTrades.ItemListing> adder) {
        if (profession != VanityProfessions.STYLIST.get()) return;

        List<ResourceLocation> art = new ArrayList<>();
        for (var entry : ServerConceptArtManager.INSTANCE.getAllConceptArt().entrySet()) {
            if (!entry.getValue().canBeSold()) continue;
            art.add(entry.getKey());
        }

        int basicTrades = Math.min(maxTier, art.size() + 1);
        for (int i = 1; i < basicTrades && !art.isEmpty(); i++) {
            adder.accept(minTier + i, new ConceptArtListing(art));
        }

        for (int i = minTier; i <= maxTier; i++) {
            for (VillagerTrade trade : VillagerTradeManager.getTrades(i)) {
                adder.accept(i, trade);
            }
        }
    }

    private record ConceptArtListing(List<ResourceLocation> art) implements VillagerTrades.ItemListing {

        private static final int USES = 4;
        private static final int EMERALD_COST = 15;
        private static final int XP_GAIN = 24;
        private static final float PRICE_MULTIPLIER = 0.05F;

        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            ItemStack emeralds = new ItemStack(Items.EMERALD, EMERALD_COST + (random.nextInt(16)));

            ResourceLocation art = this.art.get(random.nextInt(this.art.size()));

            ItemStack item = new ItemStack(VanityItems.CONCEPT_ART.get());
            ConceptArtHelper.setConceptArt(item, art);

            return new MerchantOffer(emeralds, item, USES, XP_GAIN, PRICE_MULTIPLIER);
        }
    }
}
