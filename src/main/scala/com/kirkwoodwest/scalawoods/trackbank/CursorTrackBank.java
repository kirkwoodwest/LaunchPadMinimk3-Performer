package com.kirkwoodwest.scalawoods.trackbank;


import com.bitwig.extension.controller.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

//Long Live the cursor track. This is my most favorite class of all time. perfect for retaining permanent focus on a track.
//This is a helper class to create multiple cursor tracks and position them by using a reference name and index.
//This index is limited to the size of the CursorTrackHelper size of Bank.
//Simulates a track bank for multiple cursor tracks, this allows you to create a collection of tracks. These can be positioned freely or together
public class CursorTrackBank {

  private final ArrayList<CursorTrack> cursorTracks = new ArrayList<>();
  private final ControllerHost host;
  private final CursorTrackHelper cursorTrackHelper;
  private final String name;
  private String settingsName = "Cursor Track Bank";


  private SettableStringValue settingTargetName;

  private Signal settingTargetSet;
  private Action actionOpenControllerConsole;

  public enum INDICATOR_STYLE {
    PAN,
    VOLUME,
    NONE,
  }

  public CursorTrackBank(ControllerHost host, ArrayList<CursorTrack> cursorTracks, CursorTrackHelper cursorTrackHelper, String name){
    this.host = host;
    this.cursorTrackHelper = cursorTrackHelper;
    this.name = name;

    IntStream.range(0, cursorTracks.size())
            .forEach(i -> {
              CursorTrack cursorTrack = cursorTracks.get(i);
              setupCursorTrack(cursorTrack, i);
            });
  }

  public CursorTrackBank(ControllerHost host, CursorTrackHelper cursorTrackHelper, int trackCount, int numSends, int numScenes, String name){
    this.host = host;
    this.cursorTrackHelper = cursorTrackHelper;
    this.name = name;

    for (int i = 0; i < trackCount; i++) {
      CursorTrack cursorTrack = host.createCursorTrack(name + i, name + i, numSends, numScenes, true);
      setupCursorTrack(cursorTrack, i);
    }
  }

  private void setupCursorTrack(CursorTrack cursorTrack, int index){
//    SendBank cursorTrackSendBank = cursorTrack.sendBank();
//    IntStream.range(0, cursorTrackSendBank.getSizeOfBank())
//            .forEach(sendIndex -> {
//              cursorTrackSendBank.getItemAt(sendIndex).setIndication(true);
//            });

    cursorTrack.isPinned().markInterested();
    cursorTrack.exists().markInterested();
    cursorTrack.name().markInterested();
    cursorTrack.position().markInterested();

    cursorTracks.add(cursorTrack);
  }

  public void setIndicatorStyle(INDICATOR_STYLE style){
    switch (style){
      case PAN:
        cursorTracks.forEach(cursorTrack -> {
          cursorTrack.pan().setIndication(true);
          cursorTrack.volume().setIndication(false);
        });
        break;
      case VOLUME:
        cursorTracks.forEach(cursorTrack -> {
          cursorTrack.pan().setIndication(false);
          cursorTrack.volume().setIndication(true);
        });
        break;
      case NONE:
        cursorTracks.forEach(cursorTrack -> {
          cursorTrack.pan().setIndication(false);
          cursorTrack.volume().setIndication(false);
        });
        break;
    }
  }



  public int getSizeOfBank(){
    return cursorTracks.size();
  }

  public int getNumItems(){
    //Since all items are consider valid, this is the same as size of bank.
    return cursorTracks.size();
  }

  public CursorTrack getItemAt(int index){
    return cursorTracks.get(index);
  }

  //Come up with better names you suck.
  public void positionAllByIndexes(List<Integer> indexes){
    if(indexes.size() != cursorTracks.size()){
      throw new IllegalArgumentException("Indexes must be the same size as the cursor track bank");
    }
    IntStream.range(0, cursorTracks.size())
            .forEach(index -> {
              CursorTrack cursorTrack = cursorTracks.get(index);

              positionCursorByTrackIndex(index, indexes.get(index));
            });
  }


  public void positionAllByFirstTrackIndex(int trackIndex){
    IntStream.range(0, cursorTracks.size())
            .forEach(index -> {
              CursorTrack cursorTrack = cursorTracks.get(index);
              positionCursorByTrackIndex(index, trackIndex + index);
            });
  }

  public void positionSplitByFirstTrackIndex(int trackIndex){
    int splitIndex = (cursorTracks.size() + 1) / 2;
    ArrayList<CursorTrack> firstHalf = new ArrayList<>(cursorTracks.subList(0, splitIndex)),
            secondHalf = new ArrayList<>(cursorTracks.subList(splitIndex, cursorTracks.size()));
    positionCursorTracksByTrackIndex(firstHalf, trackIndex);
    positionCursorTracksByTrackIndex(secondHalf, trackIndex);
  }

  private void positionCursorTracksByTrackIndex(ArrayList<CursorTrack> cursorTracks, int trackIndex) {
    IntStream.range(0, cursorTracks.size())
            .forEach(index -> {
              CursorTrack cursorTrack = cursorTracks.get(index);
              positionCursorByTrackIndex(index, trackIndex + index);
            });
  }

  public void positionCursorByTrackIndex(int cursorIndex, int trackIndex){
    CursorTrack cursorTrack = cursorTracks.get(cursorIndex);
    Track track = cursorTrackHelper.getTrack(trackIndex);
    if(track != null) {
      cursorTrack.isPinned().set(false);
      cursorTrack.selectChannel(track);
      cursorTrack.isPinned().set(true);
    }
  }

  public void positionAllByTrackName(String trackName){
    positionAllByFirstTrackIndex(cursorTrackHelper.getIndexByName(trackName));
  }


  public void positionSplitByTrackName(String trackName){

    int splitIndex = (cursorTracks.size() + 1) / 2;
    ArrayList<CursorTrack> firstHalf = new ArrayList<>(cursorTracks.subList(0, splitIndex)),
            secondHalf = new ArrayList<>(cursorTracks.subList(splitIndex, cursorTracks.size()));
    positionCursorTracksByTrackIndex(firstHalf, cursorTrackHelper.getIndexByName(trackName));
    positionCursorTracksByTrackIndex(secondHalf, cursorTrackHelper.getIndexByName(trackName));

  }

  public void positionCursorByTrackName(int cursorIndex, String trackName){
    CursorTrack cursorTrack = cursorTracks.get(cursorIndex);
    Track track = cursorTrackHelper.getTrackByName(trackName);
    if(track != null) {
      cursorTrack.isPinned().set(false);
      cursorTrack.selectChannel(track);
      cursorTrack.isPinned().set(true);
    }
  }


  private void positionCursorTracksByTrackName(ArrayList<CursorTrack> cursorTracks, String trackName) {
    positionCursorTracksByTrackIndex(cursorTracks, cursorTrackHelper.getIndexByName(trackName));
  }

  public List<CursorTrack> getCursors() {
    return cursorTracks;
  }
}
