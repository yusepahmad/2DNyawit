package com.uph_lpjk.sawit2d.farm;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;
import com.uph_lpjk.sawit2d.utility.AssetLoader;

public class FarmSystem {
    private static final double RAIN_PRICE_MULTIPLIER = 0.7;
    private static final int PLANT_COST = 30;
    private static final int FIREBREAK_COST = 20;
    private static final int MANUAL_FIRE_HANDLE_COST = 25;
    private static final int AUTO_ACTION_INTERVAL_FRAMES = 30;
    private static final int CLOCK_INTERVAL_FRAMES = 120;
    private static final int FIRE_ANIM_INTERVAL_FRAMES = 12;
    private static final int LAND_SEIZURE_DAYS = 14;

    private final GamePanel gp;
    private final FarmGrid farmGrid;
    private final GameState gameState;
    private final EconomySystem economySystem;
    private final RiskSystem riskSystem;
    private final DisasterSystem disasterSystem;
    private final FirefighterEventSystem firefighterEventSystem;
    private final GardenerService gardenerService;
    private final WeatherSystem weatherSystem;
    private final AssetLoader assetLoader = new AssetLoader();

    private final int farmOriginCol = 8;
    private final int farmOriginRow = 8;
    private final int farmCols = 34;
    private final int farmRows = 34;

    private final BufferedImage soilImage;
    private final BufferedImage plantedImage;
    private final BufferedImage sawitStage1Image;
    private final BufferedImage sawitStage2Image;
    private final BufferedImage sawitReadyImage;
    private final BufferedImage burnedImage;
    private final BufferedImage[] fireFrames;
    private final BufferedImage rainImage;
    private final BufferedImage firebreakImage;
    private final BufferedImage laneImage;
    private final Random eventRandom = new Random();

    private boolean autoPlantEnabled = false;
    private boolean autoSellEnabled = false;
    private boolean autoHarvestEnabled = false;
    private int autoActionFrameCounter = 0;
    private int clockFrameCounter = 0;
    private int fireFrameCounter = 0;
    private int fireFrameIndex = 0;
    private int daysWithoutPlanting = 0;
    private boolean plantedToday = false;

    public FarmSystem(GamePanel gp) {
        this.gp = gp;
        this.farmGrid = new FarmGrid(farmRows, farmCols);
        this.gameState = new GameState();
        this.economySystem = new EconomySystem();
        this.riskSystem = new RiskSystem();
        this.disasterSystem = new DisasterSystem();
        this.firefighterEventSystem = new FirefighterEventSystem();
        this.gardenerService = new GardenerService();
        this.weatherSystem = new WeatherSystem();

        int size = gp.getTileSize();
        this.soilImage = assetLoader.loadImage(size, size, "/tile/earth.png", "assets/tanah rumput.png", "../assets/tanah rumput.png");
        this.plantedImage = assetLoader.loadImage(size, size, "assets/tanah 1.png", "../assets/tanah 1.png", "/tile/grass00.png");
        this.sawitStage1Image = assetLoader.loadImage(size, size, "/sawit/sawit-fase-1.png");
        this.sawitStage2Image = assetLoader.loadImage(size, size, "/sawit/sawit-fase-2.png");
        this.sawitReadyImage = assetLoader.loadImage(size, size, "/sawit/sawit-panen.png");
        this.burnedImage = assetLoader.loadImage(size, size, "assets/kebakaran.png", "../assets/kebakaran.png", "/tile/wall.png");
        this.fireFrames = new BufferedImage[] {
            assetLoader.loadImage(size, size, "/fire/api-1.png"),
            assetLoader.loadImage(size, size, "/fire/api-3.png"),
            assetLoader.loadImage(size, size, "/fire/api-5.png")
        };
        this.rainImage = assetLoader.loadImage(size, size, "assets/after banjir.png", "../assets/after banjir.png", "/tile/water00.png");
        this.firebreakImage = assetLoader.loadImage(size, size, "assets/kebakaran-2.png", "../assets/kebakaran-2.png", "/tile/wall.png");
        this.laneImage = assetLoader.loadImage(size, size, "/tile/road00.png", "assets/air.png", "../assets/air.png");
    }

    public GameState getGameState() {
        return gameState;
    }

    public FarmGrid getFarmGrid() {
        return farmGrid;
    }

    public void draw(Graphics2D g2) {
        drawFarmArea(g2);
        if (gameState.isRaining()) {
            drawRainOverlay(g2);
        }
    }

