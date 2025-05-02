package main;

import java.io.Serializable;

import entity.Entity;
import entity.NPC_Mysterious_Stranger;
import monster.MON_Island_Native;
import monster.MON_Pig;
import object.*;
import tile_interactive.IT_DryTree;

public class AssetSetter implements Serializable{

    GamePanel gp;
    int objectCounter = 0;
    int iTileCounter = 0;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject() {
        
        // Other manually placed objects:
        OBJ_CAMPFIRE campfire1 = new OBJ_CAMPFIRE(gp);
        addObject(campfire1, 23, 7);

        OBJ_KEY key1 = new OBJ_KEY(gp);
        addObject(key1, 23, 8);

        OBJ_AXE axe1 = new OBJ_AXE(gp);
        addObject(axe1, 23, 9);

        OBJ_SPEAR spear1 = new OBJ_SPEAR(gp);
        addObject(spear1, 23, 10);

        OBJ_TORCH torch1 = new OBJ_TORCH(gp);
        addObject(torch1, 23, 11);

        OBJ_SHELTER shelter1 = new OBJ_SHELTER(gp);
        addObject(shelter1, 23, 12);

        IT_DryTree dryTree1 = new IT_DryTree(gp);
        addObject(dryTree1, 23, 13);

        OBJ_WATER_BUCKET waterBucket = new OBJ_WATER_BUCKET(gp);
        addObject(waterBucket, 23, 14);

        OBJ_STONE stone = new OBJ_STONE(gp);
        addObject(stone, 23, 16);

        setRandomTrees();
        setRandomBushes();
        setRandomStones();
    }

    public void addObject(Entity obj, int x, int y) {
        obj.worldX = x * gp.tileSize;
        obj.worldY = y * gp.tileSize;
        gp.obj[objectCounter] = obj;
        objectCounter++;
    }

    // Method to randomly generate bushes on the map (completely random, fixed
    // count).
    public void setRandomBushes() {
        // Define how many bushes you want to generate.
        int numberOfBushes = 30; // increased the number because some won't spam because of water tiles.

        // Choose a starting index for bushes.

        // Loop to create bushes at random positions.
        for (int i = 0; i < numberOfBushes; i++) {
            int randomCol = (int) (Math.random() * gp.maxWorldCol);
            int randomRow = (int) (Math.random() * gp.maxWorldRow);

            // if the tile we are trying to spawn a tree is not water.
            // We may add other statements in the future if we will have other tiles that we
            // do not want bushes on them.
            if (gp.tileM.getMapTileNum()[randomCol][randomRow] != 1) {
                int worldX = randomCol * gp.tileSize;
                int worldY = randomRow * gp.tileSize;

                gp.obj[objectCounter] = new OBJ_BUSH(gp);
                gp.obj[objectCounter].worldX = worldX;
                gp.obj[objectCounter].worldY = worldY;

                objectCounter++;
            }
        }
    }

    // This method generates trees based on location weights.
    public void setRandomTrees() {

        // Define a region where trees spawn more frequently
        // Here we can get the coordinates of the places where we want more trees.
        int highDensityMinCol = 30;
        int highDensityMaxCol = 50;
        int highDensityMinRow = 30;
        int highDensityMaxRow = 50;

        // Define probabilities for each area.
        // For high density areas, we use a higher chance.
        double highDensityProbability = 0.4; // 100% chance to place a tree at a given attempt.
        double lowDensityProbability = 0.02; // 10% chance outside the high density area.

        // Number of random attempts. Increasing this number will result in more trees
        // overall.
        int totalAttempts = 2000;

        for (int i = 0; i < totalAttempts; i++) {
            int randomCol = (int) (Math.random() * gp.maxWorldCol);
            int randomRow = (int) (Math.random() * gp.maxWorldRow);

            // Determine the probability based on whether the cell is in the high density
            // area.
            double probability = lowDensityProbability;
            if (randomCol >= highDensityMinCol && randomCol <= highDensityMaxCol
                    && randomRow >= highDensityMinRow && randomRow <= highDensityMaxRow) {
                probability = highDensityProbability;
            }

 
            //Only place a tree if our random chance succeeds.
            //and if the tile we are trying to spawn a tree is not water. 
            //We may add other statements in the future if we will have other tiles that we do not want bushes on them. 
            if (Math.random() < probability && gp.tileM.getMapTileNum()[randomCol][randomRow] != 1) 
            {
                int worldX = randomCol * gp.tileSize;
                int worldY = randomRow * gp.tileSize;

                // Randomly choose which type of tree to spawn.
                Entity tree;
                if (Math.random() < 0.3) 
                {
                    tree = new OBJ_APPLE_TREE(gp);
                    gp.obj[objectCounter] = tree;
                    tree.worldX = worldX;
                    tree.worldY = worldY;
                    objectCounter++;
                } else 
                {
                    tree = new IT_DryTree(gp);
                    if (iTileCounter < gp.iTile.length) 
                    {
                        gp.iTile[iTileCounter] = (IT_DryTree) tree;
                        gp.iTile[iTileCounter].worldX = worldX;
                        gp.iTile[iTileCounter].worldY = worldY;
                        iTileCounter++;
                    }
                }
            }
        }
    }

