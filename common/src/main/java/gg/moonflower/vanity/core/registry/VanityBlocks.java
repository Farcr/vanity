package gg.moonflower.vanity.core.registry;

import gg.moonflower.pollen.api.registry.PollinatedBlockRegistry;
import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import gg.moonflower.vanity.common.block.StylingTableBlock;
import gg.moonflower.vanity.common.block.entity.StylingTableBlockEntity;
import gg.moonflower.vanity.core.Vanity;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class VanityBlocks {

    public static final PollinatedBlockRegistry BLOCKS = PollinatedRegistry.createBlock(VanityItems.REGISTRY);
    public static final PollinatedRegistry<BlockEntityType<?>> BLOCK_ENTITIES = PollinatedRegistry.create(Registry.BLOCK_ENTITY_TYPE, Vanity.MOD_ID);

    public static final Supplier<Block> STYLING_TABLE = BLOCKS.registerWithItem("styling_table", () -> new StylingTableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD).noOcclusion()), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
    public static final Supplier<BlockEntityType<StylingTableBlockEntity>> STYLING_TABLE_BE = BLOCK_ENTITIES.register("styling_table", () -> BlockEntityType.Builder.of(StylingTableBlockEntity::new, STYLING_TABLE.get()).build(null));
}
