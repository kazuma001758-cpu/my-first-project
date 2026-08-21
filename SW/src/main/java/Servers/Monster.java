package Servers;

/**
 * モンスターのデータを保持するクラス
 */
public class Monster {
    public String name;      // モンスター名
    public int hp;           // 最大HP
    public int defense;      // 防護点
    public int attack;       // 打撃点（攻撃時の固定追加ダメージ）

    public Monster(String name, int hp, int defense, int attack) {
        this.name = name;
        this.hp = hp;
        this.defense = defense;
        this.attack = attack;
    }
}