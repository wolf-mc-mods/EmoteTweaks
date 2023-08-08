package dev.bsmp.emotetweaks.util.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.util.UUID;

public class EmotePropertiesImpl {
    public static Path getGameDir() {
        return FabricLoader.getInstance().getGameDir();
    }
}
