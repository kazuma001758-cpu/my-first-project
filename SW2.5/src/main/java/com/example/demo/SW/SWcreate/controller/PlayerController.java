package com.example.demo.SW.SWcreate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.SW.SWbattle.entity.PlayerEntity;
import com.example.demo.SW.SWbattle.repository.PlayerRepository;
import com.example.demo.SW.SWbattle.repository.WeaponRepository;

@Controller
@RequestMapping("/players")
public class PlayerController {

    private final PlayerRepository playerRepository;
    private final WeaponRepository weaponRepository;

    public PlayerController(PlayerRepository playerRepository, WeaponRepository weaponRepository) {
        this.playerRepository = playerRepository;
        this.weaponRepository = weaponRepository;
    }

    // GET /players/new ➔ プレイヤー追加画面を表示
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("player", new PlayerEntity());
        // 武器の選択肢をDBから取得して画面に送る
        model.addAttribute("weapons", weaponRepository.findAll());
        return "SW/create/player_form";
    }

    // POST /players ➔ プレイヤーをDBに保存
    @PostMapping
    public String createPlayer(@ModelAttribute PlayerEntity player) {
        playerRepository.save(player);
        return "redirect:/SW/create/createmenu";
    }
}