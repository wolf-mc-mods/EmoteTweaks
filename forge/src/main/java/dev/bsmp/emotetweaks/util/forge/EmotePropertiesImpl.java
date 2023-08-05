package dev.bsmp.emotetweaks.util.forge;

import dev.bsmp.emotetweaks.emotetweaks.forge.EmoteTweaksForge;
import dev.bsmp.emotetweaks.voicefx.forge.SFXPacket;
import net.minecraftforge.fml.loading.FMLLoader;

import java.nio.file.Path;
import java.util.UUID;

public class EmotePropertiesImpl {
    public static void dataToServer(UUID uuid, byte[] frame, long sequenceNumber) {
        EmoteTweaksForge.NETWORK.sendToServer(new SFXPacket(uuid, frame, sequenceNumber));
    }

    public static Path getGameDir() {
        return FMLLoader.getGamePath();
    }
}
