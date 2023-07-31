package dev.bsmp.emotetweaks.emotetweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

import dev.bsmp.emotetweaks.emotetweaks.EmoteTweaks;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Pair;
import io.github.kosmx.emotes.server.network.AbstractServerEmotePlay;
import io.github.kosmx.emotes.server.network.EmotePlayTracker;
import io.github.kosmx.emotes.server.network.IServerNetworkInstance;

@Mixin(value = AbstractServerEmotePlay.class, remap = false)
public abstract class ServerEmotePlayMixin<P> {
    @Invoker("getPlayerNetworkInstance")
    public abstract IServerNetworkInstance invokeGetPlayerNetworkInstance(P player);

    @Inject(method = "playerEntersInvalidPose", at = @At(value = "INVOKE", target = "Lio/github/kosmx/emotes/server/network/AbstractServerEmotePlay;stopEmote(Ljava/lang/Object;Lio/github/kosmx/emotes/common/network/objects/NetData;)V"), cancellable = true)
    private void checkInvalid(P player, CallbackInfo ci) {
        Pair<KeyframeAnimation, Integer> anim = invokeGetPlayerNetworkInstance(player).getEmoteTracker().getPlayedEmote();
        if(anim != null) {
            boolean b = anim.getLeft().extraData.containsKey("crouchCancel") && (boolean) anim.getLeft().extraData.get("crouchCancel");
            if (!b) {
                ci.cancel();
            }
        }
    }
}
