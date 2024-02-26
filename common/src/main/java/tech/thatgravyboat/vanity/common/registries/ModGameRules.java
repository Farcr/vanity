package tech.thatgravyboat.vanity.common.registries;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import tech.thatgravyboat.vanity.common.network.NetworkHandler;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundSyncConfigPacket;
import tech.thatgravyboat.vanity.common.util.CachedGameRule;

import java.util.function.BiConsumer;

public class ModGameRules {

    public static final CachedGameRule<GameRules.BooleanValue, Boolean> UNLOCKABLE_DESIGNS = createCachedBooleanRule(
            "vanityUnlockableDesigns", GameRules.Category.PLAYER, false,
            (server, rule) -> NetworkHandler.CHANNEL.sendToAllPlayers(new ClientboundSyncConfigPacket(server), server)
    );

    public static final CachedGameRule<GameRules.BooleanValue, Boolean> LOCK_DESIGN_STORAGE = createCachedBooleanRule(
            "vanityLockDesignStorage", GameRules.Category.PLAYER, false,
            (server, rule) -> NetworkHandler.CHANNEL.sendToAllPlayers(new ClientboundSyncConfigPacket(server), server)
    );

    public static void init() {
        // NO-OP
    }

    private static CachedGameRule<GameRules.BooleanValue, Boolean> createCachedBooleanRule(String name, GameRules.Category category, boolean value, BiConsumer<MinecraftServer, GameRules.BooleanValue> callback) {
        return new CachedGameRule<>(createBooleanGameRule(name, category, value, callback), GameRules.BooleanValue::get, value);
    }

    @ExpectPlatform
    private static GameRules.Key<GameRules.BooleanValue> createBooleanGameRule(String name, GameRules.Category category, boolean value, BiConsumer<MinecraftServer, GameRules.BooleanValue> callback) {
        throw new NotImplementedException();
    }
}
