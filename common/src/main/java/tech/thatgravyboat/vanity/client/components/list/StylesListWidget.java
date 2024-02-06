package tech.thatgravyboat.vanity.client.components.list;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import tech.thatgravyboat.vanity.client.components.base.BaseParentWidget;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.item.ConceptArtHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class StylesListWidget extends BaseParentWidget {

    private static final int OFFSET = 2;
    private static final ResourceLocation THUMB = new ResourceLocation(Vanity.MOD_ID, "textures/gui/sprites/scrollbar/thumb.png");
    private static final Comparator<Pair<ResourceLocation, String>> SORTER = Comparator.comparing((Pair<ResourceLocation, String> o) -> o.getFirst()).thenComparing(Pair::getSecond);

    private int oldHash = -1;

    private double scroll = 0;
    private int lastHeight = 0;
    private boolean scrolling = false;

    private GridLayout layout;

    public StylesListWidget() {
        super(64, 73);
    }

    public void addAll(Map<ResourceLocation, List<String>> styles, ItemStack input) {
        if (styles.hashCode() == this.oldHash) return;
        this.scroll = 0;
        this.oldHash = styles.hashCode();

        clear();
        List<Pair<ResourceLocation, String>> pairs = new ArrayList<>();
        styles.forEach((art, variants) -> variants.forEach(variant -> pairs.add(Pair.of(art, variant))));
        pairs.sort(SORTER);
        pairs.forEach(pair -> addRenderableWidget(new StyleButton(pair.getFirst(), pair.getSecond(), input)));
        setupButtons();
    }

    public void select(ItemStack stack) {
        ResourceLocation art = ConceptArtHelper.getArtId(stack);
        String style = ConceptArtHelper.getStyle(stack);
        for (AbstractWidget child : this.children) {
            if (child instanceof StyleButton styleButton) {
                styleButton.select(art, style);
            }
        }
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.enableScissor(getX() + OFFSET, getY() + OFFSET, getX() + 56, getY() + 71);
        if (this.layout != null) this.layout.setPosition(this.getX() + OFFSET, this.getY() + OFFSET - (int) scroll);
        mouseX = this.isHovered() ? mouseX : -1;
        mouseY = this.isHovered() ? mouseY : -1;
        for (Renderable renderable : this.children) {
            renderable.render(graphics, mouseX, mouseY, partialTicks);
        }
        graphics.disableScissor();

        if (this.lastHeight > this.height) {
            int scrollbarHeight = Math.round((this.height / (float) this.lastHeight) * this.height);
            int middleHeight = scrollbarHeight - 2;
            int splits = middleHeight / 12;
            int remainder = middleHeight % 12;
            int y = this.getY() + Math.round(((float) this.scroll / (float) this.lastHeight) * this.height);

            graphics.blit(THUMB, this.getX() + 59, y, 0, 0, 5, 1);
            for (int i = 0; i <= splits; i++) {
                int height = i == splits ? remainder : 12;
                if (height == 0) continue;
                graphics.blit(THUMB, this.getX() + 59, y + i * 12 + 1, 0, 1, 5, height);
            }
            graphics.blit(THUMB, this.getX() + 59, y + scrollbarHeight - 1, 0, 13, 5, 1);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling) {
            this.scroll = Mth.clamp(
                this.scroll + dragY / (this.height - 15) * this.lastHeight, 0,
                Math.max(0, this.lastHeight - this.height)
            );
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
        this.scroll = Mth.clamp(this.scroll - scrollY * 9, 0, Math.max(0, this.lastHeight - this.height));
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            if (isOverScrollBar(mouseX) && button == InputConstants.MOUSE_BUTTON_LEFT) {
                this.scrolling = true;
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == InputConstants.MOUSE_BUTTON_LEFT) {
            this.scrolling = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public boolean isOverScrollBar(double mouseX) {
        return mouseX > this.getX() + 59 && this.lastHeight > this.height;
    }

    private void setupButtons() {
        this.lastHeight = 0;
        var layout = new GridLayout().createRowHelper(3);
        for (AbstractWidget child : this.children) {
            layout.addChild(child);
        }
        this.layout = layout.getGrid();

        this.layout.arrangeElements();
        this.lastHeight = this.layout.getHeight() + 4; // The space that is missing from the last row
        this.layout.setPosition(this.getX() + OFFSET, this.getY() + OFFSET - (int) scroll);
    }
}
