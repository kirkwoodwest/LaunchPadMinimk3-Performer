package com.kirkwoodwest.scalawoods

import com.bitwig.extension.controller.api.{CursorTrack, PlayingNote}
import com.kirkwoodwest.scalawoods.utils.LogUtil

trait GridNote {
  def noteOn(noteOn: Boolean): Unit
}
class GridButtonNote(cursorTrack: CursorTrack, gridNote: GridNote) {
  private val timer:Boolean = false
  private val numNotes: Int = 0
  var lastNote:Int = -1
  var lastVelocity:Int = -1


  cursorTrack.playingNotes().addValueObserver((notes: Array[PlayingNote]) => {
    
    if (notes.length == 0) {
      gridNote.noteOn(false)
      lastNote = -1
      lastVelocity = -1
    } else {
      val note: PlayingNote = notes(0)
      if (lastNote != note.pitch() || lastVelocity != note.velocity()) {
        lastNote = notes(0).pitch()
        lastVelocity = notes(0).velocity()
        gridNote.noteOn(true)
        LogUtil.getHost.scheduleTask(() => {
          gridNote.noteOn(false)
        }, 75)
      }
    }

  })
}
