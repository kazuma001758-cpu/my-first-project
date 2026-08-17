package com.example.demo.SW.SWbattle.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.demo.SW.SWbattle.entity.MonsterEntity;
import com.example.demo.SW.SWbattle.entity.MonsterPartEntity;
import com.example.demo.SW.SWbattle.entity.WeaponEntity;
import com.example.demo.SW.SWbattle.model.BattleState;
import com.example.demo.SW.SWbattle.model.Monster;
import com.example.demo.SW.SWbattle.model.Player;
import com.example.demo.SW.SWbattle.model.SetupForm;
import com.example.demo.SW.SWbattle.model.Weapon;
import com.example.demo.SW.SWbattle.repository.MonsterRepository;
import com.example.demo.SW.SWbattle.repository.PlayerRepository;
import com.example.demo.SW.SWbattle.repository.WeaponRepository;

@Service
public class BattleService {

	private final PlayerRepository playerRepository;
	private final MonsterRepository monsterRepository;
	private final WeaponRepository weaponRepository;
	private final DamageTable damageTable;
	private final Random random = new Random();

	public BattleService(PlayerRepository playerRepository,
			MonsterRepository monsterRepository,
			WeaponRepository weaponRepository,
			DamageTable damageTable) {
		this.playerRepository = playerRepository;
		this.monsterRepository = monsterRepository;
		this.weaponRepository = weaponRepository;
		this.damageTable = damageTable;
	}

	// -------------------------------------------------------------
	// DBの選択情報から BattleState（3vs3）を動的生成
	// -------------------------------------------------------------
	public BattleState createCustomState(SetupForm form) {
		BattleState state = new BattleState();

		// 1. プレイヤー側の生成（最大3名）
		if (form.getPlayerIds() != null) {
			for (int i = 0; i < form.getPlayerIds().size(); i++) {
				Long playerId = form.getPlayerIds().get(i);
				if (playerId != null && playerId > 0) {
					var playerOpt = playerRepository.findById(playerId);
					if (playerOpt.isPresent()) {
						var pEntity = playerOpt.get();
						
						// ========================================
						// ★武器の取得：SetupForm の武器IDを優先
						// ========================================
						Weapon weapon = null;
						
						// SetupForm に武器IDが指定されている場合
						if (form.getWeaponIds() != null && i < form.getWeaponIds().size()) {
							Long weaponId = form.getWeaponIds().get(i);
							
							// 武器ID > 0 = DBから武器を取得
							if (weaponId != null && weaponId > 0) {
								var weaponOpt = weaponRepository.findById(weaponId);
								if (weaponOpt.isPresent()) {
									var wEntity = weaponOpt.get();
									weapon = new Weapon(wEntity.getName(), wEntity.getPower(), wEntity.getCritical());
								}
							} else {
								// 武器ID = 0 = PlayerEntity のデフォルト武器を使用
								WeaponEntity wEntity = pEntity.getWeapon();
								if (wEntity != null) {
									weapon = new Weapon(wEntity.getName(), wEntity.getPower(), wEntity.getCritical());
								}
							}
						} else {
							// SetupForm に武器指定がない場合 = PlayerEntity のデフォルト武器
							WeaponEntity wEntity = pEntity.getWeapon();
							if (wEntity != null) {
								weapon = new Weapon(wEntity.getName(), wEntity.getPower(), wEntity.getCritical());
							}
						}

						// Player を生成
						Player player = new Player(
								pEntity.getName(),
								pEntity.getHp(),
								pEntity.getDefense(),
								pEntity.getDexterity(),
								pEntity.getAgility(),
								weapon);
						state.getParty().add(player);
					}
				}
			}
		}

		// 2. モンスター側の生成（最大3体）
		if (form.getMonsterIds() != null) {
			int monsterCount = 1;
			for (Long monsterId : form.getMonsterIds()) {
				if (monsterId != null && monsterId > 0) {
					MonsterEntity mEntity = monsterRepository.findById(monsterId).orElse(null);
					if (mEntity != null) {
						int hp = 20;
						int defense = 2;
						int attack = 4;
						int accuracy = 10;
						int evasion = 10;

						if (!mEntity.getParts().isEmpty()) {
							MonsterPartEntity part = mEntity.getParts().get(0);
							hp = part.getHp();
							defense = part.getDefense();
							attack = part.getAttack();
							accuracy = part.getAccuracy();
							evasion = part.getEvasion();
						}

						String displayName = mEntity.getName() + " " + (char) ('A' + (monsterCount - 1));

						Monster monster = new Monster(
								displayName,
								hp,
								defense,
								attack,
								accuracy,
								evasion,
								mEntity.getExp(),
								mEntity.getDropItem());
						state.getEnemies().add(monster);
						monsterCount++;
					}
				}
			}
		}

		state.addLog("⚔️ 戦闘が開始されました！");
		return state;
	}

