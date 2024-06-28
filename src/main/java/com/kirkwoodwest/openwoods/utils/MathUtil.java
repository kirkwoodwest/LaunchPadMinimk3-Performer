package com.kirkwoodwest.openwoods.utils;

/**
 * Custom Math Class for remapping values
 */
public class MathUtil {

  public static int doubleToRange(double v, double range){
    double new_v = v * range;
    return (int) Math.round(new_v);
  }

  public static double map(double x, double in_min, double in_max, double out_min, double out_max) {
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
  }

  public static double valueLimit(double x, double min, double max) {
    return (x > min) ? ((x < max) ? x : max) : min;
  }


  public static float getDistance(float r1, float g1, float b1, float r2, float g2, float b2) {
    return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
  }

  public static String padInteger(int n, int length){
    String input_string = String.valueOf(n);
    if (input_string.length() >= length) {
      return input_string;
    }
    String padding_string = "";
    while (padding_string.length() < length - input_string.length()) {
      padding_string = padding_string + "0";
    }
    return padding_string + input_string;
  }

  //wraps a number around a limit
  public static int wrapNumber(int number, int limit) {
    int target_index = number - limit;
    if(target_index<0) {
      target_index = limit+1 + target_index;
    }
    return target_index;
  }

  //wraps a number around a limit
  public static double wrapRange(double x, double x_min, double x_max) {
    x = ((x-x_min) % (x_max - x_min) ) + x_min;
    return x;
  }

  //wraps a number around a limit
  public static float wrapRange(float x, float x_min, float x_max) {
    x = ((x - x_min) % (x_max - x_min)) + x_min;
    return x;
  }
}
