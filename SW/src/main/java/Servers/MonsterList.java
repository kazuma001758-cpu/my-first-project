package Servers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * モンスターのデータを管理するリストクラス
 */
public class MonsterList {
    private Map<String, Monster> list = new HashMap<>();

    public MonsterList() {
        // サンプルモンスターデータを登録 (名前, 最大HP, 防護点, 打撃点)
        register(new Monster("ゴブリン", 15, 2, 4));
        register(new Monster("ボルグ", 35, 4, 6));
        register(new Monster("トロール", 60, 6, 8));
    }

    private void register(Monster m) {
        list.put(normalize(m.name), m);
    }

    /**
     * 名前からモンスターを検索して取得する
     */
    public Monster get(String name) {
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
