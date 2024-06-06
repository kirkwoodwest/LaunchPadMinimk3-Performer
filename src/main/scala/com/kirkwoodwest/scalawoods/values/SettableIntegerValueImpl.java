package com.kirkwoodwest.scalawoods.values;

import com.bitwig.extension.callback.IntegerValueChangedCallback;
import com.bitwig.extension.controller.api.RelativeHardwareControl;
import com.bitwig.extension.controller.api.RelativeHardwareControlBinding;
import com.bitwig.extension.controller.api.SettableIntegerValue;

import java.util.ArrayList;
import java.util.List;

public class SettableIntegerValueImpl implements SettableIntegerValue {

  private int value;
  private final List<IntegerValueChangedCallback> observers = new ArrayList<>();

  public SettableIntegerValueImpl(int value) {
    this.value = value;
  }

  @Override
  public void set(int value) {
    if (value != this.value) {
      this.value = value;
      updateObservers();
    }
  }

  @Override
  public void inc(int amount) {
    set(get() + amount);
  }

  @Override
  public int get() {
    return value;
  }

  private void updateObservers() {
    for (IntegerValueChangedCallback observer : observers) {
      observer.valueChanged(value);
    }
  }

  @Override
  public void addValueObserver(IntegerValueChangedCallback callback) {
    observers.add(callback);
  }

  //The rest are unimplemented methods

  @Override
  public void addValueObserver(IntegerValueChangedCallback callback, int valueWhenUnassigned) {
    observers.add(callback);
  }

  @Override
  public RelativeHardwareControlBinding addBindingWithSensitivity(RelativeHardwareControl hardwareControl, double sensitivity) {
    return null;
  }

  @Override
  public void markInterested() {
  }

  @Override
  public boolean isSubscribed() {
    return false;
  }

  @Override @Deprecated
  public void setIsSubscribed(boolean value) {
  }

  @Override
  public void subscribe() {

  }

  @Override
  public void unsubscribe() {
  }
}
