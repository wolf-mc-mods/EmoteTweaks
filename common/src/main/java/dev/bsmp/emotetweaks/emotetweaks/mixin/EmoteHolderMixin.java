package dev.bsmp.emotetweaks.emotetweaks.mixin;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.mojang.blaze3d.platform.InputConstants;
import dev.bsmp.emotetweaks.emotetweaks.EmoteTweaksMain;
import dev.bsmp.emotetweaks.emotetweaks.IMixedKey;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.MathHelper;
import dev.kosmx.playerAnim.core.util.Vec3d;
import io.github.kosmx.emotes.executor.EmoteInstance;
import io.github.kosmx.emotes.executor.dataTypes.InputKey;
import io.github.kosmx.emotes.executor.emotePlayer.IEmotePlayerEntity;
import io.github.kosmx.emotes.main.EmoteHolder;
import io.github.kosmx.emotes.main.config.ClientConfig;
import io.github.kosmx.emotes.main.network.ClientEmotePlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

@Mixin(value = EmoteHolder.class, remap = false)
public abstract class EmoteHolderMixin {

    @Inject(method = "playEmote(Ldev/kosmx/playerAnim/core/data/KeyframeAnimation;Lio/github/kosmx/emotes/executor/emotePlayer/IEmotePlayerEntity;Lio/github/kosmx/emotes/main/EmoteHolder;)Z", at = @At("HEAD"), cancellable = true)
    private static void attachData(KeyframeAnimation emote, IEmotePlayerEntity player, EmoteHolder emoteHolder, CallbackInfoReturnable<Boolean> cir) {
        if(EmoteTweaksMain.CROUCH_CANCEL_MAP.containsKey(emote.get())) {
            boolean b = EmoteTweaksMain.CROUCH_CANCEL_MAP.getBoolean(emote.get());
            if(b)
                cir.setReturnValue(ClientEmotePlay.clientStartLocalEmote(emote));
        }
    }

    @Inject(method = "canRunEmote", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private static void doubleCheckCrouch(IEmotePlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if(player.getEmote() != null) {
            KeyframeAnimation anim = player.getEmote().getData();
            if (anim.extraData.containsKey("crouchCancel") && ((boolean) anim.extraData.get("crouchCancel"))) {
                Vec3d prevPos2 = player.getPrevPos();
                cir.setReturnValue(!(player.emotesGetPos().distanceTo(new Vec3d(prevPos2.getX(), MathHelper.lerp(((ClientConfig) EmoteInstance.config).yRatio.get(), prevPos2.getY(), player.emotesGetPos().getY()), prevPos2.getZ())) > ((ClientConfig) EmoteInstance.config).stopThreshold.get()));
            }
        }
    }

    @Inject(method = "handleKeyPress", at = @At("HEAD"))
    private static void handleKey(InputKey key, CallbackInfo ci) {
        if(Screen.hasControlDown())
            ((IMixedKey) key).setModifier(2);
        else if(Screen.hasShiftDown())
            ((IMixedKey) key).setModifier(1);
        else if(InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_ALT))
            ((IMixedKey) key).setModifier(4);
        else if(InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_ALT))
            ((IMixedKey) key).setModifier(6);
    }

}
