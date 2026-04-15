# Game Description

## Gameplay overview
Sawit2D is a top-down tile game where the player explores a world and manages a palm farm. The farm area is a grid where tiles can be planted, harvested, or protected with firebreaks.

## Core mechanics
- Movement and combat: player moves with WASD and can attack with a tool.
- Farming: plant sawit, wait for growth, harvest, and sell.
- Risk and disasters: higher risk can trigger fires; player may call the elephant firefighter team.
- Economy: gold is used for planting, firebreaks, and services; selling harvest earns gold.

## Objectives
- Keep the farm productive and avoid land seizure due to long inactivity.
- Manage risk and respond to fires.
- Maintain gold balance to keep the simulation running.

## Player interactions
Keyboard:
- `WASD` move
- `E` or `Enter` interact with farm tiles
- `F` attack
- `B` place or remove firebreak
- `H` auto-plant, `K` auto-harvest, `J` auto-sell
- `N` advance to next day
- `Q` sell inventory
- `P` pause

Mouse:
- Left click: interact with a farm tile
- Right click: toggle firebreak at a farm tile

## Game over conditions
- Gold becomes negative (triggered when expenses exceed gold).
- Land is seized after 14 consecutive days without any planting.

## Notes and assumptions
- The game is designed as a sandbox-style simulation; no explicit win condition is implemented in code.
