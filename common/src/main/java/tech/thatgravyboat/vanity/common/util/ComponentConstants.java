package tech.thatgravyboat.vanity.common.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class ComponentConstants {

    public static final Component ORIGINAL = Component.translatable("screen.vanity.styling_table.original");
    public static final Component STYLING_TAB = Component.translatable("screen.vanity.styling_table.styling");
    public static final Component DESIGN_TAB = Component.translatable("screen.vanity.styling_table.design");
    public static final Component AUTO_ROTATE = Component.translatable("screen.vanity.styling_table.auto_rotate");
    public static final Component STORAGE_TOOLTIP = Component.translatable("screen.vanity.styling_table.storage.tooltip");

    public static final Component CONTAINER_TITLE = Component.translatable("container.vanity.styling_table");

    public static final Component DESIGN_OPEN_FAILURE = Component.translatable("text.vanity.design.fail")
            .withStyle(ChatFormatting.RED);

    public static final Component JEI_TITLE = Component.translatable("jei.vanity.designs");
}
