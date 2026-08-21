package Servers;

import java.util.Scanner;

/**
 * 装備品設定とダメージ計算、および戦闘シミュレーション（反撃付き）を統括するメインクラス
 */
public class Damage {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PlayerList pl = new PlayerList();
        MonsterList ml = new MonsterList();
        WeaponList wl = new WeaponList();

        System.out.println("=== 装備機能＆反撃付き・戦闘シミュレーター ===");

        // --- 1. プレイヤーの選択・作成 ---
        System.out.print("プレイヤー名を入力してください (「手動」で新規作成): ");
        String pInput = sc.next().trim();
        Player player = null;

        if (pInput.equals("手動") || pInput.equalsIgnoreCase("syudou") || pInput.equalsIgnoreCase("manual")) {
            player = createCustomPlayer(sc);
        } else {
            player = pl.get(pInput);
            if (player == null) {
                System.out.println("[WARNING] プレイヤー「" + pInput + "」が見つかりませんでした。");
                System.out.print("新規プレイヤーとして手動登録しますか？ (y/n): ");
                String choice = sc.next().trim();
                if (choice.equalsIgnoreCase("y")) {
                    player = createCustomPlayerWithName(sc, pInput);
                } else {
                    return;
                }
            }
        }

        // プレイヤーの現在HPをセット
        player.currentHp = player.maxHp;

        // --- 2. プレイヤーの武器装備セットアップ ---
        setupEquipment(sc, player, wl);

        // --- 3. モンスターの選択・作成 ---
        System.out.println("\n--------------------------------");
        System.out.print("攻撃対象のモンスター名を入力してください (「手動」で新規設定): ");
        String mInput = sc.next().trim();
        Monster monster = null;

        if (mInput.equals("手動") || mInput.equalsIgnoreCase("syudou") || mInput.equalsIgnoreCase("manual")) {
            monster = createCustomMonster(sc);
        } else {
            monster = ml.get(mInput);
            if (monster == null) {
                System.out.println("[WARNING] モンスター「" + mInput + "」が見つかりませんでした。");
                System.out.print("新規モンスターとして手動登録しますか？ (y/n): ");
                String choice = sc.next().trim();
                if (choice.equalsIgnoreCase("y")) {
                    monster = createCustomMonsterWithName(sc, mInput);
                } else {
                    return;
                }
            }
        }

