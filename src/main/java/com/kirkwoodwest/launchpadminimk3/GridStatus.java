package com.kirkwoodwest.launchpadminimk3;

import com.bitwig.extension.controller.api.ClipLauncherSlot;
import com.bitwig.extension.controller.api.CursorTrack;
import com.kirkwoodwest.launchpadminimk3.hardware.GridButton;
import com.kirkwoodwest.launchpadminimk3.hardware.GridButtonColor;

public class GridStatus implements GridNote {
    private final GridButton gridButton;
    private final ClipLauncherSlot clipLauncherSlot;
    private final CursorTrack cursorTrack;
    private boolean noteOnStatus = false;
    private DoubleGridState doubleGridState;

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

    public void setGridState(DoubleGridState state) {
        doubleGridState = state;
        setStatus();
    }

    private void setStatus() {
        //Launch Mode
        GridButtonColor clipPlaying  = GridButtonColor.ClipPlaying;
        GridButtonColor clipPlayingNote = GridButtonColor.ClipPlayingNote;
        GridButtonColor hasClip = GridButtonColor.HasClip;
        GridButtonColor off = GridButtonColor.Off;

        if(doubleGridState == DoubleGridState.LaunchAltMode) {
            clipPlaying = GridButtonColor.AltClipPlayingNote;
            clipPlayingNote = GridButtonColor.AltClipPlaying;
            hasClip = GridButtonColor.AltHasClip;
        } else if (doubleGridState == DoubleGridState.SceneMode) {
            clipPlaying = GridButtonColor.SceneClipPlayingNote;
            clipPlayingNote = GridButtonColor.SceneClipPlaying;
            hasClip = GridButtonColor.SceneHasClip;
        } else if (doubleGridState == DoubleGridState.SceneAltMode) {
            clipPlaying = GridButtonColor.SceneAltClipPlayingNote;
            clipPlayingNote = GridButtonColor.SceneAltClipPlaying;
            hasClip = GridButtonColor.SceneAltHasClip;
        } else if (doubleGridState == DoubleGridState.StopMode) {
            clipPlaying = GridButtonColor.StopClipPlayingNote;
            clipPlayingNote = GridButtonColor.StopClipPlaying;
            hasClip = GridButtonColor.StopHasClip;
        } else if (doubleGridState == DoubleGridState.RecordMode) {
            clipPlaying = GridButtonColor.NoteRecording;
            clipPlayingNote = GridButtonColor.NoteOffRecording;
        } else if (doubleGridState == DoubleGridState.DeleteMode){
            clipPlaying = GridButtonColor.DeleteClipPlaying;
            clipPlayingNote = GridButtonColor.DeleteClipPlayingNote;
            hasClip = GridButtonColor.DeleteHasClip;
        }

        if (clipLauncherSlot.isPlaying().get()) {
            if (noteOnStatus) {
                gridButton.setState(clipPlaying);
            } else {
                gridButton.setState(clipPlayingNote);
            }
        } else if (clipLauncherSlot.hasContent().get()) {
            gridButton.setState(hasClip);
        } else {
            gridButton.setState(off);
        }
    }
}
