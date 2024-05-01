package dev.bsmp.emotetweaks.emotetweaks.mixin;

import io.github.kosmx.emotes.common.network.objects.NetData;
import java.util.HashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = { NetData.class }, remap = false)
public interface NetDataAccessor {
    @Accessor
    HashMap<String, Object> getExtraData();
}

