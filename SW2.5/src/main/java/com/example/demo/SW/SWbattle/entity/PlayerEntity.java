package com.example.demo.SW.SWbattle.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "players")
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;       // プレイヤー名・役職（例: ファイター）
    private int hp;            // HP（maxHp）
    private int defense;       // 防護点
    private int dexterity = 10;// 器用度 / 命中基準
    private int agility = 10;  // 敏捷度 / 回避基準

    @ManyToOne
    @JoinColumn(name = "weapon_id")
    private WeaponEntity weapon; // 武器Entityとの直接リレーション

    public PlayerEntity() {}

    // Getter / Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }

    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }

    public int getDexterity() { return dexterity; }
    public void setDexterity(int dexterity) { this.dexterity = dexterity; }

    public int getAgility() { return agility; }
    public void setAgility(int agility) { this.agility = agility; }

    public WeaponEntity getWeapon() { return weapon; }
    public void setWeapon(WeaponEntity weapon) { this.weapon = weapon; }
}