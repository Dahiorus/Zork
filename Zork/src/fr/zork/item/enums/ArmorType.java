package fr.zork.item.enums;

public enum ArmorType {
	HEAD("tete"),
	ARM("bras"),
	BODY("corps"),
	LEG("jambes");
	
	
	private final String type;
	
	
	ArmorType(final String type) {
		this.type = type;
	}
	
	
	public String toString() {
		return this.type;
	}
	
	
	public static ArmorType find(String word) {
		if (word == null) return null;
		
		for (ArmorType armorType : ArmorType.values()) {
			if (armorType.type.equals(word)) return armorType;
		}
		
		return null;
	}
}
