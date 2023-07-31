package dev.bsmp.emotetweaks.emotetweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.bsmp.emotetweaks.emotetweaks.EmoteTweaks;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import io.github.kosmx.emotes.main.network.ClientEmotePlay;

@Mixin(value = ClientEmotePlay.class, remap = false)
public abstract class ClientEmotePlayMixin {

    @Inject(method = "clientStartLocalEmote(Ldev/kosmx/playerAnim/core/data/KeyframeAnimation;)Z", at = @At("HEAD"))
    private static void attachData(KeyframeAnimation emote, CallbackInfoReturnable<Boolean> cir) {
        if(EmoteTweaks.CROUCH_CANCEL_MAP.containsKey(emote.get())) {
            boolean b = EmoteTweaks.CROUCH_CANCEL_MAP.getBoolean(emote.get());
            emote.extraData.put("crouchCancel", b);
        }
    }

}
