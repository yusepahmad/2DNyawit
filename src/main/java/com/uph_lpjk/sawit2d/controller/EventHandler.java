package com.uph_lpjk.sawit2d.controller;

public class EventHandler {
    final private GamePanel gp;
    final private EventRect eventRect[][];

    private int previousEventX, previousEventY;
    private boolean canTouchEvent = true;

    public EventHandler(GamePanel gp) {
        this.gp = gp;
        this.eventRect = new EventRect[this.gp.getMaxWorldCol()][this.gp.getMaxWorldRow()];

        int col = 0;
        int row = 0;
        
        while(col < this.gp.getMaxWorldCol() && row < this.gp.getMaxWorldRow()) {
            this.eventRect[col][row] = new EventRect();
            this.eventRect[col][row].x = 23;
            this.eventRect[col][row].y = 23;
            this.eventRect[col][row].width = 2;
            this.eventRect[col][row].height = 2;
            this.eventRect[col][row].eventRectDefaultX = this.eventRect[col][row].x;
            this.eventRect[col][row].eventRectDefaultY = this.eventRect[col][row].y;

            col++;
            if(col == this.gp.getMaxWorldCol()) {
                col = 0;
                row++;
            }
        }
    }

    public void checkEvent() {
        // Check if the player character is more than 1 tile away from the last event
        int xDistance = Math.abs(this.gp.getPlayerWorldX() - this.previousEventX);
        int yDistance = Math.abs(this.gp.getPlayerWorldY() - this.previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if(distance > this.gp.getTileSize()) {
            canTouchEvent = true;
        }
    }

    public boolean hit(int col, int row, String reqDirection) {
        boolean hit = false;

        this.gp.setPlayerSolidAreaX(this.gp.getPlayerWorldX() + this.gp.getPlayerSolidAreaX());
        this.gp.setPlayerSolidAreaY(this.gp.getPlayerWorldY() + this.gp.getPlayerSolidAreaY());
        eventRect[col][row].x = col * this.gp.getTileSize() + eventRect[col][row].x;
        eventRect[col][row].y = row * this.gp.getTileSize() + eventRect[col][row].y;

        if(this.gp.getPlayerSolidArea().intersects(eventRect[col][row]) && eventRect[col][row].eventDone == false) {
            if(this.gp.getPlayerDirection().contentEquals(reqDirection) || reqDirection.contentEquals("any"))  {
                hit = true;

                previousEventX = this.gp.getPlayerWorldX();
                previousEventY = this.gp.getPlayerWorldY();
            }
        }

        this.gp.setPlayerSolidAreaX(this.gp.getPlayerSolidAreaDefaultX());
        this.gp.setPlayerSolidAreaY(this.gp.getPlayerSolidAreaDefaultY());
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }
}
