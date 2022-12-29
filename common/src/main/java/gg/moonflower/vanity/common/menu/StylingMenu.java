package gg.moonflower.vanity.common.menu;

import com.mojang.datafixers.util.Pair;
import gg.moonflower.pollen.api.util.QuickMoveHelper;
import gg.moonflower.vanity.api.concept.ConceptArt;
import gg.moonflower.vanity.api.concept.ConceptArtManager;
import gg.moonflower.vanity.common.item.ConceptArtItem;
import gg.moonflower.vanity.core.Vanity;
import gg.moonflower.vanity.core.registry.VanityBlocks;
import gg.moonflower.vanity.core.registry.VanityItems;
import gg.moonflower.vanity.core.registry.VanityMenuTypes;
import gg.moonflower.vanity.core.registry.VanitySounds;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class StylingMenu extends AbstractContainerMenu {

    public static final ResourceLocation EMPTY_CONCEPT_ART_SLOT = new ResourceLocation(Vanity.MOD_ID, "item/empty_styling_table_slot_concept_art");
    private static final QuickMoveHelper MOVE_HELPER = new QuickMoveHelper().
            add(0, 11, 11, 36, false). // to Inventory
                    add(11, 36, 0, 11, false); // from Inventory
    private static final Comparator<Pair<ResourceLocation, String>> SORTER = Comparator.comparing((Pair<ResourceLocation, String> o) -> o.getFirst()).thenComparing(Pair::getSecond);


    private final DataSlot selectedConceptArtIndex = DataSlot.standalone();
    private final ContainerLevelAccess access;

    private final Container conceptContainer = new SimpleContainer(9) {
        @Override
        public void setChanged() {
            super.setChanged();
            StylingMenu.this.updateConceptArt();
            StylingMenu.this.slotsChanged(this);
        }
    };
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
    private final List<Pair<ResourceLocation, String>> conceptArt;
    private final Map<ResourceLocation, Integer> conceptArtSlots;
    private boolean lock;

    public StylingMenu(int window, Inventory inventory) {
        this(window, inventory, ContainerLevelAccess.NULL, ConceptArtManager.get(true));
    }

    public StylingMenu(int window, Inventory inventory, ContainerLevelAccess containerLevelAccess, ConceptArtManager conceptArtManager) {
        super(VanityMenuTypes.STYLING_MENU.get(), window);
        this.access = containerLevelAccess;
        this.conceptArtManager = conceptArtManager;

        this.conceptArt = new LinkedList<>();
        this.conceptArtSlots = new Object2IntArrayMap<>(9);

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
                containerLevelAccess.execute((level, blockPos) -> {
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
                this.addSlot(new Slot(this.conceptContainer, k + j * 3, 116 + k * 18, 30 + j * 18) {

                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        ResourceLocation id = ConceptArtItem.getConceptArtId(stack);
                        return id != null;
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
        int old = this.conceptArt.hashCode();
        this.conceptArt.clear();
        this.conceptArtSlots.clear();

        ItemStack input = this.inputContainer.getItem(0);
        if (!input.isEmpty()) {
            ResourceLocation inputId = ConceptArtItem.getConceptArtId(input);
            String inputVariant = ConceptArtItem.getVariantName(input);
            List<Pair<ResourceLocation, String>> temp = new LinkedList<>();
            for (int i = 0; i < this.conceptContainer.getContainerSize(); i++) {
                ConceptArt conceptArt = this.conceptArtManager.getItemConceptArt(this.conceptContainer.getItem(i));
                if (conceptArt != null) {
                    Optional<ResourceLocation> idOptional = this.conceptArtManager.getConceptArtId(conceptArt);
                    if (idOptional.isEmpty()) {
                        continue;
                    }

                    ResourceLocation id = idOptional.get();
                    if (this.conceptArtSlots.containsKey(id)) {
                        continue;
                    }

                    this.conceptArtSlots.put(id, i);
                    temp.clear();
                    for (String name : conceptArt.variants().keySet()) {
                        ConceptArt.Variant variant = conceptArt.getVariantForItem(name, input);
                        if (variant != null && (!id.equals(inputId) || !name.equals(inputVariant))) {
                            temp.add(Pair.of(id, name));
                        }
                    }
                    temp.sort(SORTER);
                    this.conceptArt.addAll(temp);
                }
            }
        }

        if (old != this.conceptArt.hashCode()) {
            this.selectedConceptArtIndex.set(-1);
        }
    }

    public int getSelectedConceptArtIndex() {
        return this.selectedConceptArtIndex.get();
    }

    public List<Pair<ResourceLocation, String>> getConceptArt() {
        return conceptArt;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, VanityBlocks.STYLING_TABLE.get());
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id >= 0 && id < this.conceptArt.size()) {
            this.selectedConceptArtIndex.set(id);
            this.setupResultSlot();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void slotsChanged(Container container) {
        if (this.lock) {
            return;
        }

        this.lock = true;
        ItemStack inputStack = this.getInputItem();
        if (!this.resultSlot.getItem().isEmpty() && (inputStack.isEmpty() || this.conceptContainer.isEmpty())) {
            this.resultSlot.set(ItemStack.EMPTY);
            this.selectedConceptArtIndex.set(-1);
        }

        this.setupResultSlot();
        this.broadcastChanges();
        this.lock = false;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return MOVE_HELPER.quickMoveStack(this, player, index);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((arg2, arg3) -> {
            this.clearContainer(player, this.inputContainer);
            this.clearContainer(player, this.conceptContainer);
        });
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
        if (conceptArtIndex < 0 || conceptArtIndex >= this.conceptContainer.getContainerSize()) {
            return OptionalInt.empty();
        }

        return OptionalInt.of(conceptArtIndex);
    }

    private void setupResultSlot() {
        this.resultSlot.set(ItemStack.EMPTY);
        if (this.selectedConceptArtIndex.get() >= 0) {
            ItemStack inputStack = this.getInputItem();
            if (inputStack.isEmpty()) {
                return;
            }

            Pair<ResourceLocation, String> conceptArt = this.conceptArt.get(this.selectedConceptArtIndex.get());
            if (conceptArt == null) {
                return;
            }

            ResourceLocation conceptArtId = conceptArt.getFirst();
            String variant = conceptArt.getSecond();

            int conceptIndex = this.getConceptArtIndex().orElse(-1);
            if (conceptIndex < 0 || conceptIndex >= this.conceptContainer.getContainerSize() || this.conceptContainer.getItem(conceptIndex).isEmpty() || conceptArtId == null || variant == null) {
                return;
            }

            ItemStack outputStack = inputStack.copy();
            outputStack.setCount(1);
            ConceptArtItem.setItemConceptArtVariant(outputStack, conceptArtId, variant);
            this.resultSlot.set(outputStack);
        }
    }

    public ItemStack getInputItem() {
        return this.inputSlot.getItem();
    }
}
