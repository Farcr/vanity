package tech.thatgravyboat.vanity.core.forge;

import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.client.VanityClient;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Vanity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class VanityForgeClient {

    @SubscribeEvent
    public static void onEvent(ModelEvent.RegisterAdditional event) {
        VanityClient.registerModels(Minecraft.getInstance().getResourceManager(), event::register);
    }
}
