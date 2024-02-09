package tech.thatgravyboat.vanity.api.condtional.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import tech.thatgravyboat.vanity.api.condtional.Conditions;

import java.util.List;

public record AndCondition(List<Condition> conditions) implements Condition {

    public static final Codec<AndCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Conditions.CODEC.listOf().fieldOf("conditions").forGetter(AndCondition::conditions)
    ).apply(instance, AndCondition::new));
    public static final String ID = "and";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean test() {
        for (Condition condition : this.conditions) {
            if (!condition.test()) {
                return false;
            }
        }
        return true;
    }
}
