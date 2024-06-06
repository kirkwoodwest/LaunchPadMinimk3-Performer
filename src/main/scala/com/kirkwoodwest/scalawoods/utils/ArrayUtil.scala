package com.kirkwoodwest.scalawoods.utils

object ArrayUtil {
  def indexOfIntArray(array: Array[Int], key: Int): Int = {
    array.indexOf(key)
  }

  def generateIncrementalArray(startValue: Int, indexCount: Int): Array[Int] = {
    Array.tabulate(indexCount)(_ + startValue)
  }
}