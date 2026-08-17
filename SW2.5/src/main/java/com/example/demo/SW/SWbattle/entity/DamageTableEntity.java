package com.example.demo.SW.SWbattle.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "damage_tables")
public class DamageTableEntity {

	@Id
	private Integer power;

	private int d3;
	private int d4;
	private int d5;
	private int d6;
	private int d7;
	private int d8;
	private int d9;
	private int d10;
	private int d11;
	private int d12;

	public int getDamageByDice(int dice) {
		return switch (dice) {
			case 3 -> d3;
			case 4 -> d4;
			case 5 -> d5;
			case 6 -> d6;
			case 7 -> d7;
			case 8 -> d8;
			case 9 -> d9;
			case 10 -> d10;
			case 11 -> d11;
			case 12 -> d12;
			default -> 0;
		};
	}

	// Getter & Setter
	public Integer getPower() { return power; }
	public void setPower(Integer power) { this.power = power; }
	public int getD3() { return d3; }
	public void setD3(int d3) { this.d3 = d3; }
	public int getD4() { return d4; }
	public void setD4(int d4) { this.d4 = d4; }
	public int getD5() { return d5; }
	public void setD5(int d5) { this.d5 = d5; }
	public int getD6() { return d6; }
	public void setD6(int d6) { this.d6 = d6; }
	public int getD7() { return d7; }
	public void setD7(int d7) { this.d7 = d7; }
	public int getD8() { return d8; }
	public void setD8(int d8) { this.d8 = d8; }
	public int getD9() { return d9; }
	public void setD9(int d9) { this.d9 = d9; }
	public int getD10() { return d10; }
	public void setD10(int d10) { this.d10 = d10; }
	public int getD11() { return d11; }
	public void setD11(int d11) { this.d11 = d11; }
	public int getD12() { return d12; }
	public void setD12(int d12) { this.d12 = d12; }
}