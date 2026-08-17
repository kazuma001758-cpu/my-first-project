package com.example.demo.SW.SWbattle.model;

public class Weapon {
    private String name;
    private int power;       // 威力
    private int critical = 10; // クリティカル値

    public Weapon() {}

    public Weapon(String name, int power, int critical) {
        this.name = name;
        this.power = power;
        this.critical = critical;
    }

    // Getter / Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }
    public int getCritical() { return critical; }
    public void setCritical(int critical) { this.critical = critical; }
}