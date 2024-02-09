package tech.thatgravyboat.vanity.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import tech.thatgravyboat.vanity.client.VanityClient;
import tech.thatgravyboat.vanity.common.registries.ModBlocks;

public class VanityFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        VanityClient.setup();

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.STYLING_TABLE.get(), RenderType.cutout());
    }
}
