package gg.moonflower.vanity.core.registry;

import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import gg.moonflower.vanity.core.Vanity;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class VanitySounds {

    public static final PollinatedRegistry<SoundEvent> REGISTRY = PollinatedRegistry.create(Registry.SOUND_EVENT, Vanity.MOD_ID);

    public static final Supplier<SoundEvent> UI_STYLING_TABLE_TAKE_RESULT = REGISTRY.register("ui.styling_table.take_result", () -> new SoundEvent(new ResourceLocation(Vanity.MOD_ID, "ui.styling_table.take_result")));
}
