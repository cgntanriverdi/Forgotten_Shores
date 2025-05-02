package object;
import java.awt.Rectangle;

import entity.Entity;
import entity.Player;
import main.GamePanel;

public class Item extends Entity implements Cloneable
{
    public boolean isStackable = false; 
    public int quantity = 1;    
    
    public ItemType itemType;
    public boolean isEquipped = false;

    public enum ItemType
    {
        WEAPON, SHIELD, CONSUMABLE, LIGHTER, OTHER
    }

    public Item( GamePanel gp ) 
    {
        super(gp);
    }

    public void use(Player player) {
        // Default does nothing; consumables will override this
    }

    public Item clone() 
    {
        Item newItem;
        
            if (this instanceof object.OBJ_APPLE) {
                newItem = new object.OBJ_APPLE(gp);
            } else if (this instanceof object.OBJ_AXE) {
                newItem = new object.OBJ_AXE(gp);
            } else if (this instanceof object.OBJ_CAMPFIRE) {
                newItem = new object.OBJ_CAMPFIRE(gp);
            } else if (this instanceof object.OBJ_CHEST) {
                newItem = new object.OBJ_CHEST(gp);
            } else if (this instanceof object.OBJ_KEY) {
                newItem = new object.OBJ_KEY(gp);
            } else if (this instanceof object.OBJ_RAW_MEAT) {
                newItem = new object.OBJ_RAW_MEAT(gp);
            }  else if (this instanceof object.OBJ_SHIELD_WOOD) {
                newItem = new object.OBJ_SHIELD_WOOD(gp);
            } else if (this instanceof object.OBJ_SPEAR) {
                newItem = new object.OBJ_SPEAR(gp);
            } else if (this instanceof object.OBJ_STONE) {
                newItem = new object.OBJ_STONE(gp);
            } else if (this instanceof object.OBJ_SWORD_NORMAL) {
                newItem = new object.OBJ_SWORD_NORMAL(gp);
            } else if (this instanceof object.OBJ_TORCH) {
                newItem = new object.OBJ_TORCH(gp);
            } else if (this instanceof object.OBJ_WATER_BUCKET) {
                newItem = new object.OBJ_WATER_BUCKET(gp);
            } else if (this instanceof object.OBJ_WOOD) {
                newItem = new object.OBJ_WOOD(gp);
            } else {
                newItem = new Item(gp);
            }
        
            newItem.name = this.name;
            newItem.image = this.image;
            newItem.down1 = this.down1;
            newItem.idle1 = this.idle1;
            newItem.quantity = this.quantity;
            newItem.isStackable = this.isStackable;
            newItem.itemType = this.itemType;
            newItem.solidArea = new Rectangle(this.solidArea);
            newItem.collision = this.collision;
            newItem.isEquipped = this.isEquipped;
            newItem.alive = true;
        
            return newItem;
    }
}
