package tech.thatgravyboat.vanity.common.menu;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.vanity.api.concept.ConceptArtManager;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.block.StylingTableBlock;
import tech.thatgravyboat.vanity.common.item.ConceptArtHelper;
import tech.thatgravyboat.vanity.common.menu.container.AwareContainer;
import tech.thatgravyboat.vanity.common.menu.content.StylingMenuContent;
import tech.thatgravyboat.vanity.common.registries.VanityItems;
import tech.thatgravyboat.vanity.common.registries.VanityMenuTypes;
import tech.thatgravyboat.vanity.common.registries.VanitySounds;

import java.util.*;

public class StylingMenu extends BaseContainerMenu {

    public static final ResourceLocation REMOVE_CONCEPT_ART = new ResourceLocation(Vanity.MOD_ID, "remove_concept_art");

    private final List<ResourceLocation> conceptArt;
    private final Map<ResourceLocation, List<String>> styles = new LinkedHashMap<>();
    private final ConceptArtManager manager;

    private final Container input = new AwareContainer(1, () -> {
        this.updateConceptArt();
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
            ConceptArtManager.get(true),
            content.map(StylingMenuContent::unlockables).orElse(new ArrayList<>())
        );
    }

    public StylingMenu(int i, Inventory inventory, ContainerLevelAccess access, ConceptArtManager manager, List<ResourceLocation> conceptArt) {
        super(i, VanityMenuTypes.STYLING.get(), inventory, access);

        this.conceptArt = conceptArt;
        this.manager = manager;

        this.addSlot(new Slot(this.input, 0, 80, 50) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return !stack.is(VanityItems.CONCEPT_ART.get());
            }

            @Override
            public void onTake(Player player, ItemStack itemStack) {
                super.onTake(player, itemStack);
                StylingMenu.this.updateConceptArt();
            }
        });
        this.addSlot(new Slot(this.result, 0, 80, 98) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                StylingMenu.this.access.execute((level, blockPos) -> level.playSound(null, blockPos, VanitySounds.UI_STYLING_TABLE_TAKE_RESULT.get(), SoundSource.BLOCKS, 1.0F, 1.0F));

                StylingMenu.this.input.removeItem(0, 1);
                StylingMenu.this.result.removeItem(0, 1);
                StylingMenu.this.updateConceptArt();

                super.onTake(player, stack);
            }
        });
    }

    private void updateConceptArt() {
        var previous = this.styles.hashCode();
        this.styles.clear();

        ItemStack input = this.input.getItem(0);
        if (!input.isEmpty()) {
            ResourceLocation inputArt = ConceptArtHelper.getArtId(input);
            String inputStyle = ConceptArtHelper.getStyle(input);

            if (inputArt != null) {
                this.styles.put(REMOVE_CONCEPT_ART, List.of(""));
            }

            this.conceptArt.forEach(id -> this.manager.getConceptArt(id)
                .map(art -> art.getStylesForItem(input))
                .ifPresent(styles -> this.styles.put(id, new ArrayList<>(styles)))
            );

            if (inputArt != null && this.styles.containsKey(inputArt)) {
                this.styles.get(inputArt).remove(inputStyle);
            }
        }

        if (previous != this.styles.hashCode()) {
            this.result.setItem(0, ItemStack.EMPTY);
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((arg2, arg3) -> this.clearContainer(player, this.input));
    }

    public void select(ResourceLocation concept, @Nullable String style) {
        ItemStack input = this.input.getItem(0);
        if (input.isEmpty()) return;
        if (!this.styles.containsKey(concept) || !this.styles.get(concept).contains(style)) return;
        ItemStack stack = input.copyWithCount(1);
        style = REMOVE_CONCEPT_ART.equals(concept) ? null : style;
        ConceptArtHelper.setItemConceptArtVariant(stack, concept, style);
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
        return this.access.evaluate((level, pos) -> {
            BlockState state = level.getBlockState(pos);
            return !state.hasProperty(StylingTableBlock.POWERED) || !state.getValue(StylingTableBlock.POWERED);
        }, true);
    }
}
