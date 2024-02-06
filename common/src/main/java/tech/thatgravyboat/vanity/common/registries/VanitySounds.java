package tech.thatgravyboat.vanity.common.registries;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import tech.thatgravyboat.vanity.common.Vanity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class VanitySounds {

    public static final ResourcefulRegistry<SoundEvent> SOUNDS = ResourcefulRegistries.create(BuiltInRegistries.SOUND_EVENT, Vanity.MOD_ID);

    public static final Supplier<SoundEvent> TAKE_RESULT_STYLING_TABLE = SOUNDS.register("ui.styling_table.take_result", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Vanity.MOD_ID, "ui.styling_table.take_result")));
    public static final Supplier<SoundEvent> OPEN_DESIGN = SOUNDS.register("item.design.open", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Vanity.MOD_ID, "item.design.open")));
}
