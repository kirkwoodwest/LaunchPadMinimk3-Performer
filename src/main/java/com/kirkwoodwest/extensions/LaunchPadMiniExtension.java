package com.kirkwoodwest.extensions;

import com.bitwig.extension.callback.ShortMidiDataReceivedCallback;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorTrack;
import com.kirkwoodwest.launchpadminimk3.DoubleGrid;
import com.kirkwoodwest.launchpadminimk3.hardware.HardwareLaunchPadMiniMK3;
import com.kirkwoodwest.openwoods.trackbank.CursorTrackBank;
import com.kirkwoodwest.openwoods.trackbank.CursorTrackHelper;
import com.kirkwoodwest.openwoods.utils.LogUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LaunchPadMiniExtension extends ControllerExtension {
    private HardwareLaunchPadMiniMK3 hardware;

    protected LaunchPadMiniExtension(LaunchPadMiniExtensionDefinition definition, ControllerHost host) {
        super(definition, host);
    }

    @Override
    public void init() {
        LogUtil.init(getHost());
        ShortMidiDataReceivedCallback midiCallbackFunc = (d, d2, d3) -> {
            getHost().println("Button Pressed: " + d + " " + d2 + " " + d3);
        };

        hardware = new HardwareLaunchPadMiniMK3(getHost(), 0, midiCallbackFunc);

        CursorTrackHelper cursorTrackHelper = new CursorTrackHelper(getHost(), 64);

        CursorTrackBank cursorTrackBank = new CursorTrackBank(getHost(), cursorTrackHelper, 16, 0, 4, "Clip Launcher");

        List<CursorTrack> cursorList = StreamSupport.stream(cursorTrackBank.getCursors().spliterator(), false)
                .collect(Collectors.toList());

        new DoubleGrid(getHost(), "DoubleGrid", cursorList, hardware);

        getHost().println("Initializing LaunchPad Mini Extension");
    }

    @Override
    public void exit() {
        LogUtil.reportExtensionStatus(this, "Exited");
    }

    @Override
    public void flush() {
        hardware.flush();
    }
}