    public void update() {
        clockFrameCounter++;
        if (clockFrameCounter >= CLOCK_INTERVAL_FRAMES) {
            clockFrameCounter = 0;
            if (gameState.advanceHour()) {
                resolveDailyTransition(true);
                gameState.resetMorningHour();
            }
        }

        fireFrameCounter++;
        if (fireFrameCounter >= FIRE_ANIM_INTERVAL_FRAMES) {
            fireFrameCounter = 0;
            fireFrameIndex = (fireFrameIndex + 1) % fireFrames.length;
        }

        autoActionFrameCounter++;
        if (autoActionFrameCounter < AUTO_ACTION_INTERVAL_FRAMES) {
            return;
        }
        autoActionFrameCounter = 0;

        if (autoPlantEnabled) {
            String message = performAutoPlant();
            if (message != null) {
                gp.addUIMessage(message);
            }
        }
        if (autoHarvestEnabled) {
            String message = performHarvestAssist(true);
            if (message != null) {
                gp.addUIMessage(message);
            }
        }
        if (autoSellEnabled && gameState.getInventory() > 0) {
            sellInventory();
        }
    }

    public void interact(Entity player) {
        Point tilePos = resolvePlayerTile(player);
        if (tilePos == null) {
            gp.addUIMessage("Masuk ke area kebun dulu.");
            return;
        }
        handleInteractionAtPlot(tilePos);
    }

    public void interactAtScreenPoint(int screenX, int screenY) {
        Point tilePos = resolveScreenPoint(screenX, screenY);
        if (tilePos == null) {
            gp.addUIMessage("Klik di area kebun dulu.");
            return;
        }
        handleInteractionAtPlot(tilePos);
    }

    public void toggleFirebreakAtScreenPoint(int screenX, int screenY) {
        Point tilePos = resolveScreenPoint(screenX, screenY);
        if (tilePos == null) {
            gp.addUIMessage("Klik di area kebun dulu.");
            return;
        }
        toggleFirebreakAtPlot(tilePos);
    }

    public void sellInventory() {
        int stock = gameState.getInventory();
        if (stock == 0) {
            gp.addUIMessage("Tidak ada stok untuk dijual.");
            return;
        }
        double weatherMultiplier = gameState.isRaining() ? RAIN_PRICE_MULTIPLIER : 1.0;
        int income = economySystem.collectIncome(stock, weatherMultiplier);
        int sold = gameState.takeInventory(stock);
        gp.setPlayerGold(income);
        gp.playSoundEffect(1);
        gameState.setLastNotification("Truk pengangkut datang, " + sold + " unit sawit terjual.");
        gp.addUIMessage("Truk pengangkut berangkat membawa " + sold + " unit. Gold +" + income + ".");
    }

    public void nextDay() {
        if (!resolveDailyTransition(true)) {
            gameState.resetMorningHour();
        }
    }

    public void advanceClock() {
        if (gameState.advanceHour()) {
            if (!resolveDailyTransition(true)) {
                gameState.resetMorningHour();
            }
        }
    }

    public void toggleAutoPlantMode() {
        autoPlantEnabled = !autoPlantEnabled;
        autoActionFrameCounter = AUTO_ACTION_INTERVAL_FRAMES;
        gp.addUIMessage("Auto tanam: " + (autoPlantEnabled ? "ON" : "OFF"));
    }

    public void toggleAutoSellMode() {
        autoSellEnabled = !autoSellEnabled;
        autoActionFrameCounter = AUTO_ACTION_INTERVAL_FRAMES;
        gp.addUIMessage("Auto jual: " + (autoSellEnabled ? "ON" : "OFF"));
    }

    public void toggleAutoHarvestMode() {
        autoHarvestEnabled = !autoHarvestEnabled;
        autoActionFrameCounter = AUTO_ACTION_INTERVAL_FRAMES;
        gp.addUIMessage("Auto panen: " + (autoHarvestEnabled ? "ON" : "OFF"));
    }

    public boolean isAutoPlantEnabled() {
        return autoPlantEnabled;
    }

    public boolean isAutoSellEnabled() {
        return autoSellEnabled;
    }

    public boolean isAutoHarvestEnabled() {
        return autoHarvestEnabled;
    }

