package fr.zork.character.enums;

public enum Level {
	EASY("facile"),
	NORMAL("normal"),
	HARD("difficile"),
	EXTREME("extreme");
	
	
	private final String level;
	
	
	Level(final String level) {
		this.level = level;
	}
	
	
	public String toString() {
		return this.level;
	}
	
	
	public static Level find(String word) {
		if (word == null) return null;
		
		for (Level level : Level.values()) {
			if (level.level.equals(word)) return level;
		}
		
		return null;
	}
}
