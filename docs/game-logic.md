# Game Logic

## Game states
- TITLE: main menu.
- PLAY: active gameplay.
- PAUSE: gameplay frozen.
- GAME_OVER: message and return to title.

Transitions:
- TITLE -> PLAY: Enter
- PLAY -> PAUSE: P
- PAUSE -> PLAY: P
- GAME_OVER -> TITLE: Enter

## Farm rules
- Planting costs 30 gold per tile.
- Firebreak costs 20 gold per tile.
- Manual fire handling costs 25 gold.
- Growth time for sawit is 3 days.

## Daily cycle
- Hour increments during update; day advances after hour 23.
- Daily transition:
  - Growth and unused-land counters update.
  - Fire spreads if uncontrolled.
  - Risk evaluated; rain may reduce risk and extinguish fires.
  - Disaster may trigger (risk-based).

## Economy and inventory
- Harvest adds to inventory.
- Selling converts inventory to gold (base price 120, rain penalty multiplier).
- Gardener service can harvest or sell with a cost or discount.

## Land seizure rule
- If no planting happens for 14 consecutive days, game over.

## Event handling
- KeyHandler sets input flags; GamePanel consumes them each update.
- Mouse clicks interact with farm tiles or firebreaks.
- FirefighterEventSystem shows a modal dialog to choose a response.

## Notes and assumptions
- Gold game over triggers when gold drops below 0.