    public void setRandomStones() {
        int totalPatches = 60; // Number of stone patches
        int stonesPerPatchMin = 3;
        int stonesPerPatchMax = 5;

        for (int i = 0; i < totalPatches; i++) {
            int baseCol = (int) (Math.random() * gp.maxWorldCol);
            int baseRow = (int) (Math.random() * gp.maxWorldRow);

            int stonesInPatch = stonesPerPatchMin + (int) (Math.random() * (stonesPerPatchMax - stonesPerPatchMin + 1));

            for (int j = 0; j < stonesInPatch; j++) {
                // Random offset around the base position, to make them close
                int offsetCol = baseCol + (int) (Math.random() * 3) - 1; // -1, 0, or 1
                int offsetRow = baseRow + (int) (Math.random() * 3) - 1; // -1, 0, or 1

                // Make sure offset position is within world bounds
                if (offsetCol >= 0 && offsetCol < gp.maxWorldCol && offsetRow >= 0 && offsetRow < gp.maxWorldRow) {
                    // Make sure stone is not placed on water
                    if (gp.tileM.getMapTileNum()[offsetCol][offsetRow] != 1) {
                        int worldX = offsetCol * gp.tileSize;
                        int worldY = offsetRow * gp.tileSize;

                        Entity stone = new OBJ_STONE(gp);
                        if (objectCounter < gp.obj.length) {
                            gp.obj[objectCounter] = stone;
                            stone.worldX = worldX;
                            stone.worldY = worldY;
                            objectCounter++;
                        }
                    }
                }
            }
        }
    }

    public void setNPC() {
        gp.npc[0] = new NPC_Mysterious_Stranger(gp);
        gp.npc[0].worldX = gp.tileSize * 21;
        gp.npc[0].worldY = gp.tileSize * 21;
    }

    public void setMonster() {
        gp.monster[0] = new MON_Island_Native(gp);
        gp.monster[0].worldX = gp.tileSize * 25;
        gp.monster[0].worldY = gp.tileSize * 25;

        gp.monster[1] = new MON_Island_Native(gp);
        gp.monster[1].worldX = gp.tileSize * 26;
        gp.monster[1].worldY = gp.tileSize * 26;
    }

    public void setPigs() 
    {
    gp.monster[2] = new MON_Pig(gp);
    gp.monster[2].worldX = gp.tileSize * 25;
    gp.monster[2].worldY = gp.tileSize * 23;
    
    gp.monster[3] = new MON_Pig(gp);
    gp.monster[3].worldX = gp.tileSize * 27;
    gp.monster[3].worldY = gp.tileSize * 25;
}

    /*public void setInteractiveTile()
    {
        int i = 0;
        gp.iTile[i] = new IT_DryTree(gp, 23, 13); i++;
        gp.iTile[i] = new IT_DryTree(gp, 23, 14); i++;
        gp.iTile[i] = new IT_DryTree(gp, 23, 15); i++;
        gp.iTile[i] = new IT_DryTree(gp, 23, 16); i++;
        gp.iTile[i] = new IT_DryTree(gp, 23, 17); i++;
        gp.iTile[i] = new IT_DryTree(gp, 23, 18); i++;
        gp.iTile[i] = new IT_DryTree(gp, 23, 19); i++;
        System.out.println("metod oldu");
    }*/
}
