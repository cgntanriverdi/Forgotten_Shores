package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import main.GamePanel;
import monster.MON_Island_Native;
import monster.MON_Pig;
import object.OBJ_RAW_MEAT;

public class Entity implements Serializable {
    public GamePanel gp;
    public int worldX; // x-coordinate of entity
    public int worldY; // y-coordinate of entity
    public int speed; // movement speed of entity
    public float scale = 1.0f;
    public int actionLockCounter = 0;

    public int hp; // For monsters (for now)

    public transient BufferedImage up1, up2, up3, up4, down1, down2, down3, down4, left1, left2, left3, left4, right1, right2,
            right3, right4, idle1, idle2, idle3, idle4, attackUp1, attackUp2, attackLeft1, attackLeft2, attackDown1,
            attackDown2, attackRight1, attackRight2; // representing the images which will swap during movement & idle
                                                     // animations & attack animations
    public String direction; // where entity looks

    public transient BufferedImage scaledUp1, scaledUp2, scaledDown1, scaledDown2,
            scaledLeft1, scaledLeft2, scaledRight1, scaledRight2,
            scaledIdle1, scaledIdle2, scaledIdle3, scaledIdle4,
            scaledAttackUp1, scaledAttackUp2, scaledAttackDown1, scaledAttackDown2,
            scaledAttackLeft1, scaledAttackLeft2, scaledAttackRight1, scaledAttackRight2,
            scaledImage;

    public int spriteCounter = 0; //
    public int spriteNum = 1; // for example: is it up1 or up2

    public transient Rectangle solidArea; // part that cannot colide
    public transient Rectangle attackArea;
    public int solidAreaDefaultX;
    public int solidAreaDefaultY;
    public boolean collisionOn = false;
    public boolean collision = false;
    public boolean hpBarStatus = false;

    public boolean alive = true;
    public boolean dying = false;

    public int dyingCounter = 0;
    public int hpBarCounter = 0;

    public boolean isMovingEntity = false;

    public BufferedImage image;
    public String name;

    public boolean invincible = false;
    public int invincibilityTimer = 0;
    public int invincibilityDuration = 60;

    public int attackValue;
    public int defenseValue;

    public int lightRadius;

    public Entity(GamePanel gp) {
        this.gp = gp;
        this.solidArea = new Rectangle();
    }

    // Method to scale images once during initialization
    public void scaleImages(float scale) {
        int scaledWidth = (int) (gp.tileSize * scale);
        int scaledHeight = (int) (gp.tileSize * scale);

        scaledUp1 = scaleImage(up1, scaledWidth, scaledHeight);
        scaledUp2 = scaleImage(up2, scaledWidth, scaledHeight);
        scaledDown1 = scaleImage(down1, scaledWidth, scaledHeight);
        scaledDown2 = scaleImage(down2, scaledWidth, scaledHeight);
        scaledLeft1 = scaleImage(left1, scaledWidth, scaledHeight);
        scaledLeft2 = scaleImage(left2, scaledWidth, scaledHeight);
        scaledRight1 = scaleImage(right1, scaledWidth, scaledHeight);
        scaledRight2 = scaleImage(right2, scaledWidth, scaledHeight);
        scaledIdle1 = scaleImage(idle1, scaledWidth, scaledHeight);
        scaledIdle2 = scaleImage(idle2, scaledWidth, scaledHeight);
        scaledIdle3 = scaleImage(idle3, scaledWidth, scaledHeight);
        scaledIdle4 = scaleImage(idle4, scaledWidth, scaledHeight);
        scaledAttackUp1 = scaleImage(attackUp1, scaledWidth, scaledHeight);
        scaledAttackUp2 = scaleImage(attackUp2, scaledWidth, scaledHeight);
        scaledAttackDown1 = scaleImage(attackDown1, scaledWidth, scaledHeight);
        scaledAttackDown2 = scaleImage(attackDown2, scaledWidth, scaledHeight);
        scaledAttackLeft1 = scaleImage(attackLeft1, scaledWidth, scaledHeight);
        scaledAttackLeft2 = scaleImage(attackLeft2, scaledWidth, scaledHeight);
        scaledAttackRight1 = scaleImage(attackRight1, scaledWidth, scaledHeight);
        scaledAttackRight2 = scaleImage(attackRight2, scaledWidth, scaledHeight);
        scaledImage = scaleImage(image, scaledWidth, scaledHeight); // For OBJ_RAW_MEAT
    }

    private BufferedImage scaleImage(BufferedImage original, int width, int height) {
        if (original == null)
            return null;
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return scaled;
    }

