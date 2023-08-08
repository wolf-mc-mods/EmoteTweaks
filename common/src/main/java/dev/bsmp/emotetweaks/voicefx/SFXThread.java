package dev.bsmp.emotetweaks.voicefx;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.api.opus.OpusEncoder;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.common.LocationSoundPacket;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.Message;
import dev.architectury.networking.simple.SimpleNetworkManager;
import dev.bsmp.emotetweaks.util.EmoteProperties;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.UUID;

import static dev.bsmp.emotetweaks.emotetweaks.EmoteTweaksMain.PACKET_ID;

public class SFXThread extends Thread {

    private final OpusEncoder encoder;
    private final UUID uuid;
    private boolean started;
    private AudioInputStream stream;

    private SFXThread(UUID uuid, OpusEncoder encoder, AudioInputStream stream) {
        this.uuid = uuid;
        this.encoder = encoder;
        this.stream = stream;

        this.setDaemon(true);
    }

    @Override
    public void run() {
        int framePosition = 0;
        long startTime = System.nanoTime();

        try {
            byte[] bytes = new byte[1920];
            short[] frame;
            while ((stream.read(bytes) != -1) && !isInterrupted()) {
//        while ((frame = getFrameData(framePosition)) != null && !isInterrupted()) {
                frame = SoundPlugin.voicechatApi.getAudioConverter().bytesToShorts(bytes);
                bytes = new byte[1920];

                if (frame.length != 960) {
                    Voicechat.LOGGER.error("Got invalid audio frame size {}!={}", frame.length, 960);
                    break;
                }

                //Send Data Packet
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                buf.writeUUID(uuid);
                buf.writeByteArray(encoder.encode(frame));
                buf.writeLong(framePosition);

                NetworkManager.sendToServer(PACKET_ID, buf);
                System.out.println("Sent Packet");
                short[] finalFrame = frame;

                Minecraft.getInstance().execute(() -> ClientManager.getClient().processSoundPacket(new LocationSoundPacket(uuid, finalFrame, Minecraft.getInstance().player.position(), 15f, null)));

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
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.encoder.close();
    }

    public void startPlaying() {
        if (!this.started) {
            this.start();
            this.started = true;
        }
    }

    public static SFXThread playSFX(AudioInputStream stream) throws UnsupportedAudioFileException, IOException {
        return new SFXThread(UUID.randomUUID(), SoundPlugin.voicechatApi.createEncoder(), stream);
    }

}
