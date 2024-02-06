package tech.thatgravyboat.vanity.api.design;

import tech.thatgravyboat.vanity.api.style.Style;
import tech.thatgravyboat.vanity.client.design.ClientDesignManager;
import tech.thatgravyboat.vanity.common.handler.design.ServerDesignManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public interface DesignManager {

    /**
     * Retrieves a sided design manager for the specified side.
     *
     * @param client Whether the client sided design manager should be returned
     * @return The sided design manager
     */
    static DesignManager get(boolean client) {
        return client ? DesignManager.client() : DesignManager.server();
    }

    static DesignManager client() {
        return ClientDesignManager.INSTANCE;
    }

    static DesignManager server() {
        return ServerDesignManager.INSTANCE;
    }

    /**
     * Retrieves a design by the specified id.
     *
     * @param location The id of the design to retrieve
     * @return An optional of the design
     */
    Optional<Design> getDesign(ResourceLocation location);

    /**
     * @return A map of all designs by id
     */
    Map<ResourceLocation, Design> getAllDesigns();

    /**
     * @return A map of all designs by id
     */
    Map<ResourceLocation, Design> getDefaultDesigns();

    /**
     * Retrieves the design applied to an item.
     *
     * @param stack The item stack to get the design from
     * @return The applied design, null if there is none
     */
    @Nullable
    Design getDesignFromItem(ItemStack stack);

    /**
     * Retrieves the design style applied to an item.
     *
     * @param stack The item stack to get the design from
     * @return The design style, null if there is none
     */
    @Nullable
    Style getStyleFromItem(ItemStack stack);
}
