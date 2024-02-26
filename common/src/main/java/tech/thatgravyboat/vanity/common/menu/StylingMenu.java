package tech.thatgravyboat.vanity.common.menu;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.vanity.api.design.DesignManager;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.block.StylingTableBlock;
import tech.thatgravyboat.vanity.common.item.DesignHelper;
import tech.thatgravyboat.vanity.common.menu.container.AwareContainer;
import tech.thatgravyboat.vanity.common.menu.content.StylingMenuContent;
import tech.thatgravyboat.vanity.common.registries.ModGameRules;
import tech.thatgravyboat.vanity.common.registries.ModItems;
import tech.thatgravyboat.vanity.common.registries.ModMenuTypes;
import tech.thatgravyboat.vanity.common.registries.ModSounds;

import java.util.*;

public class StylingMenu extends BaseContainerMenu {

    public static final ResourceLocation REMOVE_DESIGN = new ResourceLocation(Vanity.MOD_ID, "remove_design");

    private final List<ResourceLocation> designs;
    private final Map<ResourceLocation, List<String>> styles = new LinkedHashMap<>();
    private final DesignManager manager;

    private ItemStack lastInput = ItemStack.EMPTY;

    private final Container input = new AwareContainer(1, () -> {
        this.updateDesign();
        this.slotsChanged(this.input);
    });

    private final Container result = new AwareContainer(1, () ->
        this.slotsChanged(this.input)
    );

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public StylingMenu(int i, Inventory inventory, Optional<StylingMenuContent> content) {
        this(
            i, inventory,
            content.map(StylingMenuContent.access(inventory.player)).orElse(ContainerLevelAccess.NULL),
            DesignManager.get(true),
            content.map(StylingMenuContent::unlockables).orElse(new ArrayList<>())
        );
    }

    public StylingMenu(int i, Inventory inventory, ContainerLevelAccess access, DesignManager manager, List<ResourceLocation> designs) {
        super(i, ModMenuTypes.STYLING.get(), inventory, access);

        this.designs = designs;
        this.manager = manager;

        this.addSlot(new Slot(this.input, 0, 80, 50) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return !stack.is(ModItems.DESIGN.get());
            }

            @Override
            public void setChanged() {
                super.setChanged();
                lastInput = this.getItem();
            }

            @Override
            public void onTake(Player player, ItemStack itemStack) {
                StylingMenu.this.updateDesign();
                super.onTake(player, itemStack);
                lastInput = ItemStack.EMPTY;
            }
        });
        this.addSlot(new Slot(this.result, 0, 80, 98) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                StylingMenu.this.access.execute((level, blockPos) -> level.playSound(null, blockPos, ModSounds.TAKE_RESULT_STYLING_TABLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F));

                int count = stack.getCount();

                StylingMenu.this.input.removeItem(0, count);
                StylingMenu.this.updateDesign();

                super.onTake(player, stack);
            }
        });
    }

    private void updateDesign() {
        var previous = this.styles.hashCode();
        this.styles.clear();

        ItemStack input = this.input.getItem(0);
        if (!input.isEmpty()) {
            ResourceLocation inputDesign = DesignHelper.getDesign(input);
            String inputStyle = DesignHelper.getStyle(input);

            if (inputDesign != null) {
                this.styles.put(REMOVE_DESIGN, List.of(""));
            }

            this.designs.forEach(id -> this.manager.getDesign(id)
                .map(design -> design.getStylesForItem(input))
                .ifPresent(styles -> this.styles.put(id, new ArrayList<>(styles)))
            );

            if (inputDesign != null && this.styles.containsKey(inputDesign)) {
                this.styles.get(inputDesign).remove(inputStyle);
            }
        }

        if (previous != this.styles.hashCode() || !ItemStack.isSameItemSameTags(lastInput, this.input.getItem(0))) {
            this.result.setItem(0, ItemStack.EMPTY);
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((arg2, arg3) -> this.clearContainer(player, this.input));
    }

    public void select(ResourceLocation design, @Nullable String style) {
        ItemStack input = this.input.getItem(0);
        if (input.isEmpty()) return;
        if (!this.styles.containsKey(design) || !this.styles.get(design).contains(style)) return;
        ItemStack stack = input.copy();
        style = REMOVE_DESIGN.equals(design) ? null : style;
        DesignHelper.setDesignAndStyle(stack, design, style);
        this.result.setItem(0, stack);
    }

    public Map<ResourceLocation, List<String>> styles() {
        return this.styles;
    }

    public ItemStack getInput() {
        return this.input.getItem(0);
    }

    public ItemStack getResult() {
        return this.result.getItem(0);
    }

    public boolean canShowStorage() {
        return this.access.evaluate(StylingTableBlock::canShowStorage, !ModGameRules.LOCK_DESIGN_STORAGE.getCachedValue());
    }
}
