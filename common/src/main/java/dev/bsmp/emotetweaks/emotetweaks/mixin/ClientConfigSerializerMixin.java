package dev.bsmp.emotetweaks.emotetweaks.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

import dev.bsmp.emotetweaks.emotetweaks.EmoteTweaks;
import dev.kosmx.playerAnim.core.util.Pair;
import io.github.kosmx.emotes.common.SerializableConfig;
import io.github.kosmx.emotes.executor.dataTypes.InputKey;
import io.github.kosmx.emotes.main.config.ClientConfig;
import io.github.kosmx.emotes.main.config.ClientConfigSerializer;

@Mixin(value = ClientConfigSerializer.class, remap = false)
public abstract class ClientConfigSerializerMixin {

    @Inject(method = "clientDeserialize", at = @At("TAIL"))
    private void deserializeCrouchCancels(JsonObject node, SerializableConfig sconfig, CallbackInfo ci) {
        if(node.has("crouchCancel")) crouchCancelDeserializer(node.get("crouchCancel"));
    }

    private void crouchCancelDeserializer(JsonElement node) {
        for(Map.Entry<String, JsonElement> element : node.getAsJsonObject().entrySet()) {
            EmoteTweaks.CROUCH_CANCEL_MAP.put(UUID.fromString(element.getKey()), element.getValue().getAsBoolean());
        }
    }

    @Inject(method = "clientSerialize", at = @At("TAIL"))
    private void serializeCrouchCancels(ClientConfig config, JsonObject node, CallbackInfo ci) {
        node.add("crouchCancel", crouchCancelSerializer());
    }

    private JsonElement crouchCancelSerializer() {
        JsonObject array = new JsonObject();
        for(Map.Entry<UUID, Boolean> entry : EmoteTweaks.CROUCH_CANCEL_MAP.object2BooleanEntrySet()){
            array.addProperty(entry.getKey().toString(), entry.getValue());
        }
        return array;
    }

}
