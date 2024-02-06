package tech.thatgravyboat.vanity.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import tech.thatgravyboat.vanity.common.handler.design.ServerDesignManager;
import tech.thatgravyboat.vanity.common.handler.trades.VillagerTradeManager;
import tech.thatgravyboat.vanity.common.network.NetworkHandler;
import tech.thatgravyboat.vanity.common.registries.*;

import java.util.function.BiConsumer;

public class Vanity {
    public static final String MOD_ID = "vanity";

    public static MinecraftServer server;

    public static void init() {
        VanityBlocks.BLOCKS.init();
        VanityBlocks.BLOCK_ENTITIES.init();

        VanityItems.ITEMS.init();
        VanityMenuTypes.MENUS.init();
        VanitySounds.SOUNDS.init();

        VanityProfessions.POIS.init();
        VanityProfessions.PROFESSIONS.init();

        ModCreativeModeTabs.init();

        NetworkHandler.init();
    }

    public static void onRegisterReloadListeners(BiConsumer<ResourceLocation, PreparableReloadListener> registry) {
        registry.accept(new ResourceLocation(Vanity.MOD_ID, "design_manager"), ServerDesignManager.INSTANCE);
        registry.accept(new ResourceLocation(Vanity.MOD_ID, "villager_trades"), VillagerTradeManager.INSTANCE);
    }
}
