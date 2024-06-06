package com.kirkwoodwest.scalawoods.utils

object StringUtil {
  //Pads an int with 0s on the left
  def padInt(padding: Int, value: Int): String = String.format("%0" + padding + "d", value)

  //pad int pads the integer with spaces on the right
  def padIntRight(padding: Int, value: Int): String = String.format("%-" + padding + "d", value)

  /**
   * Converts a double value to a string with a specified number of decimal places.
   *
   * @param value    the double value to format.
   * @param decimals the number of decimal places.
   * @return the formatted string.
   */
  def formatDouble(value: Double, decimals: Int): String = {
    if (decimals < 0) throw new IllegalArgumentException("Decimal places cannot be negative.")
    val format = "%." + decimals + "f"
    val s = String.format(format, value)
    s
  }
}