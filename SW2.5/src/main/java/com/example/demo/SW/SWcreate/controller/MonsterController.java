package com.example.demo.SW.SWcreate.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.SW.SWbattle.entity.MonsterEntity;
import com.example.demo.SW.SWbattle.entity.MonsterPartEntity;
import com.example.demo.SW.SWbattle.repository.MonsterRepository;

@Controller
@RequestMapping("/monsters")
public class MonsterController {

    private final MonsterRepository monsterRepository;

    public MonsterController(MonsterRepository monsterRepository) {
        this.monsterRepository = monsterRepository;
    }

    // GET /monsters/new ➔ モンスター追加画面を表示（ここで10個の部位枠を初期化）
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        MonsterEntity monster = new MonsterEntity();
        
        // 画面用に部位入力枠を10個あらかじめ準備
        for (int i = 0; i < 10; i++) {
            monster.addPart(new MonsterPartEntity());
        }
        
        model.addAttribute("monster", monster);
        return "SW/create/monster_form";
    }

    // POST /monsters ➔ モンスターをDBに保存（空欄の部位を除外して保存）
    @PostMapping
    public String createMonster(@ModelAttribute MonsterEntity monster) {
        // 部位名が入力されている項目のみを抽出してセットし直す
        List<MonsterPartEntity> validParts = monster.getParts().stream()
                .filter(p -> p.getPartName() != null && !p.getPartName().isBlank())
                .toList();

        monster.getParts().clear();
        for (MonsterPartEntity part : validParts) {
            monster.addPart(part);
        }

        monsterRepository.save(monster);
        return "redirect:/SW/create/createmenu";
    }
}