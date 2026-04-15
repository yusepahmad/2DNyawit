package com.uph_lpjk.sawit2d.controller;

import com.uph_lpjk.sawit2d.entity.Entity;

public class CollisionChecker {

    private final GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.getWorldX() + entity.getSolidAreaX();
        int entityRightWorldX =
                entity.getWorldX() + entity.getSolidAreaX() + entity.getSolidAreaWidth();

        int entityTopWorldY = entity.getWorldY() + entity.getSolidAreaY();
        int entityBottmWorldY =
                entity.getWorldY() + entity.getSolidAreaY() + entity.getSolidAreaHeight();

        int entityLeftCol = entityLeftWorldX / this.gp.getTileSize();
        int entityRightCol = entityRightWorldX / this.gp.getTileSize();
        int entityTopRow = entityTopWorldY / this.gp.getTileSize();
        int entityBottomRow = entityBottmWorldY / this.gp.getTileSize();

        int tileNum1, tileNum2;

        switch (entity.getDirection()) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.getSpeed()) / this.gp.getTileSize();
                if (entityTopRow < 0) {
                    entity.setCollisionOn(true);
                    break;
                }
                tileNum1 = this.gp.getMapTileNum(entityLeftCol, entityTopRow);
                tileNum2 = this.gp.getMapTileNum(entityRightCol, entityTopRow);
                if (this.gp.getTileCollision(tileNum1) == true
                        || this.gp.getTileCollision(tileNum2) == true) {
                    entity.setCollisionOn(true);
                }
                break;
            case "down":
                entityBottomRow = (entityBottmWorldY + entity.getSpeed()) / this.gp.getTileSize();
                if (entityBottomRow >= gp.getMaxWorldRow()) {
                    entity.setCollisionOn(true);
                    break;
                }
                tileNum1 = this.gp.getMapTileNum(entityLeftCol, entityBottomRow);
                tileNum2 = this.gp.getMapTileNum(entityRightCol, entityBottomRow);
                if (this.gp.getTileCollision(tileNum1) == true
                        || this.gp.getTileCollision(tileNum2) == true) {
                    entity.setCollisionOn(true);
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.getSpeed()) / this.gp.getTileSize();
                if (entityLeftCol < 0) {
                    entity.setCollisionOn(true);
                    break;
                }
                tileNum1 = this.gp.getMapTileNum(entityLeftCol, entityTopRow);
                tileNum2 = this.gp.getMapTileNum(entityLeftCol, entityBottomRow);
                if (this.gp.getTileCollision(tileNum1) == true
                        || this.gp.getTileCollision(tileNum2) == true) {
                    entity.setCollisionOn(true);
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.getSpeed()) / this.gp.getTileSize();
                if (entityRightCol >= gp.getMaxWorldCol()) {
                    entity.setCollisionOn(true);
                    break;
                }
                tileNum1 = this.gp.getMapTileNum(entityRightCol, entityTopRow);
                tileNum2 = this.gp.getMapTileNum(entityRightCol, entityBottomRow);
                if (this.gp.getTileCollision(tileNum1) == true
                        || this.gp.getTileCollision(tileNum2) == true) {
                    entity.setCollisionOn(true);
                }
                break;
        }
    }

    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        for (int i = 0; i < this.gp.getObjectLength(); i++) {
            if (this.gp.getObject(i) != null) {
                // Get entity's solid area position
                entity.setSolidAreaX(entity.getWorldX() + entity.getSolidAreaX());
                entity.setSolidAreaY(entity.getWorldY() + entity.getSolidAreaY());

                // Get the object's solid area position
                this.gp.setObjectSolidAreaX(
                        i, this.gp.getObjectWorldX(i) + this.gp.getObjectSolidAreaX(i));
                this.gp.setObjectSolidAreaY(
                        i, this.gp.getObjectWorldY(i) + this.gp.getObjectSolidAreaY(i));

                switch (entity.getDirection()) {
                    case "up":
                        entity.setSolidAreaY(entity.getSolidAreaY() - entity.getSpeed());
                        break;
                    case "down":
                        entity.setSolidAreaY(entity.getSolidAreaY() + entity.getSpeed());
                        break;
                    case "right":
                        entity.setSolidAreaX(entity.getSolidAreaX() + entity.getSpeed());
                        break;
                    case "left":
                        entity.setSolidAreaX(entity.getSolidAreaX() - entity.getSpeed());
                        break;
                }

                if (entity.getSolidArea().intersects(this.gp.getObjectSolidArea(i))) {
                    if (this.gp.getObject(i) != entity) {
                        if (this.gp.getObject(i).isCollision() == true) {
                            entity.setCollisionOn(true);
                        }
                        if (player == true) {
                            index = i;
                        }
                    }
                }

                entity.setSolidAreaX(entity.getSolidAreaDefaultX());
                entity.setSolidAreaY(entity.getSolidAreaDefaultY());
                this.gp.setObjectSolidAreaX(i, this.gp.getObjectSolidAreaDefaultX(i));
                this.gp.setObjectSolidAreaY(i, this.gp.getObjectSolidAreaDefaultY(i));
            }
        }
        return index;
    }

    // Branch A Integration: Added checkEntity for interacting with trees/targets
    public int checkEntity(Entity entity, Entity[] target) {
        int index = 999;

        for (int i = 0; i < target.length; i++) {
            if (target[i] != null) {
                // Get entity's solid area position
                entity.setSolidAreaX(entity.getWorldX() + entity.getSolidAreaX());
                entity.setSolidAreaY(entity.getWorldY() + entity.getSolidAreaY());

                // Get the object's solid area position
                target[i].setSolidAreaX(target[i].getWorldX() + target[i].getSolidAreaX());
                target[i].setSolidAreaY(target[i].getWorldY() + target[i].getSolidAreaY());

                switch (entity.getDirection()) {
                    case "up":
                        entity.setSolidAreaY(entity.getSolidAreaY() - entity.getSpeed());
                        break;
                    case "down":
                        entity.setSolidAreaY(entity.getSolidAreaY() + entity.getSpeed());
                        break;
                    case "right":
                        entity.setSolidAreaX(entity.getSolidAreaX() + entity.getSpeed());
                        break;
                    case "left":
                        entity.setSolidAreaX(entity.getSolidAreaX() - entity.getSpeed());
                        break;
                }

                if (entity.getSolidArea().intersects(target[i].getSolidArea())) {
                    if (target[i] != entity) {
                        entity.setCollisionOn(true);
                        index = i;
                    }
                }

                entity.setSolidAreaX(entity.getSolidAreaDefaultX());
                entity.setSolidAreaY(entity.getSolidAreaDefaultY());
                target[i].setSolidAreaX(target[i].getSolidAreaDefaultX());
                target[i].setSolidAreaY(target[i].getSolidAreaDefaultY());
            }
        }
        return index;
    }

    // Branch A Integration: Added checkPlayer for NPC/Object interactions
    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;

        // Get entity's solid area position
        entity.setSolidAreaX(entity.getWorldX() + entity.getSolidAreaX());
        entity.setSolidAreaY(entity.getWorldY() + entity.getSolidAreaY());

        // Get player's solid area position
        this.gp.setPlayerSolidAreaX(this.gp.getPlayerWorldX() + this.gp.getPlayerSolidAreaX());
        this.gp.setPlayerSolidAreaY(this.gp.getPlayerWorldY() + this.gp.getPlayerSolidAreaY());

        switch (entity.getDirection()) {
            case "up":
                entity.setSolidAreaY(entity.getSolidAreaY() - entity.getSpeed());
                break;
            case "down":
                entity.setSolidAreaY(entity.getSolidAreaY() + entity.getSpeed());
                break;
            case "right":
                entity.setSolidAreaX(entity.getSolidAreaX() + entity.getSpeed());
                break;
            case "left":
                entity.setSolidAreaX(entity.getSolidAreaX() - entity.getSpeed());
                break;
        }

        if (entity.getSolidArea().intersects(this.gp.getPlayerSolidArea())) {
            entity.setCollisionOn(true);
            contactPlayer = true;
        }

        entity.setSolidAreaX(entity.getSolidAreaDefaultX());
        entity.setSolidAreaY(entity.getSolidAreaDefaultY());
        this.gp.setPlayerSolidAreaX(this.gp.getPlayerSolidAreaDefaultX());
        this.gp.setPlayerSolidAreaY(this.gp.getPlayerSolidAreaDefaultY());

        return contactPlayer;
    }
}
