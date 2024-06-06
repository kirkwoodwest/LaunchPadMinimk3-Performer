package com.kirkwoodwest.scalawoods.values;

import com.bitwig.extension.callback.BooleanValueChangedCallback;
import com.bitwig.extension.controller.api.BooleanValue;

import java.util.ArrayList;
import java.util.List;


public class BooleanValueImpl implements BooleanValue {

  //observer array
  private List<BooleanValueChangedCallback> observers = new ArrayList<>();
  private boolean value;

  public BooleanValueImpl(boolean b) {
    this.value = b;
  }


  public void set(boolean b) {
    if (this.value != b) {
      this.value = b;
      notifyObservers();
    }
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
