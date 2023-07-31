package dev.bsmp.emotetweaks.emotetweaks;

import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;

import java.util.HashMap;
import java.util.UUID;

public class EmoteTweaks {
    public static final String MOD_ID = "emotetweaks";

    public static HashMap<Integer, String> MODIFIERS = new HashMap<>();
    public static Object2BooleanOpenHashMap<UUID> CROUCH_CANCEL_MAP = new Object2BooleanOpenHashMap<>();

    public EmoteTweaks() {
        MODIFIERS.put(1, "SHIFT");
        MODIFIERS.put(2, "CTRL");
        MODIFIERS.put(4, "L ALT");
        MODIFIERS.put(6, "R ALT");

    }

}
