package tech.thatgravyboat.vanity.fabric;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.registries.ModCreativeModeTabs;
import tech.thatgravyboat.vanity.fabric.mixin.PoiTypesAccessor;
import tech.thatgravyboat.vanity.common.registries.ModProfessions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;

public class VanityFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Vanity.init();
        ResourceManagerHelper serverResourceHelper = ResourceManagerHelper.get(PackType.SERVER_DATA);
        Vanity.onRegisterReloadListeners((id, listener) ->
            serverResourceHelper.registerReloadListener(new FabricReloadListener(id, listener))
        );
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            Vanity.server = server;
            FabricVillagerTrades.init();
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> Vanity.server = null);

        for (RegistryEntry<PoiType> entry : ModProfessions.POIS.getEntries()) {
            Holder<PoiType> holder = BuiltInRegistries.POINT_OF_INTEREST_TYPE
                    .getHolderOrThrow(ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, entry.getId()));
            for (BlockState matchingState : entry.get().matchingStates()) {
                PoiTypesAccessor.getByType().put(matchingState, holder);
            }
        }

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((tab, stacks) -> {
            ResourceLocation id = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
            if (id == null) return;
            ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
            ModCreativeModeTabs.setupCreativeTab(key, stacks::addAfter);
        });
    }
}
