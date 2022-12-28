package gg.moonflower.vanity.core.registry;

import gg.moonflower.pollen.api.registry.PollinatedBlockRegistry;
import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import gg.moonflower.vanity.common.block.StylingTableBlock;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class VanityBlocks {

    public static final PollinatedBlockRegistry REGISTRY = PollinatedRegistry.createBlock(VanityItems.REGISTRY);

    public static final Supplier<Block> STYLING_TABLE = REGISTRY.registerWithItem("styling_table", () -> new StylingTableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD).noOcclusion()), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
}
