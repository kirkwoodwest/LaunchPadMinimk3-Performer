package com.kirkwoodwest.scalawoods

import com.kirkwoodwest.scalawoods.utils.ColorUtil

object LaunchPadMiniMK3Colors {
  val hexColorMap: List[String] = List(
    "333333", // 2
    "888888", // 3
    "AAAAAA", // 4
    "df73c7", // 5
    "e8100b", // 6
    "e12118", // 7
    "d13d31", // 8
    "bbb9ee", // 9
    "da7c39", // 10
    "ba7038", // 11
    "ad542d", // 12
    "cad0b6", // 13
    "bbde70", // 14
    "a8c85f", // 15
    "9db85e", // 16
    "86e1b6", // 17
    "58de6a", // 18
    "4eb655", // 19
    "46a350", // 20
    "3edcc5", // 21
    "02db73", // 22
    "08c75f", // 23
    "1fb85c", // 24
    "57e7e4", // 25
    "01d3a9", // 26
    "04af7d", // 27
    "08a260", // 28
    "3fe0de", // 29
    "00dbe8", // 30
    "01c2c2", // 31
    "0db1b3", // 32
    "4cdcf4", // 33
    "00d8f1", // 34
    "00a3d2", // 35
    "028db6", // 36
    "39cef7", // 37
    "00a7f6", // 38
    "007eec", // 39
    "0d6ccc", // 40
    "4fa2f4", // 41
    "006ff5", // 42
    "004be8", // 43
    "0143dc", // 44
    "1732ed", // 45
    "0001f0", // 46
    "0103ea", // 47
    "070de4", // 48
    "6777f6", // 49
    "2301f3", // 50
    "1b01eb", // 51
    "1804dc", // 52
    "c369f8", // 53
    "b000f5", // 54
    "8300ef", // 55
    "7c0be5", // 56
    "d271f1", // 57
    "e801e2", // 58
    "c60ccd", // 59
    "b40fcc", // 60
    "e93517", // 61
    "bd934c", // 62
    "b9ad56", // 63
    "9d9f62", // 65
    "3ab061", // 66
    "25a9b9", // 67
    "0a41ea", // 68
    "0202f2", // 69
    "1979c5", // 70
    "2c04f1", // 71
    "8f62e7", // 72
    "8866b6", // 73
    "e6172f", // 74
    "9bd0b6", // 75
    "9cc972", // 76
    "6fd371", // 77
    "39b962", // 78
    "03ccd4", // 79
    "0188f3", // 80
    "0148f5", // 81
    "2303f4", // 82
    "5c07f2", // 83
    "a841ed", // 84
    "945f44", // 85
    "d26143", // 86
    "88c66a", // 87
    "80d2a5", // 88
    "08e27c", // 89
    "58d4bf", // 90
    "5fc1dc", // 91
    "4abdee", // 92
    "537ff3", // 93
    "335ff1", // 94
    "5c5eed", // 95
    "9330f4", // 96
    "e60bf0", // 97
    "da824f", // 98
    "b8c374", // 99
    "8ed078", // 100
    "ada363", // 101
    "8f7f50", // 102
    "229d8b", // 103
    "46a0b7", // 104
    "5e62d9", // 105
    "3d57ec", // 106
    "ab8c9a", // 107
    "c9272c", // 108
    "cf75ae", // 109
    "ce756c", // 110
    "c2b694", // 111
    "aedab6", // 112
    "8cd695", // 113
    "5866d5", // 114
    "a9bfe2", // 115
    "72beeb", // 116
    "758ef6", // 117
    "6d6cf6", // 118
    "7588d8", // 119
    "8396eb", // 120
    "8db6f8", // 121
    "e1212e", // 122
    "c13135", // 123
    "38d26f", // 124
    "31a45f", // 125
    "c1c56f", // 126
    "b2aa61", // 127
    "d69a58" // 128
  )

  private val colorMap: Array[Array[Int]] = makeRGBColorMap

  // Builds color map for class...
  private def makeRGBColorMap: Array[Array[Int]] = {
    hexColorMap.map(ColorUtil.hex2Rgb).toArray
  }

  def getIndexBlack: Int = 0

  def getIndexFromRGB(R: Int, G: Int, B: Int): Int = {
    if (R == 0 && G == 0 && B == 0) return 0 // return 0 if black

    // Get distance from RGB
    var distance: Float = 900
    var shortestDistanceIndex = 0

    for ((color, i) <- colorMap.zipWithIndex) {
      val Array(r2, g2, b2) = color.map(_ / 255.0f)
      val c1 = Array(r2, g2, b2)
      val c2 = Array(R, G, B).map(_ / 255.0f)

      val newDistance: Float = ColorUtil.getDistance(c1(0), c1(1), c1(2), c2(0), c2(1), c2(2))
      if (newDistance < distance) {
        shortestDistanceIndex = i
        distance = newDistance
      }
    }

    shortestDistanceIndex + 1 // First color is skipped so we add one to the index
  }
}
