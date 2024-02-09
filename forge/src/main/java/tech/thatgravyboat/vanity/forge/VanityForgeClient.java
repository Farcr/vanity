package tech.thatgravyboat.vanity.forge;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tech.thatgravyboat.vanity.client.VanityClient;
import tech.thatgravyboat.vanity.common.Vanity;

@Mod.EventBusSubscriber(modid = Vanity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class VanityForgeClient {

    @SubscribeEvent
    public static void onEvent(ModelEvent.RegisterAdditional event) {
        VanityClient.registerModels(Minecraft.getInstance().getResourceManager(), event::register);
    }
}
