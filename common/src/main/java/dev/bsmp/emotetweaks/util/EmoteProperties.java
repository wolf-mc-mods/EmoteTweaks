package dev.bsmp.emotetweaks.util;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;
import java.util.UUID;

public class EmoteProperties {

    @ExpectPlatform
    public static Path getGameDir() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void dataToServer(UUID uuid, byte[] frame, long sequenceNumber) {
        throw new AssertionError();
    }
}
