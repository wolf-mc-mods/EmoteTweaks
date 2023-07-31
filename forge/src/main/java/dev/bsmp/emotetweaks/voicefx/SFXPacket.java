package dev.bsmp.emotetweaks.voicefx;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.api.events.SoundPacketEvent;
import de.maxhenkel.voicechat.voice.common.LocationSoundPacket;
import de.maxhenkel.voicechat.voice.server.ServerWorldUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

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

    public static void handleMessage(SFXPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        UUID uuid = msg.uuid;
        byte[] frame = msg.frame;
        long sequenceNumber = msg.sequenceNumber;
        ServerPlayer player = contextSupplier.get().getSender();


        contextSupplier.get().enqueueWork(() -> {
            //ToDo: Check Distance and maybe add custom category for EmoteFX?
            LocationSoundPacket packet = new LocationSoundPacket(uuid, player.position(), frame, sequenceNumber, 15f, null);
            Voicechat.SERVER.getServer().broadcast(
                    ServerWorldUtils.getPlayersInRange(player.getLevel(), player.position(), Voicechat.SERVER_CONFIG.voiceChatDistance.get(), p -> p != player),
//                ServerWorldUtils.getPlayersInRange(serverPlayer.getWorld(), serverPlayer.getPos(), Voicechat.SERVER_CONFIG.voiceChatDistance.get(), p -> true),
                    packet,
                    null, null, null,
                    SoundPacketEvent.SOURCE_PROXIMITY
            );
        });
        contextSupplier.get().setPacketHandled(true);
    }

}
