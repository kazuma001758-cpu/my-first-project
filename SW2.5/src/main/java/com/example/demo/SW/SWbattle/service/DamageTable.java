package com.example.demo.SW.SWbattle.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.SW.SWbattle.entity.DamageTableEntity;
import com.example.demo.SW.SWbattle.repository.DamageTableRepository;

@Component
public class DamageTable {

    private final DamageTableRepository damageTableRepository;

    public record DamageResult(int totalDamage, String diceLog) {}

    public DamageTable(DamageTableRepository damageTableRepository) {
        this.damageTableRepository = damageTableRepository;
    }

    // 第3引数に criticalValue を追加
    public DamageResult calculateDamage(int weaponPower, int modifier, int criticalValue) {
        int power = Math.min(Math.max(weaponPower, 0), 100);

        DamageTableEntity entity = damageTableRepository.findById(power)
                .orElseGet(() -> damageTableRepository.findById(0).orElse(new DamageTableEntity()));

        int totalDamage = 0;
        List<String> rollDetails = new ArrayList<>();

        while (true) {
            int dice1 = (int) (Math.random() * 6 + 1);
            int dice2 = (int) (Math.random() * 6 + 1);
            int diceSum = dice1 + dice2;

            // ピンゾロ(出目2)は自動失敗
            if (diceSum == 2) {
                rollDetails.add(String.format("[2d6=%d(%d,%d) -> 自動失敗]", diceSum, dice1, dice2));
                break;
            }

            int dmg = entity.getDamageByDice(diceSum);
            totalDamage += dmg;

            // 武器ごとのクリティカル値で判定
            if (diceSum >= criticalValue) {
                rollDetails.add(String.format("[2d6=%d(%d,%d) -> 威%d:%d (Critical! C値:%d)]", 
                        diceSum, dice1, dice2, power, dmg, criticalValue));
                continue; // 威力表を振り足す
            } else {
                rollDetails.add(String.format("[2d6=%d(%d,%d) -> 威%d:%d]", diceSum, dice1, dice2, power, dmg));
                break;
            }
        }

        int finalDamage = Math.max(0, totalDamage + modifier);
        String diceLog = String.join(" + ", rollDetails);

        return new DamageResult(finalDamage, diceLog);
    }
}