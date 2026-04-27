# GEMINI Context: Sawit2D (2D Nyawit)

Foundational mandates and architectural context for the Sawit2D project. This file takes precedence over general instructions.

## Project Overview
**Sawit2D** is a 2D top-down palm oil plantation simulation game built with Java Swing and AWT. Players manage a farm, plant sawit (palm oil), handle disasters (fire), and manage an economy.

## Core Mandates & Standards
- **Tech Stack:** Java 8+, Maven, Swing/AWT (Native Java). No external game engines (e.g., LibGDX) should be introduced.
- **Conventions:** Strict Java CamelCase. All new logic must be documented with Javadoc for complex methods.
- **Rendering:** Tile-based (48x48 pixels). Depth sorting is mandatory for entities using `worldY`.
- **Coordinate Systems:**
  - **World:** 50x50 tiles.
  - **Farm Grid:** 34x34 tiles, starting at world coordinates (8, 8).
  - **Screen:** 20x12 tiles (960x576 px).

## Object-Oriented Design (OOD) & SOLID Principles
All code must adhere to professional Java OOD standards to ensure maintainability and scalability:
- **Encapsulation:** All fields must be `private` or `protected`. Use explicit getters/setters. Logic affecting an object's state must reside within that object.
- **Inheritance & Polymorphism:** Utilize `Entity` as a base class for all game objects. Use interfaces or abstract classes for defining system behaviors (e.g., `Updateable`, `Drawable`).
- **Composition over Inheritance:** Prefer combining small, focused objects/components to build complex behavior rather than deep inheritance hierarchies.
- **Single Responsibility (SRP):** Each class must have one reason to change. Subsystems (Economy, Disaster, etc.) must remain decoupled.
- **Open/Closed Principle:** Design systems that are open for extension (e.g., adding new plant types) but closed for modification.
- **Dependency Inversion:** Depend on abstractions (interfaces) rather than concrete implementations where possible to facilitate testing and modularity.

## Architectural Patterns
- **Game Loop:** Managed in `GamePanel.java` at 60 FPS. Uses `delta` timing for updates and rendering.
- **System Integration:** `FarmSystem` acts as the orchestrator for all plantation-related logic, delegating to specialized subsystems (`EconomySystem`, `DisasterSystem`, `RiskSystem`, etc.).
- **State Management:** `GamePanel.State` (TITLE, PLAY, PAUSE, GAME_OVER). `GameState` holds farm-specific session data (inventory, risk, reputation).

## Key Systems & Logic
### 1. Farm Logic (`FarmSystem`, `FarmGrid`)
- **Planting:** Costs 30 gold. Only allowed on "Plantable Plots" (non-lane tiles).
- **Growth:** 3 stages of growth. Ready to harvest when stage 3 is reached.
- **Harvesting:** Manual or assisted. Adds to `inventory`.
- **Land Seizure:** Game Over occurs if no planting happens for 14 consecutive days.

### 2. Disaster & Fire (`DisasterSystem`, `RiskSystem`)
- **Risk Calculation:** Based on cluster size of planted tiles. Firebreaks (cost 20) reduce risk and stop fire spread.
- **Fire Spread:** Uncontrolled fires spread to adjacent tiles daily.
- **Firefighter Events:** Modal dialogue flow (`FirefighterEventSystem`) triggered by high-risk disasters.

### 3. Economy (`EconomySystem`)
- **Selling:** Inventory is sold for gold.
- **Weather Impact:** Rain reduces sell price (multiplier 0.7) but extinguishes fires and reduces risk.
- **Gardener Service:** Provides paid assistance for harvesting and selling.

### 4. Sound System (`Sound.java`)
- **Structure:** Must maintain the instance-based structure (`setFile(i)`, `play()`, `stop()`, `loop()`).
- **No Refactoring:** Do not change these function signatures or convert the system to static-only during updates, as it maintains compatibility with existing controller logic.
- **Pathing:** Resources must always point to `/sounds/`.

## Implementation Guidelines
- **Adding Features:** New gameplay mechanics should be modularized within the `com.uph_lpjk.sawit2d.farm` or `com.uph_lpjk.sawit2d.entity` packages.
- **UI Updates:** HUD changes must be made in `UserInterface.java`. Use `gp.addUIMessage()` for player notifications.
- **Input:** Keyboard handling in `KeyHandler`, Mouse handling in `GamePanel`.
- **Assets:** Load via `AssetLoader` or `utility/AssetLoader.java`. Support fallback paths for cross-platform compatibility.

## Validation Protocol
- **Build:** Always verify changes with `mvn clean compile`.
- **Testing:** New logic should include JUnit tests in `src/test/java`.
- **Performance:** Avoid heavy object allocation inside the `update()` or `draw()` methods of `GamePanel`.

## Code Quality & Style Checks
- **Method Naming:** Ensure all method names follow `camelCase`. Refactored legacy `snake_case` methods (e.g., `market_main`, `pause_settings`) to standard `camelCase`.
- **Check Date:** 2026-04-28 (Refactored Market and Pause UI methods).
