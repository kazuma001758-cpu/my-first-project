package com.example.demo.SW.SWbattle.model;

public class Monster {
    private String name;
    private int hp;
    private int defense;
    private int attack;      // 打撃点固定値修正または基準値
    private int accuracy = 0; // 命中力
    private int evasion = 0;  // 回避力
    private int weaponPower = 0; // モンスター固有の威力（使用する場合）
    private int exp;
    private String dropItem;

    public Monster() {}

    public Monster(String name, int hp, int defense, int attack, int accuracy, int evasion, int exp, String dropItem) {
        this.name = name;
        this.hp = hp;
        this.defense = defense;
        this.attack = attack;
        this.accuracy = accuracy;
        this.evasion = evasion;
        this.exp = exp;
        this.dropItem = dropItem;
    }

    // 既存の Getter/Setter...

    public int getAccuracy() { return accuracy; }
    public void setAccuracy(int accuracy) { this.accuracy = accuracy; }

    public int getEvasion() { return evasion; }
    public void setEvasion(int evasion) { this.evasion = evasion; }

    public int getWeaponPower() { return weaponPower; }
    public void setWeaponPower(int weaponPower) { this.weaponPower = weaponPower; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, hp); }
    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }
    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }
    public int getExp() { return exp; }
    public void setExp(int exp) { this.exp = exp; }
    public String getDropItem() { return dropItem; }
    public void setDropItem(String dropItem) { this.dropItem = dropItem; }
}