package tech.thatgravyboat.vanity.tests;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import tech.thatgravyboat.vanity.common.handler.design.ServerDesignManager;

public class ConditionTests implements FabricGameTest {

    private static final ResourceLocation ALWAYS_TRUE = new ResourceLocation("test", "always_true");
    private static final ResourceLocation ALWAYS_FALSE = new ResourceLocation("test", "always_false");

    @GameTest(template = EMPTY_STRUCTURE)
    public void testFailingConditions(GameTestHelper helper) {
        ServerDesignManager.INSTANCE.getDesign(ALWAYS_FALSE)
            .ifPresentOrElse(
                design -> helper.fail("Design should not exist"),
                helper::succeed
            );
    }

    @GameTest(template = EMPTY_STRUCTURE)
    public void testPassingConditions(GameTestHelper helper) {
        ServerDesignManager.INSTANCE.getDesign(ALWAYS_TRUE)
            .ifPresentOrElse(
                design -> helper.succeed(),
                () -> helper.fail("Design should exist")
            );
    }

    @GameTest(template = EMPTY_STRUCTURE)
    public void testFailingStyleConditions(GameTestHelper helper) {
        ServerDesignManager.INSTANCE.getDesign(ALWAYS_TRUE)
            .ifPresentOrElse(
                design -> {
                    if (!design.styles().containsKey("failing")) {
                        helper.succeed();
                    } else {
                        helper.fail("Style should not exist");
                    }
                },
                () -> helper.fail("Design should exist")
            );
    }

    @GameTest(template = EMPTY_STRUCTURE)
    public void testPassingStyleConditions(GameTestHelper helper) {
        ServerDesignManager.INSTANCE.getDesign(ALWAYS_TRUE)
            .ifPresentOrElse(
                design -> {
                    if (design.styles().containsKey("passing")) {
                        helper.succeed();
                    } else {
                        helper.fail("Style should exist");
                    }
                },
                () -> helper.fail("Design should exist")
            );
    }


}
