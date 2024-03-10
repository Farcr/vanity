package tech.thatgravyboat.vanity.api.style;

import java.util.HashMap;
import java.util.Map;

public class AssetTypes {

    private static final Map<String, AssetType> TYPES = new HashMap<>();

    public static final AssetType DEFAULT = register("default");
    public static final AssetType HAND = register("hand");
    public static final AssetType PROJECTILE = register("projectile");
    public static final AssetType ARMOR = register("armor");
    public static final AssetType GECKOLIB_ARMOR = register("geckolib_armor");

    public static AssetType register(String id) {
        return TYPES.computeIfAbsent(id, InternalType::new);
    }

    record InternalType(String id) implements AssetType {
    }
}
