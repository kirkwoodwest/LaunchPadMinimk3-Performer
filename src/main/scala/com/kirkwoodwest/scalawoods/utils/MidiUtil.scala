package com.kirkwoodwest.scalawoods.utils

/**
 * Some basic helpers for midi
 */
object MidiUtil {
  val NOTE_ON = 0x90
  val NOTE_OFF = 0x80
  val CC = 0xB0

  def random_127: Int = (Math.random * 127).toInt

  def doubleTo127(value: Double): Int = (value * 127).toInt
  
  def intToHex(value: Int): String = value.toHexString
}

class MidiUtil {
  def getStatusWithChannel(status: Int, channel: Int): Int = status | channel
}