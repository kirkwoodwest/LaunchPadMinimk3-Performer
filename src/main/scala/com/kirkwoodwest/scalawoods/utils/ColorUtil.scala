package com.kirkwoodwest.scalawoods.utils

import com.bitwig.extension.api.Color

import java.util
import java.lang.Math.pow
import java.lang.Math.sqrt
import scala.collection.mutable.ListBuffer

object ColorUtil {
  /**
   * @param colorStr e.g. "#FFFFFF"
   * @return
   */
  def hex2Rgb(colorStr: String): Array[Int] = {
    val r = colorStr.substring(0, 2)
    val g = colorStr.substring(2, 4)
    val b = colorStr.substring(4, 6)
    val color = Array[Int](Integer.valueOf(r, 16), Integer.valueOf(g, 16), Integer.valueOf(b, 16))
    color
  }

  def getDistance(r1: Float, g1: Float, b1: Float, r2: Float, g2: Float, b2: Float): Float = {
    //A simple weighting can be derived from RGB to grayscale weigthing (Converting RGB to grayscale/intensity)
    (Math.abs(r1 - r2) * 0.2989F) + (Math.abs(g1 - g2) * 0.5870F) + (Math.abs(b1 - b2) * 0.1140F)
  }

  def getDistance(r1: Double, g1: Double, b1: Double, r2: Double, g2: Double, b2: Double): Double = {
    //A simple weighting can be derived from RGB to grayscale weighting (Converting RGB to grayscale/intensity)
    (Math.abs(r1 - r2) * 0.2989) + (Math.abs(g1 - g2) * 0.5870) + (Math.abs(b1 - b2) * 0.1140)
  }

  def getDistance(c1: Color, c2: Color): Double = {
    val rDiff = c1.getRed - c2.getRed
    val gDiff = c1.getGreen - c2.getGreen
    val bDiff = c1.getBlue - c2.getBlue
    Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff)
  }

  def doDistance(r1: Double, g1: Double, b1: Double, r2: Double, g2: Double, b2: Double): Double = {
    //A simple weighting can be derived from RGB to grayscale weigthing (Converting RGB to grayscale/intensity)
    sqrt(pow(r2 - r1, 2.0) + pow(g2 - g1, 2.0) + pow(b2 - b1, 2.0))
  }

  def compare(c1: Color, c2: Color): Boolean = c1.getRed.compare(c2.getRed) == 0 && c1.getGreen.compare(c2.getGreen) == 0 && c1.getBlue.compare(c2.getBlue) == 0

  def getIndexFromColor(color: Color, map: ListBuffer[Color]): Int = {
    if (Color.blackColor.eq(color)) return 0
    //Get distance from RGB
    var distance = 900.0
    var shortest_distance_index = 0
    val size = map.size
    for (i <- 0 until size) {
      val mappedColor = map(i)
      val new_distance = ColorUtil.getDistance(color, mappedColor)
      if (new_distance < distance) {
        shortest_distance_index = i
        distance = new_distance
      }
    }
    shortest_distance_index
  }
}