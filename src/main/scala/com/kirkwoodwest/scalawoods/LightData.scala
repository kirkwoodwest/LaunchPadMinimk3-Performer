package com.kirkwoodwest.scalawoods

import com.bitwig.extension.controller.api.MidiOut

case class LightData(val midiOut: MidiOut, val note: Int, var velocity: Int)

