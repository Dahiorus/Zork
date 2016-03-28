package fr.zork.world.enums;

import java.util.Random;

public enum Dice {
	D4(4),
	D6(6),
	D8(8),
	D10(10),
	D12(12),
	D20(20),
	D100(100);
	
	
	private final int faces;
	
	
	Dice(final int faces) {
		this.faces = faces;
	}
	
	
	public int getFaces() {
		return this.faces;
	}
	
	
	public int roll() {
		Random random = new Random();
		
		return 1 + random.nextInt(this.faces);
	}
	
	
	public String toString() {
		return String.valueOf(this.faces);
	}
}
