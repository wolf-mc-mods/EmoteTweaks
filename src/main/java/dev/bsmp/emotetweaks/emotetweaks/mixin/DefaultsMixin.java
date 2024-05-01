package dev.bsmp.emotetweaks.emotetweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.mojang.blaze3d.platform.InputConstants;
import dev.bsmp.emotetweaks.emotetweaks.IMixedKey;
import io.github.kosmx.emotes.arch.executor.Defaults;
import io.github.kosmx.emotes.arch.executor.types.Key;
import io.github.kosmx.emotes.executor.dataTypes.InputKey;

@Mixin(value = Defaults.class, remap = false)
public abstract class DefaultsMixin {

    @Inject(method = "getKeyFromString", at = @At("HEAD"), cancellable = true)
    private void getKey(String str, CallbackInfoReturnable<InputKey> cir) {
        if(str.contains(";")) {
            String[] split = str.split(";");
            Key key = new Key(InputConstants.getKey(split[0]));
            ((IMixedKey) key).setModifier(Integer.parseInt(split[1]));
            cir.setReturnValue(key);
        }
    }

}
