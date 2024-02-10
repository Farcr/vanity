package tech.thatgravyboat.vanity.common.compat.jei.categories;

import com.google.common.collect.Streams;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.item.DesignHelper;
import tech.thatgravyboat.vanity.common.registries.ModItems;
import tech.thatgravyboat.vanity.common.util.ComponentConstants;

import java.util.List;

public class DesignCategory implements IRecipeCategory<DesignCategoryRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(Vanity.MOD_ID, "design");
    public static final RecipeType<DesignCategoryRecipe> TYPE = new RecipeType<>(ID, DesignCategoryRecipe.class);

    private static final ResourceLocation TEXTURE = new ResourceLocation(Vanity.MOD_ID, "textures/gui/container/jei.png");

    private final IGuiHelper helper;
    private final IDrawable background;

    public DesignCategory(IGuiHelper helper) {
        this.helper = helper;
        this.background = helper.drawableBuilder(TEXTURE, 0, 0, 120, 18)
                .addPadding(5, 5, 5, 5)
                .setTextureSize(120, 18)
                .build();
    }

    @Override
    public @NotNull RecipeType<DesignCategoryRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return ComponentConstants.JEI_TITLE;
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.helper.createDrawableItemStack(ModItems.DESIGN.get().getDefaultInstance());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DesignCategoryRecipe recipe, IFocusGroup focuses) {
        if (Minecraft.getInstance().getConnection() == null) return;
        RegistryAccess access = Minecraft.getInstance().getConnection().registryAccess();
        Registry<Item> registry = access.registry(Registries.ITEM).orElse(null);
        if (registry == null) return;

        ItemStack designItem = DesignHelper.createDesignItem(recipe.id());

        builder.addSlot(RecipeIngredientRole.INPUT, 6, 6)
                .setSlotName("design")
                .addItemStack(designItem)
                .addTooltipCallback((view, tooltips) -> {
                    if (!recipe.alwaysAvailable()) return;
                    tooltips.add(CommonComponents.EMPTY);
                    tooltips.add(ComponentConstants.JEI_ALWAYS_AVAILABLE);
                });

        List<ItemStack> items = recipe.style().item().map(
                tag -> Streams.stream(registry.getTagOrEmpty(tag))
                        .filter(Holder::isBound)
                        .map(Holder::value)
                        .map(ItemStack::new)
                        .toList(),
                item -> List.of(new ItemStack(item))
        );

        builder.addSlot(RecipeIngredientRole.INPUT, 54, 6).addItemStacks(items);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 6)
                .addItemStacks(
                        items.stream().map(ItemStack::copy)
                                .peek(stack -> DesignHelper.setDesignAndStyle(stack, recipe.id(), recipe.styleId()))
                                .toList()
                );
    }
}
