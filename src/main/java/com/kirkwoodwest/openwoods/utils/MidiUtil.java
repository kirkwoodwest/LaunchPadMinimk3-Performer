package com.kirkwoodwest.openwoods.utils;

/**
 * Some basic helpers for midi
 */
public class MidiUtil {
  public static final int NOTE_ON = 0x90;
  public static final int NOTE_OFF = 0x80;
  public static final int CC   = 0xB0;
  public int getStatusWithChannel(int status, int channel){
    return status | channel;
  }
  public static int random_127() {
    return (int) (Math.random() * 127);
  };
  public static int doubleTo127(double value) {
    return (int) (value * 127);
  };
}
