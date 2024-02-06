package tech.thatgravyboat.vanity.mixins.common;

import tech.thatgravyboat.vanity.common.handler.design.ServerDesignManager;
import tech.thatgravyboat.vanity.common.network.NetworkHandler;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Inject(
        method = "placeNewPlayer",
        at = @At(
            target = "Lnet/minecraft/server/players/PlayerList;sendPlayerPermissionLevel(Lnet/minecraft/server/level/ServerPlayer;)V",
            value = "INVOKE",
            shift = At.Shift.AFTER
        )
    )
    private void vanity$onPlayerConnect(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        NetworkHandler.CHANNEL.sendToPlayer(ServerDesignManager.INSTANCE.createPacket(), serverPlayer);
    }
}
