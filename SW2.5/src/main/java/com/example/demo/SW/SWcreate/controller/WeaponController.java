package com.example.demo.SW.SWcreate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.SW.SWbattle.entity.WeaponEntity;
import com.example.demo.SW.SWbattle.repository.WeaponRepository;

@Controller
@RequestMapping("/weapons")
public class WeaponController {

    private final WeaponRepository weaponRepository;

    public WeaponController(WeaponRepository weaponRepository) {
        this.weaponRepository = weaponRepository;
    }

    // GET /weapons/new ➔ 武器追加画面を表示
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("weapon", new WeaponEntity());
        return "SW/create/weapon_form"; // フォルダ階層を合わせた戻り値
    }

    // POST /weapons ➔ 武器をDBに保存
    @PostMapping
    public String createWeapon(@ModelAttribute WeaponEntity weapon) {
        weaponRepository.save(weapon);
        return "redirect:/SW/create/createmenu"; // データ追加メニューへリダイレクト
    }
}