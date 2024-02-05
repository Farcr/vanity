package tech.thatgravyboat.vanity.common.menu;

import com.teamresourceful.resourcefullib.common.menu.MenuContent;
import com.teamresourceful.resourcefullib.common.menu.MenuContentSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record StylingMenuContent(
        List<ResourceLocation> unlockables
) implements MenuContent<StylingMenuContent> {

    public static final MenuContentSerializer<StylingMenuContent> SERIALIZER = new Serializer();

    @Override
    public MenuContentSerializer<StylingMenuContent> serializer() {
        return SERIALIZER;
    }

    private static class Serializer implements MenuContentSerializer<StylingMenuContent> {

        @Override
        public @Nullable StylingMenuContent from(FriendlyByteBuf buffer) {
            return new StylingMenuContent(buffer.readCollection(ArrayList::new, FriendlyByteBuf::readResourceLocation));
        }

        @Override
        public void to(FriendlyByteBuf buffer, StylingMenuContent content) {
            buffer.writeCollection(content.unlockables, FriendlyByteBuf::writeResourceLocation);
        }
    }
}