    // NPC movement method for future NPCs implementation
    public void update() {
        if (isMovingEntity & !dying) {
            setAction();
            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkObject(this, false);
            gp.cChecker.checkEntity(this, gp.monster);
            gp.cChecker.checkPlayer(this);
            gp.cChecker.checkEntity(this, gp.iTile);

            if (!this.collisionOn) {
                if (this.direction != null) {
                    if (this.direction.equals("up")) {
                        this.worldY -= this.speed;
                    } else if (direction.equals("down")) {
                        this.worldY += this.speed;
                    } else if (direction.equals("left")) {
                        this.worldX -= this.speed;
                    } else if (direction.equals("right")) {
                        this.worldX += this.speed;
                    }
                }
            }
        }

        this.spriteCounter++;
        if (this.spriteCounter > 12) {
            if (this.spriteNum == 1) {
                this.spriteNum = 2;
            } else if (this.spriteNum == 2) {
                this.spriteNum = 1;
            }
            this.spriteCounter = 0;
        }

        if (invincibilityTimer > 0) {
            invincibilityTimer--;

            if (invincibilityTimer == 0) {
                invincible = false;
            }
        }

        if (dying) {
            dyingCounter++;
        }

    }

    public void draw(Graphics2D g2, boolean isPlayer, boolean isMoving) {

        // skips draw if already dead.
        if (!alive & !dying) {
            return;
        }

        BufferedImage image = null;

        int screenX = worldX;
        int screenY = worldY;

        int tileSize = 48;

        if (gp != null && gp.player != null) {
            screenX = worldX - gp.player.worldX + gp.player.screenX;
            screenY = worldY - gp.player.worldY + gp.player.screenY;
            tileSize = gp.tileSize;
        }

        int scaledWidth = (int) (tileSize * scale);
        int scaledHeight = (int) (tileSize * scale);

        int adjustedScreenX = screenX - (scaledWidth - tileSize) / 2;
        int adjustedScreenY = screenY - (scaledHeight - tileSize) / 2;

        // int tempScreenX = adjustedScreenX;
        // int tempScreenY = adjustedScreenY;

        if (this instanceof OBJ_RAW_MEAT) {
            image = scaledImage;
            if (image != null) {
                g2.drawImage(image, adjustedScreenX, adjustedScreenY, scaledWidth, scaledHeight, null);
            }
            return;
        }

        if (isPlayer) {
            if (!isMoving) {
                switch (spriteNum) {
                    case 1:
                        if (!((Player) this).attacking) {
                            image = scaledIdle1;
                        } else {
                            image = scaledAttackUp1;
                        }
                        break;
                    case 2:
                        if (!((Player) this).attacking) {
                            image = scaledIdle2;
                        } else {
                            image = scaledAttackDown1;
                        }
                        break;
                    case 3:
                        if (!((Player) this).attacking) {
                            image = scaledIdle3;
                        } else {
                            image = scaledAttackLeft1;
                        }
                        break;
                    case 4:
                        if (!((Player) this).attacking) {
                            image = scaledIdle4;
                        } else {
                            image = scaledAttackRight1;
                        }
                        break;
                    default:
                        image = scaledIdle1;
                        break;
                }
            } else {
                int walkingFrame = (spriteNum == 1 || spriteNum == 2) ? spriteNum : 1;
                switch (direction) {
                    case "up":
                        if (!((Player) this).attacking) {
                            image = (walkingFrame == 1) ? scaledUp1 : scaledUp2;
                        } else {
                            // tempScreenY = screenY - gp.tileSize;
                            image = (walkingFrame == 1) ? scaledAttackUp1 : scaledAttackUp2;
                        }
                        break;
                    case "down":
                        if (!((Player) this).attacking) {
                            image = (walkingFrame == 1) ? scaledDown1 : scaledDown2;
                        } else {
                            image = (walkingFrame == 1) ? scaledAttackDown1 : scaledAttackDown2;
                        }
                        break;
                    case "left":
                        if (!((Player) this).attacking) {
                            image = (walkingFrame == 1) ? scaledLeft1 : scaledLeft2;
                        } else {
                            // tempScreenX = screenX - gp.tileSize;
                            image = (walkingFrame == 1) ? scaledAttackLeft1 : scaledAttackLeft2;
                        }
                        break;
                    case "right":
                        if (!((Player) this).attacking) {
                            image = (walkingFrame == 1) ? scaledRight1 : scaledRight2;
                        } else {
                            image = (walkingFrame == 1) ? scaledAttackRight1 : scaledAttackRight2;
                        }
                        break;
                    default:
                        image = scaledIdle1;
                        break;
                }
            }
        }

        else if (isMovingEntity) {
            int frame;
            if (spriteNum == 1) {
                frame = 1;
            } else {
                frame = 2;
            }

            switch (direction) {
                case "up":
                    if (frame == 1) {
                        image = scaledUp1;
                    } else {
                        image = scaledUp2;
                    }
                    break;
                case "down":
                    if (frame == 1) {
                        image = scaledDown1;
                    } else {
                        image = scaledDown2;
                    }
                    break;
                case "left":
                    if (frame == 1) {
                        image = scaledLeft1;
                    } else {
                        image = scaledLeft2;
                    }
                    break;
                case "right":
                    if (frame == 1) {
                        image = scaledRight1;
                    } else {
                        image = scaledRight2;
                    }
                    break;
                default:
                    image = scaledDown1;
                    break;
            }
        } else {
            switch (spriteNum) {
                case 1:
                    image = scaledIdle1;
                    break;
                case 2:
                    image = scaledIdle2;
                    break;
                case 3:
                    image = scaledIdle3;
                    break;
                default:
                    image = scaledIdle1;
                    break;
            }
        }

        // Enemy Health Bar
        if ((this instanceof monster.MON_Island_Native || this instanceof MON_Pig) && hpBarStatus) {
            if (this instanceof MON_Island_Native) {
                hp = ((MON_Island_Native) this).getLife();
            } else {
                hp = ((MON_Pig) this).getLife();
            }

            double scale = (double) gp.tileSize / 4;
            double healthBar = (double) scale * hp;

            g2.setColor(Color.RED);
            g2.fillRect(screenX, screenY - 15, (int) healthBar, 10);
            hpBarCounter++;

            if (hpBarCounter > 600) {
                hpBarCounter = 0;
                hpBarStatus = false;
            }
        }

        if (image != null) {
            if (invincible) {

                if (isPlayer || this instanceof MON_Island_Native || this instanceof MON_Pig) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                    g2.drawImage(image, adjustedScreenX, adjustedScreenY, scaledWidth, scaledHeight, null);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }

                if (!isPlayer && (this instanceof MON_Island_Native || this instanceof MON_Pig)) {
                    hpBarStatus = true;
                    hpBarCounter = 0;
                }
            } else if (dying) {
                dyingAnimation(g2, image, adjustedScreenX, adjustedScreenY, scaledWidth, scaledHeight);
            } else {
                g2.drawImage(image, adjustedScreenX, adjustedScreenY, scaledWidth, scaledHeight, null);
            }
        }

