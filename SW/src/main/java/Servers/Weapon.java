package Servers;

public class Weapon {
    public String name;       // 武器名
    public int critical;      // クリティカル値 (C値)
    public int row;           // 威力表の行 (威力)
    public String usage;      // 用法 ("1H":片手専用, "2H":両手専用, "1H両":両用)

    public Weapon(String name, int critical, int row, String usage) {
        this.name = name;
        this.critical = critical;
        this.row = row;
        this.usage = usage;
    }

    /**
     * 両手持ち（2H）として扱える武器か判定する
     */
    public boolean canBeTwoHanded() {
        return "2H".equals(usage) || "1H両".equals(usage);
    }

    /**
     * 片手持ち（1H）として扱える武器か判定する
     */
    public boolean canBeOneHanded() {
        return "1H".equals(usage) || "1H両".equals(usage);
    }
}