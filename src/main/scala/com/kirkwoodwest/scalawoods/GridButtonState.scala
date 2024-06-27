package com.kirkwoodwest.scalawoods

enum GridButtonState(val value: Int):
  case Off extends GridButtonState(0)
  case HasClip extends GridButtonState(103)
  case ClipPlaying extends GridButtonState(43)
  case ClipPlayingNote extends GridButtonState(41)
  case NoteRecording extends GridButtonState(60)
  case NoteOffRecording extends GridButtonState(70)
