package tech.thatgravyboat.vanity.api.design;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.codecs.EnumCodec;

public enum DesignType {
    ITEM,
    SELLABLE,
    DEFAULT,
    HIDDEN,
    ;

    public static final Codec<DesignType> CODEC = EnumCodec.of(DesignType.class);

    public boolean hasItem() {
        return this == ITEM || this == SELLABLE;
    }

    public boolean isDefault() {
        return this == DEFAULT;
    }
}
