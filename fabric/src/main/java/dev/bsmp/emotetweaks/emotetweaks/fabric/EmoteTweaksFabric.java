package dev.bsmp.emotetweaks.emotetweaks.fabric;

import dev.bsmp.emotetweaks.emotetweaks.client.EmoteTweaksClient;
import dev.bsmp.emotetweaks.voicefx.fabric.SFXPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;


public class EmoteTweaksFabric implements ModInitializer {
    public static ResourceLocation PACKET_ID = new ResourceLocation("emotecraft", "main");

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID, SFXPacket::handleMessage);
        EmoteTweaksClient.onInitializeClient();
    }

}

