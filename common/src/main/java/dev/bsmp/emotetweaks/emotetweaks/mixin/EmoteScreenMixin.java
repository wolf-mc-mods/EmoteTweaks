package dev.bsmp.emotetweaks.emotetweaks.mixin;

import dev.bsmp.emotetweaks.emotetweaks.EmoteTweaksMain;
import dev.bsmp.emotetweaks.emotetweaks.IEmoteScreen;
import dev.bsmp.emotetweaks.emotetweaks.IMixedKey;
import dev.bsmp.emotetweaks.emotetweaks.client.ToggleButton;
import io.github.kosmx.emotes.arch.executor.types.TextImpl;
import io.github.kosmx.emotes.executor.EmoteInstance;
import io.github.kosmx.emotes.executor.dataTypes.InputKey;
import io.github.kosmx.emotes.main.screen.AbstractScreenLogic;
import io.github.kosmx.emotes.main.screen.EmoteMenu;
import io.github.kosmx.emotes.main.screen.IScreenSlave;
import io.github.kosmx.emotes.main.screen.widget.IEmoteListWidgetHelper;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EmoteMenu.class, remap = false)
public abstract class EmoteScreenMixin<MATRIX, SCREEN, WIDGET> extends AbstractScreenLogic<MATRIX, SCREEN> implements IEmoteScreen {
    @Shadow protected abstract boolean setKey(InputKey key);
    @Shadow private IEmoteListWidgetHelper<MATRIX, WIDGET> emoteList;

    private ToggleButton crouchToggleButton;
    private IEmoteListWidgetHelper.IEmoteEntry lastEntry;

    protected EmoteScreenMixin(IScreenSlave screen) {
        super(screen);
    }

    @Inject(method = "emotes_initScreen", at = @At(value = "INVOKE", target = "Lio/github/kosmx/emotes/main/screen/IScreenSlave;addButtonsToChildren()V"))
    private void addCrouchToggle(CallbackInfo ci) {
        crouchToggleButton = new ToggleButton((EmoteMenu) (Object) this, screen.getWidth() / 2 + 124, 95, 96, 20, false);
        screen.addToButtons(crouchToggleButton);
    }

    @Inject(method = "emotes_onKeyPressed", at = @At(value = "INVOKE", target = "Lio/github/kosmx/emotes/main/screen/EmoteMenu;setKey(Lio/github/kosmx/emotes/executor/dataTypes/InputKey;)Z", ordinal = 1), cancellable = true)
    private void init(int keyCode, int scanCode, int mod, CallbackInfoReturnable<Boolean> cir) {
        if(keyCode != 343 && (keyCode >= 340 && keyCode <= 346)) {
            cir.setReturnValue(false);
        } else {
            InputKey key = EmoteInstance.instance.getDefaults().getKeyFromCode(keyCode, scanCode);
            ((IMixedKey) key).setModifier(mod);
            cir.setReturnValue(this.setKey(key));
        }
    }

    @Inject(method = "emotes_renderScreen", at = @At("TAIL"))
    private void crouchButtonActive(MATRIX matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        IEmoteListWidgetHelper.IEmoteEntry selected = emoteList.getSelectedEntry();
        this.crouchToggleButton.setActive(selected != null);

        if(lastEntry != selected) {
            lastEntry = selected;
            if (selected != null) {
                boolean ignoreCrouch = EmoteTweaksMain.CROUCH_CANCEL_MAP.getBoolean(emoteList.getSelectedEntry().getEmote().getEmote().get());
                this.crouchToggleButton.setCurrentState(ignoreCrouch);
            }
        }

        textDraw(matrices, new TextImpl(Component.literal("Ignore Sneaking:").plainCopy()), screen.getWidth() / 2 + 8, 100, 0xFFFFFF);
    }

    @Inject(method = "saveConfig", at = @At("HEAD"))
    private void save(CallbackInfo ci) {

    }

    public IEmoteListWidgetHelper<MATRIX, WIDGET> getEmoteList() {
        return emoteList;
    }

}
