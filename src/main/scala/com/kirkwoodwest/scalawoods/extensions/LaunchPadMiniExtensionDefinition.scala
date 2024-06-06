package com.kirkwoodwest.scalawoods.extensions
import com.kirkwoodwest.scalawoods.extensions.*
import com.bitwig.extension.api.PlatformType
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList
import com.bitwig.extension.controller.ControllerExtensionDefinition
import com.bitwig.extension.controller.api.ControllerHost
import java.util.UUID


object LaunchPadMiniExtensionDefinition {
  private val DRIVER_ID = UUID.fromString("05d2c83f-594d-4833-917a-763fe52358e8")
}

class LaunchPadMiniExtensionDefinition extends ControllerExtensionDefinition:
  override def getName = "Launchpad Mini Mk3 - Performer"

  override def getAuthor = "Kirkwood West"

  override def getVersion = "1.0"

  override def getId: UUID = LaunchPadMiniExtensionDefinition.DRIVER_ID

  override def getHardwareVendor = "Novation"

  override def getHardwareModel = "Launchpad Mini MK3"

  override def getRequiredAPIVersion = 18

  override def getNumMidiInPorts = 1

  override def getNumMidiOutPorts = 1

  override def listAutoDetectionMidiPortNames(list: AutoDetectionMidiPortNamesList, platformType: PlatformType): Unit = {
  }

  override def createInstance(host: ControllerHost): LaunchPadMiniExtension = LaunchPadMiniExtension(this, host)
end LaunchPadMiniExtensionDefinition
