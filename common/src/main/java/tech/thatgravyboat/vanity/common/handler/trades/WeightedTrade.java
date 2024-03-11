package tech.thatgravyboat.vanity.common.handler.trades;

import com.teamresourceful.resourcefullib.common.collections.WeightedCollection;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record WeightedTrade(WeightedCollection<VillagerTrade> trades) implements VillagerTrades.ItemListing {

    @Nullable
    @Override
    public MerchantOffer getOffer(@NotNull Entity entity, @NotNull RandomSource random) {
        this.trades.setRandom(random);
        return this.trades.next().getOffer(entity, random);
    }
}
