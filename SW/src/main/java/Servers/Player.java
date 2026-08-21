package Servers;

/**
 * プレイヤー（PC）のステータスおよび装備品を管理するクラス
 */
public class Player {
    public String name;                 // プレイヤー名
    public int maxHp;                   // 最大HP
    public int currentHp;               // 現在HP
    public int defense;                 // 防護点
    public int additionalDamage;        // 追加ダメージ固定値（攻撃時のボーナス）
    
    // 装備スロット
    public Weapon rightHandWeapon;      // 右手武器
    public Weapon leftHandWeapon;       // 左手武器
    public boolean isTwoHanded;         // 両手持ちしているかフラグ

    public Player(String name, int additionalDamage) {
        // デフォルトのHPと防護点を設定
        this(name, additionalDamage, 30, 4); 
    }

    public Player(String name, int additionalDamage, int maxHp, int defense) {
        this.name = name;
        this.additionalDamage = additionalDamage;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.defense = defense;
        this.rightHandWeapon = null;
        this.leftHandWeapon = null;
        this.isTwoHanded = false;
    }

    /**
     * 武器を完全に外す
     */
    public void clearEquipment() {
        this.rightHandWeapon = null;
        this.leftHandWeapon = null;
        this.isTwoHanded = false;
    }

    /**
     * 両手持ちとして武器を装備する
     */
    public void equipTwoHanded(Weapon weapon) {
        clearEquipment();
        this.rightHandWeapon = weapon;
        this.isTwoHanded = true;
        System.out.println(name + " は " + weapon.name + " を【両手持ち】で装備しました。");
    }

    /**
     * 片手武器として装備する
     * @param isRight 右手ならtrue, 左手ならfalse
     */
    public void equipOneHanded(Weapon weapon, boolean isRight) {
        if (this.isTwoHanded) {
            clearEquipment(); // 両手持ちをしていた場合は一旦解除
        }

        if (isRight) {
            this.rightHandWeapon = weapon;
            System.out.println(name + " の【右手】に " + weapon.name + " を装備しました。");
        } else {
            this.leftHandWeapon = weapon;
            System.out.println(name + " の【左手】に " + weapon.name + " を装備しました。");
        }
    }

    /**
     * 何か武器を装備しているか判定
     */
    public boolean hasWeapon() {
        return rightHandWeapon != null || leftHandWeapon != null;
    }
}