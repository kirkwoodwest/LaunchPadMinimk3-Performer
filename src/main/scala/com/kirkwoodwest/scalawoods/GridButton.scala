package com.kirkwoodwest.scalawoods

import com.bitwig.extension.controller.api.{HardwareButton, MidiIn, MidiOut}
import com.kirkwoodwest.scalawoods.GridButtonState

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
    if(isDirty)
      isDirty = false
      var channel: Int = 0
      
      if (state.equals(GridButtonState.ClipPlaying) || state.equals(GridButtonState.ClipPlayingNote))
        channel = 2
      
      lightData.midiOut.sendMidi(0x90 +channel, lightData.note, this.state.value)

end GridButton


