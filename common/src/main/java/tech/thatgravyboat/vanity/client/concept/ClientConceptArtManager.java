package tech.thatgravyboat.vanity.client.concept;

import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.vanity.api.style.AssetType;
import tech.thatgravyboat.vanity.api.style.Style;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundConceptArtSyncPacket;
import tech.thatgravyboat.vanity.common.handler.concept.ConceptArtManagerImpl;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ClientConceptArtManager extends ConceptArtManagerImpl {

    public static final ClientConceptArtManager INSTANCE = new ClientConceptArtManager();
    private static final Map<ResourceLocation, ModelResourceLocation> MODEL_LOCATION_CACHE = new HashMap<>();

    public static ModelResourceLocation getModelLocation(ResourceLocation location) {
        return MODEL_LOCATION_CACHE.computeIfAbsent(location, loc -> new ModelResourceLocation(new ResourceLocation(location.getNamespace(), "vanity_concept_art/" + location.getPath()), "inventory"));
    }

    public void readPacket(ClientboundConceptArtSyncPacket packet) {
        ClientConceptArtManager.MODEL_LOCATION_CACHE.clear();
        this.clear();
        this.conceptArt.putAll(packet.conceptArt());
        this.setupDefaults();
    }

    @Nullable
    public ResourceLocation getTexture(ItemStack stack, AssetType type) {
        Style style = this.getItemConceptArtVariant(stack);
        if (style == null) return null;
        return style.asset(type);
    }

    @Nullable
    public ModelResourceLocation getModel(ItemStack stack, AssetType type, AssetType... additionalTypes) {
        Style style = this.getItemConceptArtVariant(stack);
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
        return ClientConceptArtManager.getModelLocation(model);
    }
}
