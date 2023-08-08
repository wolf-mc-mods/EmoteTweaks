package dev.bsmp.emotetweaks.emotetweaks.fabric;

import dev.bsmp.emotetweaks.emotetweaks.EmoteTweaksMain;
import net.fabricmc.api.ModInitializer;


public class EmoteTweaksFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        new EmoteTweaksMain();
    }

}

