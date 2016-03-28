package fr.zork.item;

import fr.zork.item.enums.Hand;
import fr.zork.item.enums.WeaponType;

public class Weapon extends Equipment {
	private final WeaponType type;
	private final Hand hand;
	

	public Weapon(String name, int bonus, int lifespawn, WeaponType type, Hand hands) throws IllegalArgumentException {
		super(name, bonus, lifespawn);
		
		this.type = type;
		this.hand = hands;
	}

	
	/**
	 * @return the type
	 */
	public WeaponType getType() {
		return type;
	}


	/**
	 * @return the hands
	 */
	public Hand getHand() {
		return hand;
	}
	
	
	public boolean isTwoHanded() {
		return this.hand == Hand.BOTH;
	}

	
	@Override
	public String toString() {
		return "Weapon [type=" + this.type 
				+ ", hand=" + this.hand 
				+ ", bonus=" + this.bonus 
				+ ", lifespawn=" + this.lifespawn + 
				", name=" + this.name + "]";
	}
	
	
	@Override
	public Object clone() {
		return (Weapon) super.clone();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((hand == null) ? 0 : hand.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		if (!(obj instanceof Weapon)) return false;
		
		Weapon other = (Weapon) obj;
		
		if (this.hand != other.hand) return false;
		if (this.type != other.type) return false;
		
		return true;
	}
	
	
	@Override
	public String getDescription() {
		return this.name + ": " + this.getItemType() + " " + this.type 
				+ ", " + this.hand + ", bonus +" + this.bonus + ", "
				+ (this.isUsable() ? this.lifespawn + " utilisations" : "inutilisable");
	}


	@Override
	public String getItemType() {
		return "Arme";
	}
}
