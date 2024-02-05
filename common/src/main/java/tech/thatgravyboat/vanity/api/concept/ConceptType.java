package tech.thatgravyboat.vanity.api.concept;

import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.codecs.EnumCodec;

public enum ConceptType {
    ITEM,
    SELLABLE,
    DEFAULT,
    HIDDEN,
    ;

    public static final Codec<ConceptType> CODEC = EnumCodec.of(ConceptType.class);

    public static ConceptType fromBoolean(boolean sellable) {
        return sellable ? SELLABLE : ITEM;
    }

    public boolean hasItem() {
        return this == ITEM || this == SELLABLE;
    }

    public boolean isDefault() {
        return this == DEFAULT;
    }
}
