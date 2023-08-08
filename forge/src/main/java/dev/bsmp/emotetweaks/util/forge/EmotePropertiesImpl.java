package dev.bsmp.emotetweaks.util.forge;

import net.minecraftforge.fml.loading.FMLLoader;

import java.nio.file.Path;

public class EmotePropertiesImpl {
    public static Path getGameDir() {
        return FMLLoader.getGamePath();
    }
}
