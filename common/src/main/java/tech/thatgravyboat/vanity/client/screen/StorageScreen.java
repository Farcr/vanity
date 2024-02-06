package tech.thatgravyboat.vanity.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import tech.thatgravyboat.vanity.client.components.StylingTabButton;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.menu.StorageMenu;
import tech.thatgravyboat.vanity.common.network.NetworkHandler;
import tech.thatgravyboat.vanity.common.network.packets.server.ServerboundOpenTabPacket;
import tech.thatgravyboat.vanity.common.registries.VanityItems;
import tech.thatgravyboat.vanity.common.util.ComponentConstants;

public class StorageScreen extends AbstractContainerScreen<StorageMenu> {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(Vanity.MOD_ID, "textures/gui/container/concept_art_storage.png");

    public StorageScreen(StorageMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageHeight = 220;
        this.titleLabelY = UIConstants.TITLE_Y;
        this.inventoryLabelY = UIConstants.INVENTORY_Y;
    }

    @Override
    protected void init() {
        super.init();

        GridLayout buttons = new GridLayout();

        buttons.addChild(
            StylingTabButton.create(VanityItems.STYLING_TABLE.get(), ComponentConstants.STYLING_TAB, () ->
                NetworkHandler.CHANNEL.sendToServer(new ServerboundOpenTabPacket(false))
            ),
            0, 0
        );

        buttons.addChild(
            StylingTabButton.create(Items.CHEST, ComponentConstants.CONCEPT_ART_TAB),
            0, 1
        );

        buttons.arrangeElements();
        buttons.setPosition(this.leftPos + 16, this.topPos);
        buttons.visitWidgets(this::addRenderableWidget);
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
    protected void renderBg(GuiGraphics graphics, float f, int i, int j) {
        this.renderBackground(graphics);
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        graphics.renderFakeItem(VanityItems.CONCEPT_ART.get().getDefaultInstance(), this.leftPos + 80, this.topPos + 45);
    }
}
