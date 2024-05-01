package dev.bsmp.emotetweaks.voicefx;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.api.opus.OpusEncoder;
import de.maxhenkel.voicechat.plugins.PluginManager;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import de.maxhenkel.voicechat.voice.common.LocationSoundPacket;

public class SFXThread extends Thread {

    private final OpusEncoder encoder;
    private final UUID uuid;
    private boolean started;
    private short[] data;

    private SFXThread(UUID uuid, OpusEncoder encoder, short[] data) {
        this.uuid = uuid;
        this.encoder = encoder;
        this.data = data;

        this.setDaemon(true);
    }

    @Override
    public void run() {
        int framePosition = 0;
        long startTime = System.nanoTime();

        short[] frame;
        while ((frame = getFrameData(framePosition)) != null && !isInterrupted()) {
            if (frame.length != 960) {
                Voicechat.LOGGER.error("Got invalid audio frame size {}!={}", frame.length, 960);
                break;
            }

            //Send Data Packet
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeUUID(uuid);
            buf.writeByteArray(encoder.encode(frame));
            buf.writeLong(framePosition);
            ClientPlayNetworking.send(SFXPacket.PACKET_ID, buf);

            short[] finalFrame = frame;
            Minecraft.getInstance().executeIfPossible(() -> ClientManager.getClient().processSoundPacket(new LocationSoundPacket(uuid, finalFrame, Minecraft.getInstance().player.position(), 15f, null)));

            ++framePosition;
            long waitTimestamp = startTime + (long) framePosition * 20000000L;
            long waitNanos = waitTimestamp - System.nanoTime();

            try {
                if (waitNanos > 0L) {
                    Thread.sleep(waitNanos / 1000000L, (int) (waitNanos % 1000000L));
                }
            } catch (InterruptedException var10) {
                break;
            }
        }

        this.encoder.close();
    }

    private short[] getFrameData(int currentFrame) {
        int startIndex = currentFrame * (960);
        if(startIndex > data.length)
            return null;
        return Arrays.copyOfRange(this.data, startIndex, startIndex + 960);
    }

    public void startPlaying() {
        if (!this.started) {
            this.start();
            this.started = true;
        }
    }

    public static SFXThread playSFX(short[] data) throws UnsupportedAudioFileException, IOException {
        return new SFXThread(UUID.randomUUID(), SoundPlugin.voicechatApi.createEncoder(), data);
    }

}
