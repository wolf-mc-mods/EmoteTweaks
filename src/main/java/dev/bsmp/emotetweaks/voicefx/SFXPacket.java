package dev.bsmp.emotetweaks.voicefx;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import java.util.UUID;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.api.events.SoundPacketEvent;
import de.maxhenkel.voicechat.voice.common.LocationSoundPacket;
import de.maxhenkel.voicechat.voice.server.ServerWorldUtils;

public class SFXPacket {

    public static ResourceLocation PACKET_ID = new ResourceLocation("emotecraft", "packet_sfx");

    public static void receiveMessage(MinecraftServer minecraftServer, ServerPlayer serverPlayer, ServerGamePacketListenerImpl serverGamePacketListener, FriendlyByteBuf PacketByteBuf, PacketSender packetSender) {
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
