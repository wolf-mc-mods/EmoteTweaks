package dev.bsmp.emotetweaks.emotetweaks.client;

import dev.bsmp.emotetweaks.emotetweaks.EmoteTweaksMain;
import dev.bsmp.emotetweaks.emotetweaks.IEmoteScreen;
import io.github.kosmx.emotes.arch.gui.screen.IButtonImpl;
import io.github.kosmx.emotes.main.screen.EmoteMenu;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public class ToggleButton extends IButtonImpl {
    public EmoteMenu parent;
    private boolean currentState = false;

    public ToggleButton(EmoteMenu parent, int x, int y, int width, int height, boolean state) {
        super(x, y, width, height, Component.literal("" + state), null);
        this.currentState = state;
        this.parent = parent;
    }

    @Override
    public void onPress() {
        setCurrentState(!currentState);
    }

    public void setCurrentState(boolean newState) {
        currentState = newState;
        parent.save = true;
        setMessage(Component.literal(""+currentState));
        UUID uuid = ((IEmoteScreen)parent).getEmoteList().getSelectedEntry().getEmote().getUuid();
        EmoteTweaksMain.CROUCH_CANCEL_MAP.put(uuid, currentState);
    }

    public boolean getCurrentState() {
        return currentState;
    }
}
