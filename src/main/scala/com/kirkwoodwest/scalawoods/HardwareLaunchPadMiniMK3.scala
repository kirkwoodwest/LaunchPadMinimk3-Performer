package com.kirkwoodwest.scalawoods

import com.kirkwoodwest.scalawoods.utils.MidiUtil

import com.bitwig.extension.api.Color
import com.bitwig.extension.callback.ShortMidiDataReceivedCallback
import com.bitwig.extension.controller.api.*

import java.text.MessageFormat
import java.util
import java.util.{ArrayList, Arrays, HashMap}
import java.util.function.Supplier
import scala.collection.mutable.ListBuffer


class HardwareLaunchPadMiniMK3(private val host: ControllerHost, midi_port: Int, input_callback: ShortMidiDataReceivedCallback) 
  extends HardwareBasic(host.getMidiInPort(midi_port), host.getMidiOutPort(midi_port)) {
  
  val hardwareSurface: HardwareSurface = host.createHardwareSurface
  val baseHardwareName: String = "LPM" + midi_port + "_"
  //Build grid buttons

  //CCs for the interior part of the pad.
  var index = 88 //starts at 88 we will count backwards and down...
  val rows = 8
  val cols = 8
  val gridMap = Array.ofDim[Int](rows, cols)
  
  for (row <- 0 until rows) { //Start at one
    for (col <- cols - 1 until -1 by -1) {
      gridMap(col)(row) = index
      index = index - 1
    }
    index = index - 2
  }
  
  val ccGridMap: Array[Array[Int]] = gridMap
  val gridButtons: ListBuffer[ListBuffer[GridButton]] = ListBuffer.fill(rows)(ListBuffer.empty[GridButton])

  for (col <- 0 until rows) {
    for (row <- 0 until cols) {
      val note = gridMap(col)(row)
      val nameLight = baseHardwareName + "grid_light_" + row + "_" + col
      val nameButton = baseHardwareName + "grid_button_" + row + "_" + col
      val button: HardwareButton = hardwareSurface.createHardwareButton(nameButton)
      val gridButton: GridButton = new GridButton(button, LightData(midiOut, note, 123))
      gridButton.setupNoteButton(inputPort, note)
      gridButtons(col) += gridButton
    }
  }
  
  //Top Row buttons (uses CC
  private val hardwareFuncMap: Array[Int] = Array(91, 92, 93, 94, 95, 96, 97, 98)
  val hardwareFuncButtons: ListBuffer[GridButton] = ListBuffer()
  for (i <- hardwareFuncMap.indices) {
    val cc = hardwareFuncMap(i)
    val nameLight = baseHardwareName + "_func_light_" + i
    val nameButton = baseHardwareName + "_func_button_" + i

    val button: HardwareButton = hardwareSurface.createHardwareButton(nameButton)
    val gridButton: GridButton = new GridButton(button, LightData(midiOut, cc, 123))
    gridButton.setupCCButton(inputPort, cc)

    hardwareFuncButtons += gridButton
  }

  //Scene buttons (uses CC
  private val hardwareSceneMap: Array[Int] = Array[Int](89, 79, 69, 59, 49, 39, 29, 19)
  val hardwareSceneButtons: ListBuffer[GridButton] = ListBuffer()

  for (i <- hardwareSceneMap.indices) {
    val cc = hardwareSceneMap(i)
    val nameLight = baseHardwareName + "_scenes_light_" + i
    val nameButton = baseHardwareName + "_scene_button_" + i
    val button: HardwareButton = hardwareSurface.createHardwareButton(nameButton)
    val gridButton: GridButton = new GridButton(button, LightData(midiOut, cc, 123))
    gridButton.setupCCButton(inputPort, cc)

    hardwareSceneButtons += gridButton
  }

  MidiData.sendSysexBooter(host, midiOut)
  MidiData.setBrightness(midiOut, 127)
//  sendMidi(MidiUtil.NOTE_ON, 88, 15)
//  sendMidi(MidiUtil.NOTE_ON, 87, 15)
//  sendMidi(MidiUtil.NOTE_ON, 86, 45)

  val novationLight: MultiStateHardwareLight = hardwareSurface.createMultiStateHardwareLight("NovationLight")
  novationLight.state.onUpdateHardware((hwState: InternalHardwareLightState) =>
    updatePadLed(hwState, 0x63))

  def flush(): Unit = {
    hardwareSurface.updateHardware()

    //Flush Lights
    for (col <- 0 until rows) {
      for (row <- 0 until cols) {
        gridButtons(col)(row).flush()
      }
    }

    //Flush Function Buttons
    for (i <- hardwareFuncButtons.indices) {
      hardwareFuncButtons(i).flush()
    }

    //Flush Scene
    for (i <- hardwareSceneButtons.indices) {
      hardwareSceneButtons(i).flush()
    }
  }


  def getGridButton(col: Int, row: Int): GridButton = {
    gridButtons(col)(row)
  }
  
  def getFuncButton(index: Int): GridButton = {
    hardwareFuncButtons(index)
  }
  
  def getSceneButton(index: Int): GridButton = {
    hardwareSceneButtons(index)
  }

  def updatePadLed(state: InternalHardwareLightState, ccValue: Int): Unit = {
    sendMidi(MidiUtil.NOTE_ON, ccValue, 3)
  }


  //
//  def setGridColor(col: Int, row: Int, R: Int, G: Int, B: Int): Unit = {
//    val color_index = LaunchPadMiniMK3Colors.getIndexFromRGB(R, G, B)
//    val cc = ccGridMap(col)(row)
//    sendMidi(MidiData.LIGHT_MODE_STATUS_STATIC, cc, color_index)
//  }
//
//  def setGridColorPulsing(col: Int, row: Int, R: Int, G: Int, B: Int): Unit = {
//    val color_index = LaunchPadMiniMK3Colors.getIndexFromRGB(R, G, B)
//    val cc = ccGridMap(col)(row)
//    sendMidi(MidiData.LIGHT_MODE_STATUS_PULSE, cc, color_index)
//  }
//
//  def clearGridColor(col: Int, row: Int): Unit = {
//    val cc = ccGridMap(col)(row)
//    sendMidi(MidiData.LIGHT_MODE_STATUS_STATIC, cc, 0)
//  }
//
//  //Utils not for production...
//  //Fills all lights with random colors on a grid. startin index is the light index. choose 0 or 64.
//  private def lightsFillWithColorIndex(starting_index: Int): Unit = {
//    var index = starting_index
//    for (row <- 1 until 8) {
//      for (col <- 0 until 8 - 1) {
//        sendMidi(MidiData.LIGHT_MODE_STATUS_STATIC, ccGridMap(col)(row), index)
//        index = index + 1
//      }
//      if (index == 127) break //todo: break is not supported
//    }
//  }
//  
}

