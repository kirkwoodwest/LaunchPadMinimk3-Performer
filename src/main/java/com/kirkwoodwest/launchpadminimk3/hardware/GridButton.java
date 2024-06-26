package com.kirkwoodwest.launchpadminimk3.hardware;

import com.bitwig.extension.controller.api.HardwareButton;
import com.bitwig.extension.controller.api.MidiIn;
import com.kirkwoodwest.launchpadminimk3.DoubleGridState;

public class GridButton {
    private final HardwareButton button;
    private final LightData lightData;
    private final GridButtonColorMapper colorMapper;
    private boolean isDirty = false;
    private GridButtonColor state = GridButtonColor.Off;
    private DoubleGridState doubleGridState = DoubleGridState.LaunchMode;

    public GridButton(HardwareButton button, LightData lightData, GridButtonColorMapper colorMapper) {
        this.button = button;
        this.lightData = lightData;
        this.colorMapper = colorMapper;
    }

    public void setupNoteButton(MidiIn midiIn, int note) {
        button.pressedAction().setActionMatcher(midiIn.createNoteOnActionMatcher(0, note));
        button.releasedAction().setActionMatcher(midiIn.createNoteOffActionMatcher(0, note));
    }

    public void setupCCButton(MidiIn midiIn, int cc) {
        button.pressedAction().setActionMatcher(midiIn.createCCActionMatcher(0, cc, 127));
        button.releasedAction().setActionMatcher(midiIn.createCCActionMatcher(0, cc, 0));
    }

    public void setState(GridButtonColor state) {
        this.state = state;
        isDirty = true;
    }

    public void setDirty() {
        isDirty = true;
    }

    public void flush() {
        if (isDirty) {
            isDirty = false;

            int vel = colorMapper.getVelocity(this.state);
            int channel = colorMapper.getChannel(this.state);

            lightData.getMidiOut().sendMidi(0x90 + channel, lightData.getNote(), vel);
        }
    }

    public HardwareButton getButton() {
        return button;
    }

    public LightData getLightData() {
        return lightData;
    }

    public void setGridState(DoubleGridState doubleGridState) {
        this.doubleGridState = doubleGridState;
    }
}
