package tech.thatgravyboat.vanity.api.style;

public enum AssetType {
    DEFAULT,
    HAND,
    PROJECTILE,
    ARMOR
    ;

    private final String id;

    AssetType() {
        this.id = this.name().toLowerCase();
    }

    public String id() {
        return this.id;
    }
}
