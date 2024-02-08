package tech.thatgravyboat.vanity.tests;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import tech.thatgravyboat.vanity.common.block.StylingTableBlockEntity;
import tech.thatgravyboat.vanity.common.handler.unlockables.UnlockableSaveHandler;
import tech.thatgravyboat.vanity.common.item.DesignHelper;
import tech.thatgravyboat.vanity.common.registries.ModBlocks;
import tech.thatgravyboat.vanity.common.registries.ModGameRules;
import tech.thatgravyboat.vanity.common.registries.ModItems;

import java.util.List;

public class DesignTests implements FabricGameTest {

    private static final ResourceLocation TEST_DESIGN = new ResourceLocation("test", "test");
    private static final ResourceLocation DEFAULT_DESIGN = new ResourceLocation("test", "default");
    private static final ResourceLocation UNLOCKABLE_DESIGN = new ResourceLocation("test", "unlockable");

    @GameTest(template = EMPTY_STRUCTURE)
    public void testDesignUnlocking(GameTestHelper helper) {
        InteractionResultHolder<ItemStack> result;
        Player mockPlayer = helper.makeMockPlayer();
        Level level = helper.getLevel();

        setGameRule(helper, ModGameRules.UNLOCKABLE_DESIGNS.key(), false);

        setItem(mockPlayer, new ItemStack(ModItems.DESIGN.get()));
        result = useItem(mockPlayer);
        helper.assertTrue(result.getResult() == InteractionResult.PASS, "Design item failed to pass when disabled");
        helper.assertTrue(result.getObject().getCount() == 1, "Design item used when disabled");

        setGameRule(helper, ModGameRules.UNLOCKABLE_DESIGNS.key(), true);

        result = useItem(mockPlayer);
        helper.assertTrue(result.getResult() == InteractionResult.FAIL, "Design item failed to fail when design is null");
        helper.assertTrue(result.getObject().getCount() == 1, "Design item used when design is null");

        setItem(mockPlayer, DesignHelper.createDesignItem(UNLOCKABLE_DESIGN));
        result = useItem(mockPlayer);
        helper.assertTrue(result.getResult() == InteractionResult.CONSUME, "Design item failed to consume when design is not null");
        helper.assertTrue(result.getObject().getCount() == 0, "Design item not used when design is not null");
        List<ResourceLocation> unlockables = UnlockableSaveHandler.getUnlockables(level, mockPlayer.getUUID());
        helper.assertTrue(unlockables.contains(UNLOCKABLE_DESIGN), "Design not unlocked");

        setItem(mockPlayer, DesignHelper.createDesignItem(UNLOCKABLE_DESIGN));
        result = useItem(mockPlayer);
        helper.assertTrue(result.getResult() == InteractionResult.FAIL, "Design item failed to fail when design is already unlocked");
        helper.assertTrue(result.getObject().getCount() == 1, "Design item used when design is already unlocked");

        helper.assertTrue(unlockables.size() == 1, "Design unlocked multiple times");

        helper.succeed();
    }

    @SuppressWarnings("DataFlowIssue")
    @GameTest(template = EMPTY_STRUCTURE)
    public void testDesignAvailableDesigns(GameTestHelper helper) {
        Player mockPlayer = helper.makeMockPlayer();
        Level level = helper.getLevel();
        BlockPos tablePos = BlockPos.ZERO.above();

        helper.setBlock(tablePos, ModBlocks.STYLING_TABLE.get());

        List<ResourceLocation> designs;
        StylingTableBlockEntity table = (StylingTableBlockEntity) helper.getBlockEntity(tablePos);

        // Clear the unlockables
        UnlockableSaveHandler.setUnlockables(level, mockPlayer.getUUID(), List.of());

        // Tests
        designs = table.styling().getDesignsForPlayer(mockPlayer);
        helper.assertTrue(designs.size() == 1, "Designs has only the default design");
        helper.assertTrue(designs.contains(DEFAULT_DESIGN), "Designs does not contain the default design");

        UnlockableSaveHandler.addUnlockable(level, mockPlayer.getUUID(), UNLOCKABLE_DESIGN);
        designs = table.styling().getDesignsForPlayer(mockPlayer);
        helper.assertTrue(designs.size() == 2, "Designs has the default and unlockable designs");
        helper.assertTrue(designs.contains(DEFAULT_DESIGN), "Designs does not contain the default design");
        helper.assertTrue(designs.contains(UNLOCKABLE_DESIGN), "Designs does not contain the unlockable design");

        table.items().set(0, DesignHelper.createDesignItem(TEST_DESIGN));
        designs = table.styling().getDesignsForPlayer(mockPlayer);
        helper.assertTrue(designs.size() == 3, "Designs has the default, unlockable, and table designs");
        helper.assertTrue(designs.contains(DEFAULT_DESIGN), "Designs does not contain the default design");
        helper.assertTrue(designs.contains(UNLOCKABLE_DESIGN), "Designs does not contain the unlockable design");

        helper.succeed();
    }

    private static void setItem(Player player, ItemStack stack) {
        player.setItemInHand(InteractionHand.MAIN_HAND, stack);
    }

    private static InteractionResultHolder<ItemStack> useItem(Player player) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).use(player.level(), player, InteractionHand.MAIN_HAND);
    }

    private static void setGameRule(GameTestHelper helper, GameRules.Key<GameRules.BooleanValue> key, boolean value) {
        helper.getLevel().getGameRules().getRule(key).set(value, helper.getLevel().getServer());
    }
}
