package save;

import java.io.*;

import environment.Lighting;
import main.GamePanel;

public class SaveStorage {
    GamePanel gp;

    public SaveStorage(GamePanel gp) {
        this.gp = gp;
    }

    private String getSlotFileName(int slot) {
        return "save_slot_" + slot + ".dat";
    }

    public void saveGame(int slot) {
        try (ObjectOutputStream stream = new ObjectOutputStream(
                new FileOutputStream(new File(getSlotFileName(slot))))) {

            Storage stor = new Storage();

            stor.health = gp.player.getCurrentHealth();
            stor.hunger = gp.player.getCurrentHunger();

            stor.level = gp.player.getLevel();
            stor.strength = gp.player.getStrength();
            stor.dexterity = gp.player.getDexterity();
            stor.exp = gp.player.getExp();
            stor.expToNextLevel = gp.player.getExpToNextLevel();
            stor.coin = gp.player.getCoin();

            stor.currentWeapon = gp.player.getCurrentWeapon();
            stor.currentShield = gp.player.getCurrentShield();
            stor.inventory = gp.player.inventory;

            stor.defense = gp.player.getDefense();
            stor.attack = gp.player.getAttack();

            stor.day = Lighting.currentDay; // GÜN sayısını göstermek için (eğer gp.day varsa)

            stream.writeObject(stor);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGame(int slot) {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(new File(getSlotFileName(slot))))) {

            Storage s = (Storage) stream.readObject();

            gp.player.setCurrentHealth(s.health);
            gp.player.setCurrentHunger(s.hunger);

            gp.player.setLevel(s.level);
            gp.player.setStrength(s.strength);
            gp.player.setDexterity(s.dexterity);
            gp.player.setExp(s.exp);
            gp.player.setExpToNextLevel(s.expToNextLevel);
            gp.player.setCoin(s.coin);

            gp.player.setCurrentWeapon(s.currentWeapon);
            gp.player.setCurrentShield(s.currentShield);
            gp.player.setInventory(s.inventory);

            gp.player.setDefense(s.defense);
            gp.player.setAttack(s.attack);

            Lighting.currentDay = s.day;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isSlotOccupied(int slot) {
        return new File(getSlotFileName(slot)).exists();
    }

    public int getDayOfSlot(int slot) {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(new File(getSlotFileName(slot))))) {
            Storage s = (Storage) stream.readObject();
            return s.day;
        } catch (Exception e) {
            return -1; // Dosya yoksa veya hata varsa
        }
    }
}
