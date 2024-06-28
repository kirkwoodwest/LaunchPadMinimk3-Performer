package com.kirkwoodwest.launchpadminimk3.hardware;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.MidiOut;
import com.kirkwoodwest.openwoods.utils.MidiUtil;

import java.util.Arrays;

public class MidiData {
  public static final String SYSEX_HEADER = "F0 00 20 29 02 0D";
  public static final String SYSEX_SLEEP_MESSAGE = SYSEX_HEADER + " F0 00 20 29 02 0D 09";
  public static final String SYSEX_MODE_DAW = SYSEX_HEADER + " 10 01 F7";
  public static final String SYSEX_MODE_DAW_CLEAR = SYSEX_HEADER + " 12 00 00 00 F7";
  public static final String SYSEX_MODE_SESSION = SYSEX_HEADER + " 00 00 F7";
  public static final String SYSEX_PROGRAMMER_MODE = SYSEX_HEADER + " 0E 01 F7";

  public static final int BUTTON_MIDI_CHANNEL = 0;
  public static final int LIGHT_MODE_STATIC_CHANNEL = 0;
  public static final int LIGHT_MODE_FLASH_CHANNEL = 1;
  public static final int LIGHT_MODE_PULSE_CHANNEL = 2;

  public static final int LIGHT_MODE_STATUS_STATIC = MidiUtil.NOTE_ON | LIGHT_MODE_STATIC_CHANNEL;
  public static final int LIGHT_MODE_STATUS_FLASH = MidiUtil.NOTE_ON | LIGHT_MODE_FLASH_CHANNEL;
  public static final int LIGHT_MODE_STATUS_PULSE = MidiUtil.NOTE_ON | LIGHT_MODE_PULSE_CHANNEL;

  public enum LIGHT_MODE_STATUS {
    STATIC(LIGHT_MODE_STATUS_STATIC),
    FLASH(LIGHT_MODE_STATUS_FLASH),
    PULSE(LIGHT_MODE_STATUS_PULSE);

    private final int value;

    LIGHT_MODE_STATUS(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  public enum FUNCTION_BUTTONS {
    UP, DOWN, LEFT, RIGHT, SESSION, DRUMS, KEYS, USER
  }

  public static int[] fillArrayWithRange(int min, int max) {
    int count = max - min + 1;
    int[] array = new int[count];
    for (int i = 0; i < count; i++) {
      array[i] = min + i;
    }
    return array;
  }

  public static int[] combineArrays(int[] array1, int[] array2) {
    int[] bothArrays = Arrays.copyOf(array1, array1.length + array2.length);
    System.arraycopy(array2, 0, bothArrays, array1.length, array2.length);
    return bothArrays;
  }

  public static int getColorFromRGB(float r, float g, float b) {
    return LaunchPadMiniMK3Colors.getIndexFromRGB((int) Math.floor(r * 255), (int) Math.floor(g * 255), (int) Math.floor(b * 255));
  }

  public static void sendSysexBooter(ControllerHost host, MidiOut midiOut) {
    host.println("sysex sent");
    midiOut.sendSysex(SYSEX_MODE_DAW);
    midiOut.sendSysex(SYSEX_MODE_DAW_CLEAR);
    midiOut.sendSysex(SYSEX_MODE_SESSION);
    midiOut.sendSysex(SYSEX_PROGRAMMER_MODE);
  }

  public static void setBrightness(MidiOut midiOut, int brightness) {
    String hexString = Integer.toHexString(brightness);
    String paddedHexString = new StringBuilder(hexString).reverse().toString().concat("00").substring(0, 2);
    midiOut.sendSysex("F0 00 20 29 02 0D 0A " + paddedHexString + " F7");
  }
}
