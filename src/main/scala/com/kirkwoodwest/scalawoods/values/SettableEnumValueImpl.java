package com.kirkwoodwest.scalawoods.values;

import com.bitwig.extension.callback.EnumValueChangedCallback;
import com.bitwig.extension.controller.api.EnumDefinition;
import com.bitwig.extension.controller.api.EnumValueDefinition;
import com.bitwig.extension.controller.api.SettableEnumValue;

public class SettableEnumValueImpl implements SettableEnumValue {


  private EnumDefinition enumDefinition;
  private String value;

  public SettableEnumValueImpl(EnumDefinition enumDefinition, String value) {
    this.enumDefinition = enumDefinition;
    this.value = value;
  }

  @Override
  public void set(String value) {
    this.value = value;
    EnumValueDefinition enumValueDefinition = enumDefinition.valueDefinitionFor(value);
    enumValueDefinition.getValueIndex();
  }

  @Override
  public String get() {
    return "";
  }

  @Override
  public EnumDefinition enumDefinition() {
    return this.enumDefinition;
  }

  @Override
  public void markInterested() {

  }

  @Override
  public void addValueObserver(EnumValueChangedCallback callback) {

  }

  @Override
  public boolean isSubscribed() {
    return false;
  }

  @Override
  public void setIsSubscribed(boolean value) {

  }

  @Override
  public void subscribe() {

  }

  @Override
  public void unsubscribe() {

  }
}
