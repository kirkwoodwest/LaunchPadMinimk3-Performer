package com.kirkwoodwest.openwoods.trackbank;

import com.bitwig.extension.controller.api.*;

import java.util.stream.IntStream;

/**
 * A basic implementation of trackbank which allows you to get visible tracks by name or index.
 * Useful for getting a cursor track to pin to a specific track.
 *
 * The point of this is to keep things simple to allow other bitwig objects to align with the current view.
 * Since this item will always be locked to scroll position 0, this will always be in sync with current view.
 */
public class CursorTrackHelper implements Helper {
  private final TrackBank trackBank;
  private final ControllerHost host;

  public CursorTrackHelper(ControllerHost host, int numTracks){
    this.host = host;
    trackBank = host.createTrackBank(numTracks, 0, 0);
    trackBank.scrollPosition().markInterested();
    IntStream.range(0, trackBank.getSizeOfBank())
            .forEach(trackIndex -> {
              trackBank.getItemAt(trackIndex).name().markInterested();
              trackBank.getItemAt(trackIndex).exists().markInterested();
            });
  }

  public Track getTrack(int index){
    if(index < 0 || index >= trackBank.getSizeOfBank()){
      return null;
    }
    return trackBank.getItemAt(index);
  }

  public Track getTrackByName(String name){
    return IntStream.range(0, trackBank.getSizeOfBank())
            .mapToObj(trackBank::getItemAt)
            .filter(trackItem -> trackItem.name().get().equals(name))
            .findFirst().orElse(null);
  }

  public int getIndexByName(String name){
    return IntStream.range(0, trackBank.getSizeOfBank())
            .filter(trackIndex -> trackBank.getItemAt(trackIndex).name().get().equals(name))
            .findFirst().orElse(-1);
  }

  public int getSizeOfBank() {
    return this.trackBank.getSizeOfBank();
  }

}
