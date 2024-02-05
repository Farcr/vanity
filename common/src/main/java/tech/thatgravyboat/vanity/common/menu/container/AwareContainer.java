package tech.thatgravyboat.vanity.common.menu.container;

import net.minecraft.world.SimpleContainer;

public class AwareContainer extends SimpleContainer {

    private final Runnable onChange;

    public AwareContainer(int size, Runnable onChange) {
        super(size);
        this.onChange = onChange;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.onChange.run();
    }
}
