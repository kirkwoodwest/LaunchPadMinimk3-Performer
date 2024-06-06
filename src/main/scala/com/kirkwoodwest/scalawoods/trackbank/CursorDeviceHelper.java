package com.kirkwoodwest.scalawoods.trackbank;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.Device;
import com.bitwig.extension.controller.api.DeviceBank;

import java.util.stream.IntStream;

/**
 * A basic implementation of device bank which allows you to get visible devices by name or index.
 * Useful for getting a cursor device to pin to a specific device.
 *
 * This item will always be locked to scroll position 0, and will always be in sync with current view.
 */
public class CursorDeviceHelper implements Helper{
  private final DeviceBank deviceBank;
  private final ControllerHost host;

  public CursorDeviceHelper(ControllerHost host, CursorTrack cursorTrack, int numDevices){
    this.host = host;
    deviceBank = cursorTrack.createDeviceBank(numDevices);
    deviceBank.scrollPosition().markInterested();
    IntStream.range(0, deviceBank.getSizeOfBank())
            .forEach(deviceIndex -> {
              deviceBank.getItemAt(deviceIndex).name().markInterested();
              deviceBank.getItemAt(deviceIndex).exists().markInterested();
            });
  }

  public Device getDevice(int index){
    if(index < 0 || index >= deviceBank.getSizeOfBank()){
      return null;
    }
    return deviceBank.getItemAt(index);
  }

  public Device getDeviceByName(String name){
    return IntStream.range(0, deviceBank.getSizeOfBank())
            .mapToObj(deviceBank::getItemAt)
            .filter(deviceItem -> deviceItem.name().get().equals(name))
            .findFirst().orElse(null);
  }

  public int getIndexByName(String name){
    return IntStream.range(0, deviceBank.getSizeOfBank())
            .filter(deviceIndex -> deviceBank.getItemAt(deviceIndex).name().get().equals(name))
            .findFirst().orElse(-1);
  }

  public int getSizeOfBank() {
    return this.deviceBank.getSizeOfBank();
  }
}
