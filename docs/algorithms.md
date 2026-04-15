# Algorithms

## Movement and animation
- Input flags set direction.
- Collision checks run before applying movement.
- Sprite animation flips between frames using a counter.

## Collision detection (tile)
1. Compute entity bounding box in world space.
2. Convert to tile grid coordinates.
3. Check the two edge tiles in the direction of movement.
4. Block movement if any tile has collision.

## Collision detection (object and entity)
- Adjust entity and target rectangles by world position.
- Offset by speed in direction of motion.
- Use rectangle intersection to detect overlap.
- Restore original rectangle positions after the check.

## Farm growth and harvest
- Each planted tile stores `remainingGrowDays`.
- Daily advance decreases `remainingGrowDays` until 0.
- When 0, tile becomes ready-to-harvest.

## Risk and disaster
- Risk is evaluated from planted count, firebreak count, and cluster size.
- Disaster chance increases with risk and can ignite random planted tiles.
- Fire spread checks adjacent tiles of burning cells.

## Rendering order (depth)
- All entities are collected into a list.
- The list is sorted by worldY.
- Entities are drawn in sorted order to simulate depth.

## Notes and assumptions
- Fire spread uses four-direction adjacency (no diagonals).
