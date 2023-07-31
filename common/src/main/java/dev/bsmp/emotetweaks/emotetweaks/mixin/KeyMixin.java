package dev.bsmp.emotetweaks.emotetweaks.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import net.minecraft.network.chat.TextComponent;
import com.mojang.blaze3d.platform.InputConstants;
import dev.bsmp.emotetweaks.emotetweaks.EmoteTweaks;
import dev.bsmp.emotetweaks.emotetweaks.IMixedKey;
import io.github.kosmx.emotes.arch.executor.types.Key;
import io.github.kosmx.emotes.arch.executor.types.TextImpl;
import io.github.kosmx.emotes.executor.dataTypes.InputKey;
import io.github.kosmx.emotes.executor.dataTypes.Text;

@Mixin(value = Key.class, remap = false)
public abstract class KeyMixin implements IMixedKey {
    @Shadow @Final private InputConstants.Key storedKey;
    private int modifier;

    @Override
    public int getModifier() {
        return modifier;
    }

    @Override
    public void setModifier(int newMod) {
        modifier = newMod;
    }

    @Inject(method = "equals(Lio/github/kosmx/emotes/executor/dataTypes/InputKey;)Z", at = @At("RETURN"), cancellable = true)
    private void checkEquals(InputKey key, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && ((KeyMixin)key).modifier == modifier);
    }

    @Inject(method = "getLocalizedText", at = @At("RETURN"), cancellable = true)
    private void getText(CallbackInfoReturnable<Text> cir) {
        if(modifier != 0)
            cir.setReturnValue(new TextImpl(new TextComponent(EmoteTweaks.MODIFIERS.get(modifier) + " + ").plainCopy()).append(cir.getReturnValue()));
    }

    @Inject(method = "getTranslationKey", at = @At("RETURN"), cancellable = true)
    private void translationKey(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(storedKey.getName() + ";" + modifier);
    }

    @Inject(method = "hashCode", at = @At("RETURN"), cancellable = true)
    private void hash(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Objects.hash(cir.getReturnValue(), modifier));
    }

}
