package tech.thatgravyboat.vanity.common.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public class CachedGameRule<T extends GameRules.Value<T>, V> {

    private final GameRules.Key<T> key;
    private final Function<T, V> mapper;
    private V value;

    public CachedGameRule(GameRules.Key<T> key, Function<T, V> mapper, V value) {
        this.key = key;
        this.mapper = mapper;
        this.value = value;
    }

    public V getValue(MinecraftServer server, boolean useCache) {
        return getValue(server.getGameRules(), useCache);
    }

    public V getValue(Level level, boolean useCache) {
        return getValue(level.getGameRules(), useCache);
    }

    @SuppressWarnings("ConstantValue")
    public V getValue(GameRules gameRules, boolean useCache) {
        if (useCache) {
            return value;
        }
        T gameRule = gameRules.getRule(key);
        if (gameRule == null) return value;
        return mapper.apply(gameRule);
    }

    public void setValue(V value) {
        this.value = value;
    }

    public GameRules.Key<T> key() {
        return key;
    }
}
