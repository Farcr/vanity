package tech.thatgravyboat.vanity.common.handler.trades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

public record VillagerTrade(
        int tier,
        TradeStack first,
        TradeStack second,
        TradeStack result,
        IntProvider maxUses,
        IntProvider xp,
        FloatProvider priceMultiplier,
        float chance
) implements VillagerTrades.ItemListing {

    public static final Codec<VillagerTrade> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("tier", 1).forGetter(VillagerTrade::tier),
            TradeStack.CODEC.fieldOf("first").forGetter(VillagerTrade::first),
            TradeStack.CODEC.optionalFieldOf("second", TradeStack.EMPTY).forGetter(VillagerTrade::second),
            TradeStack.CODEC.fieldOf("result").forGetter(VillagerTrade::result),
            IntProvider.POSITIVE_CODEC.fieldOf("maxUses").forGetter(VillagerTrade::maxUses),
            IntProvider.POSITIVE_CODEC.optionalFieldOf("xp", ConstantInt.of(1)).forGetter(VillagerTrade::xp),
            FloatProvider.codec(0.1f, 5f).optionalFieldOf("priceMultiplier", ConstantFloat.ZERO).forGetter(VillagerTrade::priceMultiplier),
            Codec.floatRange(0f, 1f).optionalFieldOf("chance", 0.25f).forGetter(VillagerTrade::chance)
    ).apply(instance, VillagerTrade::new));

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity entity, RandomSource random) {
        boolean flag = this.chance > 0.0F && random.nextFloat() < this.chance;
        if (flag) {
            int maxUses = this.maxUses.sample(random);
            int xp = this.xp.sample(random);
            float priceMultiplier = this.priceMultiplier.sample(random);
            return new MerchantOffer(
                    this.first.create(random),
                    this.second.create(random),
                    this.result.create(random),
                    maxUses,
                    xp,
                    priceMultiplier
            );
        }
        return null;
    }
}
