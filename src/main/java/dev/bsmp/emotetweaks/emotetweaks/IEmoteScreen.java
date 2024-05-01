package dev.bsmp.emotetweaks.emotetweaks;

import io.github.kosmx.emotes.main.screen.widget.IEmoteListWidgetHelper;

public interface IEmoteScreen<MATRIX, WIDGET> {
    IEmoteListWidgetHelper<MATRIX, WIDGET> getEmoteList();
}
