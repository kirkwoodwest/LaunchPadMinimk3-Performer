package com.kirkwoodwest.scalawoods

import com.bitwig.extension.controller.api.*
import com.kirkwoodwest.scalawoods.GridButtonState

import scala.collection.mutable

class DoubleGrid(
                  val host: ControllerHost,
                  val id: String,
                  val cursorTrackList: List[CursorTrack],
                  val hardware: HardwareLaunchPadMiniMK3
                ) {
  val clipLauncherSlotBanks: Seq[ClipLauncherSlotBank] = cursorTrackList.map { cursorTrack =>
    cursorTrack.clipLauncherSlotBank
  }

  val sceneBank: SceneBank = host.createSceneBank(4)

  var launchSceneModeActive: Boolean = false
  val clipButtonActions: Array[Array[mutable.HashMap[ActionID, HardwareActionBindable]]] =
    Array.ofDim[mutable.HashMap[ActionID, HardwareActionBindable]](8, 8)

  for {
    i <- clipButtonActions.indices
    j <- clipButtonActions(i).indices
  } {
    clipButtonActions(i)(j) = mutable.HashMap.empty[ActionID, HardwareActionBindable]
  }

  var altLaunchModeActive: Boolean = false
  var stopModeActive: Boolean = false
  val transport: Transport = host.createTransport
  transport.isClipLauncherOverdubEnabled.markInterested()
  transport.isClipLauncherAutomationWriteEnabled.markInterested()

  // Setup Fill Mode
  hardware
    .getSceneButton(0)
    .button
    .pressedAction
    .addBinding(transport.isFillModeActive.setToTrueAction())
  hardware
    .getSceneButton(0)
    .button
    .releasedAction
    .addBinding(transport.isFillModeActive.setToFalseAction())
  hardware.getSceneButton(ButtonIndexes.FillActive).setDirty()

  //Fill Led
  transport.isFillModeActive.addValueObserver((active: Boolean) => {
    if (active) {
      hardware.getSceneButton(0).setState(GridButtonState.ClipPlaying)
    } else {
      hardware.getSceneButton(0).setState(GridButtonState.HasClip)
    }
  })

  // Alternate Launch Mode
  val launchModeToggle: HardwareActionBindable = host.createAction(
    () => {
      toggleLaunchModeState()
    },
    () => "Alt Launch Mode Active"
  )

  // Setup buttons for moving up and down the grid
  val clipLauncherUp: HardwareActionBindable = host.createAction(
    () => {
      moveClipLauncher(-1)
    },
    () => "Move Grid Up"
  )
  val clipLauncherDown: HardwareActionBindable = host.createAction(
    () => {
      moveClipLauncher(1)
    },
    () => "Move Grid Down"
  )

  hardware.getFuncButton(0).button.pressedAction().addBinding(clipLauncherUp)
  hardware.getFuncButton(1).button.pressedAction().addBinding(clipLauncherDown)

  //Scene Mode
  val launchSceneActiveAction: HardwareActionBindable = host.createAction(
    () => {
      setSceneLaunchMode(true)
    },
    () => "LaunchScene Mode Active"
  )
  val launchSceneInactiveAction: HardwareActionBindable = host.createAction(
    () => {
       setSceneLaunchMode(false)
    },
    () => "LaunchScene Mode Deactive"
  )
  hardware.getSceneButton(ButtonIndexes.LaunchScene).button.pressedAction().addBinding(launchSceneActiveAction)
  hardware.getSceneButton(ButtonIndexes.LaunchScene).button.releasedAction().addBinding(launchSceneInactiveAction)

  //Stop Mode
  val stopModeActiveAction: HardwareActionBindable = host.createAction(
    () => {
      setStopMode(true)
    },
    () => "Stop Mode Active"
  )
  val stopModeInactiveAction: HardwareActionBindable = host.createAction(
    () => {
      setStopMode(false)
    },
    () => "Stop Mode Inactive"
  )
  hardware.getSceneButton(ButtonIndexes.Stop).button.pressedAction().addBinding(stopModeActiveAction)
  hardware.getSceneButton(ButtonIndexes.Stop).button.releasedAction().addBinding(stopModeInactiveAction)

  // Loop through cursor track list and build out the clip launcher slots
  createButtonBindings()
  updateState()



  private def setStopMode(bool: Boolean): Unit =
    stopModeActive = bool
    updateState()

  private def setSceneLaunchMode(active:Boolean): Unit =
    launchSceneModeActive = active
    updateState()

  private def updateState(): Unit = {
    if(stopModeActive) {
      hardware.getSceneButton(ButtonIndexes.Stop).setState(GridButtonState.NoteRecording)
    } else {
      hardware.getSceneButton(ButtonIndexes.Stop).setState(GridButtonState.ClipPlaying)
    }
    if (launchSceneModeActive && !stopModeActive) {
      hardware.getSceneButton(ButtonIndexes.LaunchScene).setState(GridButtonState.NoteRecording)

      if (altLaunchModeActive) {
        bindSceneAltButtonsClips()
        hardware.getSceneButton(ButtonIndexes.LaunchMode).setState(GridButtonState.NoteRecording)
      } else {
        bindSceneButtonsClips()
        hardware.getSceneButton(ButtonIndexes.LaunchMode).setState(GridButtonState.ClipPlaying)
      }
    } else if (stopModeActive) {
        bindStopButtons()
        hardware.getSceneButton(ButtonIndexes.Stop).setState(GridButtonState.ClipPlaying)
    } else {
      hardware.getSceneButton(ButtonIndexes.LaunchScene).setState(GridButtonState.HasClip)
      bindGridButtonsClips()
      if(altLaunchModeActive) {
        bindGridAltButtonsClips()
        hardware.getSceneButton(ButtonIndexes.LaunchMode).setState(GridButtonState.NoteRecording)
      } else {
        bindGridButtonsClips()
        hardware.getSceneButton(ButtonIndexes.LaunchMode).setState(GridButtonState.ClipPlaying)
      }
      hardware.getSceneButton(ButtonIndexes.FillActive).setState(GridButtonState.HasClip)
    }
  }
  private def toggleLaunchModeState(): Unit = {
    // Implement the state toggling logic here
    altLaunchModeActive = !altLaunchModeActive
    updateState()
  }

  
  private def buttonMap(col: Int, row: Int): (Int, Int) = {
    var r = row
    if (col >= 8) {
      r = row + 4
    }
    (col % 8, r)
  }

  private def createButtonBindings(): Unit = {
    clipLauncherSlotBanks.zipWithIndex.foreach { (clipLauncherSlotBank, trackIndex) =>
      clipLauncherSlotBank.scrollPosition().markInterested()
      val sizeOfBank = clipLauncherSlotBank.getSizeOfBank

      for (row_index <- 0 until sizeOfBank) {
        val (c, r) = buttonMap(trackIndex, row_index)
        val clipLauncherSlot: ClipLauncherSlot = clipLauncherSlotBank.getItemAt(row_index)
        val launchAction: HardwareActionBindable = clipLauncherSlot.launchAction()
        val launchReleaseAction: HardwareActionBindable = clipLauncherSlot.launchReleaseAction()
        val launchAltAction: HardwareActionBindable = clipLauncherSlot.launchAltAction()
        val launchReleaseAltAction: HardwareActionBindable = clipLauncherSlot.launchReleaseAltAction()
        val stopAction: HardwareActionBindable = clipLauncherSlotBank.stopAction()
        val stopAltAction: HardwareActionBindable = clipLauncherSlotBank.stopAltAction()

        val sceneLaunchAction: HardwareActionBindable = sceneBank.getItemAt(row_index).launchAction()
        val sceneAltLaunchAction: HardwareActionBindable = sceneBank.getItemAt(row_index).launchAltAction()
        val sceneReleaseAction: HardwareActionBindable = sceneBank.getItemAt(row_index).launchReleaseAction()
        val sceneAltReleaseAction: HardwareActionBindable = sceneBank.getItemAt(row_index).launchReleaseAltAction()

        val actions = mutable.HashMap[ActionID, HardwareActionBindable](
          ActionID.ClipLaunch -> launchAction,
          ActionID.ClipLaunchRelease -> launchReleaseAction,
          ActionID.ClipAltLaunch -> launchAltAction,
          ActionID.ClipAltLaunchRelease -> launchReleaseAltAction,
          ActionID.ClipStop -> stopAction,
          ActionID.SceneLaunch -> sceneLaunchAction,
          ActionID.SceneAltLaunch -> sceneAltLaunchAction,
          ActionID.SceneLaunchRelease -> sceneReleaseAction,
          ActionID.SceneAltLaunchRelease -> sceneAltReleaseAction,
        )

        clipButtonActions(c)(r) = actions

        val gridButton: GridButton = hardware.getGridButton(c, r)
        val gridStatus = GridStatus(gridButton, clipLauncherSlot, cursorTrackList(trackIndex))
      }
    }
  }

  private def bindGridButtonsClips(): Unit = {
    bindActionsToGrid(ActionID.ClipLaunch, ActionID.ClipLaunchRelease)
  }

  private def bindGridAltButtonsClips(): Unit = {
    bindActionsToGrid(ActionID.ClipAltLaunch, ActionID.ClipAltLaunchRelease)
  }

  private def bindSceneButtonsClips(): Unit = {
    bindActionsToGrid(ActionID.SceneLaunch, ActionID.SceneLaunchRelease)
  }

  private def bindSceneAltButtonsClips(): Unit = {
    bindActionsToGrid(ActionID.SceneAltLaunch, ActionID.SceneAltLaunchRelease)
  }

  private def bindStopButtons(): Unit = {
    bindActionsToGrid(ActionID.ClipStop, null)
  }
  private def bindStopAltButtons(): Unit = {
    bindActionsToGrid(ActionID.ClipAltStop, null)
  }

  private def bindActionsToGrid(pressedAction: ActionID, releasedAction: ActionID): Unit = {
    for (col <- 0 until 8) {
      for (row <- 0 until 8) {
        bindActionsToGridButton(col, row, pressedAction, releasedAction)
      }
    }
  }

  private def bindActionsToGridButton(col:Int, row:Int, pressedAction:ActionID, releasedAction:ActionID): Unit = {
    val actions = clipButtonActions(col)(row)
    if(pressedAction != null)
      val action = actions(pressedAction)
      hardware.getGridButton(col, row).button.pressedAction().setBinding(action)
    else
      hardware.getGridButton(col, row).button.pressedAction().clearBindings()
    if (releasedAction != null)
      val action = actions(releasedAction)
      hardware.getGridButton(col, row).button.releasedAction().setBinding(action)
    else
      hardware.getGridButton(col, row).button.releasedAction().clearBindings()
  }

  def moveClipLauncher(direction: Int): Unit = {
    val steps: Int = direction * 4
    cursorTrackList.zipWithIndex.foreach { case (cursorTrack, trackIndex) =>
      val clipLauncherSlotBank: ClipLauncherSlotBank = cursorTrack.clipLauncherSlotBank
      val index = clipLauncherSlotBank.scrollPosition().get
      val sizeOfBank = clipLauncherSlotBank.getSizeOfBank
      clipLauncherSlotBank.scrollPosition().set(index + steps)
    }
  }
}

//Scene Button Indexes
private object ButtonIndexes {
  val FillActive: Int = 0
  val FillAlt: Int = 1
  val LaunchMode: Int = 2
  val LaunchScene: Int = 3
  val Record: Int = 4
  val Stop: Int = 5
}

enum Modes:
  case  Clip,
        ClipAlt,
        Scene,
        SceneAlt

enum ActionID:
  case  ClipLaunch,
        ClipLaunchRelease,
        ClipAltLaunch,
        ClipAltLaunchRelease,
        SceneLaunch,
        SceneLaunchRelease,
        SceneAltLaunch,
        SceneAltLaunchRelease,
        ClipStop,
        ClipAltStop