package tech.thatgravyboat.vanity.common.registries;

import com.google.common.collect.ImmutableSet;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.handler.concept.ServerConceptArtManager;
import tech.thatgravyboat.vanity.common.item.ConceptArtHelper;

import java.util.*;
import java.util.function.BiConsumer;

public class VanityProfessions {

    public static final ResourcefulRegistry<PoiType> POIS = ResourcefulRegistries.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, Vanity.MOD_ID);
    public static final ResourcefulRegistry<VillagerProfession> PROFESSIONS = ResourcefulRegistries.create(BuiltInRegistries.VILLAGER_PROFESSION, Vanity.MOD_ID);

    public static final RegistryEntry<PoiType> STYLIST_POI = POIS.register("stylist", () -> new PoiType(new HashSet<>(VanityBlocks.STYLING_TABLE.get().getStateDefinition().getPossibleStates()), 1, 1));
    public static final RegistryEntry<VillagerProfession> STYLIST = PROFESSIONS.register("stylist", () -> new VillagerProfession(Vanity.MOD_ID + ":stylist", poi -> poi.is(STYLIST_POI.getId()), poi -> poi.is(STYLIST_POI.getId()), ImmutableSet.of(), ImmutableSet.of(), VanitySounds.UI_STYLING_TABLE_TAKE_RESULT.get()));

    public static void registerTrades(VillagerProfession profession, int maxTier, int minTier, BiConsumer<Integer, VillagerTrades.ItemListing> adder) {
        if (profession != STYLIST.get()) return;

        List<ResourceLocation> availableArt = new ArrayList<>();
        for (var entry : ServerConceptArtManager.INSTANCE.getAllConceptArt().entrySet()) {
            if (!entry.getValue().canBeSold()) continue;
            availableArt.add(entry.getKey());
        }

        if (availableArt.isEmpty()) {
            return;
        }

        int basicTrades = Math.min(maxTier - minTier - 1, availableArt.size());
        for (int i = 0; i < basicTrades; i++) {
            adder.accept(minTier + i, new ConceptArtTrade(availableArt, i));
        }

        for (int i = 0; i < Math.min(2, (availableArt.size() - basicTrades) / 2); i++) {
            adder.accept(maxTier - 1 + i, new ConceptArtTrade(availableArt, basicTrades + i * 2));
            adder.accept(maxTier - 1 + i, new ConceptArtTrade(availableArt, basicTrades + i * 2 + 1));
        }
    }

    static class ConceptArtTrade implements VillagerTrades.ItemListing {

        private static final int USES = 4;
        private static final int EMERALD_COST = 15;
        private static final int XP_GAIN = 24;
        private static final float PRICE_MULTIPLIER = 0.05F;
        private static final Random RANDOM = new Random(42L);

        private final List<ResourceLocation> availableArt;
        private final int index;

        private ConceptArtTrade(List<ResourceLocation> availableArt, int index) {
            this.availableArt = availableArt;
            this.index = index;
        }

        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            ItemStack emeralds = new ItemStack(Items.EMERALD, EMERALD_COST + (random.nextInt(16)));

            RANDOM.setSeed(entity.getUUID().getMostSignificantBits());

            List<ResourceLocation> conceptArt = new ArrayList<>(this.availableArt);
            Collections.shuffle(conceptArt, RANDOM);

            ItemStack item = new ItemStack(VanityItems.CONCEPT_ART.get());
            ConceptArtHelper.setConceptArt(item, conceptArt.get(this.index));

            return new MerchantOffer(emeralds, item, USES, XP_GAIN, PRICE_MULTIPLIER);
        }
    }
}
