package tech.thatgravyboat.vanity.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import tech.thatgravyboat.vanity.client.components.StylingTabButton;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.menu.StylingMenu;
import tech.thatgravyboat.vanity.common.network.NetworkHandler;
import tech.thatgravyboat.vanity.common.network.packets.server.ServerboundOpenTabPacket;
import tech.thatgravyboat.vanity.common.network.packets.server.ServerboundSelectStylePacket;
import tech.thatgravyboat.vanity.common.registries.VanityItems;
import tech.thatgravyboat.vanity.common.util.ConstantComponents;

import java.util.ArrayList;
import java.util.List;

public class StylingScreen extends AbstractContainerScreen<StylingMenu> implements ContainerListener {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(Vanity.MOD_ID, "textures/gui/container/styling_table.png");
    private static final Component ORIGINAL = Component.translatable("screen.vanity.styling_table.original");

    private final List<AbstractWidget> buttons = new ArrayList<>();

    public StylingScreen(StylingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageHeight = 220;
        this.titleLabelY = UIConstants.TITLE_Y;
        this.inventoryLabelY = UIConstants.INVENTORY_Y;

        this.menu.addSlotListener(this);
    }

    @Override
    protected void init() {
        super.init();

        GridLayout buttons = new GridLayout();

        buttons.addChild(
                StylingTabButton.create(VanityItems.STYLING_TABLE.get(), ConstantComponents.STYLING_TAB),
                0, 0
        );

        if (getMenu().canShowStorage()) {
            buttons.addChild(
                StylingTabButton.create(Items.CHEST, ConstantComponents.CONCEPT_ART_TAB, () ->
                    NetworkHandler.CHANNEL.sendToServer(new ServerboundOpenTabPacket(true))
                ),
                0, 1
            );
        }

        buttons.arrangeElements();
        buttons.setPosition(this.leftPos + 16, this.topPos);
        buttons.visitWidgets(this::addRenderableWidget);

        updateSlots();
    }

    public void updateSlots() {
        GridLayout layout = new GridLayout();
        var helper = layout.spacing(1).createRowHelper(6);
        this.buttons.forEach(this::removeWidget);
        this.buttons.clear();

        this.menu.styles().forEach((concept, styles) -> {
            for (String style : styles) {
                Button button = Button.builder(CommonComponents.EMPTY, b -> {
                    ServerboundSelectStylePacket packet = new ServerboundSelectStylePacket(concept, style);
                    NetworkHandler.CHANNEL.sendToServer(packet);
                }).size(10, 10).tooltip(Tooltip.create(Component.literal(concept + " - " + style))).build();
                helper.addChild(button);
            }
        });

        layout.arrangeElements();
        layout.setPosition(this.leftPos + 24, this.topPos + 24);
        layout.visitWidgets(this.buttons::add);
        this.buttons.forEach(this::addRenderableWidget);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, UIConstants.TITLE_COLOR, false);
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, UIConstants.INVENTORY_COLOR, false);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        this.renderBackground(graphics);
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        graphics.blit(BACKGROUND, this.leftPos + 42, this.topPos, 176, 54, 26, 28);
    }

    @Override
    public void slotChanged(AbstractContainerMenu abstractContainerMenu, int i, ItemStack itemStack) {
        this.updateSlots();
    }

    @Override
    public void dataChanged(AbstractContainerMenu abstractContainerMenu, int i, int j) {}
}

