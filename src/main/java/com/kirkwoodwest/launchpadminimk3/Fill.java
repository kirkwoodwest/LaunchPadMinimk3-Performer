package com.kirkwoodwest.launchpadminimk3;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.values.SettableBooleanValueImpl;

public class Fill {

	private final ControllerHost host;
	private final Transport transport;
	private final HardwareActionBindable fillPressedAction;
	private final HardwareActionBindable fillReleasedAction;
	private final HardwareActionBindable fillOptionPressedAction;
	private final FillTimer fillTimer;

	private String old_pos;

	private SettableBooleanValue fillModeLocked = new SettableBooleanValueImpl(false);
	private boolean btnFillIsPressed;
	private boolean button_fill_option_is_pressed;

	public Fill(ControllerHost host, Transport transport) {
		this.host = host;

		this.transport = host.createTransport();
		this.transport.isFillModeActive().markInterested();

		fillTimer = new FillTimer(transport);

		//Fill Pressed Action
		fillPressedAction = host.createAction(()->{
			btnFillIsPressed = true;
			fillModeLocked.set(false);
			fillTimer.engageTimedFill(false);
			this.transport.isFillModeActive().set(true);

		}, () -> "Fill Button Pressed");


		//Fill Released Action
		fillReleasedAction = host.createAction(()->{
			btnFillIsPressed = false;
			if (fillModeLocked.get()) return;
			this.transport.isFillModeActive().set(false);
		}, () -> "Fill Button Released");

		//Fill Option Pressed Action
		fillOptionPressedAction = host.createAction(()->{
			boolean fillActive = this.transport.isFillModeActive().get();
			if(fillActive) {
				fillTimer.engageTimedFill(false);
			}
			if(btnFillIsPressed) {
				fillModeLocked.set(true);
			} else {
				this.transport.isFillModeActive().set(true);
				fillModeLocked.set(false);
				fillTimer.engageTimedFill(true);
				fillTimer.reset();
			}
		}, () -> "Fill Button Pressed");
	}

	public BooleanValue isFillModeActive() {
		return transport.isFillModeActive();
	}

	public BooleanValue isFillOptionActive() {
		return fillTimer.timerActive();
	}

	public BooleanValue isFillModeLocked() {
		return fillModeLocked;
	}

	public HardwareActionBindable getFillPressedAction() {
		return fillPressedAction;
	}

	public HardwareActionBindable getFillReleasedAction() {
		return fillReleasedAction;
	}


	public HardwareActionBindable getFillOptionPressedAction() {
		return fillOptionPressedAction;
	}
}