package tech.thatgravyboat.vanity.api.style;

public sealed interface AssetType permits AssetTypes.InternalType {

    String id();
}
