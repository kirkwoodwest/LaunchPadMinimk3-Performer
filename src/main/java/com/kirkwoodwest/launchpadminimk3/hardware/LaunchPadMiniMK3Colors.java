package com.kirkwoodwest.launchpadminimk3.hardware;

import com.kirkwoodwest.openwoods.utils.ColorUtil;
import java.util.List;
import java.util.Arrays;

public class LaunchPadMiniMK3Colors {
    private static final List<String> hexColorMap = Arrays.asList(
        "333333", "888888", "AAAAAA", "df73c7", "e8100b", "e12118", "d13d31", "bbb9ee",
        "da7c39", "ba7038", "ad542d", "cad0b6", "bbde70", "a8c85f", "9db85e", "86e1b6",
        "58de6a", "4eb655", "46a350", "3edcc5", "02db73", "08c75f", "1fb85c", "57e7e4",
        "01d3a9", "04af7d", "08a260", "3fe0de", "00dbe8", "01c2c2", "0db1b3", "4cdcf4",
        "00d8f1", "00a3d2", "028db6", "39cef7", "00a7f6", "007eec", "0d6ccc", "4fa2f4",
        "006ff5", "004be8", "0143dc", "1732ed", "0001f0", "0103ea", "070de4", "6777f6",
        "2301f3", "1b01eb", "1804dc", "c369f8", "b000f5", "8300ef", "7c0be5", "d271f1",
        "e801e2", "c60ccd", "b40fcc", "e93517", "bd934c", "b9ad56", "9d9f62", "3ab061",
        "25a9b9", "0a41ea", "0202f2", "1979c5", "2c04f1", "8f62e7", "8866b6", "e6172f",
        "9bd0b6", "9cc972", "6fd371", "39b962", "03ccd4", "0188f3", "0148f5", "2303f4",
        "5c07f2", "a841ed", "945f44", "d26143", "88c66a", "80d2a5", "08e27c", "58d4bf",
        "5fc1dc", "4abdee", "537ff3", "335ff1", "5c5eed", "9330f4", "e60bf0", "da824f",
        "b8c374", "8ed078", "ada363", "8f7f50", "229d8b", "46a0b7", "5e62d9", "3d57ec",
        "ab8c9a", "c9272c", "cf75ae", "ce756c", "c2b694", "aedab6", "8cd695", "5866d5",
        "a9bfe2", "72beeb", "758ef6", "6d6cf6", "7588d8", "8396eb", "8db6f8", "e1212e",
        "c13135", "38d26f", "31a45f", "c1c56f", "b2aa61", "d69a58"
    );

    private static final int[][] colorMap = makeRGBColorMap();

    private static int[][] makeRGBColorMap() {
        int[][] map = new int[hexColorMap.size()][];
        for (int i = 0; i < hexColorMap.size(); i++) {
            map[i] = ColorUtil.hex2Rgb(hexColorMap.get(i));
        }
        return map;
    }

    public static int getIndexBlack() {
        return 0;
    }

    public static int getIndexFromRGB(int R, int G, int B) {
        if (R == 0 && G == 0 && B == 0) {
            return 0; // return 0 if black
        }

        float distance = 900;
        int shortestDistanceIndex = 0;

        for (int i = 0; i < colorMap.length; i++) {
            int[] color = colorMap[i];
            float r2 = color[0] / 255.0f;
            float g2 = color[1] / 255.0f;
            float b2 = color[2] / 255.0f;
            float[] c1 = {r2, g2, b2};
            float[] c2 = {R / 255.0f, G / 255.0f, B / 255.0f};

            float newDistance = ColorUtil.getDistance(c1[0], c1[1], c1[2], c2[0], c2[1], c2[2]);
            if (newDistance < distance) {
                shortestDistanceIndex = i;
                distance = newDistance;
            }
        }

        return shortestDistanceIndex + 1; // First color is skipped so we add one to the index
    }
}
