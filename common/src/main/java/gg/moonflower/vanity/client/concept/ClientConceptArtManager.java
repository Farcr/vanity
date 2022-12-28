package gg.moonflower.vanity.client.concept;

import gg.moonflower.vanity.api.concept.ConceptArt;
import gg.moonflower.vanity.api.concept.ConceptArtManager;
import gg.moonflower.vanity.common.network.common.message.ClientboundConceptArtSyncPacket;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ClientConceptArtManager extends ConceptArtManager {

    public static final ClientConceptArtManager INSTANCE = new ClientConceptArtManager();

    public static ModelResourceLocation getModelLocation(ResourceLocation location) {
        return new ModelResourceLocation(new ResourceLocation(location.getNamespace(), "vanity_concept_art/" + location.getPath()), "inventory");
    }

    public void readPacket(ClientboundConceptArtSyncPacket packet) {
        this.conceptArt.clear();
        this.conceptArt.putAll(packet.getConceptArt());

        LOGGER.info("Loaded " + this.conceptArt.size() + " concept art");
    }

    @Nullable
    public ModelResourceLocation getModel(ItemStack stack) {
        ConceptArt.Variant variant = this.getItemConceptArtVariant(stack);
        if (variant == null)
            return null;

        return ClientConceptArtManager.getModelLocation(variant.model());
    }

    @Nullable
    public ModelResourceLocation getHandModel(ItemStack stack) {
        ConceptArt.Variant variant = this.getItemConceptArtVariant(stack);
        if (variant == null || variant.handModel() == null)
            return null;

        return ClientConceptArtManager.getModelLocation(variant.handModel());
    }
}
