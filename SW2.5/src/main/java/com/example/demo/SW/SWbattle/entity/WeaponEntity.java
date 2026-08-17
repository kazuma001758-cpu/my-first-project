package com.example.demo.SW.SWbattle.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "weapons")
public class WeaponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // 武器名
    private int power;          // 威力
    private int critical = 10;  // クリティカル値（C値）

    public WeaponEntity() {}

    public WeaponEntity(String name, int power, int critical) {
        this.name = name;
        this.power = power;
        this.critical = critical;
    }

    // Getter / Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }
    public int getCritical() { return critical; }
    public void setCritical(int critical) { this.critical = critical; }
}