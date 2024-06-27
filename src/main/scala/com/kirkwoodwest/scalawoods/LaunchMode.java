package com.kirkwoodwest.scalawoods;

import com.bitwig.extension.controller.api.EnumDefinition;
import com.bitwig.extension.controller.api.EnumValueDefinition;

public class LaunchMode implements EnumDefinition {

  //Launch Modes
  public static final String LAUNCH_MODE_CLIP = "Clip";
  public static final String LAUNCH_MODE_SCENE = "Scene";
  public static final String[] LAUNCH_MODES = {LAUNCH_MODE_CLIP, LAUNCH_MODE_SCENE};

  public LaunchMode() {
  }

  @Override
  public int getValueCount() {
    return 2;
  }

  @Override
  public EnumValueDefinition valueDefinitionAt(int valueIndex) {
    return new EnumValueDefinition() {
      @Override
      public EnumDefinition enumDefinition() {
        return new LaunchMode();
      }

      @Override
      public int getValueIndex() {
        return valueIndex;
      }

      @Override
      public String getId() {
        return LAUNCH_MODES[valueIndex];
      }

      @Override
      public String getDisplayName() {
        return LAUNCH_MODES[valueIndex];
      }

      @Override
      public String getLimitedDisplayName(int maxLength) {
        return LAUNCH_MODES[valueIndex].substring(0, maxLength);
      }
    };
  }

  @Override
  public EnumValueDefinition valueDefinitionFor(String id) {
    for (int i = 0; i < LAUNCH_MODES.length; i++) {
      if (LAUNCH_MODES[i].equals(id)) {
        return valueDefinitionAt(i);
      }
    }
    return null;
  }
}

