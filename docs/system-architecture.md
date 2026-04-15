# System Architecture

## High-level structure
The architecture is a simple, game-loop-driven design centered around `GamePanel`. It is not strict MVC, but follows a similar separation:
- Model: farm systems, game state, entities
- View: rendering code and UI overlay
- Controller: input handling and update loop

ASCII component diagram:
```
[KeyHandler/Mouse] -> [GamePanel] -> [Player, FarmSystem]
                                   -> [TileManager, UI]
```

## Key components
- GamePanel: main loop, rendering orchestration, state transitions.
- TileManager: loads maps and draws tile layers.
- Player/Entity: movement, combat, and shared entity behavior.
- FarmSystem: farm grid simulation, risk, disasters, and UI messaging.
- UserInterface: HUD, banners, and game over screens.
- Sound: preloads and plays SFX and music.
- FirefighterEventSystem: modal dialog flow for firefighter decisions.

## Data flow
- Input events update flags in KeyHandler.
- GamePanel consumes flags and calls Player and FarmSystem.
- FarmSystem updates GameState and sends messages to UI.
- UI reads GameState and FarmSystem to display status and banners.

## Design notes
- Entity rendering is sorted by worldY to simulate depth.
- FarmSystem uses helper subsystems (risk, economy, weather, disaster) for focused logic.

## Notes and assumptions
- The project uses lightweight custom patterns rather than a full engine framework.
