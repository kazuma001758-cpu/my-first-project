package com.example.demo.SW.SWbattle.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "monster_parts")
public class MonsterPartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String partName;  // 部位名 (例: 頭部, 胴体)
    private int accuracy;      // 命中力
    private int attack;        // 打撃点
    private int evasion;       // 回避力
    private int defense;       // 防護点
    private int hp;            // HP

    private boolean isCore;    // ★コア部位かどうか
    private Integer protectedByPartIndex; // ★攻撃許可に必要な前提部位の番号（例: 2番目の部位が生きていると攻撃不可）

    @ManyToOne
    @JoinColumn(name = "monster_id")
    private MonsterEntity monster;

    public MonsterPartEntity() {}

    // --- Getter / Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPartName() { return partName; }
    public void setPartName(String partName) { this.partName = partName; }
    public int getAccuracy() { return accuracy; }
    public void setAccuracy(int accuracy) { this.accuracy = accuracy; }
    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }
    public int getEvasion() { return evasion; }
    public void setEvasion(int evasion) { this.evasion = evasion; }
    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public boolean isCore() { return isCore; }
    public void setCore(boolean core) { isCore = core; }
    public Integer getProtectedByPartIndex() { return protectedByPartIndex; }
    public void setProtectedByPartIndex(Integer protectedByPartIndex) { this.protectedByPartIndex = protectedByPartIndex; }
    public MonsterEntity getMonster() { return monster; }
    public void setMonster(MonsterEntity monster) { this.monster = monster; }
}