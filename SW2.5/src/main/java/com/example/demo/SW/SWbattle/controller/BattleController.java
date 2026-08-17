package com.example.demo.SW.SWbattle.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.SW.SWbattle.model.BattleState;
import com.example.demo.SW.SWbattle.model.SetupForm;
import com.example.demo.SW.SWbattle.repository.MonsterRepository;
import com.example.demo.SW.SWbattle.repository.PlayerRepository;
import com.example.demo.SW.SWbattle.repository.WeaponRepository; // ★追加
import com.example.demo.SW.SWbattle.service.BattleService;

@Controller
@RequestMapping("/battle")
public class BattleController {

	private final BattleService battleService;
	private final PlayerRepository playerRepository;
	private final MonsterRepository monsterRepository;
	private final WeaponRepository weaponRepository; // ★追加

	public BattleController(BattleService battleService,
			PlayerRepository playerRepository,
			MonsterRepository monsterRepository,
			WeaponRepository weaponRepository) { // ★追加
		this.battleService = battleService;
		this.playerRepository = playerRepository;
		this.monsterRepository = monsterRepository;
		this.weaponRepository = weaponRepository;
	}

	// ① Setup画面：DBから登録されているプレイヤー・モンスター・武器のリストを取得
	@GetMapping("/setup")
	public String showSetupForm(Model model) {
		model.addAttribute("setupForm", new SetupForm());
		model.addAttribute("players", playerRepository.findAll());
		model.addAttribute("monsters", monsterRepository.findAll());
		model.addAttribute("weapons", weaponRepository.findAll()); // ★追加: 武器リストを渡す
		return "SW/battle/setup";
	}

	// ② 設定内容を受け取って戦闘開始
	@PostMapping("/start")
	public String startBattle(@ModelAttribute SetupForm setupForm, HttpSession session) {
		BattleState state = battleService.createCustomState(setupForm);
		session.setAttribute("battleState", state);
		return "redirect:/battle";
	}

	// ③ 戦闘画面の表示
	@GetMapping
	public String showBattleScreen(HttpSession session, Model model) {
		BattleState state = (BattleState) session.getAttribute("battleState");

		if (state == null) {
			return "redirect:/battle/setup";
		}

		if (state.isFinished()) {
			model.addAttribute("battleState", state);
			return "SW/battle/result";
		}

		model.addAttribute("battleState", state);
		return "SW/battle/battle";
	}

	// ④ 再戦・リセット
	@GetMapping("/reset")
	public String resetBattle(HttpSession session) {
		session.removeAttribute("battleState");
		return "redirect:/battle/setup";
	}

	// ⑤ 攻撃の実行
	@PostMapping("/attack")
	public String attack(
			@RequestParam("attackerIndex") int attackerIndex,
			@RequestParam("targetIndex") int targetIndex,
			@RequestParam(value = "modifier", defaultValue = "0") int modifier,
			HttpSession session) {

		BattleState state = (BattleState) session.getAttribute("battleState");
		if (state != null) {
			battleService.processAttack(state, attackerIndex, targetIndex, modifier);
		}

		return "redirect:/battle";
	}
}