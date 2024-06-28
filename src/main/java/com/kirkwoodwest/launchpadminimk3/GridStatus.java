package com.kirkwoodwest.launchpadminimk3;

import com.bitwig.extension.controller.api.ClipLauncherSlot;
import com.bitwig.extension.controller.api.CursorTrack;
import com.kirkwoodwest.launchpadminimk3.hardware.GridButton;
import com.kirkwoodwest.launchpadminimk3.hardware.GridButtonState;

public class GridStatus implements GridNote {
    private final GridButton gridButton;
    private final ClipLauncherSlot clipLauncherSlot;
    private final CursorTrack cursorTrack;
    private boolean noteOnStatus = false;

    public GridStatus(GridButton gridButton, ClipLauncherSlot clipLauncherSlot, CursorTrack cursorTrack) {
        this.gridButton = gridButton;
        this.clipLauncherSlot = clipLauncherSlot;
        this.cursorTrack = cursorTrack;

        clipLauncherSlot.isPlaying().addValueObserver((b)->setStatus());
        clipLauncherSlot.hasContent().addValueObserver((b)->setStatus());
        clipLauncherSlot.isRecording().addValueObserver((b)->setStatus());

        new GridButtonNote(cursorTrack, this);
    }


    @Override
    public void noteOn(boolean noteOn) {
        this.noteOnStatus = noteOn;
        setStatus();
    }

    private void setStatus() {
        if (clipLauncherSlot.isPlaying().get()) {
            if (noteOnStatus) {
                gridButton.setState(GridButtonState.ClipPlayingNote);
            } else {
                gridButton.setState(GridButtonState.ClipPlaying);
            }
        } else if (clipLauncherSlot.isRecording().get()) {
            gridButton.setState(GridButtonState.NoteRecording);
        } else if (clipLauncherSlot.hasContent().get()) {
            gridButton.setState(GridButtonState.HasClip);
        } else {
            gridButton.setState(GridButtonState.Off);
        }
    }
}
