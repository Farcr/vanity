package tech.thatgravyboat.vanity.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StylingTableBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = Shapes.or(
            box(0, 0, 0, 3, 9, 3),
            box(13, 0, 0, 16, 9, 3),
            box(0, 0, 13, 3, 9, 16),
            box(13, 0, 13, 16, 9, 16),
            box(0, 9, 0, 16, 16, 16)
    );

    public StylingTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof StylingTableBlockEntity stylingTable && level instanceof ServerLevel) {
                Containers.dropContents(level, pos, stylingTable);
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            if (level.getBlockEntity(pos) instanceof StylingTableBlockEntity table) {
                table.openMenu((ServerPlayer) player);
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StylingTableBlockEntity(pos, state);
    }
}