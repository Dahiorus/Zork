package fr.zork.item.enums;

public enum Hand {
	RIGHT("main droite"),
	LEFT("main gauche"),
	BOTH("2 mains");
	
	
	private String hand;
	
	
	Hand(final String hand) {
		this.hand = hand;
	}
	
	
	public String toString() {
		return this.hand;
	}
}
