package dev.bsmp.emotetweaks.emotetweaks.mixin;

import de.maxhenkel.voicechat.voice.client.SoundManager;
import dev.bsmp.emotetweaks.emotetweaks.SoundFrame;
import dev.bsmp.emotetweaks.util.EmoteProperties;
import dev.bsmp.emotetweaks.voicefx.SoundPlugin;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import io.github.kosmx.emotes.executor.EmoteInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(value = KeyframeAnimationPlayer.class, remap = false)
public class KeyframeAnimationPlayerMixin {
    @Shadow private boolean isRunning;
    @Shadow @Final private KeyframeAnimation data;

    @Shadow private int currentTick;
    private List<SoundFrame> soundFrames;
    private SoundFrame nextSound;
    private Map<String, short[]> audioFiles = new HashMap<>();

    @Inject(method = "<init>(Ldev/kosmx/playerAnim/core/data/KeyframeAnimation;I)V", at = @At("TAIL"))
    private void onConstruct(KeyframeAnimation emote, int t, CallbackInfo ci) {
        if(emote.extraData.containsKey("name")) {
            Path autoFile = EmoteProperties.getGameDir().resolve("emotes" + FileSystems.getDefault().getSeparator() + ((String) emote.extraData.get("name")).replace("\"", "") + ".wav");
            if (autoFile.toFile().exists()) {
                try {
                    AudioInputStream stream = loadAudioFile(autoFile);
                    if (stream != null)
                        SoundPlugin.playSound(stream);
                } catch (UnsupportedAudioFileException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //Temp Disabled
//        if(emote.extraData.containsKey("sound_effects")) {
//            soundFrames = new ArrayList<>((ArrayList<SoundFrame>) emote.extraData.get("sound_effects"));
//            soundFrames.sort(Comparator.comparingInt(o -> o.tick));
//            for(SoundFrame frame : soundFrames) {
//                if (!audioFiles.containsKey(frame.value)) {
//                    try {
//                        audioFiles.put(frame.value, loadAudioFile(frame.value));
//                    } catch (UnsupportedAudioFileException | IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
    }

    private AudioInputStream loadAudioFile(String name) throws UnsupportedAudioFileException, IOException {
        return loadAudioFile(EmoteProperties.getGameDir().resolve("emotes" + FileSystems.getDefault().getSeparator() + name));
    }

    private AudioInputStream loadAudioFile(Path path) throws UnsupportedAudioFileException, IOException {
        AudioInputStream input = AudioSystem.getAudioInputStream(path.toFile());
        AudioInputStream formattedInput = AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, SoundManager.SAMPLE_RATE, 16, 1, 2, SoundManager.SAMPLE_RATE, false), input);
        return formattedInput;
    }

//    @Inject(method = "tick", at = @At("HEAD"))
//    private void soundTick(CallbackInfo ci) {
//        if(this.isRunning && this.soundFrames != null) {
//            if(nextSound != null) {
//                if(nextSound.tick == this.currentTick) {
//                    if(this == EmoteInstance.instance.getClientMethods().getMainPlayer().getEmote())
//                        SoundPlugin.playSound(audioFiles.remove(nextSound.value));
//                }
//            }
//            else if(!soundFrames.isEmpty()) {
//                nextSound = soundFrames.remove(0);
//            }
//        }
//    }

    @Inject(method = "stop", at = @At("TAIL"))
    private void stopSounds(CallbackInfo ci) {
        if(this == EmoteInstance.instance.getClientMethods().getMainPlayer().getEmote())
            SoundPlugin.stopSounds();
    }

}