        // --- 4. モンスターの現在HP設定 ---
        System.out.println("\n対象: " + monster.name + " (最大HP:" + monster.hp + " / 防護点:" + monster.defense + " / 打撃点:" + monster.attack + ")");
        int currentMonsterHp = monster.hp;
        while (true) {
            try {
                System.out.print("モンスターの【現在HP】を入力してください (最大HPと同じにする場合は「0」を入力): ");
                int inputHp = Integer.parseInt(sc.next().trim());
                if (inputHp == 0) {
                    currentMonsterHp = monster.hp;
                } else if (inputHp < 1) {
                    System.out.println("現在HPは1以上にしてください。");
                    continue;
                } else {
                    currentMonsterHp = inputHp;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("無効な数値です。整数を入力してください。");
            }
        }

        // --- 5. 攻撃に使う武器の選択 ---
        System.out.println("\n--------------------------------");
        System.out.println("【戦闘開始】");
        System.out.println(" 攻撃者: " + player.name + " (HP: " + player.currentHp + "/" + player.maxHp + " / 防護点: " + player.defense + ")");
        System.out.println(" 防御者: " + monster.name + " (HP: " + currentMonsterHp + "/" + monster.hp + " / 防護点: " + monster.defense + ")");
        
        Weapon activeWeapon = null;
        if (player.isTwoHanded) {
            activeWeapon = player.rightHandWeapon;
            System.out.println("攻撃武器: " + activeWeapon.name + " (両手持ち用法 / 威力:" + activeWeapon.row + " / C値:" + activeWeapon.critical + ")");
        } else {
            if (player.rightHandWeapon != null && player.leftHandWeapon != null) {
                System.out.println("両手に武器を装備しています。どちらの武器で攻撃しますか？");
                System.out.println(" 1: [右手] " + player.rightHandWeapon.name + " (威力:" + player.rightHandWeapon.row + ")");
                System.out.println(" 2: [左手] " + player.leftHandWeapon.name + " (威力:" + player.leftHandWeapon.row + ")");
                while (true) {
                    System.out.print("選択 (1 または 2): ");
                    String sel = sc.next().trim();
                    if (sel.equals("1")) {
                        activeWeapon = player.rightHandWeapon;
                        break;
                    } else if (sel.equals("2")) {
                        activeWeapon = player.leftHandWeapon;
                        break;
                    }
                }
            } else if (player.rightHandWeapon != null) {
                activeWeapon = player.rightHandWeapon;
                System.out.println("攻撃武器: " + activeWeapon.name + " (右手 / 威力:" + activeWeapon.row + " / C値:" + activeWeapon.critical + ")");
            } else if (player.leftHandWeapon != null) {
                activeWeapon = player.leftHandWeapon;
                System.out.println("攻撃武器: " + activeWeapon.name + " (左手 / 威力:" + activeWeapon.row + " / C値:" + activeWeapon.critical + ")");
            } else {
                System.out.println("[ERROR] 武器が装備されていません！素手として計算します。");
                activeWeapon = new Weapon("素手", 12, 0, "1H");
            }
        }

        // --- 6. プレイヤーの攻撃ターン ---
        System.out.println("\n>>> [プレイヤーの攻撃] " + player.name + " のターン！");
        System.out.println("--- ダイスロール開始 ---");
        DamageTable act = new DamageTable();

        int rollDamage = act.rest(activeWeapon.critical, activeWeapon.row);
        int totalPlayerDamage = rollDamage + player.additionalDamage;
        int actualMonsterDamage = totalPlayerDamage - monster.defense;
        if (actualMonsterDamage < 0) {
            actualMonsterDamage = 0;
        }

        currentMonsterHp -= actualMonsterDamage;
        if (currentMonsterHp < 0) {
            currentMonsterHp = 0;
        }

        System.out.println("\n[攻撃結果]");
        System.out.println(" 威力表ダメージ: " + rollDamage + " + 追加D: " + player.additionalDamage + " = 合計: " + totalPlayerDamage);
        System.out.println(" " + monster.name + " に " + actualMonsterDamage + " ダメージを与えた！");
        System.out.println(" " + monster.name + " の残りHP: " + currentMonsterHp + " / " + monster.hp);

        // --- 7. モンスターの反撃ターン ---
        if (currentMonsterHp > 0) {
            System.out.println("\n>>> [モンスターの反撃] " + monster.name + " は倒れなかった！反撃してくる！");
            System.out.println("--- モンスターのダイスロール (2d6) ---");
            
            // モンスターの攻撃はシンプルに 2d6 + 打撃点 (attack)
            int md1 = (int)(Math.random() * 6 + 1);
            int md2 = (int)(Math.random() * 6 + 1);
            int mdRoll = md1 + md2;
            
            int totalMonsterDamage = mdRoll + monster.attack;
            int actualPlayerDamage = totalMonsterDamage - player.defense;
            if (actualPlayerDamage < 0) {
                actualPlayerDamage = 0;
            }

            player.currentHp -= actualPlayerDamage;
            if (player.currentHp < 0) {
                player.currentHp = 0;
            }

            System.out.println("  ダイスの出た値: " + mdRoll + " (" + md1 + " + " + md2 + ")");
            System.out.println("  基本ダメージ: " + mdRoll + " + 打撃点: " + monster.attack + " = 合計: " + totalMonsterDamage);
            System.out.println("\n[反撃結果]");
            System.out.println(" " + player.name + " は " + actualPlayerDamage + " ダメージを受けた！ (防護点: " + player.defense + " 適用)");
            System.out.println(" " + player.name + " の残りHP: " + player.currentHp + " / " + player.maxHp);

            if (player.currentHp == 0) {
                System.out.println("\n★ " + player.name + " は力尽きてしまった...");
            }
        } else {
            System.out.println("\n★ " + monster.name + " を撃破した！反撃されることはない！");
        }
        System.out.println("\n========================================");
    }

