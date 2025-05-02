package main;

import entity.Entity;
import entity.NPC_Mysterious_Stranger;
import entity.Player;
import environment.EnvironmentMngr;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JPanel;
import monster.MON_Island_Native;
import object.Item;
import object.OBJ_AXE;
import object.OBJ_TORCH;
import object.OBJ_WOOD;
import save.SaveStorage;
import tile.TileManager;
import tile_interactive.InteractiveTile;

public class GamePanel extends JPanel implements Runnable {

    public boolean drawHitboxes; // debug boolean make this true !! IN CONSTRUCTOR !! if there are any errors
                                 // with hit boxes.

    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = scale * originalTileSize;
    public final int maxScreenCol = 22;
    public final int maxScreenRow = 16;
    // public final int screenWidth = tileSize * maxScreenCol;
    // public final int screenHeight = tileSize * maxScreenRow;
    // SCREEN DIMENSIONS
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public final int screenWidth = screenSize.width;
    public final int screenHeight = screenSize.height;

    // WORLD SETTINGS
    public final int maxWorldCol = 250;
    public final int maxWorldRow = 250;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    // FOR FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    private boolean lastRightArrowPressed = false;
    private boolean lastLeftArrowPressed = false;

    // FPS
    int fps = 60;
    public int currentFPS;

    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);

    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    public Entity[] obj = new Entity[800]; // can be displayed 300 objects at the same time
    public Entity[] npc = new Entity[10]; // 10 npcs can be displayed
    public Entity[] monster = new Entity[10]; // 10 monsters can be displayed at the same time
    public InteractiveTile[] iTile = new InteractiveTile[50];
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public JDialog pausePanel = new PauseScreen(this);
    EnvironmentMngr eManager = new EnvironmentMngr(this);
    public List<CraftingCategory> craftingCategories = new ArrayList<>();
    public SaveStorage saveStorage = new SaveStorage(this);
    // Game State (Pause/Unpause)
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int gameOverState = 5;
    public final int sleepState = 6;
    public final int craftingState = 7;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(keyH);
        this.setFocusable(true);
        drawHitboxes = false; // !!!!! MAKE THIS FALSE IF YOU DONT WANT HITBOXES TO BE DRAWN !!!!!!!!
        setUpGame();
        requestFocusInWindow();
        System.out.println("Screen resolution: " + Toolkit.getDefaultToolkit().getScreenSize());
    }

    public void setUpGame() {
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setPigs();
        // aSetter.setInteractiveTile();
        eManager.setup();
        gameState = playState;

        // to test if inventory works
        player.inventory.setItem(0, new OBJ_AXE(this));

        // Creating a blank buffered image which is as large as our screen
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics(); // first we will draw our game to tempScreen, then fit it to full
                                                    // screen.

        setupCraftingRecipes();
        setFullScreen();
    }

    private void setupCraftingRecipes() {
        CraftingCategory tools = new CraftingCategory("Tools");
        Item torch = new OBJ_TORCH(this);
        List<Material> torchMaterials = new ArrayList<>();
        torchMaterials.add(new Material(new OBJ_WOOD(this), 2));
        tools.addRecipe(new CraftingRecipe(torch, torchMaterials));
        craftingCategories.add(tools);
    }

    public void setFullScreen() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("mac")) {
            Main.frame.dispose();
            Main.frame.setUndecorated(true);
        
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // true full screen dimensions
            Main.frame.setSize(screenSize);
            Main.frame.setLocation(0, 0);
        
            Main.frame.setVisible(true);
        
            screenWidth2 = screenSize.width;
            screenHeight2 = screenSize.height;
        } else {
            Main.frame.dispose();
            Main.frame.setUndecorated(true);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            screenWidth2 = screenSize.width;
            screenHeight2 = screenSize.height;

            Main.frame.setSize(screenWidth2, screenHeight2);
            Main.frame.setLocation(0, 0);
            Main.frame.setVisible(true);
        }
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();

    }

    // Former run method()

    /*
     * public void run()
     * {
     * double drawInterval = 1000000000 / fps;
     * double nextDrawTime = System.nanoTime() + drawInterval;
     * 
     * while(gameThread != null)
     * {
     * // Updates character position
     * update();
     * 
     * // Draws the screen with the updated information
     * repaint();
     * 
     * try
     * {
     * double remainingTime = nextDrawTime - System.nanoTime();
     * remainingTime = remainingTime / 1000000;
     * 
     * if (remainingTime < 0)
     * {
     * remainingTime = 0;
     * }
     * 
     * Thread.sleep((long) remainingTime);
     * nextDrawTime += drawInterval;
     * }
     * catch (InterruptedException ex)
     * {
     * ex.printStackTrace();
     * }
     * }
     * }
     */
    public void run() {
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

        long lastFpsTime = System.currentTimeMillis();
        int fpsCounter = 0;

        while (gameThread != null) {
            long startTime = System.nanoTime();

            update();

            synchronized (tempScreen) {
                drawToTempScreen();
            }

            repaint();
            fpsCounter++;

            // FPS display
            if (System.currentTimeMillis() - lastFpsTime >= 1000) {
                currentFPS = fpsCounter;
                fpsCounter = 0;
                lastFpsTime += 1000;
                System.out.println("FPS: " + currentFPS);
            }

            // Consistent pacing (instead of sleepTime = remaining time)
            try {
                Thread.sleep(2); // Fixed pacing wins here
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Optional: prevent runaway if frame took too long
            while (System.nanoTime() - startTime < OPTIMAL_TIME) {
                Thread.yield(); // Chill until next frame time
            }
        }
    }

    // Run method where the windows had visual bug.

    /* public void run() {
        double drawInterval = 1000000000 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();

                synchronized (tempScreen) {
                    drawToTempScreen(); // draws game elements onto tempScreen
                }
                repaint(); // now we draw the buffered image to the screen.
                drawCount++;
                delta--;
            }

            try {
                Thread.sleep(2); // CPU rahatlasın
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                currentFPS = drawCount;
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer += 1000;
            }
            // // fps cap to 60 by introducing sleep time to only refresh each frame in 60fps
            //long elapsed = System.nanoTime() - lastTime;
            //long sleepTime = (long) (drawInterval - elapsed);
            //if (sleepTime > 0) {
            //    try {
            //        Thread.sleep(sleepTime / 1000000, (int) (sleepTime % 1000000)); // Sleep in ms and ns
            //    } catch (InterruptedException e) {
            //        e.printStackTrace();  
            //    }
            //}

            //if (timer >= 1000000000) {
            //    System.out.println("FPS: " + drawCount);
            //    currentFPS = drawCount;
            //    drawCount = 0;
            //    timer = 0;
            //}
        }
    } */

    public void update() {

        if (gameState == dialogueState && !hasFocus()) 
        {
            requestFocusInWindow();
        }

        if (ui.dialogueInputCooldown > 0) 
        {
            ui.dialogueInputCooldown--;
        } else if (ui.dialogueInputCooldown < 0) 
        {
            ui.dialogueInputCooldown = 0;
        }

        if (keyH.kPressed) {
            if (gameState == playState) {
                gameState = craftingState;
            } else if (gameState == craftingState) {
                gameState = playState;
            }
            keyH.kPressed = false;
        }

        if (keyH.cPressed) {
            if (gameState == playState) {
                gameState = characterState;
            } else if (gameState == characterState) {
                gameState = playState;
            }
            keyH.cPressed = false;
        }

        if (gameState == dialogueState) 
        {
            if (ui.dialogueStateEntered) 
            {
                ui.dialogueInputCooldown = 0;
            }

            if (ui.dialogueInputCooldown <= 0) 
            {
                boolean rightPressedThisFrame = keyH.rightArrowPressed && !lastRightArrowPressed;
                boolean leftPressedThisFrame = keyH.leftArrowPressed && !lastLeftArrowPressed;

                if (rightPressedThisFrame) 
                {
                    if (ui.currentDialoguePage < ui.dialoguePages.size() - 1) 
                    {
                        ui.currentDialoguePage++;
                        ui.dialogueChanged = true;
                        ui.dialogueInputCooldown = 10;
                        
                    } else if (ui.currentDialoguePage == ui.dialoguePages.size() - 1) 
                    {
                        gameState = playState;
                        ui.currentDialoguePage = 0;
                        ui.dialoguePages.clear();
                        player.dialogueCooldown = player.cooldownDuration;
                        ui.dialogueInputCooldown = 10;
                        
                    } 
                } else if (leftPressedThisFrame && ui.currentDialoguePage > 0) 
                {
                    ui.currentDialoguePage--;
                    ui.dialogueChanged = true;
                    ui.dialogueInputCooldown = 10;
                }

                lastRightArrowPressed = keyH.rightArrowPressed;
                lastLeftArrowPressed = keyH.leftArrowPressed;
            } 
        }

        if (gameState == playState) {
            if (keyH.onePressed) {
                player.inventory.setSelectedSlot(0);
                keyH.onePressed = false;
            }
            if (keyH.twoPressed) {
                player.inventory.setSelectedSlot(1);
                keyH.twoPressed = false;
            }
            if (keyH.threePressed) {
                player.inventory.setSelectedSlot(2);
                keyH.threePressed = false;
            }
            if (keyH.fourPressed) {
                player.inventory.setSelectedSlot(3);
                keyH.fourPressed = false;
            }
            if (keyH.fivePressed) {
                player.inventory.setSelectedSlot(4);
                keyH.fivePressed = false;
            }

            player.collisionOn = false;
            cChecker.checkTile(player);
            int playerMonsterIndex = cChecker.checkEntity(player, monster);
            cChecker.checkEntity(player, npc);

            if (playerMonsterIndex != 999 && monster[playerMonsterIndex] instanceof MON_Island_Native) {
                if (!player.isInvincible() && !player.isAttackingForCollision()) {
                    player.contactMonster(((MON_Island_Native) monster[playerMonsterIndex]).getDamage());
                }
            }

            player.update();

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null && npc[i] instanceof NPC_Mysterious_Stranger) {
                    ((NPC_Mysterious_Stranger) npc[i]).update();
                }
            }

            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if (monster[i].alive) {
                        if (!monster[i].dying) {
                            int monsterPlayerIndex = cChecker.checkEntity(monster[i], new Entity[] { player });
                            if (monsterPlayerIndex != 999 && !player.isInvincible()
                                    && !player.isAttackingForCollision()) {
                                if (monster[i] instanceof MON_Island_Native) {
                                    player.contactMonster(((MON_Island_Native) monster[i]).getDamage());
                                }
                            }
                            monster[i].update();
                        } else {
                            monster[i].update();
                        }
                    } else {
                        monster[i] = null;
                    }
                }
            }

            for (int i = 0; i < iTile.length; i++) {
                if (iTile[i] != null) {
                    iTile[i].update();
                }
            }

            eManager.update();
        }

        if (gameState == craftingState) {
            ui.updateCrafting();
        }

        if (gameState == pauseState) {
            // Nothing since the game is paused
        }
        if (gameState == gameOverState) {
            if (keyH.rPressed) {
                restartGame();
                gameState = playState;
                keyH.rPressed = false;
            }
            if (keyH.escPressed) {
                System.exit(0);
            }
        }

        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].update();
            }
        }
    }

    /*
     * Actually everything except the first and the last 2 lines are the same with
     * the previous paintComponent method.
     */
    public void drawToTempScreen() 
    {
        if (gameState == dialogueState) 
        {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }

        // tile
        tileM.draw(g2);

        // creating a list that will hold all objects
        List<Entity> entitiesToDraw = new ArrayList<>();

        for (Entity interactableEntity : iTile) {
            if (interactableEntity != null) {
                entitiesToDraw.add(interactableEntity);
            }
        }

        for (Entity objEntity : obj) {
            if (objEntity != null) {
                entitiesToDraw.add(objEntity);
            }
        }

        for (Entity npcEntity : npc) {
            if (npcEntity != null) {
                entitiesToDraw.add(npcEntity);
            }
        }

        for (Entity monsterEntity : monster) {
            if (monsterEntity != null) {
                entitiesToDraw.add(monsterEntity);
            }
        }

        entitiesToDraw.add(player);

        // sorting the entities by their worldY value
        // lower worldY are drawn first
        for (int j = 0; j < entitiesToDraw.size() - 1; j++) {

            for (int i = 0; i < entitiesToDraw.size() - 1 - j; i++) {

                if (entitiesToDraw.get(i).worldY > entitiesToDraw.get(i + 1).worldY) {
                    Entity temp = entitiesToDraw.get(i);
                    entitiesToDraw.set(i, entitiesToDraw.get(i + 1));
                    entitiesToDraw.set(i + 1, temp);
                }
            }
        }

        for (Entity entity : entitiesToDraw) {
            if (entity == player) {

                boolean isMoving = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;
                entity.draw(g2, true, isMoving);
            } else {

                entity.draw(g2, false, false);
            }
        }

        // Environment
        eManager.draw(g2);
        // ui
        ui.draw(g2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null); // we use alternative screen width and height
        Toolkit.getDefaultToolkit().sync();
    }

    public void removeObject(Entity anEntity) {
        for (int i = 0; i < this.obj.length; i++) {
            if (obj[i] == anEntity) {
                arrangeObj(i);
                return;
            }
        }
    }

    private void arrangeObj(int index) {
        for (int i = index + 1; i < this.obj.length; i++) {
            if (obj[i] == null) {
                return;
            }
            obj[i] = obj[i + 1];
        }
    }

    // Currently revives the player.
    public void restartGame() {
        player.setDefaultValues();
        player.restartPlayer();
        aSetter.setMonster();
        aSetter.setObject();
        aSetter.setNPC();
        // aSetter.setInteractiveTile();
    }
    
    public void handleSaveSlot(int slot) {
        if (saveStorage.isSlotOccupied(slot)) {
            ui.selectedSaveSlot = slot;
            gameState = UI.GAMESTATE_CONFIRM_OVERWRITE;
        } else {
            saveStorage.saveGame(slot);
            gameState = UI.GAMESTATE_PAUSE;
        }
    }

}
