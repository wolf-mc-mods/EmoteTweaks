package dev.bsmp.emotetweaks.emotetweaks.client;

import dev.bsmp.emotetweaks.voicefx.SoundPlugin;
import io.github.kosmx.emotes.api.events.client.ClientEmoteEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class EmoteTweaksClient {
    public static void onInitializeClient() {
        ClientEmoteEvents.EMOTE_PLAY.register((emoteData, userID) -> {
            if(userID == Minecraft.getInstance().player.getUUID())
                SoundPlugin.stopSounds();
        });
    }
}
