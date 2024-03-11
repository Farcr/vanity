package tech.thatgravyboat.vanity.mixins.common;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.CommonListenerCookie;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import tech.thatgravyboat.vanity.common.handler.design.ServerDesignManager;
import tech.thatgravyboat.vanity.common.network.NetworkHandler;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundSyncConfigPacket;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Shadow @Final private MinecraftServer server;

    @Inject(
            method = "placeNewPlayer",
            at = @At(
                    target = "Lnet/minecraft/server/MinecraftServer;getRecipeManager()Lnet/minecraft/world/item/crafting/RecipeManager;",
                    value = "INVOKE",
                    shift = At.Shift.BEFORE
            )
    )
    private void vanity$onBeforeSendRecipes(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        NetworkHandler.CHANNEL.sendToPlayer(ServerDesignManager.INSTANCE.createPacket(), player);
    }

    @Inject(
        method = "placeNewPlayer",
        at = @At(
            target = "Lnet/minecraft/server/players/PlayerList;sendPlayerPermissionLevel(Lnet/minecraft/server/level/ServerPlayer;)V",
            value = "INVOKE",
            shift = At.Shift.AFTER
        )
    )
    private void vanity$onPlayerConnect(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        NetworkHandler.CHANNEL.sendToPlayer(new ClientboundSyncConfigPacket(this.server), player);
    }
}
