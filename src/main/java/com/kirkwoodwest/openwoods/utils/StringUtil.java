package com.kirkwoodwest.openwoods.utils;

public class StringUtil {
  //Pads an int with 0s on the left
  public static String padInt(int padding, int value){
    return String.format("%0" + padding + "d", value);
  }

  //pad int pads the integer with spaces on the right
  public static String padIntRight(int padding, int value){
    return String.format("%-" + padding + "d", value);
  }

  /**
   * Converts a double value to a string with a specified number of decimal places.
   *
   * @param value the double value to format.
   * @param decimals the number of decimal places.
   * @return the formatted string.
   */
  public static String formatDouble(double value, int decimals) {
    if (decimals < 0) {
      throw new IllegalArgumentException("Decimal places cannot be negative.");
    }
    String format = "%." + decimals + "f";
    String s = String.format(format, value);

    return s;
  }

}
