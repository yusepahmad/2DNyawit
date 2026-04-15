# Sawit2D Technical Overview

## Project summary
Sawit2D is a 2D top-down adventure and farming game built with Java Swing/AWT. The codebase combines tile-based world exploration with a farm management subsystem (planting, harvesting, disasters).

## Project goals
- Provide a lightweight, tile-based game loop and rendering pipeline.
- Simulate farm growth, risk, and disaster events in a manageable grid.
- Support keyboard and mouse interactions for movement and farm actions.

## Technology stack
- Java (JDK 8+)
- Swing/AWT for rendering and input
- Maven for build and dependency management
- JUnit for tests (basic setup)

## Entry point
The application starts from `com.uph_lpjk.sawit2d.App` which creates a Swing `JFrame` and attaches `GamePanel`.

Run command:
```bash
mvn exec:java -Dexec.mainClass="com.uph_lpjk.sawit2d.App"
```

## Main folder structure
- `src/main/java/com/uph_lpjk/sawit2d`
  - `controller`: main loop, input, UI, audio, collision, event handling
  - `entity`: player and base entity logic
  - `farm`: farm grid, economy, risk, disaster, weather, and helper services
  - `tile`: tile map loading and rendering
  - `interactive/tile`: destructible tiles (e.g., dry trees)
  - `object`: world objects and items
  - `utility`: asset loading and image utilities
- `src/main/resources`
  - `maps`: tile maps (txt)
  - `tile`, `tiles_interactive`: tiles and interactive sprites
  - `objects`, `player`: game sprites
  - `sounds`, `fonts`: audio and fonts
  - `sawit`, `fire`: farm growth and fire animation assets
- `docs`: developer documentation (this folder)

## Notes and assumptions
- Some gameplay goals are open-ended; no explicit win condition is shown in code.
