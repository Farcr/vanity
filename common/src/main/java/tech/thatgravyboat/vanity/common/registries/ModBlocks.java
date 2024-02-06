package tech.thatgravyboat.vanity.common.registries;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import tech.thatgravyboat.vanity.common.block.StylingTableBlock;
import tech.thatgravyboat.vanity.common.block.StylingTableBlockEntity;
import tech.thatgravyboat.vanity.common.Vanity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class ModBlocks {

    public static final ResourcefulRegistry<Block> BLOCKS = ResourcefulRegistries.create(BuiltInRegistries.BLOCK, Vanity.MOD_ID);
    public static final ResourcefulRegistry<BlockEntityType<?>> BLOCK_ENTITIES = ResourcefulRegistries.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Vanity.MOD_ID);

    public static final Supplier<Block> STYLING_TABLE = BLOCKS.register("styling_table", () -> new StylingTableBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE).strength(2.5F).sound(SoundType.WOOD).noOcclusion()));
    public static final Supplier<BlockEntityType<StylingTableBlockEntity>> STYLING_TABLE_BE = BLOCK_ENTITIES.register("styling_table", () -> BlockEntityType.Builder.of(StylingTableBlockEntity::new, STYLING_TABLE.get()).build(null));
}
