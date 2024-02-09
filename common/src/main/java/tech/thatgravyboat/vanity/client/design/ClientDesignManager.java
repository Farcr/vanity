package tech.thatgravyboat.vanity.client.design;

import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.vanity.api.style.AssetType;
import tech.thatgravyboat.vanity.api.style.Style;
import tech.thatgravyboat.vanity.common.handler.design.DesignManagerImpl;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundSyncDesignsPacket;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ClientDesignManager extends DesignManagerImpl {

    public static final ClientDesignManager INSTANCE = new ClientDesignManager();
    private static final Map<ResourceLocation, ModelResourceLocation> MODEL_LOCATION_CACHE = new HashMap<>();

    public static ModelResourceLocation getModelLocation(ResourceLocation location) {
        return MODEL_LOCATION_CACHE.computeIfAbsent(location, loc -> new ModelResourceLocation(location, "inventory"));
    }

    public void readPacket(ClientboundSyncDesignsPacket packet) {
        ClientDesignManager.MODEL_LOCATION_CACHE.clear();
        this.clear();
        this.designs.putAll(packet.designs());
        this.setupDefaults();
    }

    @Nullable
    public ResourceLocation getTexture(ItemStack stack, AssetType type) {
        Style style = this.getStyleFromItem(stack);
        if (style == null) return null;
        return style.asset(type);
    }

    @Nullable
    public ModelResourceLocation getModel(ItemStack stack, AssetType type, AssetType... additionalTypes) {
        Style style = this.getStyleFromItem(stack);
        if (style == null) return null;
        ResourceLocation model = style.asset(type);
        if (model == null) {
            for (AssetType additionalType : additionalTypes) {
                model = style.asset(additionalType);
                if (model != null) break;
            }
        }
        if (model == null) {
            model = style.model();
        }
        return ClientDesignManager.getModelLocation(model);
    }
}
