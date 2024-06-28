package com.kirkwoodwest.launchpadminimk3.hardware;

public enum GridButtonColor {
    Off(0),
    HasClip(103),
    ClipPlaying(43),
    ClipPlayingNote(41),

    AltHasClip(12),
    AltClipPlaying(15),
    AltClipPlayingNote(13),

    SceneHasClip(16),
    SceneClipPlaying(18),
    SceneClipPlayingNote(17),

    SceneAltHasClip(20),
    SceneAltClipPlaying(22),
    SceneAltClipPlayingNote(21),

    StopHasClip(55),
    StopClipPlaying(59),
    StopClipPlayingNote(57),

    DeleteHasClip(107),
    DeleteClipPlaying(107),
    DeleteClipPlayingNote(4),

    NoteRecording(60),
    NoteOffRecording(70),
    LaunchNormalMode(41),
    LaunchAltMode(13),
    FillModeActive(9),
    FillModeInactive(8),

    SceneModeActive(17),
    SceneModeInactive(19),
    StopModeActive(59),
    StopModeInactive(57);

    private final int value;

    GridButtonColor(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
