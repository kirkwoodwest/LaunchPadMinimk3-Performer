# LaunchPadMini-Performer
A Different Clip Launcher &amp; Performance System For Bitwig Studio.

[![Download](https://img.shields.io/github/downloads/kirkwoodwest/LaunchPadMinimk3-Performer/total.svg)](https://github.com/unthingable/MonsterJam/releases/latest)
[![Github Release](https://img.shields.io/github/v/release/kirkwoodwest/LaunchPadMinimk3-Performer?include_prereleases)](https://img.shields.io/github/v/release/unthingable/MonsterJam?include_prereleases)

**Compatibility:** _API-18 (Bitwig 5.0+) Although it is recommended 5.2 for the Cursor Track UI!_

A 16-track split clip launcher designed around performance playback for the Novation Launchpad Mini Mk3.

![image](https://github.com/kirkwoodwest/LaunchPadMinimk3-Performer/assets/6645471/a7bee8d2-9a2b-405f-b46a-c364693284d8)

## Pre-Release Notes:
Features missing: Arm/Record Functions, Scene Launch Functions, Locking Fill

## Grid    
- Tracks are fixed in place using Cursor Tracks. A concept unique to Bitwig Studio and its Extension API.
- 4 ClipSlots Per Track
- Bank Navigation Up/Down

## Alternate Clip Launching Modes
A button to toggle between alternate clip launching or standard clip launching.

## Fill Mode Controls
Fill Button Momentary Switch -
FIll To End Of Bar Toggle
Fill Until Toggled off (Fill Button + Fill to End Of Bar)
Fill Mode:
- Momentary Button: Hold for quick fills.
- Locking Fill: Lock the fill until the end of the bar or until you turn it off.
- Clip Launching: Dedicated buttons for launching clips in Standard or Alternate Mode.
- LEDs: Fixed colors for different states. (Might add a couple of clip colors, but not going full rainbow.)


# Setup
<img width="878" alt="image" src="https://github.com/kirkwoodwest/LaunchPadMinimk3-Performer/assets/6645471/206ca823-ccd1-4359-ac8e-cd10cf1bbe73">

- Use the Midi Port in / out

Channels need to be configured using this ui on a per project basis. This UI is only available in V5.2 Beta. 

<img width="489" alt="image" src="https://github.com/kirkwoodwest/LaunchPadMinimk3-Performer/assets/6645471/d3a9c894-261a-49f5-9956-79dbdd4e684a">

_In the future you'll be able to configure the channels using the pads._

# Building the Project
For those wanting to modify or compile for themselves.Use maven `mvn clean install`

Also special thanks to minortom & carlcaulkett on the #controllerism channel for showing me some Scala ways. Also shout out to the #controllerism crew!
