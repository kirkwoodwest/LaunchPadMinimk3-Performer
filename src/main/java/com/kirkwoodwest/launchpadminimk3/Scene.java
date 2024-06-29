package com.kirkwoodwest.launchpadminimk3;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.HardwareActionBindable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {
  private final Map<SceneLauncher.SceneLaunchMode, HardwareActionBindable> actionMap = new HashMap<>();

  public Scene(ControllerHost host,
               List<HardwareActionBindable> launchActions,
               List<HardwareActionBindable> launchReleaseActions,
               List<HardwareActionBindable> altLaunchActions,
               List<HardwareActionBindable> altLaunchReleaseActions,
               List<HardwareActionBindable> stopActions,
               List<HardwareActionBindable> altStopActions) {

    actionMap.put(SceneLauncher.SceneLaunchMode.LAUNCH, createActionFromActions(host, launchActions));
    actionMap.put(SceneLauncher.SceneLaunchMode.LAUNCH_RELEASE, createActionFromActions(host, launchReleaseActions));
    actionMap.put(SceneLauncher.SceneLaunchMode.ALT_LAUNCH, createActionFromActions(host, altLaunchActions));
    actionMap.put(SceneLauncher.SceneLaunchMode.ALT_LAUNCH_RELEASE, createActionFromActions(host, altLaunchReleaseActions));
    actionMap.put(SceneLauncher.SceneLaunchMode.STOP, createActionFromActions(host, stopActions));
    actionMap.put(SceneLauncher.SceneLaunchMode.ALT_STOP, createActionFromActions(host, altStopActions));
  }

  public HardwareActionBindable getAction(SceneLauncher.SceneLaunchMode mode) {
    return actionMap.get(mode);
  }


  public HardwareActionBindable sceneLaunch(){
    return getAction(SceneLauncher.SceneLaunchMode.LAUNCH);
  }

  public HardwareActionBindable launchSceneRelease(){
    return getAction(SceneLauncher.SceneLaunchMode.LAUNCH_RELEASE);
  }

  public HardwareActionBindable sceneAltLaunch(){
    return getAction(SceneLauncher.SceneLaunchMode.ALT_LAUNCH);
  }

  public HardwareActionBindable launchAltSceneRelease(){
    return getAction(SceneLauncher.SceneLaunchMode.ALT_LAUNCH_RELEASE);
  }

  public HardwareActionBindable stopScene(){
    return getAction(SceneLauncher.SceneLaunchMode.STOP);
  }

  public HardwareActionBindable stopAltScene(){
    return getAction(SceneLauncher.SceneLaunchMode.ALT_STOP);
  }

  private HardwareActionBindable createActionFromActions(ControllerHost host, List<HardwareActionBindable> actions) {
    return host.createAction(() -> {
      for (HardwareActionBindable action : actions) {
        action.invoke();
      }
    }, () -> "Launches DoubleGrid Scene");
  }
}