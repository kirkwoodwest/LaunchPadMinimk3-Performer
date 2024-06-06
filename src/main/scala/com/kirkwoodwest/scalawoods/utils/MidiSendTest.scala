package com.kirkwoodwest.scalawoods.utils
import com.bitwig.extension.api.util.midi.ShortMidiMessage
import com.bitwig.extension.controller.api.ControllerHost
import com.bitwig.extension.controller.api.SettableRangedValue
import com.kirkwoodwest.scalawoods.HardwareBasic

import java.lang.Math.floor

class MidiSendTest(private val host: ControllerHost, private val hardware: HardwareBasic) {
  val MidiChannel:SettableRangedValue = host.getDocumentState.getNumberSetting("midi channel", "MIDISEND", 0, 15, 1, "", 0)
  val data1:SettableRangedValue = host.getDocumentState.getNumberSetting("data1", "MIDISEND", 0, 127, 1, "", 0)
  val data2:SettableRangedValue = host.getDocumentState.getNumberSetting("data2", "MIDISEND", 0, 127, 1, "", 64)
  
  MidiChannel.addValueObserver((v: Double) => {
    update(v)
  })

  data1.addValueObserver((v: Double) => {
    update(v)
  })
  data2.addValueObserver((v: Double) => {
    update(v)
  })
  
  private def update(v: Double): Unit = {
    val channel = floor(MidiChannel.get * 15).toInt
    val status = ShortMidiMessage.NOTE_ON | channel
    val d1 = floor(data1.get * 127).toInt
    val d2 = floor(data2.get * 127).toInt
    host.println("channel" + channel + "status:" + status + "d1: " + d1 + " d2:" + d2)
    hardware.sendMidi(status, d1, d2)
  }
}