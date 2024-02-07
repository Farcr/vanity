package tech.thatgravyboat.vanity.common.compat.jei.categories;

import net.minecraft.resources.ResourceLocation;
import tech.thatgravyboat.vanity.api.design.Design;
import tech.thatgravyboat.vanity.api.style.Style;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

public record DesignCategoryRecipe(
    ResourceLocation id,
    String styleId,
    Style style,
    boolean alwaysAvailable
) {
    public static Stream<DesignCategoryRecipe> fromDesign(Map.Entry<ResourceLocation, Design> entry) {
        Design design = entry.getValue();
        ResourceLocation id = entry.getKey();
        if (!design.type().hasItem() && !design.type().isDefault()) {
            return Stream.empty();
        }
        return design.styles().entrySet().stream().flatMap(value -> {
            String styleId = value.getKey();
            Collection<Style> styles = value.getValue();
            return styles.stream().map(style -> new DesignCategoryRecipe(id, styleId, style, design.type().isDefault()));
        });
    }
}
