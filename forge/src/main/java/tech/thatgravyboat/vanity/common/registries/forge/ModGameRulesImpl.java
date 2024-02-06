package tech.thatgravyboat.vanity.common.registries.forge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

import java.util.function.BiConsumer;

public class ModGameRulesImpl {
    public static GameRules.Key<GameRules.BooleanValue> createBooleanGameRule(String name, GameRules.Category category, boolean value, BiConsumer<MinecraftServer, GameRules.BooleanValue> callback) {
        return GameRules.register(name, category, GameRules.BooleanValue.create(value, callback));
    }
}
