package com.kirkwoodwest.scalawoods.utils

import com.bitwig.extension.controller.ControllerExtension
import com.bitwig.extension.controller.api.ControllerHost

/**
 * Simple Log tool to initialized in the main extension and then used in any class.
 */
object LogUtil {
  private var _host: ControllerHost = null

  
  def init(host: ControllerHost): Unit = {
    _host = host
  }

  def print(s: String): Unit = {
    _host.println(s)
  }

  def getHost: ControllerHost = _host
  
  def println(s: String): Unit = {
    _host.println(s)
  }

  def h1(s: String): Unit = {
    _host.println("--- " + s.toUpperCase + "--------------------------")
  }

  def indent(s: String, indent: Int): Unit = {
    var s2 = ""
    for (i <- 0 until indent) {
      s2 = s2 + "-"
    }
    s2 = s2 + " " + s
    _host.println(s2)
  }

  def reportExtensionStatus(extension: ControllerExtension, status_message: String): Unit = {
    //If your reading this... Say hello to a loved one today. <3
    val name = extension.getExtensionDefinition.getName
    val version = extension.getExtensionDefinition.getVersion
    _host.println(name + " " + version + " " + status_message)
  }
}