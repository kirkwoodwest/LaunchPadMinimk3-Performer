package com.kirkwoodwest.launchpadminimk3;

import com.bitwig.extension.controller.api.*;

public class CopySlots {

  private final CursorTrack floatingCursorTrack;
  private final ClipLauncherSlotBank selectClipLauncherSlotBank;
  private int slot;

  public CopySlots(ControllerHost host) {
    floatingCursorTrack = host.createCursorTrack("copyslots", "copyslots", 8, 1024, false);
    selectClipLauncherSlotBank = floatingCursorTrack.clipLauncherSlotBank();
  }

  public void setCursor(Track track) {
    floatingCursorTrack.isPinned().set(false);
    floatingCursorTrack.selectChannel(track);
    floatingCursorTrack.isPinned().set(true);
  }

  public void setSlot(int slot) {
    this.slot = slot;
    selectClipLauncherSlotBank.getItemAt(slot).showInEditor();
  }

  public ClipLauncherSlot getSlot() {
    return floatingCursorTrack.clipLauncherSlotBank().getItemAt(slot);
  }

}
