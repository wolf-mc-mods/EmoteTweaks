package dev.bsmp.emotetweaks.emotetweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashMap;

import io.github.kosmx.emotes.common.network.objects.NetData;

@Mixin(value = NetData.class, remap = false)
public interface NetDataAccessor {
    @Accessor
    HashMap<String, Object> getExtraData();
}
