package com.kirkwoodwest.launchpadminimk3.hardware;

import com.bitwig.extension.controller.api.HardwareButton;
import com.bitwig.extension.controller.api.MidiIn;

public class GridButton {
    private final HardwareButton button;
    private final LightData lightData;
    private boolean isDirty = false;
    private GridButtonState state = GridButtonState.Off;

    public GridButton(HardwareButton button, LightData lightData) {
        this.button = button;
        this.lightData = lightData;
    }

    public void setupNoteButton(MidiIn midiIn, int note) {
        button.pressedAction().setActionMatcher(midiIn.createNoteOnActionMatcher(0, note));
        button.releasedAction().setActionMatcher(midiIn.createNoteOffActionMatcher(0, note));
    }

    public void setupCCButton(MidiIn midiIn, int cc) {
        button.pressedAction().setActionMatcher(midiIn.createCCActionMatcher(0, cc, 127));
        button.releasedAction().setActionMatcher(midiIn.createCCActionMatcher(0, cc, 0));
    }

    public void setState(GridButtonState state) {
        this.state = state;
        isDirty = true;
    }

    public void setDirty() {
        isDirty = true;
    }

    public void flush() {
        if (isDirty) {
            isDirty = false;
            int channel = 0;

            if (state.equals(GridButtonState.ClipPlaying) || state.equals(GridButtonState.ClipPlayingNote)) {
                channel = 2;
            }

            lightData.getMidiOut().sendMidi(0x90 + channel, lightData.getNote(), this.state.getValue());
        }
    }

    public HardwareButton getButton() {
        return button;
    }

    public LightData getLightData() {
        return lightData;
    }
}