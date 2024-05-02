package dev.bsmp.emotetweaks.emotetweaks.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import dev.bsmp.emotetweaks.voicefx.SoundPlugin;
import io.github.kosmx.emotes.api.events.client.ClientEmoteEvents;

@Environment(EnvType.CLIENT)
public class EmoteTweaksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEmoteEvents.EMOTE_PLAY.register((emoteData, userID) -> {
            if(userID == Minecraft.getInstance().player.getUUID()) {
                SoundPlugin.stopSounds();
            }
        });
    }
}
