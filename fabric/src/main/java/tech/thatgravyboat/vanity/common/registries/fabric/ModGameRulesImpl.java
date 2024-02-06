package tech.thatgravyboat.vanity.common.registries.fabric;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

import java.util.function.BiConsumer;

public class ModGameRulesImpl {
    public static GameRules.Key<GameRules.BooleanValue> createBooleanGameRule(String name, GameRules.Category category, boolean value, BiConsumer<MinecraftServer, GameRules.BooleanValue> callback) {
        return GameRuleRegistry.register(name, category, GameRuleFactory.createBooleanRule(value, callback));
    }
}
