package com.example.demo.SW.SWbattle.model;

import java.util.ArrayList;
import java.util.List;

public class SetupForm {

    private List<Long> playerIds = new ArrayList<>();
    private List<Long> weaponIds = new ArrayList<>(); // ★追加: 装備する武器のID
    private List<Long> monsterIds = new ArrayList<>();

    public SetupForm() {
        // 初期要素を3つ作っておく
        for (int i = 0; i < 3; i++) {
            playerIds.add(0L);
            weaponIds.add(0L);
            monsterIds.add(0L);
        }
    }

    // Getter / Setter
    public List<Long> getPlayerIds() { return playerIds; }
    public void setPlayerIds(List<Long> playerIds) { this.playerIds = playerIds; }

    public List<Long> getWeaponIds() { return weaponIds; }
    public void setWeaponIds(List<Long> weaponIds) { this.weaponIds = weaponIds; }

    public List<Long> getMonsterIds() { return monsterIds; }
    public void setMonsterIds(List<Long> monsterIds) { this.monsterIds = monsterIds; }
}