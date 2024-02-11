package tech.thatgravyboat.vanity.mixins.client.transforms;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.thatgravyboat.vanity.client.rendering.ItemTransformsExtension;

import java.lang.reflect.Type;

@Mixin(targets = "net.minecraft.client.renderer.block.model.ItemTransforms$Deserializer")
public class ItemTransformsDeserializerMixin {

    @Inject(
        method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/renderer/block/model/ItemTransforms;",
        at = @At("RETURN")
    )
    private void onDeserialize(JsonElement json, Type type, JsonDeserializationContext context, CallbackInfoReturnable<ItemTransforms> cir) {
        if (json instanceof JsonObject object) {
            ItemTransformsExtension extension = (ItemTransformsExtension) cir.getReturnValue();
            for (var entry : object.entrySet()) {
                String name = entry.getKey();
                if (!name.startsWith("vanity:")) continue;
                extension.vanity$putTransform(name, context.deserialize(entry.getValue(), ItemTransform.class));
            }
        }
    }
}
