package com.kirkwoodwest.scalawoods

import com.bitwig.extension.controller.api.{ClipLauncherSlot, CursorTrack}

//implement GridButton Note
class GridStatus(gridButton: GridButton, val clipLauncherSlot: ClipLauncherSlot, val cursorTrack: CursorTrack) extends GridNote {
  var noteOnStatus: Boolean = false
  clipLauncherSlot.isPlaying.addValueObserver((_) => {
    setStatus()
  })
  clipLauncherSlot.hasContent.addValueObserver((_) => {
    setStatus()
  })
  clipLauncherSlot.isRecording.addValueObserver((_) => {
    setStatus()
  })

//  GridButtonNote(cursorTrack,this)

  override def noteOn(noteOn: Boolean): Unit = {
    this.noteOnStatus = noteOn
    setStatus()
  }

  def setStatus():Unit = {
    if (clipLauncherSlot.isPlaying.get) {
      if(noteOnStatus) gridButton.setState(GridButtonState.ClipPlayingNote)
      else gridButton.setState(GridButtonState.ClipPlaying)
    } else if (clipLauncherSlot.isRecording.get) {
      gridButton.setState(GridButtonState.NoteRecording)
    } else if (clipLauncherSlot.hasContent.get) {
      gridButton.setState(GridButtonState.HasClip)
    } else {
      gridButton.setState(GridButtonState.Off)
    }
  }
}