	// -------------------------------------------------------------
	// 攻撃処理のメインエントリ
	// -------------------------------------------------------------
	public void processAttack(BattleState state, int attackerIndex, int targetIndex, int modifier) {
		if (state.isFinished()) {
			return;
		}

		// 1. プレイヤーの攻撃実行
		processPlayerAttack(state, attackerIndex, targetIndex, modifier);

		// 敵の全滅判定
		if (checkEnemiesDefeated(state)) {
			finishBattle(state);
			return;
		}

		// 2. 全プレイヤーが行動済みか判定
		boolean allPlayersActed = state.getParty().stream()
				.filter(p -> p.getCurrentHp() > 0)
				.allMatch(Player::isHasActed);

		// 3. 全員行動完了時にモンスターのターンを実行し、行動権をリセット
		if (allPlayersActed) {
			state.addLog("--- 敵陣営の反撃ターン ---");
			processMonsterTurn(state);

			// プレイヤー生存判定
			if (checkPartyDefeated(state)) {
				state.addLog("💀 プレイヤー陣営は全滅しました...");
				state.setFinished(true);
				return;
			}

			// プレイヤーの行動フラグリセット
			for (Player player : state.getParty()) {
				if (player.getCurrentHp() > 0) {
					player.setHasActed(false);
				}
			}
			state.addLog("--- 新しいラウンドが始まります ---");
		}
	}

	// -------------------------------------------------------------
	// プレイヤーからモンスターへの攻撃処理
	// -------------------------------------------------------------
	private void processPlayerAttack(BattleState state, int attackerIndex, int targetIndex, int modifier) {
		if (attackerIndex < 0 || attackerIndex >= state.getParty().size() ||
				targetIndex < 0 || targetIndex >= state.getEnemies().size()) {
			return;
		}

		Player attacker = state.getParty().get(attackerIndex);
		Monster target = state.getEnemies().get(targetIndex);

		if (attacker.getCurrentHp() <= 0 || attacker.isHasActed() || target.getHp() <= 0) {
			return;
		}

		attacker.setHasActed(true);

		// 2d6 ダイスロール（命中判定）
		int d1 = random.nextInt(6) + 1;
		int d2 = random.nextInt(6) + 1;
		int diceSum = d1 + d2;
		// getDexterity() から getAccuracy() へ変更
		int accuracyTotal = diceSum + attacker.getAccuracy() + modifier;

		// 回避計算（roll2D6を使用）
		int evasionTotal = roll2D6() + target.getEvasion();

		state.addLog(String.format("🗡️ %s の %s への攻撃！ 命中判定: 2d6(%d,%d)+%d=%d vs 回避:%d",
				attacker.getName(), target.getName(), d1, d2, attacker.getAccuracy() + modifier, accuracyTotal, evasionTotal));

		// 自動失敗・自動成功・数値比較
		boolean isHit = false;
		if (diceSum == 2) {
			state.addLog("  └ 🎲 ピンゾロ！ 攻撃は自動失敗！");
		} else if (diceSum == 12) {
			state.addLog("  └ 🎲 ゾロ目！ 自動命中！");
			isHit = true;
		} else if (accuracyTotal >= evasionTotal) {
			isHit = true;
		} else {
			state.addLog("  └ 攻撃は回避された！");
		}

		if (isHit) {
			Weapon weapon = attacker.getRightHandWeapon();
			int power = (weapon != null) ? weapon.getPower() : 0;
			int critical = (weapon != null) ? weapon.getCritical() : 10;

			DamageTable.DamageResult result = damageTable.calculateDamage(power, 0, critical);
			int rawDamage = result.totalDamage();
			int netDamage = Math.max(0, rawDamage - target.getDefense());

			target.setHp(Math.max(0, target.getHp() - netDamage));

			state.addLog(String.format("  └ 命中！ 威力%d ロール: %s -> 与ダメージ:%d (防護点%d差引後:%d)",
					power, result.diceLog(), rawDamage, target.getDefense(), netDamage));

			if (target.getHp() == 0) {
				state.addLog(String.format("  └ 💥 %s を撃破した！", target.getName()));
			}
		}
	}

