package com.kirkwoodwest.openwoods.values;

import com.bitwig.extension.callback.BooleanValueChangedCallback;
import com.bitwig.extension.controller.api.HardwareAction;
import com.bitwig.extension.controller.api.HardwareActionBindable;
import com.bitwig.extension.controller.api.HardwareActionBinding;
import com.bitwig.extension.controller.api.SettableBooleanValue;

import java.util.ArrayList;
import java.util.List;


public class SettableBooleanValueImpl implements SettableBooleanValue {

  //observer array
  private List<BooleanValueChangedCallback> observers = new ArrayList<>();
  private boolean value;

  public SettableBooleanValueImpl(boolean b) {
    this.value = b;
  }

  @Override
  public void set(boolean b) {
    if (this.value != b) {
      this.value = b;
      notifyObservers();
    }
  }

  @Override
  public void toggle() {
    this.value = !this.value;
    notifyObservers();
  }


  @Override
  public boolean get() {
    return this.value;
  }


  private void notifyObservers() {
    for (BooleanValueChangedCallback observer : observers) {
      observer.valueChanged(this.value);
    }
  }

  @Override
  public void addValueObserver(BooleanValueChangedCallback callback) {
    observers.add(callback);
  }

  @Override
  public void markInterested() {

  }

  @Override
  public HardwareActionBindable toggleAction() {
    return null;
  }

  @Override
  public HardwareActionBindable setToTrueAction() {
    return null;
  }

  @Override
  public HardwareActionBindable setToFalseAction() {
    return null;
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

  @Override
  public HardwareActionBinding addBinding(HardwareAction action) {
    return null;
  }

  @Override
  public void invoke() {

  }
}
