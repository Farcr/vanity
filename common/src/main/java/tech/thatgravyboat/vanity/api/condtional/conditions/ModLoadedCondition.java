package tech.thatgravyboat.vanity.api.condtional.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfoUtils;

public record ModLoadedCondition(String modid) implements Condition {

    public static final Codec<ModLoadedCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("modid").forGetter(ModLoadedCondition::modid)
    ).apply(instance, ModLoadedCondition::new));

    public static final String ID = "mod_loaded";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public boolean test() {
        return ModInfoUtils.isModLoaded(this.modid());
    }
}
