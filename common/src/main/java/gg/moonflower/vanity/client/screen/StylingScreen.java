package gg.moonflower.vanity.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import gg.moonflower.vanity.common.item.ConceptArtItem;
import gg.moonflower.vanity.common.menu.StylingMenu;
import gg.moonflower.vanity.core.Vanity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class StylingScreen extends AbstractContainerScreen<StylingMenu> implements ContainerListener {

    private static final ResourceLocation BG_LOCATION = new ResourceLocation(Vanity.MOD_ID, "textures/gui/container/styling_table.png");
    private static final Component ORIGINAL = new TranslatableComponent("screen." + Vanity.MOD_ID + ".styling_table.original");
    private float scrollOffs;
    private boolean scrolling;
    private int startIndex;

    public StylingScreen(StylingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageHeight = 192;
        this.inventoryLabelY = this.imageHeight - 94;
        this.menu.addSlotListener(this);
    }

    @Override
    public void slotChanged(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack stack) {
        if (dataSlotIndex >= 2 && dataSlotIndex < 11) {
            this.scrollOffs = 0;
            this.startIndex = (int) ((double) (this.scrollOffs * (float) this.getOffscreenRows()) + 0.5) * 3;
        }
    }

    @Override
    public void dataChanged(AbstractContainerMenu containerMenu, int dataSlotIndex, int value) {
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        this.renderTooltip(poseStack, mouseX, mouseY);

        float hoveredX = (mouseX - this.leftPos - 44) / 18F;
        float hoveredY = (mouseY - this.topPos - 30) / 18F;
        if (hoveredX >= 0 && hoveredX < 3 && hoveredY >= 0 && hoveredY < 3) {
            int hoveredIndex = this.startIndex + (int) hoveredX + (int) hoveredY * 3;
            if (hoveredIndex >= 0 && hoveredIndex < this.menu.getConceptArt().size()) {
                Pair<ResourceLocation, String> conceptArt = this.menu.getConceptArt().get(hoveredIndex);
                Component text = conceptArt.getFirst() == StylingMenu.REMOVE_CONCEPT_ART ? ORIGINAL : ConceptArtItem.getTranslationKey(conceptArt.getFirst(), conceptArt.getSecond());
                this.renderTooltip(poseStack, text, mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        this.renderBackground(poseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BG_LOCATION);
        int left = this.leftPos;
        int top = this.topPos;
        this.blit(poseStack, left, top, 0, 0, this.imageWidth, this.imageHeight);

        int k = (int) (41.0F * this.scrollOffs);
        this.blit(poseStack, left + 101, top + 29 + k, 194 + (this.isScrollBarActive() ? 0 : 12), 0, 12, 15);

        List<Pair<ResourceLocation, String>> conceptArtEntries = this.menu.getConceptArt();
        for (int entry = this.startIndex; entry < this.startIndex + 9 && entry < conceptArtEntries.size(); entry++) {
            int index = entry - this.startIndex;

            int x = left + 44;
            int y = top + 30;

            int buttonX = x + index % 3 * 18;
            int l = index / 3;
            int buttonY = y + l * 18;
            int n = 0;
            if (entry == this.menu.getSelectedConceptArtIndex()) {
                n += 18;
            } else if (mouseX >= buttonX && mouseY >= buttonY && mouseX < buttonX + 18 && mouseY < buttonY + 18) {
                n += 36;
            }

            this.blit(poseStack, buttonX, buttonY - 1, 176, n, 18, 18);
        }

        for (int entry = this.startIndex; entry < this.startIndex + 9 && entry < conceptArtEntries.size(); entry++) {
            int index = entry - this.startIndex;
            int x = left + 44 + ((index % 3) * 18);
            int l = index / 3;
            int y = top + 29 + (l * 18);

            ItemStack copy = this.menu.getInputItem().copy();
            Pair<ResourceLocation, String> conceptArt = conceptArtEntries.get(entry);
            ConceptArtItem.setItemConceptArtVariant(copy, conceptArt.getFirst() != StylingMenu.REMOVE_CONCEPT_ART ? conceptArt.getFirst() : null, conceptArt.getSecond());
            this.minecraft.getItemRenderer().renderAndDecorateFakeItem(copy, x + 1, y + 1);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrolling = false;
        if (!this.menu.getConceptArt().isEmpty()) {
            int x = this.leftPos + 44;
            int y = this.topPos + 29;
            int max = this.startIndex + 9;

            for (int index = this.startIndex; index < max; ++index) {
                int pos = index - this.startIndex;
                double d = mouseX - (double) (x + pos % 3 * 18);
                double e = mouseY - (double) (y + pos / 3 * 18);
                if (d >= 0.0 && e >= 0.0 && d < 18.0 && e < 18.0 && this.menu.clickMenuButton(this.minecraft.player, index)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, index);
                    return true;
                }
            }

            x = this.leftPos + 101;
            y = this.topPos + 29;
            if (mouseX >= x && mouseX < x + 12 && mouseY >= y && mouseY < y + 56) {
                this.scrolling = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            int i = this.topPos + 28;
            int j = i + 54;
            this.scrollOffs = ((float) mouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) this.getOffscreenRows()) + 0.5) * 3;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int i = this.getOffscreenRows();
        if (this.isScrollBarActive()) {
            float f = (float) delta / (float) i;
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
        } else {
            this.scrollOffs = 0;
        }
        this.startIndex = (int) ((double) (this.scrollOffs * (float) i) + 0.5) * 3;

        return true;
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeft, int guiTop, int mouseButton) {
        return mouseX < (double) guiLeft || mouseY < (double) guiTop || mouseX >= (double) (guiLeft + this.imageWidth) || mouseY >= (double) (guiTop + this.imageHeight);
    }

    private boolean isScrollBarActive() {
        return this.menu.getConceptArt().size() > 9;
    }

    private int getOffscreenRows() {
        return (this.menu.getConceptArt().size() + 3 - 1) / 3 - 3;
    }
}

