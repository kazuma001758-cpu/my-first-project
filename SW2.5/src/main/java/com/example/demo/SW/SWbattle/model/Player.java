package com.example.demo.SW.SWbattle.model;

public class Player {
    private String name;
    private int maxHp;
    private int currentHp;
    private int defense;
    private int accuracy = 0; // 命中力 modifier
    private int evasion = 0;  // 回避力 modifier
    private Weapon rightHandWeapon;
    private boolean hasActed = false;

    public Player() {}

    public Player(String name, int maxHp, int defense, int accuracy, int evasion, Weapon rightHandWeapon) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.defense = defense;
        this.accuracy = accuracy;
        this.evasion = evasion;
        this.rightHandWeapon = rightHandWeapon;
    }

    // 既存の Getter/Setter...
    
    public int getAccuracy() { return accuracy; }
    public void setAccuracy(int accuracy) { this.accuracy = accuracy; }

    public int getEvasion() { return evasion; }
    public void setEvasion(int evasion) { this.evasion = evasion; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getMaxHp() { return maxHp; }
    public void setMaxHp(int maxHp) { this.maxHp = maxHp; }
    public int getCurrentHp() { return currentHp; }
    public void setCurrentHp(int currentHp) { this.currentHp = Math.max(0, currentHp); }
    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }
    public Weapon getRightHandWeapon() { return rightHandWeapon; }
    public void setRightHandWeapon(Weapon rightHandWeapon) { this.rightHandWeapon = rightHandWeapon; }
    public boolean isHasActed() { return hasActed; }
    public void setHasActed(boolean hasActed) { this.hasActed = hasActed; }
}