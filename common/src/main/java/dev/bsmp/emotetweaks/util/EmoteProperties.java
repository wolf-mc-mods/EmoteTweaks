package dev.bsmp.emotetweaks.util;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

public class EmoteProperties {

    @ExpectPlatform
    public static Path getGameDir() {
        throw new AssertionError();
    }
}
