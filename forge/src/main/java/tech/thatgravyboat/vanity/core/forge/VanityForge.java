package tech.thatgravyboat.vanity.core.forge;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.Validate;
import tech.thatgravyboat.vanity.client.VanityClient;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.registries.ModTrades;

import java.util.stream.Collectors;

@Mod(Vanity.MOD_ID)
public class VanityForge {
    public VanityForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::clientInit);

        Vanity.init();

        MinecraftForge.EVENT_BUS.addListener(this::villagerTrades);
        MinecraftForge.EVENT_BUS.addListener(this::onAddReloadListener);
    }

    private void clientInit(FMLClientSetupEvent event) {
        VanityClient.setup();
    }

    private void villagerTrades(VillagerTradesEvent event) {
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

    private void onAddReloadListener(AddReloadListenerEvent event) {
        Vanity.onRegisterReloadListeners((id, listener) -> event.addListener(listener));
    }
}