    public void resetSession() {
        farmGrid.reset();
        gameState.reset();
        autoPlantEnabled = false;
        autoSellEnabled = false;
        autoHarvestEnabled = false;
        autoActionFrameCounter = 0;
        clockFrameCounter = 0;
        daysWithoutPlanting = 0;
        plantedToday = false;
    }

    public String performManualHarvestHelp() {
        return performHarvestAssist(false);
    }

    private boolean resolveDailyTransition(boolean incrementDay) {
        if (incrementDay) {
            gameState.incrementDay();
        }
        farmGrid.advanceDay();
        advanceUnusedLandDays();
        updateLandSeizureCounter();
        if (checkLandSeizure()) {
            return true;
        }

        int spread = farmGrid.spreadUncontrolledFire();
        if (spread > 0) {
            economySystem.applyFireSpreadCost(gp, spread);
            gameState.modifyRisk(Math.max(1, spread / 2));
            gameState.addReputation(-Math.max(1, spread / 3));
            gameState.setLastNotification("Api melompat ke " + spread + " petak. Kerugian bertambah.");
        }

        riskSystem.evaluate(farmGrid);
        int riskValue = riskSystem.getCurrentRisk();

        boolean rain = weatherSystem.checkRain();
        gameState.setRaining(rain);
        if (rain) {
            riskValue = Math.max(0, riskValue - 3);
            farmGrid.markAllBurnedHandled(FarmBurnHandledType.RAIN);
            gameState.setLastNotification("Awan gelap turun. Hujan membasahi kebun dan api mereda.");
        }
        gameState.setRiskScore(riskValue);

        StringBuilder summary = new StringBuilder();
        summary.append("Hari ").append(gameState.getDay())
                .append(" selesai. Inventory: ").append(gameState.getInventory()).append(" unit.");

        if (rain) {
            summary.append(" Hujan membasahi kebun dan api mereda.");
        } else {
            boolean disaster = disasterSystem.attemptDisaster(farmGrid, gameState, gp, economySystem);
            if (disaster) {
                summary.append(" ").append(gameState.getLastNotification());
                FirefighterResponse firefighterResult = firefighterEventSystem.promptHelp(gp, gameState);
                summary.append(" ").append(firefighterResult.getMessage());
                if (firefighterResult.getHandledType() != FarmBurnHandledType.NONE) {
                    farmGrid.markAllBurnedHandled(firefighterResult.getHandledType());
                    summary.append(" ").append(buildFireAftermathMessage(firefighterResult.getHandledType()));
                }
            } else {
                summary.append(" Malam lewat dengan aman.");
                gameState.setLastNotification("Hari " + gameState.getDay() + " berjalan tenang.");
            }
        }

        gameState.setLastNotification(summary.toString());
        gp.addUIMessage(summary.toString());
        return false;
    }

    public String performHarvestAssist(boolean auto) {
        GardenerResult result = gardenerService.prepareHarvest(farmGrid);
        if (result.getHarvested() == 0) {
            if (!auto) {
                gp.addUIMessage(result.getMessage());
            }
            return auto ? null : result.getMessage();
        }
        if (gp.getPlayerGold() < result.getCost()) {
            String message = "Gold tidak cukup untuk bantuan panen, udah deh kerja sendiri gausah nyuruh orang.";
            if (!auto) {
                gp.addUIMessage(message);
            }
            return auto ? null : message;
        }

        gp.setPlayerGold(-result.getCost());
        int harvested = farmGrid.harvestReadyTiles();
        gameState.addInventory(harvested);
        gameState.setLastNotification(result.getMessage());
        if (!auto) {
            gp.addUIMessage(result.getMessage());
        }
        return "Tukang kebun membantu panen " + harvested + " petak.";
    }

    public String performSellAssist(boolean auto) {
        int stock = gameState.getInventory();
        if (stock == 0) {
            if (!auto) {
                gp.addUIMessage("Tidak ada stok untuk dijual lewat tukang kebun.");
            }
            return auto ? null : "Tidak ada stok untuk dijual.";
        }
        double weatherMultiplier = gameState.isRaining() ? RAIN_PRICE_MULTIPLIER : 1.0;
        GardenerSaleResult saleResult = gardenerService.sellWithDiscount(stock, economySystem, weatherMultiplier);
        int sold = gameState.takeInventory(stock);
        gp.setPlayerGold(saleResult.getIncome());
        gameState.setLastNotification(saleResult.getMessage());
        if (!auto) {
            gp.addUIMessage(saleResult.getMessage());
        }
        return "Tukang kebun menjual " + sold + " unit.";
    }

