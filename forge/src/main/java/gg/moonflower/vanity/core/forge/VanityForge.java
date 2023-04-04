package gg.moonflower.vanity.core.forge;

import dev.architectury.platform.forge.EventBuses;
import gg.moonflower.vanity.core.Vanity;
import gg.moonflower.vanity.core.VanityClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Vanity.MOD_ID)
public class VanityForge {
    public VanityForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Vanity.MOD_ID, eventBus);
        eventBus.addListener(this::clientInit);

        Vanity.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> VanityClient::init);
    }

    private void clientInit(FMLClientSetupEvent event) {
        VanityClient.postInit();
    }
}
