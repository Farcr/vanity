package gg.moonflower.vanity.core.datagen;

import gg.moonflower.pollen.api.datagen.provider.PollinatedLanguageProvider;
import gg.moonflower.pollen.api.util.PollinatedModContainer;
import gg.moonflower.vanity.core.Vanity;
import gg.moonflower.vanity.core.registry.VanityBlocks;
import gg.moonflower.vanity.core.registry.VanityItems;
import net.minecraft.data.DataGenerator;

public class VanityLanguageGenerator extends PollinatedLanguageProvider {
    public VanityLanguageGenerator(DataGenerator generator, PollinatedModContainer container) {
        super(generator, container, "en_us");
    }


    @Override
    protected void registerTranslations() {
        this.addItem(VanityItems.CONCEPT_ART, "Concept Art");
        this.addBlock(VanityBlocks.STYLING_TABLE, "Styling Table");
        this.add("container." + Vanity.MOD_ID + ".styling_table", "Styling Table");
        this.add("screen." + Vanity.MOD_ID + ".styling_table.original", "Remove Style");
        this.add("subtitles.ui." + Vanity.MOD_ID + ".styling_table.take_result", "Styling Table used");
        this.add("entity.minecraft.villager.stylist", "Stylist");
        this.add("entity.minecraft.villager.vanity.stylist", "Stylist"); // Forge lang key
    }
}
