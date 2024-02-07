package tech.thatgravyboat.vanity.mixins.common;

import tech.thatgravyboat.vanity.common.util.EntityItemHolder;
import tech.thatgravyboat.vanity.common.network.packets.client.ClientboundSyncEntityItemPacket;
import tech.thatgravyboat.vanity.common.network.NetworkHandler;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {

    @Shadow @Final private Entity entity;

    @Inject(
        method = "addPairing",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V",
            shift = At.Shift.AFTER
        )
    )
    private void vantity$addPairing(ServerPlayer serverPlayer, CallbackInfo ci) {
        if (this.entity instanceof EntityItemHolder holder) {
            ClientboundSyncEntityItemPacket packet = new ClientboundSyncEntityItemPacket(this.entity.getId(), holder.vanity$getItem());
            NetworkHandler.CHANNEL.sendToPlayer(packet, serverPlayer);
        }
    }
}
