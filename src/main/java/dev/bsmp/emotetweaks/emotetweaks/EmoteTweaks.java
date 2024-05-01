package dev.bsmp.emotetweaks.emotetweaks;

import dev.bsmp.emotetweaks.voicefx.SFXPacket;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.util.HashMap;
import java.util.UUID;

public class EmoteTweaks implements ModInitializer {
    public static HashMap<Integer, String> MODIFIERS = new HashMap<>();
    public static Object2BooleanOpenHashMap<UUID> CROUCH_CANCEL_MAP = new Object2BooleanOpenHashMap<>();

    @Override
    public void onInitialize() {
        MODIFIERS.put(1, "SHIFT");
        MODIFIERS.put(2, "CTRL");
        MODIFIERS.put(4, "L ALT");
        MODIFIERS.put(6, "R ALT");

        ServerPlayNetworking.registerGlobalReceiver(SFXPacket.PACKET_ID, SFXPacket::receiveMessage);
    }

}
