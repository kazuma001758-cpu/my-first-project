package Servers;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 武器のデータを管理するリストクラス
 */
public class WeaponList {
    private Map<String, Weapon> weaponMap = new HashMap<>();

    public WeaponList() {
        // 片手専用武器 (1H)
        register(new Weapon("ナイフ", 10, 1, "1H"));
        register(new Weapon("ショートソード", 10, 5, "1H"));
        register(new Weapon("ダガー", 10, 3, "1H"));
        register(new Weapon("レイピア", 10, 8, "1H"));

        // 両手専用武器 (2H)
        register(new Weapon("グレートソード", 10, 25, "2H"));
        register(new Weapon("ヘビーメイス", 12, 30, "2H"));

        // 片手・両手両用武器 (1H両)
        // 片手で持った時と、両手で持った時(両手)の2つのデータを登録します
        register(new Weapon("バスタードソード", 10, 15, "1H両"));
        register(new Weapon("バスタードソード(両手)", 10, 20, "2H")); // 両手持ち用専用データ

        register(new Weapon("フランベルジュ", 10, 18, "1H両"));
        register(new Weapon("フランベルジュ(両手)", 10, 23, "2H")); // 両手持ち用専用データ
    }

    private void register(Weapon w) {
        weaponMap.put(normalize(w.name), w);
    }

    /**
     * 通常の武器検索
     */
    public Weapon get(String name) {
        if (name == null) return null;
        String key = normalize(name);
        
        // 厳密一致
        if (weaponMap.containsKey(key)) {
            return weaponMap.get(key);
        }
        
        // 部分一致
        for (String k : weaponMap.keySet()) {
            if (k.contains(key) || key.contains(k)) {
                return weaponMap.get(k);
            }
        }
        
        return null;
    }

    /**
     * 【重要】両手持ちしたとき用の武器データを自動で検索して取得する
     * 例: 「バスタードソード」を両手持ちする場合、「バスタードソード(両手)」を検索して返す
     */
    public Weapon getTwoHandedVariant(Weapon originalWeapon) {
        if (originalWeapon == null) return null;
        
        // すでに2H専用武器ならそのまま返す
        if ("2H".equals(originalWeapon.usage)) {
            return originalWeapon;
        }

        // 「(両手)」を付与した名前で再検索
        String twoHandedName = originalWeapon.name + "(両手)";
        Weapon variant = get(twoHandedName);

        if (variant != null) {
            return variant;
        }

        // もし専用データが見つからなければ、元の武器をそのまま両手で使うものとする
        return originalWeapon;
    }

    public Set<String> keys() {
        return weaponMap.keySet();
    }

    public static String debugNormalize(String s) {
        return normalize(s);
    }

    private static String normalize(String s) {
        if (s == null) return null;
        s = s.trim();
        s = Normalizer.normalize(s, Normalizer.Form.NFKC);
        s = s.replaceAll("[\u200B\u200C\u200D\uFEFF]", "");
        
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 'ぁ' && c <= 'ゖ') {
                char kata = (char) (c - 'ぁ' + 'ァ');
                sb.append(kata);
                continue;
            }
            if (c == '　') {
                sb.append(' ');
                continue;
            }
            sb.append(c);
        }
        String out = sb.toString().toLowerCase();
        out = out.replaceAll("\\s+", " ");
        return out;
    }
}