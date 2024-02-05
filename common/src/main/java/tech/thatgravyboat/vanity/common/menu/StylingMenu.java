package tech.thatgravyboat.vanity.common.menu;

import com.mojang.datafixers.util.Pair;
import tech.thatgravyboat.vanity.api.concept.ConceptArt;
import tech.thatgravyboat.vanity.api.concept.ConceptArtManager;
import tech.thatgravyboat.vanity.api.style.Style;
import tech.thatgravyboat.vanity.common.handler.concept.ServerConceptArtManager;
import tech.thatgravyboat.vanity.common.handler.unlockables.UnlockableSaveHandler;
import tech.thatgravyboat.vanity.common.item.ConceptArtHelper;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.registries.VanityItems;
import tech.thatgravyboat.vanity.common.registries.VanityMenuTypes;
import tech.thatgravyboat.vanity.common.registries.VanitySounds;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StylingMenu extends AbstractContainerMenu {

    public static final ResourceLocation EMPTY_CONCEPT_ART_SLOT = new ResourceLocation(Vanity.MOD_ID, "item/empty_styling_table_slot_concept_art");
    public static final ResourceLocation REMOVE_CONCEPT_ART = new ResourceLocation(Vanity.MOD_ID, "remove_concept_art");

    private static final Comparator<Pair<ResourceLocation, String>> SORTER = Comparator.comparing((Pair<ResourceLocation, String> o) -> o.getFirst()).thenComparing(Pair::getSecond);

    private final DataSlot selectedConceptArtIndex = DataSlot.standalone();
    private final ContainerLevelAccess access;
    private final Container container;
    private final List<ResourceLocation> unlocked;

    private final Container inputContainer = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
            StylingMenu.this.updateConceptArt();
            StylingMenu.this.slotsChanged(this);
        }
    };
    private final Container outputContainer = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
            StylingMenu.this.slotsChanged(this);
        }
    };

    private final Slot inputSlot;
    private final Slot resultSlot;

    private long lastSoundTime;
    private final ConceptArtManager conceptArtManager;
    private final Set<Pair<ResourceLocation, String>> conceptArtSet;
    private final List<Pair<ResourceLocation, String>> conceptArt;
    private final Map<ResourceLocation, Integer> conceptArtSlots;
    private final Set<ResourceLocation> defaultConceptArt;
    private boolean lock;

    // Server only
    public StylingMenu(int window, Inventory inventory, Container container, ContainerLevelAccess access, Player player) {
        this(window, inventory, container, access, ServerConceptArtManager.INSTANCE, UnlockableSaveHandler.getUnlockables(player.level(), player.getUUID()));
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public StylingMenu(int window, Inventory inventory, Optional<StylingMenuContent> content) {
        this(
            window, inventory,
            new SimpleContainer(9), ContainerLevelAccess.NULL,
            ConceptArtManager.get(true), content.map(StylingMenuContent::unlockables).orElse(List.of())
        );
    }

    public StylingMenu(int window, Inventory inventory, Container container, ContainerLevelAccess access, ConceptArtManager manager, List<ResourceLocation> unlocked) {
        super(VanityMenuTypes.STYLING_MENU.get(), window);
        checkContainerSize(container, 9);
        this.container = container;

        this.access = access;
        this.conceptArtManager = manager;
        this.unlocked = unlocked;

        this.conceptArtSet = new LinkedHashSet<>();
        this.conceptArt = new LinkedList<>();
        this.conceptArtSlots = new Object2IntArrayMap<>(9);
        this.defaultConceptArt = new HashSet<>();

        this.inputSlot = this.addSlot(new Slot(this.inputContainer, 0, 12, 22) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return !stack.is(VanityItems.CONCEPT_ART.get());
            }
        });

        this.resultSlot = this.addSlot(new Slot(this.outputContainer, 0, 12, 70) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                access.execute((level, blockPos) -> {
                    long l = level.getGameTime();
                    if (StylingMenu.this.lastSoundTime != l) {
                        level.playSound(null, blockPos, VanitySounds.UI_STYLING_TABLE_TAKE_RESULT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                        StylingMenu.this.lastSoundTime = l;
                    }
                });

                StylingMenu.this.inputContainer.removeItem(0, 1);
                StylingMenu.this.outputContainer.removeItem(0, 1);
                StylingMenu.this.updateConceptArt();

                super.onTake(player, stack);
            }
        });

        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 3; ++k) {
                this.addSlot(new Slot(this.container, k + j * 3, 116 + k * 18, 30 + j * 18) {

                    @Override
                    public void setChanged() {
                        super.setChanged();
                        StylingMenu.this.updateConceptArt();
                        StylingMenu.this.slotsChanged(this.container);
                    }

                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return ConceptArtHelper.getArtId(stack) != null;
                    }

                    @Override
                    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                        return Pair.of(InventoryMenu.BLOCK_ATLAS, StylingMenu.EMPTY_CONCEPT_ART_SLOT);
                    }
                });
            }
        }


        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(inventory, k + j * 9 + 9, 8 + k * 18, 110 + j * 18));
            }
        }

        for (int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(inventory, j, 8 + j * 18, 168));
        }

        this.selectedConceptArtIndex.set(-1);
        this.addDataSlot(this.selectedConceptArtIndex);
        this.updateConceptArt();
    }

    private void updateConceptArt() {
        int old = this.conceptArtSet.hashCode();
        this.conceptArtSet.clear();
        this.conceptArtSlots.clear();
        this.defaultConceptArt.clear();

        ItemStack input = this.inputContainer.getItem(0);
        if (!input.isEmpty()) {
            ResourceLocation inputId = ConceptArtHelper.getArtId(input);
            String inputVariant = ConceptArtHelper.getVariantName(input);

            if (inputId != null) {
                this.conceptArtSet.add(Pair.of(REMOVE_CONCEPT_ART, ""));
            }

            for (int i = 0; i < this.container.getContainerSize(); i++) {
                ConceptArt conceptArt = this.conceptArtManager.getItemConceptArt(this.container.getItem(i));
                if (conceptArt != null) {
                    ResourceLocation id = this.conceptArtManager.getConceptArtId(conceptArt).orElse(null);
                    if (id == null || this.conceptArtSlots.containsKey(id)) {
                        continue;
                    }

                    this.conceptArtSlots.put(id, i);
                    addVariants(id, conceptArt, inputId, inputVariant, input);
                }
            }

            for (var entry : this.conceptArtManager.getDefaultConceptArt().entrySet()) {
                ResourceLocation id = entry.getKey();
                addVariants(id, entry.getValue(), inputId, inputVariant, input);
                this.defaultConceptArt.add(id);
            }

            for (ResourceLocation id : this.unlocked) {
                ConceptArt art = this.conceptArtManager.getConceptArt(id).orElse(null);
                if (art == null) continue;
                addVariants(id, art, inputId, inputVariant, input);
            }
        }

        if (old != this.conceptArtSet.hashCode()) {
            this.selectedConceptArtIndex.set(-1);
        }

        this.conceptArt.clear();
        this.conceptArt.addAll(this.conceptArtSet);
    }

    public void addVariants(ResourceLocation id, ConceptArt art, ResourceLocation inputId, String inputVariant, ItemStack stack) {
        List<Pair<ResourceLocation, String>> temp = new LinkedList<>();
        for (String name : art.styles().keySet()) {
            Style style = art.getVariantForItem(name, stack);
            if (style != null && (!id.equals(inputId) || !name.equals(inputVariant))) {
                temp.add(Pair.of(id, name));
            }
        }
        temp.sort(SORTER);
        this.conceptArtSet.addAll(temp);
    }

    public int getSelectedConceptArtIndex() {
        return this.selectedConceptArtIndex.get();
    }

    public List<Pair<ResourceLocation, String>> getConceptArt() {
        return conceptArt;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id >= 0 && id < this.conceptArt.size()) {
            this.selectedConceptArtIndex.set(id);
            this.setupResultSlot();
            return true;
        }
        return false;
    }

    @Override
    public void slotsChanged(Container container) {
        if (this.lock) {
            return;
        }

        this.lock = true;
        this.setupResultSlot();
        this.broadcastChanges();
        this.lock = false;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int i) {
        Slot slot = this.getSlot(i);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            if (i <= 10) {
                if (!this.moveItemStackTo(stack, 39, 47, false)) {
                    if (!this.moveItemStackTo(stack, 11, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!stack.is(VanityItems.CONCEPT_ART.get()) && !this.moveItemStackTo(stack, 0, 1, false)) {
                return ItemStack.EMPTY;
            } else if (ConceptArtHelper.getArtId(stack) != null) {
                if (!this.moveItemStackTo(stack, 1, 9, false)) {
                    return ItemStack.EMPTY;
                }
            }
            slot.onTake(player, stack);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((arg2, arg3) -> this.clearContainer(player, this.inputContainer));
    }

    private OptionalInt getConceptArtIndex() {
        int selectedIndex = this.selectedConceptArtIndex.get();
        if (selectedIndex == -1) {
            return OptionalInt.empty();
        }

        Pair<ResourceLocation, String> conceptArt = this.conceptArt.get(selectedIndex);
        if (conceptArt == null) {
            return OptionalInt.empty();
        }

        int conceptArtIndex = this.conceptArtSlots.getOrDefault(conceptArt.getFirst(), -1);
        if (conceptArtIndex < 0 || conceptArtIndex >= this.container.getContainerSize()) {
            return OptionalInt.empty();
        }

        return OptionalInt.of(conceptArtIndex);
    }

    private void setupResultSlot() {
        this.resultSlot.set(ItemStack.EMPTY);
        int selectedIndex = this.selectedConceptArtIndex.get();
        if (selectedIndex >= 0 && selectedIndex < this.conceptArt.size()) {
            ItemStack inputStack = this.getInputItem();
            if (inputStack.isEmpty()) {
                return;
            }

            Pair<ResourceLocation, String> conceptArt = this.conceptArt.get(selectedIndex);
            if (conceptArt == null) {
                return;
            }

            ResourceLocation conceptArtId = conceptArt.getFirst();
            String variant = conceptArt.getSecond();

            if (conceptArtId == REMOVE_CONCEPT_ART) {
                setResult(inputStack, null, null);
                return;
            }

            if (this.defaultConceptArt.contains(conceptArtId) || this.unlocked.contains(conceptArtId)) {
                setResult(inputStack, conceptArtId, variant);
                return;
            }

            int conceptIndex = this.getConceptArtIndex().orElse(-1);
            if (conceptIndex < 0 || conceptIndex >= this.container.getContainerSize() || this.container.getItem(conceptIndex).isEmpty() || conceptArtId == null || variant == null) {
                return;
            }

            setResult(inputStack, conceptArtId, variant);
        }
    }

    private void setResult(ItemStack stack, ResourceLocation id, String variant) {
        ItemStack outputStack = stack.copy();
        outputStack.setCount(1);
        ConceptArtHelper.setItemConceptArtVariant(outputStack, id, variant);
        this.resultSlot.set(outputStack);
    }

    public ItemStack getInputItem() {
        return this.inputSlot.getItem();
    }
}