    /**
     * 対話形式でプレイヤーの武器装備を設定する
     */
    private static void setupEquipment(Scanner sc, Player player, WeaponList wl) {
        System.out.println("\n--- " + player.name + " の装備設定 ---");
        System.out.print("装備したい武器の名前を入力してください: ");
        String wName = sc.next().trim();

        Weapon w = wl.get(wName);
        if (w == null) {
            System.out.println("[ERROR] 武器「" + wName + "」は登録されていません。装備なしで開始します。");
            return;
        }

        if ("2H".equals(w.usage)) {
            player.equipTwoHanded(w);
        } else if ("1H両".equals(w.usage)) {
            System.out.println("この武器は「片手持ち」と「両手持ち」の両方に対応しています。");
            System.out.print("どちらで持ちますか？ (1:片手持ち / 2:両手持ち): ");
            String style = sc.next().trim();
            if (style.equals("2")) {
                Weapon twoHandedW = wl.getTwoHandedVariant(w);
                player.equipTwoHanded(twoHandedW);
            } else {
                System.out.print("どちらの手に装備しますか？ (1:右手 / 2:左手): ");
                String hand = sc.next().trim();
                player.equipOneHanded(w, hand.equals("1"));
            }
        } else {
            System.out.print("どちらの手に装備しますか？ (1:右手 / 2:左手): ");
            String hand = sc.next().trim();
            player.equipOneHanded(w, hand.equals("1"));

            System.out.print("もう片方の手にも武器を装備しますか？ (y/n): ");
            String another = sc.next().trim();
            if (another.equalsIgnoreCase("y")) {
                System.out.print("もう一つの武器名を入力してください: ");
                String wName2 = sc.next().trim();
                Weapon w2 = wl.get(wName2);
                if (w2 != null && w2.canBeOneHanded()) {
                    player.equipOneHanded(w2, !hand.equals("1"));
                } else {
                    System.out.println("[WARNING] その武器は片手で持てないか、登録されていません。");
                }
            }
        }
    }

    private static Player createCustomPlayer(Scanner sc) {
        System.out.print("プレイヤーの名前を決めてください: ");
        String name = sc.next().trim();
        return createCustomPlayerWithName(sc, name);
    }

    private static Player createCustomPlayerWithName(Scanner sc, String name) {
        int additionalDamage = 0;
        int maxHp = 30;
        int defense = 4;

        while (true) {
            try {
                System.out.print("追加ダメージボーナスを入力してください (数値): ");
                additionalDamage = Integer.parseInt(sc.next().trim());
                if (additionalDamage < 0) {
                    System.out.println("追加ダメージは0以上にしてください。");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("無効な数値です。整数を入力してください。");
            }
        }

        while (true) {
            try {
                System.out.print("最大HPを入力してください (数値): ");
                maxHp = Integer.parseInt(sc.next().trim());
                if (maxHp < 1) {
                    System.out.println("HPは1以上にしてください。");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("無効な数値です。整数を入力してください。");
            }
        }

        while (true) {
            try {
                System.out.print("防護点を入力してください (数値): ");
                defense = Integer.parseInt(sc.next().trim());
                if (defense < 0) {
                    System.out.println("防護点は0以上にしてください。");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("無効な数値です。整数を入力してください。");
            }
        }

        return new Player(name, additionalDamage, maxHp, defense);
    }

    private static Monster createCustomMonster(Scanner sc) {
        System.out.print("モンスターの名前を決めてください: ");
        String name = sc.next().trim();
        return createCustomMonsterWithName(sc, name);
    }

    private static Monster createCustomMonsterWithName(Scanner sc, String name) {
        int hp = 0;
        int defense = 0;
        int attack = 0;

        while (true) {
            try {
                System.out.print("最大HPを入力してください (数値): ");
                hp = Integer.parseInt(sc.next().trim());
                if (hp < 1) {
                    System.out.println("HPは1以上にしてください。");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("無効な数値です。整数を入力してください。");
            }
        }

        while (true) {
            try {
                System.out.print("防護点を入力してください (数値): ");
                defense = Integer.parseInt(sc.next().trim());
                if (defense < 0) {
                    System.out.println("防護点は0以上にしてください。");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("無効な数値です。整数を入力してください。");
            }
        }

        while (true) {
            try {
                System.out.print("打撃点(攻撃力)を入力してください (数値): ");
                attack = Integer.parseInt(sc.next().trim());
                if (attack < 0) {
                    System.out.println("打撃点は0以上にしてください。");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("無効な数値です。整数を入力してください。");
            }
        }

        return new Monster(name, hp, defense, attack);
    }
}