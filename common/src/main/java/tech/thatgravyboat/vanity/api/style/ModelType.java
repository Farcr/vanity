package tech.thatgravyboat.vanity.api.style;

public enum ModelType {
    DEFAULT,
    HAND,
    PROJECTILE;

    private final String id;

    ModelType() {
        this.id = this.name().toLowerCase();
    }

    public String id() {
        return this.id;
    }
}
