package tech.thatgravyboat.vanity.neoforge;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import org.apache.commons.lang3.Validate;
import tech.thatgravyboat.vanity.client.VanityClient;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.registries.ModCreativeModeTabs;
import tech.thatgravyboat.vanity.common.registries.ModTrades;

import java.util.stream.Collectors;

@Mod(Vanity.MOD_ID)
public class VanityForge {
    public VanityForge(IEventBus bus) {
        bus.addListener(this::onClientInit);
        bus.addListener(this::onBuildCreativeModeTabs);

        Vanity.init();

        NeoForge.EVENT_BUS.addListener(this::onAddVillagerTrades);
        NeoForge.EVENT_BUS.addListener(this::onAddReloadListener);
        NeoForge.EVENT_BUS.addListener(this::onServerAboutToStart);
    }

    private void onClientInit(FMLClientSetupEvent event) {
        VanityClient.setup();
    }

    private void onAddVillagerTrades(VillagerTradesEvent event) {
        int minTier = event.getTrades().keySet().intStream().min().orElse(1);
        int maxTier = event.getTrades().keySet().intStream().max().orElse(5);
        ModTrades.registerTrades(event.getType(), maxTier, minTier, (tier, listing) -> {
            Validate.inclusiveBetween(minTier, maxTier, tier, "Tier must be between " + minTier + " and " + maxTier);
            int intTIer = tier;
            var registry = event.getTrades().get(intTIer);
            ResourceLocation id = BuiltInRegistries.VILLAGER_PROFESSION.getKey(event.getType());
            if (registry == null) {
                throw new IllegalStateException("No registered " + id + " Villager Trades for tier: " + tier + ". Valid tiers: " + event.getTrades().keySet().intStream().sorted().mapToObj(Integer::toString).collect(Collectors.joining(", ")));
            }
            registry.add(listing);
        });
    }

    private void onServerAboutToStart(ServerAboutToStartEvent event) {
        Vanity.server = event.getServer();
    }

    private void onAddReloadListener(AddReloadListenerEvent event) {
        Vanity.onRegisterReloadListeners((id, listener) -> event.addListener(listener));
    }

    private void onBuildCreativeModeTabs(BuildCreativeModeTabContentsEvent event) {
        ModCreativeModeTabs.setupCreativeTab(event.getTabKey(), (after, item) -> {
            ItemStack afterStack = new ItemStack(after);
            ItemStack itemStack = new ItemStack(item);
            event.getEntries().putAfter(afterStack, itemStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        });
    }
}
