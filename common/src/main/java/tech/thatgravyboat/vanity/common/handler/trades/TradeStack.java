package tech.thatgravyboat.vanity.common.handler.trades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record TradeStack(Item item, IntProvider count) {

    private static final Codec<TradeStack> STRING_EITHER = BuiltInRegistries.ITEM.byNameCodec().xmap(TradeStack::new, TradeStack::item);

    private static final Codec<TradeStack> STACK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(TradeStack::item),
            IntProvider.POSITIVE_CODEC.optionalFieldOf("count", ConstantInt.of(1)).forGetter(TradeStack::count)
    ).apply(instance, TradeStack::new));

    public static final Codec<TradeStack> CODEC = CodecExtras.eitherRight(Codec.either(STRING_EITHER, STACK_CODEC));
    public static final TradeStack EMPTY = new TradeStack(Items.AIR, ConstantInt.of(0));

    public TradeStack(Item item) {
        this(item, ConstantInt.of(1));
    }

    public ItemStack create(RandomSource random) {
        if (this.item == Items.AIR) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(this.item, this.count.sample(random));
    }
}
