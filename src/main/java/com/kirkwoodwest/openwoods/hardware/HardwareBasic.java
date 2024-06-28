package com.kirkwoodwest.openwoods.hardware;

import com.bitwig.extension.callback.ShortMidiDataReceivedCallback;
import com.bitwig.extension.callback.SysexMidiDataReceivedCallback;
import com.bitwig.extension.controller.api.MidiIn;
import com.bitwig.extension.controller.api.MidiOut;

public class HardwareBasic {
    private final MidiIn midiIn;
    private final MidiOut midiOut;

    public HardwareBasic(MidiIn midiIn, MidiOut midiOut) {
        this.midiIn = midiIn;
        this.midiOut = midiOut;
    }

    public MidiIn getMidiIn() {
        return midiIn;
    }

    public MidiOut getMidiOut() {
        return midiOut;
    }

    public void setInputCallback(ShortMidiDataReceivedCallback midiCallback) {
        midiIn.setMidiCallback(midiCallback);
    }

    public void setSysexCallback(SysexMidiDataReceivedCallback sysexCallback) {
        midiIn.setSysexCallback(sysexCallback);
    }

    public void sendMidi(int status, int data1, int data2) {
        midiOut.sendMidi(status, data1, data2);
    }

    public void sendSysex(String s) {
        midiOut.sendSysex(s);
    }
}
