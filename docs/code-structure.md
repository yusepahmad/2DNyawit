# Code Structure

## Packages
- `controller`
- `entity`
- `farm`
- `tile`
- `interactive/tile`
- `object`
- `utility`

## Core classes
- `App`: application entry point and Swing window setup.
- `GamePanel`: game loop, rendering pipeline, and state machine.
- `KeyHandler`: input capture and action flags.
- `CollisionChecker`: collision checks for tiles, objects, and entities.
- `TileManager` and `Tile`: map loading and tile rendering.
- `UserInterface`: HUD, banners, game over screens.
- `Player` and `Entity`: movement, attack logic, base entity fields.
- `FarmSystem`, `FarmGrid`, `FarmTile`: farm simulation and rendering.
- `EconomySystem`, `RiskSystem`, `DisasterSystem`, `WeatherSystem`: farm subsystems.
- `FirefighterEventSystem`: modal choice dialog.
- `Sound`: audio playback.

## Resource layout
- `maps`: text-based tile maps
- `tile`, `tiles_interactive`: terrain and interactive sprites
- `objects`, `player`: game sprites
- `sounds`, `fonts`: audio and fonts
- `sawit`, `fire`: farm growth and fire animation

## Notes and assumptions
- Only key classes are listed; object and utility classes are expandable.
