package com.kirkwoodwest.scalawoods.utils

/**
 * Custom Math Class for remapping values
 */
object MathUtil {
  def doubleToRange(v: Double, range: Double): Int = {
    val new_v = v * range
    new_v.round.toInt
  }

  def map(x: Double, in_min: Double, in_max: Double, out_min: Double, out_max: Double): Double = (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min

  def valueLimit(x: Double, min: Double, max: Double): Double = {
    math.min(math.max(x, min), max)
  }


  def getDistance(r1: Float, g1: Float, b1: Float, r2: Float, g2: Float, b2: Float): Float = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2)

  def padInteger(n: Int, length: Int): String = {
    val input_string = String.valueOf(n)
    if (input_string.length >= length) return input_string
    var padding_string = ""
    while (padding_string.length < length - input_string.length) padding_string = padding_string + "0"
    padding_string + input_string
  }

  //wraps a number around a limit
  def wrapNumber(number: Int, limit: Int): Int = {
    var target_index = number - limit
    if (target_index < 0) target_index = limit + 1 + target_index
    target_index
  }

  //wraps a number around a limit
  def wrapRange(x: Double, x_min: Double, x_max: Double): Double = {
   ((x - x_min) % (x_max - x_min)) + x_min
  }

  //wraps a number around a limit
  def wrapRange(x: Float, x_min: Float, x_max: Float): Float = {
    ((x - x_min) % (x_max - x_min)) + x_min
  }
}