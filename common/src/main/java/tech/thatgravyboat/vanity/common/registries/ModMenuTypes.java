package tech.thatgravyboat.vanity.common.registries;

import com.teamresourceful.resourcefullib.common.menu.MenuContentHelper;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import tech.thatgravyboat.vanity.common.Vanity;
import tech.thatgravyboat.vanity.common.menu.StorageMenu;
import tech.thatgravyboat.vanity.common.menu.StylingMenu;
import tech.thatgravyboat.vanity.common.menu.content.StorageMenuContent;
import tech.thatgravyboat.vanity.common.menu.content.StylingMenuContent;

import java.util.function.Supplier;

public class ModMenuTypes {

    public static final ResourcefulRegistry<MenuType<?>> MENUS = ResourcefulRegistries.create(BuiltInRegistries.MENU, Vanity.MOD_ID);

    public static final Supplier<MenuType<StylingMenu>> STYLING = MENUS.register(
            "styling_menu",
            () -> MenuContentHelper.create(StylingMenu::new, StylingMenuContent.SERIALIZER)
    );

    public static final Supplier<MenuType<StorageMenu>> STORAGE = MENUS.register(
            "storage",
            () -> MenuContentHelper.create(StorageMenu::new, StorageMenuContent.SERIALIZER)
    );
}
