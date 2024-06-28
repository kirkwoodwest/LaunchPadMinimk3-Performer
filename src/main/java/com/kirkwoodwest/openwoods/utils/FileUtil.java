package com.kirkwoodwest.openwoods.utils;

import com.bitwig.extension.controller.api.ControllerHost;

public class FileUtil {
  //Static method to get file path
  public static String getPath(final ControllerHost host, final String file)
  {
    switch (host.getPlatformType())
    {
      case WINDOWS:
        final String userProfile = System.getenv("USERPROFILE").replace("\\", "/");
        return userProfile + "/Documents/Bitwig Studio/Extensions/" + file;

      case MAC:
        return System.getProperty("user.home") + "/Documents/Bitwig Studio/Extensions/" + file;

      case LINUX:
        return System.getProperty("user.home") + "/Bitwig Studio/Extensions/" + file;

      default:
        throw new IllegalArgumentException("Unknown Platform");
    }
  }
}
