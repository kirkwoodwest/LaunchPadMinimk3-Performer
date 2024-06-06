# LaunchPadMini-Performer
A Different Clip Launcher &amp; Performance System For Bitwig Studio

a 16-track split clip launcher for the Launchpad Mini

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

![image](https://github.com/kirkwoodwest/LaunchPadMinimk3-Performer/assets/6645471/a7bee8d2-9a2b-405f-b46a-c364693284d8)

# Setup
<img width="878" alt="image" src="https://github.com/kirkwoodwest/LaunchPadMinimk3-Performer/assets/6645471/206ca823-ccd1-4359-ac8e-cd10cf1bbe73">
Use the Midi Port in / out

Because the track banks are locked they need to be configured using this ui on a per track basis. 
<img width="489" alt="image" src="https://github.com/kirkwoodwest/LaunchPadMinimk3-Performer/assets/6645471/d3a9c894-261a-49f5-9956-79dbdd4e684a">
note: I have some ideas about how to make this go faster. Possibly using a string parser to autofind a track.
