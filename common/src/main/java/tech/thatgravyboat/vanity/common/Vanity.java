package tech.thatgravyboat.vanity.common;

import com.teamresourceful.resourcefullib.common.item.tabs.ResourcefulCreativeTab;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import tech.thatgravyboat.vanity.api.concept.ConceptArtManager;
import tech.thatgravyboat.vanity.common.handler.concept.ServerConceptArtManager;
import tech.thatgravyboat.vanity.common.item.ConceptArtHelper;
import tech.thatgravyboat.vanity.common.network.VanityMessages;
import tech.thatgravyboat.vanity.common.registries.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class Vanity {
    public static final String MOD_ID = "vanity";

    @SuppressWarnings("unused")
    public static final Supplier<CreativeModeTab> TAB = new ResourcefulCreativeTab(new ResourceLocation(Vanity.MOD_ID, "concept_art_tab"))
            .setItemIcon(VanityBlocks.STYLING_TABLE)
            .addContent(() -> {
                ConceptArtManager manager = ConceptArtManager.get(true);
                List<ResourceLocation> availableArt = new ArrayList<>();
                for (var entry : manager.getAllConceptArt().entrySet()) {
                    if (!entry.getValue().type().hasItem()) continue;
                    availableArt.add(entry.getKey());
                }

                return availableArt
                        .stream()
                        .map(location -> {
                            ItemStack stack = new ItemStack(VanityItems.CONCEPT_ART.get());
                            ConceptArtHelper.setConceptArt(stack, location);
                            return stack;
                        });
            }).build();

    public static MinecraftServer server;

    public static void init() {
        VanityBlocks.BLOCKS.init();
        VanityBlocks.BLOCK_ENTITIES.init();

        VanityItems.ITEMS.init();
        VanityMenuTypes.MENUS.init();
        VanitySounds.SOUNDS.init();

        VanityProfessions.POIS.init();
        VanityProfessions.PROFESSIONS.init();

        VanityMessages.init();
    }

    public static void onRegisterReloadListeners(BiConsumer<ResourceLocation, PreparableReloadListener> registry) {
        registry.accept(new ResourceLocation(Vanity.MOD_ID, "concept_art_manager"), ServerConceptArtManager.INSTANCE);
    }
}
