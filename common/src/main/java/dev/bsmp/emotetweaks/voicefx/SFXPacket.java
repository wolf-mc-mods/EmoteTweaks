package dev.bsmp.emotetweaks.voicefx;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.api.events.SoundPacketEvent;
import de.maxhenkel.voicechat.voice.common.LocationSoundPacket;
import de.maxhenkel.voicechat.voice.server.ServerWorldUtils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class SFXPacket {
    UUID uuid;
    byte[] frame;
    long sequenceNumber;

    public SFXPacket(){}
    public SFXPacket(UUID uuid, byte[] frame, long sequenceNumber) {
        this.uuid = uuid;
        this.frame = frame;
        this.sequenceNumber = sequenceNumber;
    }

    public static void encode(SFXPacket msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.uuid);
        buf.writeByteArray(msg.frame);
        buf.writeLong(msg.sequenceNumber);
    }

    public static SFXPacket decode(FriendlyByteBuf buf) {
        return new SFXPacket(buf.readUUID(), buf.readByteArray(), buf.readLong());
    }

    public static void handleMessage(FriendlyByteBuf msg, NetworkManager.PacketContext contextSupplier) {
        UUID uuid = msg.readUUID();
        byte[] frame = msg.readByteArray();
        long sequenceNumber = msg.readLong();
        ServerPlayer player = (ServerPlayer) contextSupplier.getPlayer();



        //ToDo: Check Distance and maybe add custom category for EmoteFX?
        LocationSoundPacket packet = new LocationSoundPacket(uuid, player.position(), frame, sequenceNumber, 15f, null);
        Voicechat.SERVER.getServer().broadcast(
                ServerWorldUtils.getPlayersInRange(player.getLevel(), player.position(), Voicechat.SERVER_CONFIG.voiceChatDistance.get(), p -> p != player),
//                ServerWorldUtils.getPlayersInRange(serverPlayer.getWorld(), serverPlayer.getPos(), Voicechat.SERVER_CONFIG.voiceChatDistance.get(), p -> true),
                packet,
                null, null, null,
                SoundPacketEvent.SOURCE_PROXIMITY
        );

    }

}