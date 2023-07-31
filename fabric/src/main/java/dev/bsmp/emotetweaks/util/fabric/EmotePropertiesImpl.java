package dev.bsmp.emotetweaks.util.fabric;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;

import java.nio.file.Path;
import java.util.UUID;

import static dev.bsmp.emotetweaks.emotetweaks.EmoteTweaksFabric.PACKET_ID;

public class EmotePropertiesImpl {
    public static void dataToServer(UUID uuid, byte[] frame, long sequenceNumber) {
        FriendlyByteBuf buf = PacketByteBufs.create();

        buf.writeUUID(uuid);
        buf.writeByteArray(frame);
        buf.writeLong(sequenceNumber);
        ClientPlayNetworking.send(PACKET_ID, buf);
    }

    public static Path getGameDir() {
        return FabricLoader.getInstance().getGameDir();
    }
}
