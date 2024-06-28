package com.kirkwoodwest.launchpadminimk3;
import com.kirkwoodwest.openwoods.utils.LogUtil;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.PlayingNote;

public class GridButtonNote {
    private final CursorTrack cursorTrack;
    private final GridNote gridNote;
    private boolean timer = false;
    private int numNotes = 0;
    private int lastNote = -1;
    private int lastVelocity = -1;

    public GridButtonNote(CursorTrack cursorTrack, GridNote gridNote) {
        this.cursorTrack = cursorTrack;
        this.gridNote = gridNote;
        cursorTrack.playingNotes().addValueObserver(this::noteObserver);
    }

    private void noteObserver(PlayingNote[] notes) {
        if (notes.length == 0) {
            gridNote.noteOn(false);
            lastNote = -1;
            lastVelocity = -1;
        } else {
            PlayingNote note = notes[0];
            if (lastNote != note.pitch() || lastVelocity != note.velocity()) {
                lastNote = notes[0].pitch();
                lastVelocity = notes[0].velocity();
                gridNote.noteOn(true);
                LogUtil.getHost().scheduleTask(() -> gridNote.noteOn(false), 75);
            }
        }
    }
}
