package tech.thatgravyboat.vanity.api.condtional.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import tech.thatgravyboat.vanity.api.condtional.Conditions;

public record NotCondition(Condition condition) implements Condition {

    public static final Codec<NotCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Conditions.CODEC.fieldOf("condition").forGetter(NotCondition::condition)
    ).apply(instance, NotCondition::new));
    public static final String ID = "not";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean test() {
        return !condition.test();
    }
}
