package com.kirkwoodwest.openwoods.utils;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.SettableRangedValue;
import com.kirkwoodwest.openwoods.hardware.HardwareBasic;

import static java.lang.Math.floor;

public class MidiSendTest {
	private final ControllerHost host;
	private final HardwareBasic hardware;
	SettableRangedValue midi_channel;
	SettableRangedValue data_1;
	SettableRangedValue data_2;
	public MidiSendTest(ControllerHost host, HardwareBasic hardware){
		this.host = host;
		this.hardware = hardware;
		midi_channel = host.getDocumentState().getNumberSetting("midi channel", "MIDISEND", 0, 15, 1, "", 0);
		data_1 = host.getDocumentState().getNumberSetting("data1", "MIDISEND", 0, 127, 1, "", 0);
		data_2 = host.getDocumentState().getNumberSetting("data2", "MIDISEND", 0, 127, 1, "", 64);
		midi_channel.addValueObserver(this::update);
		data_1.addValueObserver(this::update);
		data_2.addValueObserver(this::update);
	}

	private void update(double v) {
		int channel = (int) floor(midi_channel.get() * 15);
		int status = ShortMidiMessage.NOTE_ON | channel;
		int d1 = (int) floor(data_1.get() * 127);
		int d2 = (int) floor(data_2.get() * 127);
		host.println("channel" + channel + "status:" + status + "d1: " + d1 + " d2:" + d2);
		hardware.sendMidi(status,d1,d2);
	}

}
