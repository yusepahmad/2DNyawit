# Game Loop

## Loop overview
The loop is implemented manually in `GamePanel` using a dedicated thread and a fixed time step. Each cycle updates game state, renders to an off-screen buffer, then draws to screen.

ASCII diagram:
```
Input -> Update -> Render (to temp buffer) -> Blit to screen
```

## Update phase
- Player update: movement, collision checks, attack state.
- Farm system update: auto actions, day and hour progression, fire animation.
- Interactive tiles update: invincibility timers.
- Particle update: advance and remove dead particles.

## Render phase
- Tile map background
- Farm grid overlay (growth, fire, rain)
- Object layer tiles
- Interactive tiles and entities (sorted by worldY)
- UI overlay (status, banners, hints)

## Swing integration
GamePanel uses a custom thread and draws into a `BufferedImage` (`tempScreen`) to reduce flicker. `paintComponent()` reuses this buffer when Swing repaints.

Pseudocode:
```
while (running) {
  accumulateDelta();
  if (delta >= 1) {
    update();
    drawToTempScreen();
    drawToScreen();
    delta--;
  }
}
```

## Notes and assumptions
- The loop runs at a fixed target of 60 FPS.
