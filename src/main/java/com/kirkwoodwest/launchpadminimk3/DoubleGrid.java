package com.kirkwoodwest.launchpadminimk3;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.launchpadminimk3.hardware.GridButton;
import com.kirkwoodwest.launchpadminimk3.hardware.GridButtonColor;
import com.kirkwoodwest.launchpadminimk3.hardware.HardwareLaunchPadMiniMK3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoubleGrid {
  private final ControllerHost host;
  private final String id;
  private final List<CursorTrack> cursorTrackList;
  private final HardwareLaunchPadMiniMK3 hardware;
  private final List<ClipLauncherSlotBank> clipLauncherSlotBanks = new ArrayList<>();
  private final SceneLauncher sceneLauncher;
  private final Fill fill;
  private final SceneBank sceneBank;
  private boolean launchSceneModeActive;
  private final Map<ActionID, HardwareActionBindable>[][] clipButtonActions;
  private ArrayList<ArrayList<GridStatus>> gridStatus;
  private DoubleGridState doubleGridState;
  private boolean altLaunchModeActive;
  private boolean stopModeActive;
  private boolean recordModeActive;
  private final Transport transport;
  private boolean deleteModeActive;


  public DoubleGrid(ControllerHost host, String id, List<CursorTrack> cursorTrackList, HardwareLaunchPadMiniMK3 hardware) {
    this.host = host;
    this.id = id;
    this.cursorTrackList = cursorTrackList;
    this.hardware = hardware;

    //Indicate where the scenes are
    this.sceneBank = host.createSceneBank(4);
    this.sceneBank.scrollPosition().markInterested();
    sceneBank.setIndication(true);

    //Fill
    fill = new Fill(host, host.createTransport());

    for (int i = 0; i < cursorTrackList.size(); i++) {
      ClipLauncherSlotBank clipLauncherSlotBank = cursorTrackList.get(i).clipLauncherSlotBank();
      clipLauncherSlotBanks.add(clipLauncherSlotBank);
    }

    this.launchSceneModeActive = false;
    this.clipButtonActions = new HashMap[8][8];

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        this.clipButtonActions[i][j] = new HashMap<>();
      }
    }


    this.altLaunchModeActive = false;
    this.stopModeActive = false;
    this.transport = host.createTransport();
    this.transport.isClipLauncherOverdubEnabled().markInterested();
    this.transport.isClipLauncherAutomationWriteEnabled().markInterested();

    //Create Scene Launcher
    sceneLauncher = new SceneLauncher(host, 4, clipLauncherSlotBanks);
    setupHardware();
    //Create Scene Launcher

    createButtonBindings();
    updateState();
  }

  private void setupHardware() {
    hardware.getSceneButton(ButtonIndexes.FillActive).getButton().pressedAction().addBinding(fill.getFillPressedAction());
    hardware.getSceneButton(ButtonIndexes.FillActive).getButton().releasedAction().addBinding(fill.getFillReleasedAction());
    hardware.getSceneButton(ButtonIndexes.FillActive).setDirty();
    hardware.getSceneButton(ButtonIndexes.FillAlt).getButton().pressedAction().addBinding(fill.getFillOptionPressedAction());
    hardware.getSceneButton(ButtonIndexes.FillAlt).setDirty();

    Runnable updateLed = () -> {
      boolean option = this.fill.isFillOptionActive().get();
      boolean fill = this.fill.isFillModeActive().get();
      boolean locked = this.fill.isFillModeLocked().get();

      if (option){
        hardware.getSceneButton(ButtonIndexes.FillActive).setState(GridButtonColor.FillModeBar);
        hardware.getSceneButton(ButtonIndexes.FillAlt).setState(GridButtonColor.FillModeBar);
      } else if ( locked) {
        hardware.getSceneButton(ButtonIndexes.FillActive).setState(GridButtonColor.FillModeLocked);
        hardware.getSceneButton(ButtonIndexes.FillAlt).setState(GridButtonColor.FillModeLocked);
      } else if (fill) {
        hardware.getSceneButton(ButtonIndexes.FillActive).setState(GridButtonColor.FillModeActive);
        hardware.getSceneButton(ButtonIndexes.FillAlt).setState(GridButtonColor.FillModeInactive);
      } else {
        hardware.getSceneButton(ButtonIndexes.FillActive).setState(GridButtonColor.FillModeInactive);
        hardware.getSceneButton(ButtonIndexes.FillAlt).setState(GridButtonColor.FillModeInactive);
      }
    };

    fill.isFillModeActive().addValueObserver(active -> {
      updateLed.run();
    });

    fill.isFillOptionActive().addValueObserver(active -> {
      updateLed.run();
    });

    fill.isFillModeLocked().addValueObserver(active -> {
      updateLed.run();
    });

    //Move Clip Launcher Grid
    HardwareActionBindable clipLauncherUp = host.createAction(() -> moveClipLauncher(-1), () -> "Move Grid Up");
    HardwareActionBindable clipLauncherDown = host.createAction(() -> moveClipLauncher(1), () -> "Move Grid Down");

    hardware.getFuncButton(0).getButton().pressedAction().addBinding(clipLauncherUp);
    hardware.getFuncButton(1).getButton().pressedAction().addBinding(clipLauncherDown);

    //Launch Mode Toggle
    HardwareActionBindable launchModeToggle = host.createAction(() -> toggleLaunchModeState(), () -> "Alt Launch Mode Active");
    hardware.getSceneButton(ButtonIndexes.LaunchMode).getButton().pressedAction().addBinding(launchModeToggle);

    HardwareActionBindable launchSceneActiveAction = host.createAction(() -> setSceneLaunchMode(true), () -> "LaunchScene Mode Active");
    HardwareActionBindable launchSceneInactiveAction = host.createAction(() -> setSceneLaunchMode(false), () -> "LaunchScene Mode Deactive");
    hardware.getSceneButton(ButtonIndexes.LaunchScene).getButton().pressedAction().addBinding(launchSceneActiveAction);
    hardware.getSceneButton(ButtonIndexes.LaunchScene).getButton().releasedAction().addBinding(launchSceneInactiveAction);

    //Stop Button
    HardwareActionBindable stopModeActiveAction = host.createAction(() -> setStopMode(true), () -> "Stop Mode Active");
    HardwareActionBindable stopModeInactiveAction = host.createAction(() -> setStopMode(false), () -> "Stop Mode Inactive");
    hardware.getSceneButton(ButtonIndexes.Stop).getButton().pressedAction().addBinding(stopModeActiveAction);
    hardware.getSceneButton(ButtonIndexes.Stop).getButton().releasedAction().addBinding(stopModeInactiveAction);

    HardwareActionBindable recordModeActiveAction = host.createAction(() -> setRecordMode(true), () -> "Record Mode Active");
    HardwareActionBindable recordModeInactiveAction = host.createAction(() -> setRecordMode(false), () -> "Record Mode Inactive");
    hardware.getSceneButton(ButtonIndexes.Record).getButton().pressedAction().addBinding(recordModeActiveAction);
    hardware.getSceneButton(ButtonIndexes.Record).getButton().releasedAction().addBinding(recordModeInactiveAction);

    //Delete Mode
    HardwareActionBindable deleteModeActiveAction = host.createAction(() -> setDeleteMode(true), () -> "Delete Mode Active");
    HardwareActionBindable deleteModeInactiveAction = host.createAction(() -> setDeleteMode(false), () -> "Delete Mode Inactive");
    hardware.getSceneButton(ButtonIndexes.Delete).getButton().pressedAction().addBinding(deleteModeActiveAction);
    hardware.getSceneButton(ButtonIndexes.Delete).getButton().releasedAction().addBinding(deleteModeInactiveAction);
  }

  private void setDeleteMode(boolean b) {
    deleteModeActive = b;
    updateState();
  }

  private void setRecordMode(boolean b) {
    recordModeActive = b;
    updateState();
  }

  private void setStopMode(boolean bool) {
    stopModeActive = bool;
    updateState();
  }

  private void setSceneLaunchMode(boolean active) {
    launchSceneModeActive = active;
    updateState();
  }

  private void updateState() {
    if (stopModeActive) {
      if (altLaunchModeActive) {
        bindStop();
        doubleGridState = DoubleGridState.StopAltMode;
      } else {
        bindStopAlt();
        doubleGridState = DoubleGridState.StopMode;
      }
    } else if (deleteModeActive) {
      bindActionsToGrid(ActionID.ClipDelete, null);
      doubleGridState = DoubleGridState.DeleteMode;
    } else if (launchSceneModeActive) {
      if (altLaunchModeActive) {
        bindSceneAltButtonsClips();
        doubleGridState = DoubleGridState.SceneAltMode;
      } else {
        bindSceneButtonsClips();
        doubleGridState = DoubleGridState.SceneMode;
      }
    } else if (recordModeActive){
      if (altLaunchModeActive) {
        bindRecordMode();
        doubleGridState = DoubleGridState.RecordAltMode;
      } else {
        bindRecordMode();
        doubleGridState = DoubleGridState.RecordMode;
    }
    } else {
      if (altLaunchModeActive) {
        bindLaunchAlt();
        doubleGridState = DoubleGridState.LaunchAltMode;
      } else {
        bindLaunch();
        doubleGridState = DoubleGridState.LaunchMode;
      }
    }

    //Update State Observers
    gridStatus.forEach(track -> track.forEach(gridStatus -> gridStatus.setGridState(doubleGridState)));

    if(launchSceneModeActive) {
      hardware.getSceneButton(ButtonIndexes.LaunchScene).setState(GridButtonColor.SceneModeActive);
    } else {
      hardware.getSceneButton(ButtonIndexes.LaunchScene).setState(GridButtonColor.SceneModeInactive);
    }
    if (stopModeActive) {
      hardware.getSceneButton(ButtonIndexes.Stop).setState(GridButtonColor.StopModeActive);
    } else {
      hardware.getSceneButton(ButtonIndexes.Stop).setState(GridButtonColor.StopModeInactive);
    }

    if (altLaunchModeActive) {
      hardware.getSceneButton(ButtonIndexes.LaunchMode).setState(GridButtonColor.LaunchAltMode);
    } else {
      hardware.getSceneButton(ButtonIndexes.LaunchMode).setState(GridButtonColor.LaunchNormalMode);
    }

    if(recordModeActive) {
      hardware.getSceneButton(ButtonIndexes.Record).setState(GridButtonColor.NoteRecording);
    } else {
      hardware.getSceneButton(ButtonIndexes.Record).setState(GridButtonColor.NoteOffRecording);
    }

    if(deleteModeActive) {
      hardware.getSceneButton(ButtonIndexes.Delete).setState(GridButtonColor.DeleteClipPlaying);
    } else {
      hardware.getSceneButton(ButtonIndexes.Delete).setState(GridButtonColor.DeleteHasClip);
    }

  }

  private void toggleLaunchModeState() {
    altLaunchModeActive = !altLaunchModeActive;
    updateState();
  }

  private int[] buttonMap(int col, int row) {
    if (col >= 8) {
      row += 4;
    }
    return new int[]{col % 8, row};
  }

  private void createButtonBindings() {
    gridStatus = new ArrayList<>(8);
    for (int trackIndex = 0; trackIndex < cursorTrackList.size(); trackIndex++) {
      CursorTrack cursorTrack = cursorTrackList.get(trackIndex);
      ClipLauncherSlotBank clipLauncherSlotBank = clipLauncherSlotBanks.get(trackIndex);
      clipLauncherSlotBank.scrollPosition().markInterested();
      int sizeOfBank = clipLauncherSlotBank.getSizeOfBank();
      gridStatus.add(new ArrayList<>(sizeOfBank));

      for (int rowIndex = 0; rowIndex < sizeOfBank; rowIndex++) {
        int[] mappedButton = buttonMap(trackIndex, rowIndex);
        int col = mappedButton[0];
        int row = mappedButton[1];
        ClipLauncherSlot clipLauncherSlot = clipLauncherSlotBank.getItemAt(rowIndex);

        //Map Actions
        Map<ActionID, HardwareActionBindable> actions = new HashMap<>();
        actions.put(ActionID.ClipLaunch, clipLauncherSlot.launchAction());
        actions.put(ActionID.ClipLaunchRelease, clipLauncherSlot.launchReleaseAction());
        actions.put(ActionID.ClipAltLaunch,  clipLauncherSlot.launchAltAction());
        actions.put(ActionID.ClipAltLaunchRelease, clipLauncherSlot.launchReleaseAltAction());
        actions.put(ActionID.ClipStop, clipLauncherSlotBank.stopAction());
        actions.put(ActionID.ClipAltStop, clipLauncherSlotBank.stopAltAction());
        //Scene
        Scene scene = sceneLauncher.getScene(rowIndex);
        actions.put(ActionID.SceneLaunch, scene.sceneLaunch());
        actions.put(ActionID.SceneAltLaunch, scene.sceneAltLaunch());
        actions.put(ActionID.SceneLaunchRelease, scene.launchSceneRelease());
        actions.put(ActionID.SceneAltLaunchRelease, scene.launchAltSceneRelease());
        actions.put(ActionID.RecordMode, clipLauncherSlot.recordAction());
        actions.put(ActionID.ClipDelete, clipLauncherSlot.deleteObjectAction());

        clipButtonActions[col][row] = actions;

        GridButton gridButton = hardware.getGridButton(col, row);

        gridStatus.get(trackIndex).add(new GridStatus(hardware.getGridButton(col, row), clipLauncherSlot, cursorTrack));
      }
    }
  }

  private void bindLaunch() {
    bindActionsToGrid(ActionID.ClipLaunch, ActionID.ClipLaunchRelease);
  }

  private void bindLaunchAlt() {
    bindActionsToGrid(ActionID.ClipAltLaunch, ActionID.ClipAltLaunchRelease);
  }
  private void bindStop() {
    bindActionsToGrid(ActionID.ClipStop, null);
  }

  private void bindStopAlt() {
    bindActionsToGrid(ActionID.ClipAltStop, null);
  }

  private void bindSceneButtonsClips() {
    bindActionsToGrid(ActionID.SceneLaunch, ActionID.SceneLaunchRelease);
  }

  private void bindSceneAltButtonsClips() {
    bindActionsToGrid(ActionID.SceneAltLaunch, ActionID.SceneAltLaunchRelease);
  }

  private void bindStopButtons() {
    bindActionsToGrid(ActionID.ClipStop, null);
  }

  private void bindRecordMode() {
    bindActionsToGrid(ActionID.RecordMode, ActionID.ClipLaunchRelease);
  }


  private void bindActionsToGrid(ActionID pressedAction, ActionID releasedAction) {
    for (int col = 0; col < 8; col++) {
      for (int row = 0; row < 8; row++) {
        bindActionsToGridButton(col, row, pressedAction, releasedAction);
      }
    }
  }

  private void bindActionsToGridButton(int col, int row, ActionID pressedAction, ActionID releasedAction) {
    Map<ActionID, HardwareActionBindable> actions = clipButtonActions[col][row];
    if (pressedAction != null) {
      hardware.getGridButton(col, row).getButton().pressedAction().setBinding(actions.get(pressedAction));
    } else {
      hardware.getGridButton(col, row).getButton().pressedAction().clearBindings();
    }
    if (releasedAction != null) {
      hardware.getGridButton(col, row).getButton().releasedAction().setBinding(actions.get(releasedAction));
    } else {
      hardware.getGridButton(col, row).getButton().releasedAction().clearBindings();
    }
  }

  public void moveClipLauncher(int direction) {
    int steps = direction * 4;
    this.sceneBank.scrollPosition().set(this.sceneBank.scrollPosition().get() + steps);
    for (int trackIndex = 0; trackIndex < cursorTrackList.size(); trackIndex++) {
      ClipLauncherSlotBank clipLauncherSlotBank = clipLauncherSlotBanks.get(trackIndex);
      int index = clipLauncherSlotBank.scrollPosition().get();
      clipLauncherSlotBank.scrollPosition().set(index + steps);

    }
  }

  // Scene Button Indexes
  private static class ButtonIndexes {
    static final int FillActive = 0;
    static final int FillAlt = 1;
    static final int LaunchMode = 2;
    static final int LaunchScene = 3;

    static final int Stop = 4;
    static final int Record = 5;
    static final int Delete = 6;
  }

  // ActionID Enum
  public enum ActionID {
    ClipLaunch,
    ClipLaunchRelease,
    ClipAltLaunch,
    ClipAltLaunchRelease,
    SceneLaunch,
    SceneLaunchRelease,
    SceneAltLaunch,
    SceneAltLaunchRelease,
    ClipStop,
    ClipAltStop,
    ClipDelete,
    RecordMode;
  }
}
