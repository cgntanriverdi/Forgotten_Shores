package main;

import java.io.Serializable;

import object.Item;

public class Inventory implements Serializable
{
    private Item[] slots = new Item[5]; // 5 inventory slots
    private int selectedSlot = 0;
    private transient GamePanel gp;

    public Inventory(GamePanel gp) 
    {
        this.gp = gp;
    }

    public Item getItem( int index ) 
    {
        return (index >= 0 && index < slots.length) ? slots[index] : null;
    }

    public void setItem( int index, Item item ) 
    {
        if ( index >= 0 && index < slots.length ) 
        {
            slots[index] = item;
            if (gp != null) 
            {
                gp.ui.notifyInventoryChange();
            }
        }
    }

    public int getSelectedSlot()  
    {
        return selectedSlot;
    }

    public void setSelectedSlot( int index ) 
    {
        if ( index >= 0 && index < slots.length ) 
        {
            selectedSlot = index;
        }
    }

    public int getTotalQuantity(String itemName) 
    {
        int total = 0;
        for (Item item : slots) 
        {
            if (item != null && item.name.equals(itemName)) 
            {
                total += item.quantity;
            }
        }
        return total;
    }

    public boolean hasEnough(String itemName, int requiredQuantity) {
        return getTotalQuantity(itemName) >= requiredQuantity;
    }

    public void consumeItem(String itemName, int quantity) {
        int remaining = quantity;
        for (int i = 0; i < slots.length && remaining > 0; i++) {
            if (slots[i] != null && slots[i].name.equals(itemName)) {
                if (slots[i].quantity > remaining) {
                    slots[i].quantity -= remaining;
                    remaining = 0;
                } else {
                    remaining -= slots[i].quantity;
                    slots[i] = null;
                }
            }
        }
        if (gp != null) 
        {
            gp.ui.notifyInventoryChange();
        }
    }

    public boolean addItem(Item item) 
    {
        if (item.isStackable) 
        {
            for (int i = 0; i < slots.length; i++) 
            {
                if (slots[i] != null && slots[i].name.equals(item.name)) 
                {
                    slots[i].quantity += item.quantity;
                    if (gp != null) {
                        gp.ui.notifyInventoryChange();
                    }
                    return true;
                }
            }
        }
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == null) {
                slots[i] = item.clone();
                if (gp != null) {
                    gp.ui.notifyInventoryChange();
                }
                return true;
            }
        }
        return false;
    }

    public int getSlotCount() 
    {
        return slots.length;
    }

    public int size() {
        return slots.length;
    }

    public Item get(int index) {
        return getItem(index);
    }

    public void set(int index, Item item) {
        setItem(index, item);
    }
    public Item[] getSlots() {
        return this.slots;
    }
}