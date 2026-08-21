package Servers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * プレイヤーのデータを管理するリストクラス
 */
public class PlayerList {
    private Map<String, Player> list = new HashMap<>();

    public PlayerList() {
        // テスト用のサンプルプレイヤーデータを登録
        register(new Player("戦士アルク", 6));
    //    register(new Player("魔法使いシエラ", 4));
    //    register(new Player("盗賊レオン", 3));
    }

    private void register(Player p) {
        list.put(normalize(p.name), p);
    }

    /**
     * 名前からプレイヤーを検索して取得する
     */
    public Player get(String name) {
        if (name == null) return null;
        String key = normalize(name);
        
        // 厳密一致
        if (list.containsKey(key)) {
            return list.get(key);
        }
        
        // 部分一致
        for (String k : list.keySet()) {
            if (k.contains(key) || key.contains(k)) {
                return list.get(k);
            }
        }
        
        return null;
    }

    public Set<String> keys() {
        return list.keySet();
    }

    private String normalize(String input) {
        return WeaponList.debugNormalize(input);
    }
}