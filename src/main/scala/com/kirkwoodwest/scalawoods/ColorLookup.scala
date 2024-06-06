package com.kirkwoodwest.scalawoods

import com.bitwig.extension.api.Color

object ColorLookup {
  private val BLACK_HSB: Hsb = Hsb(0, 0, 0)

  def toColor(color: Color): Int = {
    if (color == null || color.getAlpha == 0) {
      0
    } else {
      toColor(color.getRed.toFloat, color.getGreen.toFloat, color.getBlue.toFloat)
    }
  }

  def toColor(r: Float, g: Float, b: Float): Int = {
    val rv = math.floor(r * 255).toInt
    val gv = math.floor(g * 255).toInt
    val bv = math.floor(b * 255).toInt
    if (rv < 10 && gv < 10 && bv < 10) {
      0
    } else if (rv > 230 && gv > 230 && bv > 230) {
      3
    } else if (rv == gv && bv == gv) {
      val bright = rv >> 4
      if (bright > 7) {
        2
      } else {
        1
      }
    } else {
      val hsb = rgbToHsb(rv, gv, bv)
      var hueInd = if (hsb.hue > 6) hsb.hue - 1 else hsb.hue
      hueInd = math.min(13, hueInd)
      var color = 5 + hueInd * 4 + 1
      if (hsb.sat < 8) {
        color -= 2
      } else if (hsb.bright <= 8) {
        color += 2
      }
      adjust(color)
    }
  }

  private def adjust(c: Int): Int = {
    val rst = (c - 2) % 4
    if (rst == 0) {
      c - 1
    } else {
      c
    }
  }

  private def rgbToHsb(rv: Float, gv: Float, bv: Float): Hsb = {
    val rgb_max = math.max(math.max(rv, gv), bv)
    val rgb_min = math.min(math.min(rv, gv), bv)
    val bright = rgb_max.toInt
    if (bright == 0) {
      BLACK_HSB
    } else {
      val sat = (255 * (rgb_max - rgb_min) / bright).toInt
      if (sat == 0) {
        BLACK_HSB
      } else {
        var hue = 0f
        if (rgb_max == rv) {
          hue = 0 + 43 * (gv - bv) / (rgb_max - rgb_min)
        } else if (rgb_max == gv) {
          hue = 85 + 43 * (bv - rv) / (rgb_max - rgb_min)
        } else {
          hue = 171 + 43 * (rv - gv) / (rgb_max - rgb_min)
        }
        if (hue < 0) {
          hue = 256 + hue
        }
        Hsb((math.floor(hue / 16.0 + 0.3).toInt), (sat >> 4), (bright >> 4))
      }
    }
  }

  case class Hsb(hue: Int, sat: Int, bright: Int)

  def colorIndexToApiColor(colorIndex: Int): Color = {
    if (colorIndex >= 0 && colorIndex < PALETTE.length) {
      PALETTE(colorIndex)
    } else {
      PALETTE(3)
    }
  }

  private val PALETTE: Array[Color] = Array(
    rgb(97, 97, 97),
    rgb(179, 179, 179),
    rgb(221, 221, 221),
    rgb(255, 255, 255),
    rgb(250, 179, 178),
    rgb(246, 99, 102),
    rgb(215, 98, 99),
    rgb(188, 89, 101),
    rgb(254, 242, 214),
    rgb(250, 176, 112),
    rgb(225, 135, 99),
    rgb(175, 118, 100),
    rgb(253, 252, 172),
    rgb(253, 253, 104),
    rgb(220, 221, 100),
    rgb(180, 186, 97),
    rgb(219, 255, 155),
    rgb(194, 255, 104),
    rgb(194, 255, 104),
    rgb(135, 180, 94),
    rgb(196, 252, 180),
    rgb(117, 250, 110),
    rgb(99, 228, 86),
    rgb(104, 180, 96),
    rgb(191, 255, 191),
    rgb(109, 254, 142),
    rgb(109, 219, 127),
    rgb(102, 180, 106),
    rgb(186, 255, 196),
    rgb(125, 250, 205),
    rgb(103, 226, 160),
    rgb(117, 174, 132),
    rgb(199, 255, 243),
    rgb(112, 254, 229),
    rgb(110, 222, 191),
    rgb(105, 183, 146),
    rgb(197, 244, 254),
    rgb(117, 238, 252),
    rgb(106, 202, 218),
    rgb(106, 202, 218),
    rgb(196, 221, 254),
    rgb(113, 201, 245),
    rgb(108, 161, 218),
    rgb(103, 130, 178),
    rgb(162, 137, 253),
    rgb(105, 100, 243),
    rgb(102, 97, 223),
    rgb(99, 97, 179),
    rgb(207, 178, 254),
    rgb(165, 94, 253),
    rgb(129, 98, 223),
    rgb(120, 96, 185),
    rgb(245, 185, 252),
    rgb(242, 104, 247),
    rgb(216, 100, 220),
    rgb(172, 100, 180),
    rgb(251, 180, 216),
    rgb(249, 93, 193),
    rgb(218, 101, 165),
    rgb(178, 93, 137),
    rgb(246, 119, 104)
  )

  private def rgb(r: Int, g: Int, b: Int): Color = {
    Color.fromRGB255(r, g, b)
  }
}
