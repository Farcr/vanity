package tech.thatgravyboat.vanity.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import tech.thatgravyboat.vanity.api.condtional.Conditions;
import tech.thatgravyboat.vanity.common.handler.design.ServerDesignManager;
import tech.thatgravyboat.vanity.common.handler.trades.VillagerTradeManager;
import tech.thatgravyboat.vanity.common.network.NetworkHandler;
import tech.thatgravyboat.vanity.common.registries.*;

import java.util.function.BiConsumer;

public class Vanity {
    public static final String MOD_ID = "vanity";

    public static MinecraftServer server;

    public static void init() {
        ModBlocks.BLOCKS.init();
        ModBlocks.BLOCK_ENTITIES.init();

        ModItems.ITEMS.init();
        ModMenuTypes.MENUS.init();
        ModSounds.SOUNDS.init();

        ModProfessions.POIS.init();
        ModProfessions.PROFESSIONS.init();
        ModCreativeModeTabs.TABS.init();

        NetworkHandler.init();
        ModGameRules.init();
        Conditions.init();
    }

    public static void onRegisterReloadListeners(BiConsumer<ResourceLocation, PreparableReloadListener> registry) {
        registry.accept(new ResourceLocation(Vanity.MOD_ID, "design_manager"), ServerDesignManager.INSTANCE);
        registry.accept(new ResourceLocation(Vanity.MOD_ID, "villager_trades"), VillagerTradeManager.INSTANCE);
    }
}
