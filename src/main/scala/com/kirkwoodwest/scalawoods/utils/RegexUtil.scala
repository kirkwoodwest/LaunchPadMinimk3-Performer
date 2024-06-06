package com.kirkwoodwest.scalawoods.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object RegexUtil {
  def stringMatch(lookup_string: String, `match`: String): Boolean = {
    val p = Pattern.compile("" + `match`, Pattern.CASE_INSENSITIVE)
    val m = p.matcher(lookup_string)
    val b = m.find
    b
  }
}