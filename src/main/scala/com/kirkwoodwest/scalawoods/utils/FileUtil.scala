package com.kirkwoodwest.scalawoods.utils

import com.bitwig.extension.api.PlatformType
import com.bitwig.extension.controller.api.ControllerHost
import com.bitwig.extension.api.PlatformType.{LINUX, MAC, WINDOWS}
object FileUtil {
  //Static method to get file path
  def getPath(host: ControllerHost, file: String): String = host.getPlatformType match {
    case platform if platform.equals(PlatformType.WINDOWS) =>
      val userProfile = System.getenv("USERPROFILE").replace("\\", "/")
      userProfile + "/Documents/Bitwig Studio/Extensions/" + file
    case platform if platform.equals(PlatformType.MAC) =>
      System.getProperty("user.home") + "/Documents/Bitwig Studio/Extensions/" + file
    case platform if platform.equals(PlatformType.LINUX) =>
      System.getProperty("user.home") + "/Bitwig Studio/Extensions/" + file
    case _ =>
      throw new IllegalArgumentException("Unknown Platform")
  }
}