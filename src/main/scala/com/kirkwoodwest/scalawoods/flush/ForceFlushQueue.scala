package com.kirkwoodwest.scalawoods.flush

class ForceFlushQueue {
  private var queuedForceFlush = false
  var forceUpdate = false

  def forceNextFlush(): Unit = {
    queuedForceFlush = true
  }

  def getForceFlush: Boolean = {
    val forceFlush = queuedForceFlush
    queuedForceFlush = false
    forceFlush
  }
}