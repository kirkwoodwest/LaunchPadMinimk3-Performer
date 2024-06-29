package com.kirkwoodwest.launchpadminimk3;

import com.bitwig.extension.controller.api.BooleanValue;
import com.bitwig.extension.controller.api.SettableBooleanValue;
import com.bitwig.extension.controller.api.Transport;
import com.kirkwoodwest.openwoods.values.SettableBooleanValueImpl;

public class FillTimer {

  private final Transport transport;
  private String oldPos;

private SettableBooleanValue timerActive = new SettableBooleanValueImpl(false);
  public FillTimer(Transport transport) {
    this.transport = transport;
    transport.getPosition().addValueObserver(this::onPositionChanged);
  }

  public void engageTimedFill(boolean b) {
    timerActive.set(b);
    this.transport.isFillModeActive().set(true);
    setOldTime();
  }

  private void onPositionChanged(double v) {

    if(timerActive.get()) {
      //Get the bars data out of positions for compare.
      String pos     = transport.getPosition().getFormatted();
      String[] split_pos     = pos.split(":");
      String[] split_old_pos = oldPos.split(":");

      int      bars          = Integer.parseInt(split_pos[0]);
      int      beats         = Integer.parseInt(split_pos[1]);
      int      blah    			 = Integer.parseInt(split_pos[2]);

      int      bars_old      = Integer.parseInt(split_old_pos[0]);
      int      beats_old     = Integer.parseInt(split_old_pos[1]);
      int      blah_old     = Integer.parseInt(split_old_pos[2]);

      if(beats ==4 && blah == 4) {
        //always disengage on the last beat and blah.
        timerActive.set(false);
        this.transport.isFillModeActive().set(false);
      }
    }
  }

  public void reset() {
    setOldTime();
  }

  private void setOldTime() {
    oldPos = transport.getPosition().getFormatted();
  }

  public BooleanValue timerActive() {
    return timerActive;
  }
}
