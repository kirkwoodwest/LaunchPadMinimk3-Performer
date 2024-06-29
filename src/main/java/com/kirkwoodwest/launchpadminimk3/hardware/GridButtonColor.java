package com.kirkwoodwest.launchpadminimk3.hardware;

public enum GridButtonColor {
    Off(new int[]{0, 0}),
    HasClip(new int[]{103,0}),
    ClipPlaying(new int[]{43,2}),
    ClipPlayingNote(new int[]{41,2}),

    AltHasClip(new int[]{12,0}),
    AltClipPlaying(new int[]{15,2}),
    AltClipPlayingNote(new int[]{13,2}),

    SceneHasClip(new int[]{16,0}),
    SceneClipPlaying(new int[]{18,2}),
    SceneClipPlayingNote(new int[]{17,2}),

    SceneAltHasClip(new int[]{20,0}),
    SceneAltClipPlaying(new int[]{22,2}),
    SceneAltClipPlayingNote(new int[]{21,2}),

    StopHasClip(new int[]{23,0}),
    StopClipPlaying(new int[]{25,2}),
    StopClipPlayingNote(new int[]{24,2}),

    NoteRecording(new int[]{60,0}),
    NoteOffRecording(new int[]{70,0}),
    LaunchNormalMode(new int[]{41,0}),
    LaunchAltMode(new int[]{13,0}),

    FillModeActive(new int[]{9,0}),
    FillModeInactive(new int[]{8,0}),
    FillModeBar(new int[]{9,2}),
    FillModeLocked(new int[]{84,0}),

    SceneModeActive(new int[]{17,0}),
    SceneModeInactive(new int[]{19,0}),
    StopModeActive(new int[]{59,0}),
    StopModeInactive(new int[]{57,0}),

    DeleteClipPlaying(new int[]{25,0}),
    DeleteClipPlayingNote(new int[]{24,0}),
    DeleteHasClip(new int[]{23,0});

    private final int[] value;

    GridButtonColor(int[] value) {
        this.value = value;
    }

    public int[] getValue() {
        return value;
    }
}
