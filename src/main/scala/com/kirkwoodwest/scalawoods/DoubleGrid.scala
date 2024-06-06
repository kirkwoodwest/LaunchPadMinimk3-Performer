package com.kirkwoodwest.scalawoods
import com.bitwig.extension.controller.api.*
import com.kirkwoodwest.scalawoods.values.SettableBooleanValueImpl


import java.util
import java.util.function.Supplier
import scala.collection.mutable.ListBuffer


class DoubleGrid(
                 val host: ControllerHost,
                 val cursorTrackList: List[CursorTrack],
                 val hardware: HardwareLaunchPadMiniMK3) {

  val gridButtonBasePressed: ListBuffer[Runnable] = ListBuffer[Runnable]()
  val gridButtonBaseReleased: ListBuffer[Runnable] = ListBuffer[Runnable]()
  val GridButtonAltPressed: ListBuffer[Runnable] = ListBuffer[Runnable]()
  val GridButtonAltReleased: ListBuffer[Runnable] = ListBuffer[Runnable]()
  val normalMode: SettableBooleanValueImpl = new SettableBooleanValueImpl(true)
  val altMode: SettableBooleanValueImpl = new SettableBooleanValueImpl(false)

  val buttonActions: Array[Array[List[HardwareActionBindable]]] = Array.ofDim[List[HardwareActionBindable]](8, 8)

  var altLaunchModeActive: Boolean = false
  val transport: Transport = host.createTransport
  transport.isClipLauncherOverdubEnabled.markInterested()
  transport.isClipLauncherAutomationWriteEnabled.markInterested()

  //Setup Fill Mode
  hardware.getSceneButton(0).button.pressedAction.addBinding(transport.isFillModeActive.setToTrueAction())
  hardware.getSceneButton(0).button.releasedAction.addBinding(transport.isFillModeActive.setToFalseAction())
  hardware.getSceneButton(0).setDirty()

  transport.isFillModeActive.addValueObserver((active: Boolean) => {
    if(active) {
      hardware.getSceneButton(0).setState(GridButtonState.clipPlaying)
    } else {
      hardware.getSceneButton(0).setState(GridButtonState.HasClip)
    }
  })

  hardware.getSceneButton(1).setDirty()
  hardware.getGridButton(1,0).setDirty()

  //Alternate Launch Mode
  val altModeOn: HardwareActionBindable  = host.createAction(() => {
    setLaunchModeState(true)
  }, () => "Alt Launch Mode Active")

  val altModeOff: HardwareActionBindable = host.createAction(() => {
    setLaunchModeState(false)
  }, () => "Alt Launch Mode Inactive")

  hardware.getSceneButton(1).button.pressedAction().addBinding(altModeOff)
  hardware.getSceneButton(2).button.pressedAction().addBinding(altModeOn)

  normalMode.addValueObserver((active: Boolean) => {
    if (active) {
      hardware.getSceneButton(1).setState(GridButtonState.clipPlaying)
    } else {
      hardware.getSceneButton(1).setState(GridButtonState.HasClip)
    }
  } )

  altMode.addValueObserver((active: Boolean) => {
    if (active) {
      hardware.getSceneButton(2).setState(GridButtonState.clipPlaying)
    } else {
      hardware.getSceneButton(2).setState(GridButtonState.HasClip)
    }
  })

  //Setup buttons for moving up and down the grid
  val clipLauncherUp: HardwareActionBindable = host.createAction(() => {
    moveClipLauncher(-1)
  }, () => "Move Grid Up")
  val clipLauncherDown: HardwareActionBindable = host.createAction(() => {
    moveClipLauncher(1)
  }, () => "Move Grid Down")

  hardware.getFuncButton(0).button.pressedAction().addBinding(clipLauncherUp)
  hardware.getFuncButton(1).button.pressedAction().addBinding(clipLauncherDown)

    //Loop through cursor track list and build out the clip launcher slots
  createButtonBindings()
  bindGridButtons()

  

  private def setupSuppliersForLights(): Unit = {
    cursorTrackList.zipWithIndex.foreach { case (cursorTrack, trackIndex) =>
      val clipLauncherSlotBank: ClipLauncherSlotBank = cursorTrack.clipLauncherSlotBank
      clipLauncherSlotBank.cursorIndex().markInterested()
      val sizeOfBank = clipLauncherSlotBank.getSizeOfBank
      for (row_index <- 0 until sizeOfBank) {
        val (c, r) = buttonMap(trackIndex, row_index)
        val slot: ClipLauncherSlot = clipLauncherSlotBank.getItemAt(row_index)
        slot.color.markInterested()

      }
    }
  }
  

  // private def setupClipCallbacks(trackIndex: Int, cursorTrack: CursorTrack, clipLauncherSlotBank: ClipLauncherSlotBank): Unit = {
  //   // Host
  //   clipLauncherSlotBank.addIsPlayingObserver((index: Int, status: Boolean) => {
  //     if (trackIndex < 8) {
  //       if (index < 8) {
  //         //this.clip_grid_status(trackIndex)(index).setIsPlaying(status)
  //       }
  //     }
  //   })

  //   clipLauncherSlotBank.addIsRecordingObserver((index: Int, status: Boolean) => {
  //   })

  //   clipLauncherSlotBank.addHasContentObserver((index: Int, status: Boolean) => {
  //     if (trackIndex < 8) {
  //       if (!status) {
  //         if (hardware != null) {
  //           //hardware.clearGridColor(trackIndex, index)
  //         }
  //       }
  //     }
  //   })
    
  //   val isPlaybackQueued: IndexedBooleanValueChangedCallback = (index: Int, status: Boolean) => {
  //   }
    
  //   clipLauncherSlotBank.addIsPlaybackQueuedObserver(isPlaybackQueued)
  //   val isStopQueued: IndexedBooleanValueChangedCallback = (index: Int, status: Boolean) => {
  //   }
    
  //   clipLauncherSlotBank.addIsStopQueuedObserver(isStopQueued)
  //   val isRecordingQueued: IndexedBooleanValueChangedCallback = (index: Int, status: Boolean) => {
  //   }
    
  //   clipLauncherSlotBank.addIsRecordingQueuedObserver(isRecordingQueued)
  // }

  def setLaunchModeState(state: Boolean): Unit = {
    if (state) {
      bindGridAltButtons()
    } else {
      bindGridButtons()
    }
  }

  def buttonMap(col: Int, row: Int): (Int, Int) = {
    //if col >= 8 then we should wrap and increase the row by 4
    var r = row
    if (col >= 8) {
      r = row + 4
    }

    (col % 8, r)
  }
  
  def createButtonBindings(): Unit = {
    cursorTrackList.zipWithIndex.foreach { case (cursorTrack, trackIndex) =>
      val clipLauncherSlotBank: ClipLauncherSlotBank = cursorTrack.clipLauncherSlotBank
      clipLauncherSlotBank.scrollPosition().markInterested()
      val sizeOfBank = clipLauncherSlotBank.getSizeOfBank
      for (row_index <- 0 until sizeOfBank) {
        val (c, r) = buttonMap(trackIndex, row_index)
        val clipLauncherSlot: ClipLauncherSlot = clipLauncherSlotBank.getItemAt(row_index)
        val launchAction:HardwareActionBindable =  clipLauncherSlot.launchAction()
        val launchReleaseAction:HardwareActionBindable = clipLauncherSlot.launchReleaseAction()
        val launchAltAction:HardwareActionBindable = clipLauncherSlot.launchAltAction()
        val launchReleaseAltAction:HardwareActionBindable = clipLauncherSlot.launchReleaseAltAction()

        //put each action into a list
        val actions = List(launchAction, launchReleaseAction, launchAltAction, launchReleaseAltAction)

        //store actions into a list retreivable by c and r
        buttonActions(c)(r) = actions

        //Bind Lights to States
        val gridButton: GridButton = hardware.getGridButton(c, r)
        val gridStatus = GridStatus(gridButton, clipLauncherSlot, cursorTrack)
        
        
        
        
      }
    }
  }

  private def bindGridButtons(): Unit = {
    //Loop through the buttonActions and bind the actions to the hardware
    for (col <- 0 until 8) {
      for (row <- 0 until 8) {
        val actions = buttonActions(col)(row)
        val launchAction = actions(0)
        val launchReleaseAction = actions(1)

        hardware.getGridButton(col, row).button.pressedAction().setBinding(launchAction)
        hardware.getGridButton(col, row).button.releasedAction().setBinding(launchReleaseAction)
      }
    }

    altMode.set(false)
    normalMode.set(true)
  }

  def bindGridAltButtons(): Unit = {
    //Loop through the buttonActions and bind the actions to the hardware
    for (col <- 0 until 8) {
      for (row <- 0 until 8) {
        val actions = buttonActions(col)(row)
        val launchAltAction = actions(2)
        val launchReleaseAltAction = actions(3)

         hardware.getGridButton(col, row).button.pressedAction().setBinding(launchAltAction)
         hardware.getGridButton(col, row).button.releasedAction().setBinding(launchReleaseAltAction)
      }
    }
    altMode.set(true)
    normalMode.set(false)

  }

  def moveClipLauncher(direction: Int): Unit = {
    var steps:Int = direction * 4
    cursorTrackList.zipWithIndex.foreach { case (cursorTrack, trackIndex) =>
      val clipLauncherSlotBank: ClipLauncherSlotBank = cursorTrack.clipLauncherSlotBank
      val index = clipLauncherSlotBank.scrollPosition().get
      val sizeOfBank = clipLauncherSlotBank.getSizeOfBank
      for (row_index <- 0 until sizeOfBank) {
        clipLauncherSlotBank.scrollPosition().set(index + steps)
      }
    }
  }
}