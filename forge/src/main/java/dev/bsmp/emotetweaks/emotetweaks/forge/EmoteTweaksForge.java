package dev.bsmp.emotetweaks.emotetweaks.forge;

import dev.bsmp.emotetweaks.emotetweaks.EmoteTweaksMain;
import dev.bsmp.emotetweaks.emotetweaks.client.EmoteTweaksClient;
import dev.bsmp.emotetweaks.voicefx.forge.SFXPacket;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(EmoteTweaksMain.MOD_ID)
public class EmoteTweaksForge {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("emotetweaks", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public EmoteTweaksForge() {
        EventBuses.registerModEventBus(EmoteTweaksMain.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        EmoteTweaksClient.onInitializeClient();
        NETWORK.registerMessage(0, SFXPacket.class, SFXPacket::encode, SFXPacket::decode, SFXPacket::handleMessage);
    }

}

