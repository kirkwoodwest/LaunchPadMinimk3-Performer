package com.kirkwoodwest.scalawoods

import com.bitwig.extension.controller.api.{HardwareButton, MidiIn, MidiOut}

class GridButton(val button: HardwareButton, val lightData: LightData):
  protected var isDirty:Boolean = false
  protected var state: GridButtonState = GridButtonState.Off

  def setupNoteButton(midiIn: MidiIn, note: Int): Unit =
    button.pressedAction().setActionMatcher(midiIn.createNoteOnActionMatcher(0, note))
    button.releasedAction().setActionMatcher(midiIn.createNoteOffActionMatcher(0, note))

  def setupCCButton(midiIn: MidiIn, cc: Int): Unit =
    button.pressedAction().setActionMatcher(midiIn.createCCActionMatcher(0, cc, 127))
    button.releasedAction().setActionMatcher(midiIn.createCCActionMatcher(0, cc, 0))

  def setState(state: GridButtonState): Unit =
    this.state = state
    isDirty = true

  def setDirty(): Unit =
    isDirty = true

  def flush(): Unit =
    if(isDirty) {
      isDirty = false
      var channel: Int = 0
      if(state.equals(GridButtonState.clipPlaying) || state.equals(GridButtonState.clipPlayingNote)){
        channel = 2
      }
      lightData.midiOut.sendMidi(0x90 +channel, lightData.note, this.state.value)
      //do something
    }

end GridButton


enum GridButtonState(val value: Int):
  case Off extends GridButtonState(0)
  case HasClip extends GridButtonState(103)
  case clipPlaying extends GridButtonState(43)
  case clipPlayingNote extends GridButtonState(41)
  case NoteRecording extends GridButtonState(60)
  case NoteOffRecording extends GridButtonState(70)

case class LightData(val midiOut:MidiOut, val note: Int, var velocity: Int)
