package com.kirkwoodwest.scalawoods.flush

import java.io.Flushable
import java.io.IOException
import scala.collection.mutable.ListBuffer

/*
  * This class is a singleton that manages all the flushable objects in the system.
  * Anything can register itself to groups and it will be flushed when the system is flushed.
 */
object Flushables {
  private var instance: Flushables = null

  def getInstance: Flushables = {
    if (instance == null) instance = new Flushables
    instance
  }
}

class Flushables private {
  // Create list of flushable objects
  private val flushableList: ListBuffer[Flushable] = ListBuffer[Flushable]()

  def init(): Unit = {
    // Clean the list of flushable objects
    flushableList.clear()
  }

  def add(flushable: Flushable): Unit = {
    // Register the flushable object
    flushableList += flushable
  }

  def flush(): Unit = {
    // Flush all the flushable objects
    flushableList.foreach { flushable =>
      try {
        flushable.flush()
      } catch {
        case e: IOException =>
          throw new RuntimeException(e)
      }
    }
  }
}