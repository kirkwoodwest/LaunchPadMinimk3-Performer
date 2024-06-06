package com.kirkwoodwest.scalawoods.trackbank;

public class OscTrackBankPaths {
  private final String path;

  public OscTrackBankPaths(String oscPath) {
    this.path = oscPath;
  }
  private String getTrackPath(int index) {
    return path + "/" + index;
  }

  public String getName(int index) { return path + "/" + index + "/name"; }
  public String getTrackExists(int index) { return getTrackPath(index) + "/exists"; }
  public String getSolo(int index) { return getTrackPath(index) + "/solo"; }
  public String getMute(int index) {
    return getTrackPath(index) + "/mute";
  }
  public String getActivated(int index) {
    return getTrackPath(index)  + "/activated";
  }
  public String getArm(int index) {
    return getTrackPath(index)+ "/arm";
  }
  public String getSelected(int index) { return getTrackPath(index) + "/selected";}

  public String getSelect(int index) { return getTrackPath(index) + "/select"; }

  public String getVolume(int index) {
    return getTrackPath(index) + "/volume";
  }

  public String getPan(int oscIndex) { return getTrackPath(oscIndex) + "/pan"; }

  public String getTrackType(int oscIndex) { return getTrackPath(oscIndex) + "/type"; }

  public String getTrackColor(int oscIndex) {
    return getTrackPath(oscIndex) + "/color";
  }

  public String getSend(int trackIndex, int index) {
    return getTrackPath(trackIndex) + "/send/" + index;
  }

  public String getSendColor(int trackIndex, int sendIndex) {
    return getSend(trackIndex, sendIndex) + "/color";
  }


}
