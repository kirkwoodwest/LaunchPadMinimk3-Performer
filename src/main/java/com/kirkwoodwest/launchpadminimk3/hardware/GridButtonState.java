package com.kirkwoodwest.launchpadminimk3.hardware;

public enum GridButtonState {
    Off(0),
    HasClip(103),
    ClipPlaying(43),
    ClipPlayingNote(41),
    NoteRecording(60),
    NoteOffRecording(70);

    private final int value;

    GridButtonState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
