package com.kirkwoodwest.launchpadminimk3.hardware;

import com.bitwig.extension.callback.ShortMidiDataReceivedCallback;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.hardware.HardwareBasic;
import com.kirkwoodwest.openwoods.utils.MidiUtil;

import java.util.ArrayList;
import java.util.List;

public class HardwareLaunchPadMiniMK3 extends HardwareBasic {
    private final ControllerHost host;
    private final int midi_port;
    private final ShortMidiDataReceivedCallback input_callback;
    private final HardwareSurface hardwareSurface;
    private final String baseHardwareName;
    private final int[][] gridMap;
    private final List<List<GridButton>> gridButtons;
    private final List<GridButton> hardwareFuncButtons;
    private final List<GridButton> hardwareSceneButtons;

    public HardwareLaunchPadMiniMK3(ControllerHost host, int midi_port, ShortMidiDataReceivedCallback input_callback) {
        super(host.getMidiInPort(midi_port), host.getMidiOutPort(midi_port));
        this.host = host;
        this.midi_port = midi_port;
        this.input_callback = input_callback;
        this.hardwareSurface = host.createHardwareSurface();
        this.baseHardwareName = "LPM" + midi_port + "_";

        this.gridMap = new int[8][8];
        this.gridButtons = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            gridButtons.add(new ArrayList<>(8));
        }

        int index = 88;
        for (int row = 0; row < 8; row++) {
            for (int col = 7; col >= 0; col--) {
                gridMap[col][row] = index;
                index--;
            }
            index -= 2;
        }

        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                int note = gridMap[col][row];
                String nameLight = baseHardwareName + "grid_light_" + row + "_" + col;
                String nameButton = baseHardwareName + "grid_button_" + row + "_" + col;
                HardwareButton button = hardwareSurface.createHardwareButton(nameButton);
                GridButton gridButton = new GridButton(button, new LightData(getMidiOut(), note, 123));
                gridButton.setupNoteButton(getMidiIn(), note);
                gridButtons.get(col).add(gridButton);
            }
        }

        int[] hardwareFuncMap = {91, 92, 93, 94, 95, 96, 97, 98};
        this.hardwareFuncButtons = new ArrayList<>(hardwareFuncMap.length);

        for (int i = 0; i < hardwareFuncMap.length; i++) {
            int cc = hardwareFuncMap[i];
            String nameLight = baseHardwareName + "_func_light_" + i;
            String nameButton = baseHardwareName + "_func_button_" + i;
            HardwareButton button = hardwareSurface.createHardwareButton(nameButton);
            GridButton gridButton = new GridButton(button, new LightData(getMidiOut(), cc, 123));
            gridButton.setupCCButton(getMidiIn(), cc);
            hardwareFuncButtons.add(gridButton);
        }

        int[] hardwareSceneMap = {89, 79, 69, 59, 49, 39, 29, 19};
        this.hardwareSceneButtons = new ArrayList<>(hardwareSceneMap.length);

        for (int i = 0; i < hardwareSceneMap.length; i++) {
            int cc = hardwareSceneMap[i];
            String nameLight = baseHardwareName + "_scenes_light_" + i;
            String nameButton = baseHardwareName + "_scene_button_" + i;
            HardwareButton button = hardwareSurface.createHardwareButton(nameButton);
            GridButton gridButton = new GridButton(button, new LightData(getMidiOut(), cc, 123));
            gridButton.setupCCButton(getMidiIn(), cc);
            hardwareSceneButtons.add(gridButton);
        }

        MidiData.sendSysexBooter(host, getMidiOut());
        MidiData.setBrightness(getMidiOut(), 127);

        MultiStateHardwareLight novationLight = hardwareSurface.createMultiStateHardwareLight("NovationLight");
        novationLight.state().onUpdateHardware((InternalHardwareLightState hwState) ->
            updatePadLed(hwState, 0x63)
        );
    }

    public void flush() {
        hardwareSurface.updateHardware();

        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                gridButtons.get(col).get(row).flush();
            }
        }

        for (GridButton button : hardwareFuncButtons) {
            button.flush();
        }

        for (GridButton button : hardwareSceneButtons) {
            button.flush();
        }
    }

    public GridButton getGridButton(int col, int row) {
        return gridButtons.get(col).get(row);
    }

    public GridButton getFuncButton(int index) {
        return hardwareFuncButtons.get(index);
    }

    public GridButton getSceneButton(int index) {
        return hardwareSceneButtons.get(index);
    }

    private void updatePadLed(InternalHardwareLightState state, int ccValue) {
        sendMidi(MidiUtil.NOTE_ON, ccValue, 3);
    }
}
