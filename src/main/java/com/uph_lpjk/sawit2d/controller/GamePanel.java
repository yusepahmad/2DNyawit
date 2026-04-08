package com.uph_lpjk.sawit2d.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.uph_lpjk.sawit2d.entity.Entity;
import com.uph_lpjk.sawit2d.entity.Player;
import com.uph_lpjk.sawit2d.farm.GameState;
import com.uph_lpjk.sawit2d.farm.FarmSystem;
import com.uph_lpjk.sawit2d.interactive.tile.InteractiveTile;
import com.uph_lpjk.sawit2d.tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final private int originalTileSize = 16;
    final private int scale = 3;

    final private int tileSize = originalTileSize*scale; // 48 * 48
    final private int maxScreenCol = 20;
    final private int maxScreenRow = 12;
    final private int screenWidth = (tileSize * maxScreenCol); // 960 px
    final private int screenHeight = (tileSize * maxScreenRow); // 576 px

    // WORLD SETTINGS
    final private int maxWorldCol = 50;
    final private int maxWorldRow = 50;
    final private int worldWidth = tileSize * maxWorldCol;
    final private int worldHeight = tileSize * maxWorldRow;

    // FOR FULL SCREEN
    private int screenWidth2 = screenWidth;
    private int screenHeight2 = screenHeight;
    private BufferedImage tempScreen;
    private Graphics2D g2;
    private boolean fullScreenOn = false;

    // FPS
    final private int FPS = 60;

    Thread gamThread;

    // INIT CONTROLLER
    final private KeyHandler keyH = new KeyHandler(this);
    final private AssetSetter aSetter = new AssetSetter(this);
    final private UserInterface ui = new UserInterface(this);
    final private EventHandler eHandler = new EventHandler(this);
    final private Sound music = new Sound();
    final private Sound soundEffect = new Sound();
    final private FarmSystem farmSystem = new FarmSystem(this);

    // ENTITY AND OBJECT
    final private Player player = new Player(this, keyH);
    final private Entity obj[] = new Entity[20];
    
    private InteractiveTile iTile[] = new InteractiveTile[900];
    final private ArrayList<Entity> entityList = new ArrayList<>();
    final private ArrayList<Entity> particleList = new ArrayList<>(); 
    
    final private CollisionChecker cChecker = new CollisionChecker(this);
    final private TileManager tileM = new TileManager(this);

    // GAME STATE
    public enum State { TITLE, PLAY, PAUSE, GAME_OVER }
    private State gameState;


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleFarmMouseInput(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    handleFarmMouseInput(e);
                }
            }
        });
        this.setFocusable(true);
    }

    // GETTER & SETTER

    public int getTileSize() { return this.tileSize; }
    public int getScreenWidth() { return this.screenWidth; }
    public int getScreenHeight() { return this.screenHeight; }
    public int getMaxWorldCol() { return this.maxWorldCol; }
    public int getMaxWorldRow() { return this.maxWorldRow; }
    public int getWorldWidth() { return this.worldWidth; }
    public int getWorldHeight() { return this.worldHeight; }

    // KEYHANDLER
    public void setKeyHActionPressed(boolean pressed) { this.keyH.setActionPressed(pressed); }

    // EVENT HANDLER
    public void getEventHandlerCheckEvent() { this.eHandler.checkEvent(); }

    // MUSIC AND SOUND EFFECT
    public void playMusic(int i) {
        this.music.setFile(i);
        this.music.play();
        this.music.loop();
    }

    public void stopMusic() { this.music.stop(); }
    public void playSoundEffect(int i) {
        this.soundEffect.setFile(i);
        this.soundEffect.play();
    }

    // USER INTERFACE
    public void addUIMessage(String text) {
        this.ui.addMessage(text);
        if (this.farmSystem != null && text != null) {
            this.farmSystem.getGameState().setLastNotification(text);
        }
    }

    public int getUICommandNum() { return this.ui.getCommandNum(); }
    public void setUICommandNum(int num) { this.ui.setCommandNum(num); }

    // PLAYER
    public int getPlayerWorldX() { return this.player.getWorldX(); }
    public int getPlayerWorldY() { return this.player.getWorldY(); }
    public Player getPlayer() { return this.player; }
    public Rectangle getPlayerSolidArea() { return this.player.getSolidArea(); }
    public int getPlayerSolidAreaX() { return this.player.getSolidAreaX(); }
    public void setPlayerSolidAreaX(int x) { this.player.setSolidAreaX(x); }
    public int getPlayerSolidAreaY() { return this.player.getSolidAreaY(); }
    public void setPlayerSolidAreaY(int y) { this.player.setSolidAreaY(y); }
    public int getPlayerSolidAreaDefaultX() { return this.player.getSolidAreaDefaultX(); }
    public int getPlayerSolidAreaDefaultY() { return this.player.getSolidAreaDefaultY(); }
    public int getPlayerScreenX() { return this.player.getScreenX(); }
    public int getPlayerScreenY() { return this.player.getScreenY(); }
    public String getPlayerDirection() { return this.player.getDirection(); }
    public int getPlayerGold() { return this.player.getGold(); }
    public void setPlayerGold(int gold) {
        this.player.setGold(gold);
        if (this.player.getGold() < 0 && this.gameState != State.GAME_OVER) {
            setGameOver("Gold habis. Game over.");
        }
    }
    public BufferedImage getPlayerDown1() { return this.player.getDown1(); }
    public BufferedImage getPlayerMainCharacter() { return this.player.getImageMainCharacter(); }

    public FarmSystem getFarmSystem() { return this.farmSystem; }
    public GameState getFarmState() { return this.farmSystem.getGameState(); }

    private void handleFarmMouseInput(MouseEvent e) {
        if (gameState == null || gameState != GamePanel.State.PLAY) return;
        if (SwingUtilities.isLeftMouseButton(e)) {
            farmSystem.interactAtScreenPoint(e.getX(), e.getY());
            return;
        }
        if (SwingUtilities.isRightMouseButton(e) || e.isPopupTrigger()) {
            farmSystem.toggleFirebreakAtScreenPoint(e.getX(), e.getY());
        }
    }

    // TILE MANAGER
    public void loadMap() { this.tileM.loadMap("farm_land"); }
    public int getMapTileNum(int col, int row) { return this.tileM.getMapTileNum(col, row); }
    public boolean getTileCollision(int i) { return this.tileM.getTileCollision(i); }

    // INTERACTIVE TILE
    public InteractiveTile[] getInteractiveTile() { return this.iTile; }
    public InteractiveTile getInteractiveTile(int i) { return this.iTile[i]; }
    public boolean getInteractiveTileDestructible(int i) { return this.iTile[i].getDestructible(); }
    public void setInteractiveTile(int i, InteractiveTile iTile) { this.iTile[i] = iTile; }
    public boolean interactiveTileIsCorrectItem(int i, Entity entity) { return this.iTile[i].isCorrectItem(entity); }
    public boolean getInteractiveTileInvincible(int i) { return this.iTile[i].getInvincible(); }
    public void interactiveTilePlaySoundEffect(int i) { this.iTile[i].playSoundEffect(); }
    public boolean getInteractiveTileIsAttackable(int i, Entity attacker) { return this.iTile[i].isAttackable(attacker); }
    public void setInteractiveTileLife(int i, int life) { this.iTile[i].setLife(life); }
    public int getInteractiveTileLife(int i) { return this.iTile[i].getLife(); }
    public void setInteractiveTileInvincible(int i, boolean invincible) { this.iTile[i].setInvincible(invincible); }
    public boolean getInteractiveTileInvicible(int i) { return this.iTile[i].getInvincible(); } // Typo sync
    public InteractiveTile getInteractiveTileDestroyForm(int i) { return this.iTile[i].getDestroyForm(); }

    // ENTITY OBJECT
    public int getObjectLength() { return this.obj.length; }
    public Entity getObject(int i) { return this.obj[i]; }
    public void setObject(int i, Entity entity) { this.obj[i] = entity; }
    public void setObject(int i, Entity object, int worldX, int worldY) {
        this.obj[i] = object;
        this.obj[i].setWorldX(worldX);
        this.obj[i].setWorldY(worldY);
    }
    public int getObjectType(int i) { return this.obj[i].getType().ordinal(); }
    public void setObjectUse(int i, Entity entity) { this.obj[i].use(entity); }
    public int getObjectWorldX(int i) { return this.obj[i].getWorldX(); }
    public int getObjectWorldY(int i) { return this.obj[i].getWorldY(); }
    public Rectangle getObjectSolidArea(int i) { return this.obj[i].getSolidArea(); }
    public int getObjectSolidAreaX(int i) { return this.obj[i].getSolidAreaX(); }
    public void setObjectSolidAreaX(int i, int x) { this.obj[i].setSolidAreaX(x); }
    public int getObjectSolidAreaY(int i) { return this.obj[i].getSolidAreaY(); }
    public void setObjectSolidAreaY(int i, int y) { this.obj[i].setSolidAreaY(y); }
    public int getObjectSolidAreaDefaultX(int i) { return this.obj[i].getSolidAreaDefaultX(); }
    public int getObjectSolidAreaDefaultY(int i) { return this.obj[i].getSolidAreaDefaultY(); }

    // COLLISION CHECKER
    public int getCheckObject(Entity entity, boolean player) { return this.cChecker.checkObject(entity, player); }
    public int getCheckInteractiveTile(Entity entity, Entity[] target) { return this.cChecker.checkEntity(entity, target); }
    public boolean getCheckPlayer(Entity entity) { return this.cChecker.checkPlayer(entity); }
    public void checkTile(Entity entity) { this.cChecker.checkTile(entity); }

    // GAME STATE
    public void setGameState(State state) { this.gameState = state; }
    public State getGameState() { return this.gameState; }
    public void setGameOver(String reason) {
        this.gameState = State.GAME_OVER;
        this.farmSystem.getGameState().setLastNotification(reason);
        this.ui.addMessage(reason);
        this.stopMusic();
    }

    public void returnHomeFromGameOver() {
        this.player.resetToDefaultValues();
        int currentGold = this.player.getGold();
        if (currentGold != 1000) this.player.setGold(1000 - currentGold);
        this.farmSystem.resetSession();
        this.ui.resetNotifications();
        this.ui.setCommandNum(0);
        this.gameState = State.TITLE;
    }

    // PARTICLE
    public void addParticleList(Entity particle) { this.particleList.add(particle); }

    public int getCameraX() {
        int cameraX = player.getWorldX() - player.getScreenX();
        if (cameraX < 0) cameraX = 0;
        if (cameraX > getWorldWidth() - getScreenWidth()) cameraX = getWorldWidth() - getScreenWidth();
        return cameraX;
    }

    public int getCameraY() {
        int cameraY = player.getWorldY() - player.getScreenY();
        if (cameraY < 0) cameraY = 0;
        if (cameraY > getWorldHeight() - getScreenHeight()) cameraY = getWorldHeight() - getScreenHeight();
        return cameraY;
    }

    public void setupGame() {
        this.aSetter.setObject();
        this.aSetter.setInteractiveTile();
        if (this.player.getGold() == 0) this.player.setGold(1000);
        this.gameState = GamePanel.State.TITLE;
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();
    }

    public void startGameThread() {
        gamThread = new Thread(this);
        gamThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long timer = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while( gamThread!=null ) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                drawToTempScreen();
                drawToScreen();
                delta--;
            }
            if(timer >= 1000000000) timer = 0;
        }
    }

    public void update() {
        if(this.gameState == GamePanel.State.PLAY) {
            // PLAYER
            player.update();

            // FARM SYSTEM
            if (this.keyH.consumeAutoPlantPressed()) this.farmSystem.toggleAutoPlantMode();
            if (this.keyH.consumeAutoSellPressed()) this.farmSystem.toggleAutoSellMode();
            if (this.keyH.consumeAutoHarvestPressed()) this.farmSystem.toggleAutoHarvestMode();
            if (this.keyH.consumeActionPressed()) {
                if (!interactWorldObject(this.player)) {
                    this.farmSystem.interact(this.player);
                }
            }
            if (this.keyH.consumeFirebreakPressed()) this.farmSystem.toggleFirebreakAtPlayer();
            if (this.keyH.consumeNextDayPressed()) this.farmSystem.nextDay();
            if (this.keyH.consumeSellPressed()) this.farmSystem.sellInventory();

            this.farmSystem.update();

            if (this.gameState == GamePanel.State.GAME_OVER) return;

            for(int i = 0; i < this.iTile.length; i++) {
                if(this.iTile[i] != null) this.iTile[i].update();
            }

            // PARTICLE
            for(int i = 0; i < this.particleList.size(); i++) {
                if(this.particleList.get(i) != null) {
                    if(this.particleList.get(i).getAlive() == true) this.particleList.get(i).update();
                    if(this.particleList.get(i).getAlive() == false) this.particleList.remove(i--);
                }
            }
        }
    }

    public boolean interactWorldObject(Entity entity) {
        int objectIndex = this.cChecker.checkObject(entity, true);
        if (objectIndex == 999 || this.obj[objectIndex] == null) return false;
        if (this.obj[objectIndex].getType() == Entity.Type.NPC) {
            this.obj[objectIndex].use(entity);
            return true;
        }
        return false;
    }

    public void drawToTempScreen() {
        // TILE
        tileM.draw(g2);

        // FARM SYSTEM
        this.farmSystem.draw(g2);

        // MAP OBJECTS
        tileM.drawObjectLayer(g2);

        // INTERACTIVE TILE
        for(int i = 0; i < this.iTile.length; i++) {
            if(this.iTile[i] != null) this.iTile[i].draw(g2);
        }

        // ADD ENTITY TO THE LIST
        this.entityList.add(this.player);
        for(int i = 0; i < this.obj.length; i++) {
            if(this.obj[i] != null) this.entityList.add(this.obj[i]);
        }
        for(int i = 0; i < this.particleList.size(); i++) {
            if(this.particleList.get(i) != null) this.entityList.add(this.particleList.get(i));
        }

        // SORT
        Collections.sort(entityList, new Comparator<Entity>() {
            @Override
            public int compare(Entity e1, Entity e2) {
                return Integer.compare(e1.getWorldY(), e2.getWorldY());
            }
        });

        // DRAW ENTITY LIST
        for(int i = 0; i < this.entityList.size(); i++) {
            this.entityList.get(i).draw(g2);
        }

        this.ui.draw(g2);

        // EMPTY ENTITY LIST
        this.entityList.clear();

        // DEBUG
        if (this.keyH.getShowDebugText() == true) {
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 10, y = 400, lineHeight = 20;
            g2.drawString("WorldX: " + this.player.getWorldX(), x, y); y += lineHeight;
            g2.drawString("WorldY: " + this.player.getWorldY(), x, y); y += lineHeight;
            g2.drawString("Col: " + (this.player.getWorldX() + this.player.getSolidAreaX()) / tileSize, x, y); y += lineHeight;
            g2.drawString("Row: " + (this.player.getWorldY() + this.player.getSolidAreaY()) / tileSize, x, y); y += lineHeight;
        }
    }

    public void drawToScreen() {
        Graphics g = getGraphics();
        if (g != null) {
            g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
            g.dispose();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (tempScreen != null) g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
    }
}
