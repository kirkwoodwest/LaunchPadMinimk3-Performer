package com.kirkwoodwest.scalawoods.extensions

import com.bitwig.extension.callback.ShortMidiDataReceivedCallback
import com.bitwig.extension.controller.ControllerExtension
import com.bitwig.extension.controller.api.ControllerHost
import com.kirkwoodwest.scalawoods.trackbank.{CursorTrackBank, CursorTrackHelper}
import com.kirkwoodwest.scalawoods.utils.LogUtil
import com.kirkwoodwest.scalawoods.{DoubleGrid, HardwareBasic, HardwareLaunchPadMiniMK3}
import com.kirkwoodwest.scalawoods.utils.LogUtil.reportExtensionStatus

import scala.jdk.CollectionConverters.*
import java.util.HashMap

class LaunchPadMiniExtension(
                              val definition: LaunchPadMiniExtensionDefinition,
                              val host: ControllerHost)
  extends ControllerExtension(definition, host):
  var hardware: HardwareLaunchPadMiniMK3 = null
  override def init(): Unit =
    LogUtil.init(host)
    val midiCallbackFunc:ShortMidiDataReceivedCallback = (d:Int, d2:Int, d3: Int) => {
      host.println(s"Button Pressed: $d $d2 $d3")
    }

     hardware = HardwareLaunchPadMiniMK3(host, 0, midiCallbackFunc)
    //val hardwareBasic = HardwareBasic(host.getMidiInPort(0), host.getMidiOutPort(0))
    //hardwareBasic.setInputCallback(midiCallbackFunc)
    
    val cursorTrackHelper: CursorTrackHelper = CursorTrackHelper(host, 64)

    val cursorTrackBank:CursorTrackBank = CursorTrackBank(host, cursorTrackHelper, 16,0,4, "Clip Launcher")
    //Convert to list  cursorTrackBank.getCursors
    val cursorList =  cursorTrackBank.getCursors.asScala.toList

    DoubleGrid(host, cursorList, hardware)
    host.println("Initializing LaunchPad Mini Extension")

  end init

  override def exit(): Unit =
    // TODO: Perform any cleanup once the driver exits
    // For now just show a popup notification for verification that it is no longer running.
    reportExtensionStatus(this, "Exited")

  override def flush(): Unit =
    hardware.flush()

end LaunchPadMiniExtension

