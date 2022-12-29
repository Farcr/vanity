package gg.moonflower.vanity.core.registry;

import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import gg.moonflower.vanity.common.menu.StylingMenu;
import gg.moonflower.vanity.core.Vanity;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class VanityMenuTypes {

    public static final PollinatedRegistry<MenuType<?>> REGISTRY = PollinatedRegistry.create(Registry.MENU, Vanity.MOD_ID);

    public static final Supplier<MenuType<StylingMenu>> STYLING_MENU = REGISTRY.register("styling_menu", () -> new MenuType<>(StylingMenu::new));
}