        // DRAW HITBOXES FOR DEBUG
        if (gp.drawHitboxes) {
            g2.setColor(Color.RED);
            int hitboxX = adjustedScreenX + (int) (solidArea.x * scale);
            int hitboxY = adjustedScreenY + (int) (solidArea.y * scale);
            int hitboxWidth = (int) (solidArea.width * scale);
            int hitboxHeight = (int) (solidArea.height * scale);
            g2.drawRect(hitboxX, hitboxY, hitboxWidth, hitboxHeight);

            if (isPlayer && ((Player) this).attacking) {
                g2.setColor(Color.BLUE);
                int attackX = adjustedScreenX;
                int attackY = adjustedScreenY;
                switch (direction) {
                    case "up":
                        attackY -= (int) (attackArea.height * scale);
                        break;
                    case "down":
                        attackY += gp.tileSize;
                        break;
                    case "left":
                        attackX -= (int) (attackArea.width * scale);
                        break;
                    case "right":
                        attackX += gp.tileSize;
                        break;
                }

                int attackWidth = (int) (attackArea.width * scale);
                int attackHeight = (int) (attackArea.height * scale);
                g2.drawRect(attackX, attackY, attackWidth, attackHeight);
            }
        }
    }

    // Dying animation method. FIX MIGHT BE NEEDED
    public void dyingAnimation(Graphics2D g2, BufferedImage image, int x, int y, int width, int height) {

        float default_float = 1.0f;

        if (dyingCounter <= 5) {
            default_float = 0.0f;
        } else if (dyingCounter < 5 && dyingCounter <= 10) {
            default_float = 1.0f;
        } else if (dyingCounter < 10 && dyingCounter <= 15) {
            default_float = 0.0f;
        } else if (dyingCounter < 15 && dyingCounter <= 20) {
            default_float = 1.0f;
        } else if (dyingCounter < 20 && dyingCounter <= 25) {
            default_float = 0.0f;
        } else if (dyingCounter < 25 && dyingCounter <= 30) {
            default_float = 1.0f;
        } else if (dyingCounter < 30 && dyingCounter <= 35) {
            default_float = 0.0f;
        } else if (dyingCounter < 35 && dyingCounter <= 40) {
            default_float = 1.0f;
        } else if (dyingCounter > 40) {
            dying = false;
            alive = false;
            return;
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, default_float));
        g2.drawImage(image, x, y, width, height, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

    }

    public void reactToDamage() {

    }

    public void setAction() {

    }

    public boolean isInvincible() {
        return invincible;
    }

    /*
     * Direction picker method for to be used for other NPCs.
     * 
     * public void setAction()
     * {
     * actionLockCounter ++;
     * 
     * if (actionLockCounter == 120)
     * {
     * Random random = new Random();
     * int i = random.nextInt(100) + 1;
     * 
     * if ( i <= 25)
     * {
     * direction = "up";
     * }
     * 
     * if ( i > 25 && i <= 50)
     * {
     * direction = "down";
     * }
     * 
     * if ( i > 50 && i <= 75)
     * {
     * direction = "left";
     * }
     * 
     * if ( i > 75 && i <= 100)
     * {
     * direction = "right";
     * }
     * }
     * 
     * actionLockCounter = 0;
     * 
     * }
     * 
     */

    public void interact(Player player, int index) {
        // Default implementation does nothing
        // Subclasses can override this method to implement specific interaction
        // behavior
    }

}
