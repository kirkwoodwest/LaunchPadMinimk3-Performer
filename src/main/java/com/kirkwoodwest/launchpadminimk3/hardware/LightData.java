package com.kirkwoodwest.launchpadminimk3.hardware;

import com.bitwig.extension.controller.api.MidiOut;

public class LightData {
  private final MidiOut midiOut;
  private final int note;
  private int velocity;

  public LightData(MidiOut midiOut, int note, int velocity) {
    this.midiOut = midiOut;
    this.note = note;
    this.velocity = velocity;
  }

  public MidiOut getMidiOut() {
    return midiOut;
  }

  public int getNote() {
    return note;
  }

  public int getVelocity() {
    return velocity;
  }

  public void setVelocity(int velocity) {
    this.velocity = velocity;
  }
}
