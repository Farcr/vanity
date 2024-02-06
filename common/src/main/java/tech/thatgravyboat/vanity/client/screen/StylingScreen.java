package tech.thatgravyboat.vanity.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import tech.thatgravyboat.vanity.client.components.display.StyledItemWidget;
import tech.thatgravyboat.vanity.client.components.StylingTabButton;
import tech.thatgravyboat.vanity.client.components.list.StylesListWidget;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.menu.StylingMenu;
import tech.thatgravyboat.vanity.common.network.NetworkHandler;
import tech.thatgravyboat.vanity.common.network.packets.server.ServerboundOpenTabPacket;
import tech.thatgravyboat.vanity.common.registries.ModItems;
import tech.thatgravyboat.vanity.common.util.ComponentConstants;

public class StylingScreen extends AbstractContainerScreen<StylingMenu> implements ContainerListener {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(Vanity.MOD_ID, "textures/gui/container/styling_table.png");

    private StylesListWidget list;
    private StyledItemWidget display;

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
            StylingTabButton.create(ModItems.STYLING_TABLE.get(), ComponentConstants.STYLING_TAB),
            0, 0
        );

        if (getMenu().canShowStorage()) {
            buttons.addChild(
                StylingTabButton.create(Items.CHEST, ComponentConstants.DESIGN_TAB, () ->
                    NetworkHandler.CHANNEL.sendToServer(new ServerboundOpenTabPacket(true))
                ),
                0, 1
            );
        }

        buttons.arrangeElements();
        buttons.setPosition(this.leftPos + 16, this.topPos);
        buttons.visitWidgets(this::addRenderableWidget);

        this.list = this.addRenderableWidget(new StylesListWidget());
        this.list.setPosition(this.leftPos + 8, this.topPos + 47);
        this.list.addAll(this.menu.styles(), this.menu.getInput());
        this.list.select(this.menu.getResult());

        this.display = this.addRenderableWidget(new StyledItemWidget());
        this.display.setPosition(this.leftPos + 112, this.topPos + 49);
        this.display.select(this.menu.getResult());
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
    public void slotChanged(AbstractContainerMenu menu, int i, ItemStack itemStack) {
        if (this.list != null) {
            this.list.addAll(this.menu.styles(), this.menu.getInput());
            this.list.select(this.menu.getResult());
        }
        if (this.display != null) {
            this.display.select(this.menu.getResult());
        }
    }

    @Override
    public void dataChanged(AbstractContainerMenu menu, int i, int j) {}

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        if (this.menu.getCarried().isEmpty()) {
            return this.getFocused() != null && this.isDragging() && i == 0 && this.getFocused().mouseDragged(d, e, i, f, g);
        }
        return super.mouseDragged(d, e, i, f, g);
    }
}

