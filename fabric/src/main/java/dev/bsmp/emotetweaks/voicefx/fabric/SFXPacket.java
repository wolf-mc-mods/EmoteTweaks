package dev.bsmp.emotetweaks.voicefx.fabric;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.api.events.SoundPacketEvent;
import de.maxhenkel.voicechat.voice.common.LocationSoundPacket;
import de.maxhenkel.voicechat.voice.server.ServerWorldUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

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

    public static void handleMessage(MinecraftServer minecraftServer, ServerPlayer serverPlayer, ServerGamePacketListenerImpl serverGamePacketListener, FriendlyByteBuf PacketByteBuf, PacketSender packetSender) {
        UUID uuid = PacketByteBuf.readUUID();
        byte[] frame = PacketByteBuf.readByteArray();
        long sequenceNumber = PacketByteBuf.readLong();


            //ToDo: Check Distance and maybe add custom category for EmoteFX?
            LocationSoundPacket packet = new LocationSoundPacket(uuid, serverPlayer.position(), frame, sequenceNumber, 15f, null);
            Voicechat.SERVER.getServer().broadcast(
                    ServerWorldUtils.getPlayersInRange(serverPlayer.getLevel(), serverPlayer.position(), Voicechat.SERVER_CONFIG.voiceChatDistance.get(), p -> p != serverPlayer),
//                ServerWorldUtils.getPlayersInRange(serverPlayer.getWorld(), serverPlayer.getPos(), Voicechat.SERVER_CONFIG.voiceChatDistance.get(), p -> true),
                    packet,
                    null, null, null,
                    SoundPacketEvent.SOURCE_PROXIMITY
            );

    }

}
