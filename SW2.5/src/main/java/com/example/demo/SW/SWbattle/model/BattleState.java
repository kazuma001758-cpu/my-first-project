package com.example.demo.SW.SWbattle.model;

import java.util.ArrayList;
import java.util.List;

public class BattleState {
    private List<Player> party = new ArrayList<>();
    private List<Monster> enemies = new ArrayList<>();
    private List<String> logs = new ArrayList<>();
    
    // 【新規】リザルト用データ
    private boolean isFinished = false;
    private int totalExp = 0;
    private List<String> droppedItems = new ArrayList<>();

    public void addLog(String log) {
        this.logs.add(0, log); // リストの先頭に追加することで画面上部が最新になる
    }

    // Getter / Setter
    public List<Player> getParty() { return party; }
    public List<Monster> getEnemies() { return enemies; }
    public List<String> getLogs() { return logs; }
    public boolean isFinished() { return isFinished; }
    public void setFinished(boolean finished) { isFinished = finished; }
    public int getTotalExp() { return totalExp; }
    public void setTotalExp(int totalExp) { this.totalExp = totalExp; }
    public List<String> getDroppedItems() { return droppedItems; }
}