	// -------------------------------------------------------------
	// モンスター陣営の反撃処理
	// -------------------------------------------------------------
	private void processMonsterTurn(BattleState state) {
		for (Monster monster : state.getEnemies()) {
			if (monster.getHp() <= 0) continue;

			// 生存しているプレイヤーの中からランダムに対象を選択
			java.util.List<Player> alivePlayers = state.getParty().stream()
					.filter(p -> p.getCurrentHp() > 0)
					.toList();

			if (alivePlayers.isEmpty()) break;

			Player target = alivePlayers.get(random.nextInt(alivePlayers.size()));

			int d1 = random.nextInt(6) + 1;
			int d2 = random.nextInt(6) + 1;
			int diceSum = d1 + d2;
			int attackTotal = diceSum + monster.getAccuracy();
			// getAgility() から getEvasion() へ変更
			int evadeTotal = roll2D6() + target.getEvasion();

			state.addLog(String.format("👹 %s の %s への攻撃！ 命中判定:%d vs 回避:%d",
					monster.getName(), target.getName(), attackTotal, evadeTotal));

			if (diceSum != 2 && (diceSum == 12 || attackTotal >= evadeTotal)) {
				int rawDamage = monster.getAttack() + roll2D6();
				int netDamage = Math.max(0, rawDamage - target.getDefense());

				target.setCurrentHp(Math.max(0, target.getCurrentHp() - netDamage));
				state.addLog(String.format("  └ 命中！ %d ダメージを受けた！ (残りHP: %d)",
						netDamage, target.getCurrentHp()));

				if (target.getCurrentHp() == 0) {
					state.addLog(String.format("  └ 💀 %s は倒れた！", target.getName()));
				}
			} else {
				state.addLog("  └ 攻撃を回避した！");
			}
		}
	}

	// -------------------------------------------------------------
	// 判定・リザルト処理ヘルパー
	// -------------------------------------------------------------
	private int roll2D6() {
		return random.nextInt(6) + 1 + random.nextInt(6) + 1;
	}

	private boolean checkEnemiesDefeated(BattleState state) {
		return state.getEnemies().stream().allMatch(m -> m.getHp() <= 0);
	}

	private boolean checkPartyDefeated(BattleState state) {
		return state.getParty().stream().allMatch(p -> p.getCurrentHp() <= 0);
	}

	private void finishBattle(BattleState state) {
		state.setFinished(true);
		state.addLog("🎉 戦闘に勝利した！");

		int totalExp = 0;
		for (Monster monster : state.getEnemies()) {
			totalExp += monster.getExp();
			if (monster.getDropItem() != null && !monster.getDropItem().isBlank()) {
				state.getDroppedItems().add(monster.getName() + " の " + monster.getDropItem());
			}
		}
		state.setTotalExp(totalExp);
	}
}