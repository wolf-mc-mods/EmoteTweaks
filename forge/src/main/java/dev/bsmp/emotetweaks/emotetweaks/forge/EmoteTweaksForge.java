package dev.bsmp.emotetweaks.emotetweaks.forge;

import dev.bsmp.emotetweaks.emotetweaks.EmoteTweaksMain;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EmoteTweaksMain.MOD_ID)
public class EmoteTweaksForge {

    public EmoteTweaksForge() {
        EventBuses.registerModEventBus(EmoteTweaksMain.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        new EmoteTweaksMain();
    }

}

