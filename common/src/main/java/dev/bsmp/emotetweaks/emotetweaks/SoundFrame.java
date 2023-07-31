package dev.bsmp.emotetweaks.emotetweaks;

import dev.bsmp.emotetweaks.emotetweaks.mixin.AnimationBinaryMixin;

public class SoundFrame {
    public final int tick;
    public final String value;

    public SoundFrame(int tick, String value) {
        this.tick = tick;
        this.value = value;
    }

    public int calculateSize() {
        return 4 + AnimationBinaryMixin.getStringSize(value);
    }

    @Override
    public String toString() {
        return "SoundFrame{" + "tick=" + tick + ", value=" + value + "}";
    }
}
