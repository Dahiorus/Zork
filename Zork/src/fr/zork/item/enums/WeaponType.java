package fr.zork.item.enums;

public enum WeaponType {
	SWORD("epee"),
	AXE("hache"),
	HAMMER("marteau"),
	ROD("baton"),
	DAGGER("dague");
	
	
	private final String type;
	
	
	WeaponType(final String type) {
		this.type = type;
	}
	
	
	public String toString() {
		return this.type;
	}
}
