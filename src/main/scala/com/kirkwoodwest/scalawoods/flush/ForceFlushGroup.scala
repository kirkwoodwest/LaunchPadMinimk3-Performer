package com.kirkwoodwest.scalawoods.flush

import java.util
import scala.collection.mutable.ListBuffer

/**
 * A class to manage a list of groups that need to be force flushed. Clears itself after a call to get()
 *
 */
class ForceFlushGroup {
  private[flush] val groups = ListBuffer[String]() 

  def add(groupId: String): Unit = {
    groups += groupId
  }

  def get: ListBuffer[String] = {
    if (groups.isEmpty) 
      groups
    else {
      val tempGroups = ListBuffer[String]()
      groups.clear()
      tempGroups
    }
  }
}