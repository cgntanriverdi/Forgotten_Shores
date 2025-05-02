package save;

import java.io.Serializable;

import entity.Entity;
import main.Inventory;

public class Storage implements Serializable{
    
    int health;
    int hunger;
    int day;
    int level;
    int strength;
    int dexterity;
    int exp;
    int expToNextLevel; 
    int coin;
    Entity currentWeapon;
    Entity currentShield;

    int defense;
    int attack;

    Inventory inventory;
}
