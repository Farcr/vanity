package gg.moonflower.vanity.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import gg.moonflower.pollen.api.registry.wrapper.v1.PollinatedBlockRegistry;
import gg.moonflower.pollen.api.registry.wrapper.v1.PollinatedRegistry;
import gg.moonflower.vanity.common.block.StylingTableBlock;
import gg.moonflower.vanity.common.block.entity.StylingTableBlockEntity;
import gg.moonflower.vanity.core.Vanity;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class VanityBlocks {

    public static final PollinatedBlockRegistry BLOCKS = PollinatedBlockRegistry.create(VanityItems.REGISTRY);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Vanity.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);

    public static final Supplier<Block> STYLING_TABLE = BLOCKS.registerWithItem("styling_table", () -> new StylingTableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD).noOcclusion()), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));
    public static final Supplier<BlockEntityType<StylingTableBlockEntity>> STYLING_TABLE_BE = BLOCK_ENTITIES.register("styling_table", () -> BlockEntityType.Builder.of(StylingTableBlockEntity::new, STYLING_TABLE.get()).build(null));
}
