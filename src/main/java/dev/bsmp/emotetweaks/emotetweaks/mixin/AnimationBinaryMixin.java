package dev.bsmp.emotetweaks.emotetweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import dev.kosmx.playerAnim.core.data.AnimationBinary;

@Mixin(value = AnimationBinary.class, remap = false)
public interface AnimationBinaryMixin {
    @Invoker("stringSize")
    public static int getStringSize(String s) {
        throw new AssertionError();
    }
}
