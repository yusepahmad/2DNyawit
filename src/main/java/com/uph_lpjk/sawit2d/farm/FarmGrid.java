package com.uph_lpjk.sawit2d.farm;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FarmGrid {
    private final int rows;
    private final int cols;
    private final FarmTile[][] tiles;
    private final Random random = new Random();

    public FarmGrid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.tiles = new FarmTile[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                tiles[r][c] = new FarmTile();
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public FarmTile getTile(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return null;
        }
        return tiles[row][col];
    }

    public boolean plantTile(int row, int col) {
        FarmTile tile = getTile(row, col);
        if (tile == null) {
            return false;
        }
        if (tile.getType() == FarmTileType.EMPTY) {
            tile.plant();
            return true;
        }
        return false;
    }

    public void advanceDay() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                FarmTile tile = tiles[r][c];
                if (tile.isBurned() && tile.isBurnedHandled()) {
                    tile.extinguish();
                    continue;
                }
                tile.advanceDay();
            }
        }
    }

    public int harvestReadyTiles() {
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (tiles[r][c].harvest()) {
                    count++;
                }
            }
        }
        return count;
    }

    public int countReadyTiles() {
        int ready = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (tiles[r][c].isReadyToHarvest()) {
                    ready++;
                }
            }
        }
        return ready;
    }

    public int countByType(FarmTileType type) {
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (tiles[r][c].getType() == type) {
                    count++;
                }
            }
        }
        return count;
    }

    public int countConnectedPlantedArea(boolean includeFirebreak) {
        boolean[][] visited = new boolean[rows][cols];
        int largest = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (visited[r][c]) {
                    continue;
                }
                FarmTileType type = tiles[r][c].getType();
                if (type != FarmTileType.SAWIT
                        && !(includeFirebreak && type == FarmTileType.FIREBREAK)) {
                    continue;
                }

                int size = floodCount(r, c, visited, includeFirebreak);
                if (size > largest) {
                    largest = size;
                }
            }
        }

        return largest;
    }

    public boolean hasFirebreak() {
        return countByType(FarmTileType.FIREBREAK) > 0;
    }

    public void reset() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                tiles[r][c].reset();
            }
        }
    }

    private int floodCount(
            int startRow, int startCol, boolean[][] visited, boolean includeFirebreak) {
        int count = 0;
        ArrayList<Point> queue = new ArrayList<>();
        queue.add(new Point(startRow, startCol));

        while (!queue.isEmpty()) {
            Point current = queue.remove(queue.size() - 1);
            int row = current.x;
            int col = current.y;

            if (row < 0 || row >= rows || col < 0 || col >= cols || visited[row][col]) {
                continue;
            }

            FarmTileType type = tiles[row][col].getType();
            if (type != FarmTileType.SAWIT
                    && !(includeFirebreak && type == FarmTileType.FIREBREAK)) {
                continue;
            }

            visited[row][col] = true;
            count++;

            queue.add(new Point(row + 1, col));
            queue.add(new Point(row - 1, col));
            queue.add(new Point(row, col + 1));
            queue.add(new Point(row, col - 1));
        }

        return count;
    }

    public int removeRandomPlanted(int maxRemovals) {
        List<Point> planted = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (tiles[r][c].getType() == FarmTileType.SAWIT) {
                    planted.add(new Point(r, c));
                }
            }
        }
        if (planted.isEmpty()) {
            return 0;
        }

        int removals = Math.min(maxRemovals, planted.size());
        int removed = 0;
        for (int i = 0; i < removals; i++) {
            Point selected = planted.remove(random.nextInt(planted.size()));
            getTile(selected.x, selected.y).destroy();
            removed++;
        }
        return removed;
    }

    public int igniteRandomPlanted(int maxIgnitions) {
        List<Point> planted = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (tiles[r][c].getType() == FarmTileType.SAWIT && !tiles[r][c].isBurned()) {
                    planted.add(new Point(r, c));
                }
            }
        }
        if (planted.isEmpty()) {
            return 0;
        }

        int ignitions = Math.min(maxIgnitions, planted.size());
        int ignited = 0;
        for (int i = 0; i < ignitions; i++) {
            Point selected = planted.remove(random.nextInt(planted.size()));
            FarmTile tile = getTile(selected.x, selected.y);
            if (tile != null && !tile.isBurned()) {
                tile.markBurned();
                ignited++;
            }
        }
        return ignited;
    }

    public int spreadUncontrolledFire() {
        boolean[][] marked = new boolean[rows][cols];
        int spread = 0;
        ArrayList<Point> toBurn = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                FarmTile tile = tiles[r][c];
                if (!tile.isBurned() || tile.isBurnedHandled()) {
                    continue;
                }

                addBurnCandidate(r + 1, c, marked, toBurn);
                addBurnCandidate(r - 1, c, marked, toBurn);
                addBurnCandidate(r, c + 1, marked, toBurn);
                addBurnCandidate(r, c - 1, marked, toBurn);
            }
        }

        for (Point point : toBurn) {
            FarmTile target = getTile(point.x, point.y);
            if (target != null && !target.isBurned()) {
                target.markBurned();
                spread++;
            }
        }

        return spread;
    }

    public int countUncontrolledBurns() {
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                FarmTile tile = tiles[r][c];
                if (tile.isBurned() && !tile.isBurnedHandled()) {
                    count++;
                }
            }
        }
        return count;
    }

    private void addBurnCandidate(int row, int col, boolean[][] marked, List<Point> toBurn) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return;
        }
        if (marked[row][col]) {
            return;
        }

        FarmTile tile = tiles[row][col];
        if (tile.getType() == FarmTileType.FIREBREAK || tile.isBurned()) {
            return;
        }

        marked[row][col] = true;
        toBurn.add(new Point(row, col));
    }

    public void markBurnHandled(int row, int col, FarmBurnHandledType type) {
        FarmTile tile = getTile(row, col);
        if (tile != null && tile.isBurned() && !tile.isBurnedHandled()) {
            tile.extinguish();
        }
    }

    public void markAllBurnedHandled(FarmBurnHandledType type) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                FarmTile tile = tiles[r][c];
                if (tile.isBurned() && !tile.isBurnedHandled()) {
                    tile.extinguish();
                }
            }
        }
    }

    public List<Point> getEmptyTiles(int limit) {
        List<Point> result = new ArrayList<>();
        for (int r = 0; r < rows && result.size() < limit; r++) {
            for (int c = 0; c < cols && result.size() < limit; c++) {
                if (tiles[r][c].getType() == FarmTileType.EMPTY && !tiles[r][c].isBurned()) {
                    result.add(new Point(r, c));
                }
            }
        }
        return result;
    }

    public List<Point> getPlantableEmptyTiles(int limit) {
        List<Point> result = new ArrayList<>();
        for (int r = 0; r < rows && result.size() < limit; r++) {
            for (int c = 0; c < cols && result.size() < limit; c++) {
                if (tiles[r][c].getType() == FarmTileType.EMPTY && !tiles[r][c].isBurned()) {
                    result.add(new Point(r, c));
                }
            }
        }
        return result;
    }
}