    public String performAutoPlant() {
        int planted = 0;
        StringBuilder description = new StringBuilder();
        for (int row = 0; row < farmGrid.getRows() && planted < 5; row++) {
            for (int col = 0; col < farmGrid.getCols() && planted < 5; col++) {
                if (!isPlantablePlot(col, row)) {
                    continue;
                }
                if (gp.getPlayerGold() < PLANT_COST) {
                    break;
                }
                FarmTile tile = farmGrid.getTile(row, col);
                if (tile != null && canAutoPlantSafely(row, col, tile)) {
                    if (farmGrid.plantTile(row, col)) {
                        gp.setPlayerGold(-PLANT_COST);
                        planted++;
                        plantedToday = true;
                        if (description.length() > 0) {
                            description.append(", ");
                        }
                        description.append(formatPlot(new Point(col, row)));
                    }
                }
            }
        }
        if (planted == 0) {
            return "Auto tanam aktif, belum ada petak aman yang bisa ditanam.";
        }
        String message = "Auto tanam menanam " + planted + " petak " + description + ".";
        gameState.setLastNotification(message);
        return message;
    }

    public void toggleFirebreakAtPlayer() {
        Point tilePos = resolvePlayerTile(gp.getPlayer());
        if (tilePos == null) {
            gp.addUIMessage("Masuk ke area kebun dulu.");
            return;
        }
        toggleFirebreakAtPlot(tilePos);
    }

    public void requestHarvestHelp() {
        performHarvestAssist(false);
    }

    public int getDay() {
        return gameState.getDay();
    }

    public int getInventory() {
        return gameState.getInventory();
    }

    public int getRiskScore() {
        return gameState.getRiskScore();
    }

    public int getReputation() {
        return gameState.getReputation();
    }

    public int getDaysWithoutPlanting() {
        return daysWithoutPlanting;
    }

    public boolean isRaining() {
        return gameState.isRaining();
    }

    public String getLastNotification() {
        return gameState.getLastNotification();
    }

    private void drawFarmArea(Graphics2D g2) {
        int tileSize = gp.getTileSize();
        for (int row = 0; row < farmGrid.getRows(); row++) {
            for (int col = 0; col < farmGrid.getCols(); col++) {
                FarmTile tile = farmGrid.getTile(row, col);
                int worldCol = farmOriginCol + col;
                int worldRow = farmOriginRow + row;
                int worldX = worldCol * tileSize;
                int worldY = worldRow * tileSize;
                int screenX = worldX - gp.getCameraX();
                int screenY = worldY - gp.getCameraY();

                if (worldX + tileSize < gp.getCameraX() || worldY + tileSize < gp.getCameraY()
                        || worldX - tileSize > gp.getCameraX() + gp.getScreenWidth()
                        || worldY - tileSize > gp.getCameraY() + gp.getScreenHeight()) {
                    continue;
                }

                BufferedImage image = soilImage;
                double scale = 1.0;
                boolean scaledDraw = false;
                if (isLaneCell(col, row)) {
                    image = laneImage;
                } else if (tile.isBurned()) {
                    image = tile.isBurnedHandled() ? rainImage : fireFrames[fireFrameIndex];
                } else if (tile.getType() == FarmTileType.SAWIT) {
                    int stage = tile.getGrowthStage();
                    if (stage == 1) {
                        image = sawitStage1Image;
                        scale = 0.65;
                    } else if (stage == 2) {
                        image = sawitStage2Image;
                        scale = 0.82;
                    } else {
                        image = sawitReadyImage;
                        scale = 1.0;
                    }
                    scaledDraw = true;
                } else if (tile.getType() == FarmTileType.FIREBREAK) {
                    image = firebreakImage;
                } else if (tile.getType() == FarmTileType.VEGETATION) {
                    image = plantedImage;
                }
                if (scaledDraw) {
                    int drawSize = (int) Math.round(tileSize * scale);
                    int drawX = screenX + (tileSize - drawSize) / 2;
                    int drawY = screenY + (tileSize - drawSize) / 2;
                    g2.drawImage(image, drawX, drawY, drawSize, drawSize, null);
                } else {
                    g2.drawImage(image, screenX, screenY, null);
                }

                g2.setColor(getTileOverlayColor(tile, col, row));
                g2.drawRect(screenX, screenY, tileSize, tileSize);

                if (isPlantablePlot(col, row) && !tile.isBurned()) {
                    g2.setColor(new Color(255, 255, 255, 26));
                    g2.fillRect(screenX + 6, screenY + 6, tileSize - 12, tileSize - 12);
                }
            }
        }
    }

