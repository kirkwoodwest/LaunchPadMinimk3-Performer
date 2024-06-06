// Written by Kirkwood West - kirkwoodwest.com
// (c) 2024
package com.kirkwoodwest.scalawoods

import com.bitwig.extension.callback.ShortMidiDataReceivedCallback
import com.bitwig.extension.callback.SysexMidiDataReceivedCallback
import com.bitwig.extension.controller.api._

class HardwareBasic(val inputPort: MidiIn,
                    val midiOut: MidiOut) {

  def setInputCallback(midiCallback: ShortMidiDataReceivedCallback): Unit = {
    inputPort.setMidiCallback(midiCallback)
  }

  def setSysexCallback(sysexCallback: SysexMidiDataReceivedCallback): Unit = {
    inputPort.setSysexCallback(sysexCallback)
  }

  def sendMidi(status: Int, data1: Int, data2: Int): Unit = {
    this.midiOut.sendMidi(status, data1, data2)
  }

  def sendSysex(s: String): Unit = {
    this.midiOut.sendSysex(s)
  }
  
}