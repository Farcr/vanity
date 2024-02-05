package tech.thatgravyboat.vanity.client.rendering;

import tech.thatgravyboat.vanity.api.style.ModelType;

public interface RenderingManager {

    /**
     * This determines if the baked model needs to be replaced in the rendering method.
     * This is because some rendering methods call getModel before which already replaces the model and
     * has more context like overrides of the model.
     */
    ThreadLocal<Boolean> RENDERING = ThreadLocal.withInitial(() -> false);

    /**
     * This determines if the rendering is being done in the GUI. This is needed as to allow the patch above to work
     * properly for inventory rendering.
     */
    ThreadLocal<Boolean> IS_IN_GUI = ThreadLocal.withInitial(() -> false);

    void vanity$setModelType(ModelType type);
}
