
package com.kirkwoodwest.launchpadminimk3;

import com.bitwig.extension.controller.api.*;
import java.util.ArrayList;
import java.util.List;

public class SceneLauncher {
  private final List<Scene> scenes = new ArrayList<>();

  public SceneLauncher(ControllerHost host, int sceneCount, List<ClipLauncherSlotBank> clipLauncherSlotBanks) {
    int sizeOfBank = clipLauncherSlotBanks.get(0).getSizeOfBank();
    for (int rowIndex = 0; rowIndex < sizeOfBank; rowIndex++) {
      List<HardwareActionBindable> sceneLaunchActions = new ArrayList<>();
      List<HardwareActionBindable> sceneLaunchReleaseActions = new ArrayList<>();
      List<HardwareActionBindable> sceneAltLaunchActions = new ArrayList<>();
      List<HardwareActionBindable> sceneAltLaunchReleaseActions = new ArrayList<>();
      List<HardwareActionBindable> sceneStopActions = new ArrayList<>();
      List<HardwareActionBindable> sceneAltStopActions = new ArrayList<>();

      for (ClipLauncherSlotBank clipLauncherSlotBank : clipLauncherSlotBanks) {
        clipLauncherSlotBank.scrollPosition().markInterested();
        ClipLauncherSlot clipLauncherSlot = clipLauncherSlotBank.getItemAt(rowIndex);
        sceneLaunchActions.add(clipLauncherSlot.launchAction());
        sceneLaunchReleaseActions.add(clipLauncherSlot.launchReleaseAction());
        sceneAltLaunchActions.add(clipLauncherSlot.launchAltAction());
        sceneAltLaunchReleaseActions.add(clipLauncherSlot.launchReleaseAltAction());
        sceneStopActions.add(clipLauncherSlotBank.stopAction());
        sceneAltStopActions.add(clipLauncherSlotBank.stopAltAction());
      }

      scenes.add(new Scene(host, sceneLaunchActions, sceneLaunchReleaseActions, sceneAltLaunchActions,
              sceneAltLaunchReleaseActions, sceneStopActions, sceneAltStopActions));
    }
  }

  public Scene getScene(int index) {
    return scenes.get(index);
  }

  public enum SceneLaunchMode {
    LAUNCH,
    LAUNCH_RELEASE,
    ALT_LAUNCH,
    ALT_LAUNCH_RELEASE,
    STOP,
    ALT_STOP
  }
}