object MidiData { 
  val SYSEX_HEADER: String = "F0 00 20 29 02 0D"
  val SYSEX_SLEEP_MESSAGE: String = s"$SYSEX_HEADER F0 00 20 29 02 0D 09"
  val SYSEX_MODE_DAW: String = s"$SYSEX_HEADER 10 01 F7"
  val SYSEX_MODE_DAW_CLEAR: String = s"$SYSEX_HEADER 12 00 00 00 F7"
  val SYSEX_MODE_SESSION: String = s"$SYSEX_HEADER 00 00 F7"
  val SYSEX_PROGRAMMER_MODE: String = s"$SYSEX_HEADER 0E 01 F7"

  val BUTTON_MIDI_CHANNEL = 0
  val LIGHT_MODE_STATIC_CHANNEL = 0
  val LIGHT_MODE_FLASH_CHANNEL = 1
  val LIGHT_MODE_PULSE_CHANNEL = 2

  val LIGHT_MODE_STATUS_STATIC: Int = MidiUtil.NOTE_ON | LIGHT_MODE_STATIC_CHANNEL
  val LIGHT_MODE_STATUS_FLASH: Int = MidiUtil.NOTE_ON | LIGHT_MODE_FLASH_CHANNEL
  val LIGHT_MODE_STATUS_PULSE: Int = MidiUtil.NOTE_ON | LIGHT_MODE_PULSE_CHANNEL

  enum LIGHT_MODE_STATUS(val value: Int):
    case STATIC extends LIGHT_MODE_STATUS(LIGHT_MODE_STATUS_STATIC)
    case FLASH extends LIGHT_MODE_STATUS(LIGHT_MODE_STATUS_FLASH)
    case PULSE extends LIGHT_MODE_STATUS(LIGHT_MODE_STATUS_PULSE)

  enum FUNCTION_BUTTONS:
    case UP, DOWN, LEFT, RIGHT, SESSION, DRUMS, KEYS, USER


  private def fillArrayWithRange(min: Int, max: Int) = {
    val count = max - min + 1
    val array = new Array[Int](count)
    for (i <- 0 until count) {
      array(i) = min + i
    }
    array
  }

  private def combineArrays(array1: Array[Int], array2: Array[Int]) = {
    val both_arrays = util.Arrays.copyOf(array1, array1.length + array2.length)
    System.arraycopy(array2, 0, both_arrays, array1.length, array2.length)
    both_arrays
  }

  def getColorFromRGB(r: Float, g: Float, b: Float): Int = {
    val color_index = LaunchPadMiniMK3Colors.getIndexFromRGB(Math.floor(r * 255).toInt, Math.floor(g * 255).toInt, Math.floor(b * 255).toInt)
    color_index
  }

  def sendSysexBooter(host: ControllerHost, midiOut: MidiOut): Unit = {
    host.println("sysex sent")
    midiOut.sendSysex(MidiData.SYSEX_MODE_DAW)
    midiOut.sendSysex(MidiData.SYSEX_MODE_DAW_CLEAR)
    midiOut.sendSysex(MidiData.SYSEX_MODE_SESSION)
    midiOut.sendSysex(MidiData.SYSEX_PROGRAMMER_MODE)
  }

  def setBrightness(midiOut: MidiOut, brightness: Int): Unit = {
    val hexString = brightness.toHexString
    val paddedHexString = hexString.reverse.padTo(2, '0').reverse
    midiOut.sendSysex("F0 00 20 29 02 0D 0A " + paddedHexString + " F7")
  }
}

