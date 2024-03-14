package tech.thatgravyboat.vanity.common.menu.provider;

import com.teamresourceful.resourcefullib.common.menu.ContentMenuProvider;
import com.teamresourceful.resourcefullib.common.menu.MenuContent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.vanity.common.block.StylingTableBlockEntity;
import tech.thatgravyboat.vanity.common.util.ComponentConstants;

public abstract class BaseProvider<T extends MenuContent<T>> implements ContentMenuProvider<T> {

    protected final StylingTableBlockEntity entity;

    public BaseProvider(StylingTableBlockEntity entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentConstants.CONTAINER_TITLE;
    }

    @Override
    public boolean resetMouseOnOpen() {
        return false;
    }
}
