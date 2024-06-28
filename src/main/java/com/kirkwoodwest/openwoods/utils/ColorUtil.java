package com.kirkwoodwest.openwoods.utils;

import com.bitwig.extension.api.Color;

import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ColorUtil {
	/**
	 * @param colorStr e.g. "#FFFFFF"
	 * @return
	 */
	public static int[] hex2Rgb(String colorStr) {
		String r = colorStr.substring(0, 2);
		String g = colorStr.substring(2, 4);
		String b = colorStr.substring(4, 6);
		final int[] color = new int[]{
						Integer.valueOf(r, 16),
						Integer.valueOf(g, 16),
						Integer.valueOf(b, 16)
		};
		return color;
	}

	public static float getDistance(float r1, float g1, float b1, float r2, float g2, float b2) {
		//A simple weighting can be derived from RGB to grayscale weigthing (Converting RGB to grayscale/intensity)
		return (Math.abs(r1 - r2) * 0.2989F) + (Math.abs(g1 - g2) * 0.5870F) + (Math.abs(b1 - b2) * 0.1140F);
	}

	public static double getDistance(double r1, double g1, double b1, double r2, double g2, double b2) {
		//A simple weighting can be derived from RGB to grayscale weighting (Converting RGB to grayscale/intensity)
		return (Math.abs(r1 - r2) * 0.2989) + (Math.abs(g1 - g2) * 0.5870) + (Math.abs(b1 - b2) * 0.1140);
	}

	public static double getDistance(Color c1, Color c2) {
		double rDiff = c1.getRed() - c2.getRed();
		double gDiff = c1.getGreen() - c2.getGreen();
		double bDiff = c1.getBlue() - c2.getBlue();

		return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
	}

	public static double doDistance(double r1, double g1, double b1, double r2, double g2, double b2) {
		//A simple weighting can be derived from RGB to grayscale weigthing (Converting RGB to grayscale/intensity)
		return sqrt(pow(r2 - r1, 2.0) + pow(g2 - g1, 2.0) + pow(b2 - b1, 2.0));
	}

	public static boolean compare(Color c1, Color c2) {
		return Double.compare(c1.getRed(), c2.getRed()) == 0 &&
						Double.compare(c1.getGreen(), c2.getGreen()) == 0 &&
						Double.compare(c1.getBlue(), c2.getBlue()) == 0;
	}

	static public int getIndexFromColor(Color color, List<Color> map) {
		if (Color.blackColor().equals(color)) return 0;
		//Get distance from RGB
		double distance = 900.0;
		int shortest_distance_index = 0;
		int size = map.size();
		for (int i = 0; i < size; i++) {
			Color mappedColor = map.get(i);
			double new_distance = ColorUtil.getDistance(color, mappedColor);
			if (new_distance < distance) {
				shortest_distance_index = i;
				distance = new_distance;
			}
		}
		return shortest_distance_index;
	}
}
