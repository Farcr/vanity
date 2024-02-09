package tech.thatgravyboat.vanity.api.style;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.*;
import org.apache.commons.lang3.mutable.MutableObject;
import tech.thatgravyboat.vanity.api.condtional.Conditions;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

// Copy of ListCodec with condition support
public class StyleListCodec implements Codec<List<Style>> {

    public static final StyleListCodec INSTANCE = new StyleListCodec();
    private static final String KEY = "condition";

    @Override
    public <T> DataResult<T> encode(final List<Style> input, final DynamicOps<T> ops, final T prefix) {
        final ListBuilder<T> builder = ops.listBuilder();

        for (final Style a : input) {
            builder.add(Style.CODEC.encodeStart(ops, a));
        }

        return builder.build(prefix);
    }

    @Override
    public <T> DataResult<Pair<List<Style>, T>> decode(final DynamicOps<T> ops, final T input) {
        return ops.getList(input).setLifecycle(Lifecycle.stable()).flatMap(stream -> {
            final ImmutableList.Builder<Style> read = ImmutableList.builder();
            final Stream.Builder<T> failed = Stream.builder();
            final MutableObject<DataResult<Unit>> result = new MutableObject<>(DataResult.success(Unit.INSTANCE, Lifecycle.stable()));

            stream.accept(t -> {
                if (skip(ops, t)) return;
                final DataResult<Pair<Style, T>> element = Style.CODEC.decode(ops, t);
                element.error().ifPresent(e -> failed.add(t));
                result.setValue(result.getValue().apply2stable((r, v) -> {
                    read.add(v.getFirst());
                    return r;
                }, element));
            });

            final ImmutableList<Style> elements = read.build();
            final T errors = ops.createList(failed.build());

            final Pair<List<Style>, T> pair = Pair.of(elements, errors);

            return result.getValue().map(unit -> pair).setPartial(pair);
        });
    }

    private <T> boolean skip(final DynamicOps<T> ops, final T input) {
        final Optional<T> key = ops.get(input, KEY).result();
        return key.filter(t -> !Conditions.CODEC.parse(ops, t)
                .getOrThrow(false, System.err::println)
                .test()).isPresent();
    }
}
