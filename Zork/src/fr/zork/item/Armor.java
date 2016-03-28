package fr.zork.item;

import fr.zork.item.enums.ArmorType;

public class Armor extends Equipment {
	private final ArmorType type;
	private final boolean isBig;
	

	public Armor(String name, int bonus, int lifespawn, ArmorType type, boolean isBig) throws IllegalArgumentException {
		super(name, bonus, lifespawn);
		
		this.type = type;
		this.isBig = isBig;
	}

	
	/**
	 * @return the type
	 */
	public ArmorType getType() {
		return type;
	}
	

	/**
	 * @return the isBig
	 */
	public boolean isBig() {
		return isBig;
	}
	
	
	@Override
	public Object clone() {
		return (Armor) super.clone();
	}

	
	@Override
	public String toString() {
		return "Armor [type=" + this.type 
				+ ", isBig=" + this.isBig 
				+ ", bonus=" + this.bonus 
				+ ", lifespawn=" + this.lifespawn 
				+ ", name=" + this.name + "]";
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		result = prime * result + (this.isBig ? 1231 : 1237);
		result = prime * result + this.type.hashCode();
		
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		if (!(obj instanceof Armor)) return false;
		
		Armor other = (Armor) obj;
		
		if (this.isBig != other.isBig) return false;
		if (this.type != other.type) return false;
		
		return true;
	}

	
	@Override
	public String getDescription() {
		return this.name + ": " + this.getItemType() + " " + this.type
				+ ", bonus +" + this.bonus + (this.isBig() ? ", GROS" : "");
	}


	@Override
	public String getItemType() {
		return "Armure";
	}
	
}
