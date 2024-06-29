package com.kirkwoodwest.openwoods.values;

import com.bitwig.extension.callback.StringValueChangedCallback;
import com.bitwig.extension.controller.api.SettableStringValue;

import java.util.ArrayList;
import java.util.List;

public class SettableStringValueImpl implements SettableStringValue {
  private String value;
  private final List<StringValueChangedCallback> observers = new ArrayList<>();

  public SettableStringValueImpl(String s) {
    value = s;
  }

  @Override
  public void set(String value) {
    if (!value.equals(this.value)) {
      this.value = value;
      updateObservers();
    }

  }

  private void updateObservers() {
    for (StringValueChangedCallback observer : observers) {
      observer.valueChanged(value);
    }
  }

  @Override
  public String get() {
    return value;
  }

  @Override
  public String getLimited(int maxLength) {
    return value.substring(0, maxLength);
  }

  @Override
  public void addValueObserver(StringValueChangedCallback callback) {
    observers.add(callback);
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
