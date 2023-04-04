package gg.moonflower.vanity.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import gg.moonflower.vanity.common.menu.StylingMenu;
import gg.moonflower.vanity.core.Vanity;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class VanityMenuTypes {

    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Vanity.MOD_ID, Registry.MENU_REGISTRY);

    public static final Supplier<MenuType<StylingMenu>> STYLING_MENU = REGISTRY.register("styling_menu", () -> new MenuType<>(StylingMenu::new));
}