    private void drawRainOverlay(Graphics2D g2) {
        java.awt.Composite oldComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.18f));
        g2.setColor(Color.CYAN);
        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());
        g2.setComposite(oldComposite);
    }

    private Point resolvePlayerTile(Entity player) {
        int tileSize = gp.getTileSize();
        int playerCenterWorldX = player.getWorldX() + tileSize / 2;
        int playerCenterWorldY = player.getWorldY() + tileSize / 2;
        int worldCol = playerCenterWorldX / tileSize;
        int worldRow = playerCenterWorldY / tileSize;

        int farmCol = worldCol - farmOriginCol;
        int farmRow = worldRow - farmOriginRow;
        if (farmCol < 0 || farmRow < 0 || farmCol >= farmCols || farmRow >= farmRows) {
            return null;
        }
        return new Point(farmCol, farmRow);
    }

    private Point resolveScreenPoint(int screenX, int screenY) {
        int tileSize = gp.getTileSize();
        int worldX = screenX + gp.getCameraX();
        int worldY = screenY + gp.getCameraY();
        int worldCol = worldX / tileSize;
        int worldRow = worldY / tileSize;

        int farmCol = worldCol - farmOriginCol;
        int farmRow = worldRow - farmOriginRow;
        if (farmCol < 0 || farmRow < 0 || farmCol >= farmCols || farmRow >= farmRows) {
            return null;
        }
        return new Point(farmCol, farmRow);
    }

    private void handleInteractionAtPlot(Point tilePos) {
        FarmTile tile = farmGrid.getTile(tilePos.y, tilePos.x);
        if (tile == null) {
            gp.addUIMessage("Area kebun tidak ditemukan.");
            return;
        }

        if (!isPlantablePlot(tilePos.x, tilePos.y) && tile.getType() != FarmTileType.FIREBREAK) {
            gp.addUIMessage("Klik di petak tanam, bukan jalur.");
            return;
        }

        if (tile.isBurned() && !tile.isBurnedHandled()) {
            if (gp.getPlayerGold() < MANUAL_FIRE_HANDLE_COST) {
                gp.addUIMessage("Gold tidak cukup untuk tangani kebakaran dasar miskin.");
                return;
            }
            gp.setPlayerGold(-MANUAL_FIRE_HANDLE_COST);
            tile.extinguish();
            gp.playSoundEffect(3);
            gp.addUIMessage("Asap di petak " + formatPlot(tilePos) + " berhasil dipadamkan. Gold -" + MANUAL_FIRE_HANDLE_COST + ". " + buildFireAftermathMessage(FarmBurnHandledType.MANUAL));
            return;
        }

        if (tile.getType() == FarmTileType.EMPTY) {
            if (gp.getPlayerGold() < PLANT_COST) {
                gp.addUIMessage("Gold tidak cukup untuk menanam sawit, bayar dulu utang baru nyawit.");
                return;
            }
            if (farmGrid.plantTile(tilePos.y, tilePos.x)) {
                gp.setPlayerGold(-PLANT_COST);
                gp.playSoundEffect(2);
                gp.addUIMessage("Bibit sawit ditanam di petak " + formatPlot(tilePos) + ". Gold -" + PLANT_COST + ".");
                gameState.setLastNotification("Bibit sawit mulai tumbuh.");
                plantedToday = true;
                return;
            }
        }

        if (tile.isReadyToHarvest()) {
            if (tile.harvest()) {
                gp.playSoundEffect(1);
                gameState.addInventory(1);
                gp.addUIMessage("Tandan matang dipanen dari " + formatPlot(tilePos) + ".");
                gameState.setLastNotification("Panen sawit berhasil.");
                return;
            }
        }

        gp.addUIMessage("Petak " + formatPlot(tilePos) + " belum bisa dipakai.");
    }

    private void toggleFirebreakAtPlot(Point tilePos) {
        FarmTile tile = farmGrid.getTile(tilePos.y, tilePos.x);
        if (tile == null) {
            gp.addUIMessage("Area kebun tidak ditemukan.");
            return;
        }

        if (tile.getType() == FarmTileType.FIREBREAK) {
            tile.reset();
            gp.playSoundEffect(3);
            gp.addUIMessage("Firebreak di petak " + formatPlot(tilePos) + " dihapus.");
            return;
        }

        if (tile.getType() == FarmTileType.EMPTY) {
            if (gp.getPlayerGold() < FIREBREAK_COST) {
                gp.addUIMessage("Gold tidak cukup untuk pasang firebreak, makan-nya nyawit yang bener dek.");
                return;
            }
            gp.setPlayerGold(-FIREBREAK_COST);
            tile.setType(FarmTileType.FIREBREAK);
            gp.playSoundEffect(3);
            gp.addUIMessage("Firebreak dipasang di petak " + formatPlot(tilePos) + ". Gold -" + FIREBREAK_COST + ".");
            return;
        }

        gp.addUIMessage("Petak " + formatPlot(tilePos) + " tidak bisa diubah jadi firebreak.");
    }

    private boolean isLaneCell(int localCol, int localRow) {
        return localCol == 0
                || localRow == 0
                || localCol == farmCols - 1
                || localRow == farmRows - 1;
    }

    private boolean isPlantablePlot(int localCol, int localRow) {
        return !isLaneCell(localCol, localRow);
    }

    private boolean canAutoPlantSafely(int row, int col, FarmTile tile) {
        if (tile == null || tile.getType() != FarmTileType.EMPTY || tile.isBurned()) {
            return false;
        }
        if (farmGrid.hasFirebreak()) {
            return true;
        }
        int largestCluster = farmGrid.countConnectedPlantedArea(false);
        return largestCluster < 36;
    }

    private Color getTileOverlayColor(FarmTile tile, int localCol, int localRow) {
        if (isLaneCell(localCol, localRow)) {
            return new Color(125, 94, 55, 100);
        }
        if (tile.isBurned()) {
            // return tile.isBurnedHandled() ? new Color(90, 180, 220, 200) : new Color(160, 40, 40, 210);
            return tile.isBurnedHandled() ? new Color(90, 150, 130, 200) : new Color(190, 90, 40, 220);
        }
        if (tile.getType() == FarmTileType.FIREBREAK) {
            return new Color(120, 120, 120, 210);
        }
        if (tile.getType() == FarmTileType.SAWIT) {
            switch (tile.getGrowthStage()) {
                case 1:
                    return new Color(30, 95, 45, 160);
                case 2:
                    return new Color(45, 160, 70, 180);
                case 3:
                    return new Color(50, 220, 80, 220);
                default:
                    return new Color(30, 120, 50, 200);
            }
        }
        return new Color(180, 140, 70, 120);
    }

    private String formatPlot(Point plot) {
        return "(" + (plot.x + farmOriginCol) + "," + (plot.y + farmOriginRow) + ")";
    }

    private void advanceUnusedLandDays() {
        for (int row = 0; row < farmGrid.getRows(); row++) {
            for (int col = 0; col < farmGrid.getCols(); col++) {
                if (!isPlantablePlot(col, row)) {
                    continue;
                }
                FarmTile tile = farmGrid.getTile(row, col);
                if (tile == null) {
                    continue;
                }
                if (tile.getType() == FarmTileType.EMPTY && !tile.isBurned()) {
                    tile.advanceUnusedDay();
                } else {
                    tile.resetUnusedDays();
                }
            }
        }
    }

    private boolean checkLandSeizure() {
        if (daysWithoutPlanting < LAND_SEIZURE_DAYS) {
            return false;
        }
        String reason = "Tanah Anda disita negara karena dibiarkan kosong selama 2 minggu. Game over.";
        gp.setGameOver(reason);
        return true;
    }

    private void updateLandSeizureCounter() {
        if (plantedToday) {
            daysWithoutPlanting = 0;
        } else {
            daysWithoutPlanting++;
        }
        plantedToday = false;
    }

    private String buildFireAftermathMessage(FarmBurnHandledType handledType) {
        if (handledType == FarmBurnHandledType.FIREFIGHTER) {
            return eventRandom.nextBoolean()
                    ? "Verrell Bramasta datang meninjau kebakaran dan membantu menenangkan warga."
                    : "Zulhas datang membawa bantuan beras untuk warga sekitar.";
        }
        if (handledType == FarmBurnHandledType.MANUAL) {
            return eventRandom.nextBoolean()
                    ? "Verrell Bramasta datang meninjau kebakaran dan ikut memastikan area aman."
                    : "Zulhas datang membawa bantuan beras untuk warga yang terdampak.";
        }
        return "";
    }
}
