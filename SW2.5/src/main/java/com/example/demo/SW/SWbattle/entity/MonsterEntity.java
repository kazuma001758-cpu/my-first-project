package com.example.demo.SW.SWbattle.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "monsters")
public class MonsterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int lifeResist;
    private int spiritResist;
    private int exp;
    private String dropItem;

    private boolean hasCore; // ★コア部位が存在するモンスターかどうか

    @OneToMany(mappedBy = "monster", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonsterPartEntity> parts = new ArrayList<>();

    public MonsterEntity() {}

    public void addPart(MonsterPartEntity part) {
        parts.add(part);
        part.setMonster(this);
    }

    // --- モンスター撃破判定ロジック ---
    public boolean isDefeated() {
        if (hasCore) {
            // コア存在時：コア部位のHPが0以下になればモンスター撃破
            return parts.stream()
                    .filter(MonsterPartEntity::isCore)
                    .allMatch(p -> p.getHp() <= 0);
        } else {
            // コアなし時：すべての部位のHPが0以下になればモンスター撃破
            return parts.stream().allMatch(p -> p.getHp() <= 0);
        }
    }

    // --- Getter / Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getLifeResist() { return lifeResist; }
    public void setLifeResist(int lifeResist) { this.lifeResist = lifeResist; }
    public int getSpiritResist() { return spiritResist; }
    public void setSpiritResist(int spiritResist) { this.spiritResist = spiritResist; }
    public int getExp() { return exp; }
    public void setExp(int exp) { this.exp = exp; }
    public String getDropItem() { return dropItem; }
    public void setDropItem(String dropItem) { this.dropItem = dropItem; }
    public boolean isHasCore() { return hasCore; }
    public void setHasCore(boolean hasCore) { this.hasCore = hasCore; }
    public List<MonsterPartEntity> getParts() { return parts; }
    public void setParts(List<MonsterPartEntity> parts) { this.parts = parts; }
}