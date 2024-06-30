package com.kirkwoodwest.extensions;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

import java.util.UUID;

public class LaunchPadMiniExtensionDefinition extends ControllerExtensionDefinition {
    private static final UUID DRIVER_ID = UUID.fromString("05d2c83f-594d-4833-917a-763fe52358e8");

    @Override
    public String getName() {
        return "Launchpad Mini Mk3 (Performer)";
    }

    @Override
    public String getAuthor() {
        return "Kirkwood West";
    }

    @Override
    public String getVersion() {
        return "0.5a";
    }

    @Override
    public UUID getId() {
        return DRIVER_ID;
    }

    @Override
    public String getHardwareVendor() {
        return "Novation";
    }

    @Override
    public String getHardwareModel() {
        return "Launchpad Mini MK3";
    }

    @Override
    public int getRequiredAPIVersion() {
        return 18;
    }

    @Override
    public int getNumMidiInPorts() {
        return 1;
    }

    @Override
    public int getNumMidiOutPorts() {
        return 1;
    }

    @Override
    public void listAutoDetectionMidiPortNames(AutoDetectionMidiPortNamesList list, PlatformType platformType) {
        if(platformType == PlatformType.WINDOWS)
        {
            list.add(new String[]{"Launchpad Mini MK3 LPMiniMK3 MIDI Out"}, new String[]{"Launchpad Mini MK3 LPMiniMK3 MIDI In"});
        }
        else if(platformType == PlatformType.MAC)
        {
            list.add(new String[]{"Launchpad Mini MK3 LPMiniMK3 MIDI Out"}, new String[]{"Launchpad Mini MK3 LPMiniMK3 MIDI In"});
        }
        else if(platformType == PlatformType.LINUX)
        {
            list.add(new String[]{"Launchpad Mini MK3 LPMiniMK3 MIDI Out"}, new String[]{"Launchpad Mini MK3 LPMiniMK3 MIDI In"});
        }
        else
        {
            list.add(new String[]{"Launchpad Mini MK3 LPMiniMK3 MIDI Out"}, new String[]{"Launchpad Mini MK3 LPMiniMK3 MIDI In"});
        }
    }

    @Override
    public LaunchPadMiniExtension createInstance(ControllerHost host) {
        return new LaunchPadMiniExtension(this, host);
    }
}
