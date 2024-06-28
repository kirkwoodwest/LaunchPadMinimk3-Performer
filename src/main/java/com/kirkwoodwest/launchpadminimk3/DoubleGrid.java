package com.kirkwoodwest.launchpadminimk3;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.launchpadminimk3.hardware.GridButton;
import com.kirkwoodwest.launchpadminimk3.hardware.GridButtonState;
import com.kirkwoodwest.launchpadminimk3.hardware.HardwareLaunchPadMiniMK3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoubleGrid {
  private final ControllerHost host;
  private final String id;
  private final List<CursorTrack> cursorTrackList;
  private final HardwareLaunchPadMiniMK3 hardware;
  private final ClipLauncherSlotBank[] clipLauncherSlotBanks;
  private final SceneBank sceneBank;
  private boolean launchSceneModeActive;
  private final Map<ActionID, HardwareActionBindable>[][] clipButtonActions;
  private boolean altLaunchModeActive;
  private boolean stopModeActive;
  private final Transport transport;

  public DoubleGrid(ControllerHost host, String id, List<CursorTrack> cursorTrackList, HardwareLaunchPadMiniMK3 hardware) {
    this.host = host;
    this.id = id;
    this.cursorTrackList = cursorTrackList;
    this.hardware = hardware;
    this.clipLauncherSlotBanks = new ClipLauncherSlotBank[cursorTrackList.size()];
    for (int i = 0; i < cursorTrackList.size(); i++) {
      this.clipLauncherSlotBanks[i] = cursorTrackList.get(i).clipLauncherSlotBank();
    }
    this.sceneBank = host.createSceneBank(4);
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

    setupHardware();
    createButtonBindings();
    updateState();
  }

  private void setupHardware() {
    hardware.getSceneButton(0).getButton().pressedAction().addBinding(transport.isFillModeActive().setToTrueAction());
    hardware.getSceneButton(0).getButton().releasedAction().addBinding(transport.isFillModeActive().setToFalseAction());
    hardware.getSceneButton(ButtonIndexes.FillActive).setDirty();

    transport.isFillModeActive().addValueObserver(active -> {
      if (active) {
        hardware.getSceneButton(0).setState(GridButtonState.ClipPlaying);
      } else {
        hardware.getSceneButton(0).setState(GridButtonState.HasClip);
      }
    });

    HardwareActionBindable launchModeToggle = host.createAction(() -> toggleLaunchModeState(), () -> "Alt Launch Mode Active");
    HardwareActionBindable clipLauncherUp = host.createAction(() -> moveClipLauncher(-1), () -> "Move Grid Up");
    HardwareActionBindable clipLauncherDown = host.createAction(() -> moveClipLauncher(1), () -> "Move Grid Down");

    hardware.getFuncButton(0).getButton().pressedAction().addBinding(clipLauncherUp);
    hardware.getFuncButton(1).getButton().pressedAction().addBinding(clipLauncherDown);

    HardwareActionBindable launchSceneActiveAction = host.createAction(() -> setSceneLaunchMode(true), () -> "LaunchScene Mode Active");
    HardwareActionBindable launchSceneInactiveAction = host.createAction(() -> setSceneLaunchMode(false), () -> "LaunchScene Mode Deactive");
    hardware.getSceneButton(ButtonIndexes.LaunchScene).getButton().pressedAction().addBinding(launchSceneActiveAction);
    hardware.getSceneButton(ButtonIndexes.LaunchScene).getButton().releasedAction().addBinding(launchSceneInactiveAction);

    HardwareActionBindable stopModeActiveAction = host.createAction(() -> setStopMode(true), () -> "Stop Mode Active");
    HardwareActionBindable stopModeInactiveAction = host.createAction(() -> setStopMode(false), () -> "Stop Mode Inactive");
    hardware.getSceneButton(ButtonIndexes.Stop).getButton().pressedAction().addBinding(stopModeActiveAction);
    hardware.getSceneButton(ButtonIndexes.Stop).getButton().releasedAction().addBinding(stopModeInactiveAction);
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
      hardware.getSceneButton(ButtonIndexes.Stop).setState(GridButtonState.NoteRecording);
    } else {
      hardware.getSceneButton(ButtonIndexes.Stop).setState(GridButtonState.ClipPlaying);
    }
    if (launchSceneModeActive && !stopModeActive) {
      hardware.getSceneButton(ButtonIndexes.LaunchScene).setState(GridButtonState.NoteRecording);
      if (altLaunchModeActive) {
        bindSceneAltButtonsClips();
        hardware.getSceneButton(ButtonIndexes.LaunchMode).setState(GridButtonState.NoteRecording);
      } else {
        bindSceneButtonsClips();
        hardware.getSceneButton(ButtonIndexes.LaunchMode).setState(GridButtonState.ClipPlaying);
      }
    } else if (stopModeActive) {
      bindStopButtons();
      hardware.getSceneButton(ButtonIndexes.Stop).setState(GridButtonState.ClipPlaying);
    } else {
      hardware.getSceneButton(ButtonIndexes.LaunchScene).setState(GridButtonState.HasClip);
      bindGridButtonsClips();
      if (altLaunchModeActive) {
        bindGridAltButtonsClips();
        hardware.getSceneButton(ButtonIndexes.LaunchMode).setState(GridButtonState.NoteRecording);
      } else {
        bindGridButtonsClips();
        hardware.getSceneButton(ButtonIndexes.LaunchMode).setState(GridButtonState.ClipPlaying);
      }
      hardware.getSceneButton(ButtonIndexes.FillActive).setState(GridButtonState.HasClip);
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
    for (int trackIndex = 0; trackIndex < cursorTrackList.size(); trackIndex++) {
      CursorTrack cursorTrack = cursorTrackList.get(trackIndex);
      ClipLauncherSlotBank clipLauncherSlotBank = clipLauncherSlotBanks[trackIndex];
      clipLauncherSlotBank.scrollPosition().markInterested();
      int sizeOfBank = clipLauncherSlotBank.getSizeOfBank();

      for (int rowIndex = 0; rowIndex < sizeOfBank; rowIndex++) {
        int[] mappedButton = buttonMap(trackIndex, rowIndex);
        int col = mappedButton[0];
        int row = mappedButton[1];
        ClipLauncherSlot clipLauncherSlot = clipLauncherSlotBank.getItemAt(rowIndex);
        HardwareActionBindable launchAction = clipLauncherSlot.launchAction();
        HardwareActionBindable launchReleaseAction = clipLauncherSlot.launchReleaseAction();
        HardwareActionBindable launchAltAction = clipLauncherSlot.launchAltAction();
        HardwareActionBindable launchReleaseAltAction = clipLauncherSlot.launchReleaseAltAction();
        HardwareActionBindable stopAction = clipLauncherSlotBank.stopAction();
        HardwareActionBindable stopAltAction = clipLauncherSlotBank.stopAltAction();
        HardwareActionBindable sceneLaunchAction = sceneBank.getItemAt(rowIndex).launchAction();
        HardwareActionBindable sceneAltLaunchAction = sceneBank.getItemAt(rowIndex).launchAltAction();
        HardwareActionBindable sceneReleaseAction = sceneBank.getItemAt(rowIndex).launchReleaseAction();
        HardwareActionBindable sceneAltReleaseAction = sceneBank.getItemAt(rowIndex).launchReleaseAltAction();

        Map<ActionID, HardwareActionBindable> actions = new HashMap<>();
        actions.put(ActionID.ClipLaunch, launchAction);
        actions.put(ActionID.ClipLaunchRelease, launchReleaseAction);
        actions.put(ActionID.ClipAltLaunch, launchAltAction);
        actions.put(ActionID.ClipAltLaunchRelease, launchReleaseAltAction);
        actions.put(ActionID.ClipStop, stopAction);
        actions.put(ActionID.ClipAltStop, stopAltAction);
        actions.put(ActionID.SceneLaunch, sceneLaunchAction);
        actions.put(ActionID.SceneAltLaunch, sceneAltLaunchAction);
        actions.put(ActionID.SceneLaunchRelease, sceneReleaseAction);
        actions.put(ActionID.SceneAltLaunchRelease, sceneAltReleaseAction);

        clipButtonActions[col][row] = actions;

        GridButton gridButton = hardware.getGridButton(col, row);
        new GridStatus(gridButton, clipLauncherSlot, cursorTrackList.get(trackIndex));
      }
    }
  }

  private void bindGridButtonsClips() {
    bindActionsToGrid(ActionID.ClipLaunch, ActionID.ClipLaunchRelease);
  }

  private void bindGridAltButtonsClips() {
    bindActionsToGrid(ActionID.ClipAltLaunch, ActionID.ClipAltLaunchRelease);
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
    for (int trackIndex = 0; trackIndex < cursorTrackList.size(); trackIndex++) {
      ClipLauncherSlotBank clipLauncherSlotBank = clipLauncherSlotBanks[trackIndex];
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
    static final int Record = 4;
    static final int Stop = 5;
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
    ClipAltStop
  }

  // Modes Enum
  public enum Modes {
    Clip,
    ClipAlt,
    Scene,
    SceneAlt
  }
}
