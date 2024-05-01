package dev.bsmp.emotetweaks.emotetweaks.mixin;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.bsmp.emotetweaks.emotetweaks.SoundFrame;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.data.gson.AnimationJson;

@Mixin(value = AnimationJson.class, remap = false)
public class AnimationJsonMixin {

    @Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Ljava/util/List;", at = @At(value = "RETURN"))
    private void soundEffectsSerializer(JsonElement json, Type typeOfT, JsonDeserializationContext context, CallbackInfoReturnable<List<KeyframeAnimation>> cir) {
        JsonObject node = json.getAsJsonObject();
        boolean geckoLib = !node.has("emote");
        for(KeyframeAnimation entry : cir.getReturnValue()) {
            JsonObject emote = geckoLib ? node.getAsJsonObject("animations").getAsJsonObject((String) entry.extraData.get("name")) : node.getAsJsonObject("emote");
            if(emote.has("sound_effects")) {
                List<SoundFrame> soundFrames = new ArrayList<>();
                JsonObject soundsNode = emote.getAsJsonObject("sound_effects");
                for (Map.Entry<String, JsonElement> e : soundsNode.entrySet()) {
                    int tick = Math.max(geckoLib ? (int) (Float.parseFloat(e.getKey()) * 20) : Integer.parseInt(e.getKey()), 1);
                    String effect = e.getValue().getAsJsonObject().get("effect").getAsString();
                    soundFrames.add(new SoundFrame(tick, effect));
                }
                entry.extraData.put("sound_effects", soundFrames);
            }
        }
    }

}
