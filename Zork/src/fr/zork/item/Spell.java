package fr.zork.item;

public class Spell extends Item {
	private final int damage;

	
	public Spell(String name, int damage) throws IllegalArgumentException {
		super(name);
		
		if (damage <= 0) throw new IllegalArgumentException("damage is negative");
		
		this.damage = damage;
	}


	public int getDamage() {
		return damage;
	}
	
	
	@Override
	public Object clone() {
		return (Spell) super.clone();
	}


	@Override
	public String toString() {
		return "Spell [damage=" + damage + ", name=" + name + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		result = prime * result + damage;
		
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		if (!(obj instanceof Spell)) return false;
		
		Spell other = (Spell) obj;
		
		if (damage != other.damage) return false;
		
		return true;
	}
	
	
	@Override
	public String getDescription() {
		return this.name + ": " + this.getItemType() + " " + this.damage + " blessures";
	}


	@Override
	public String getItemType() {
		return "Sort";
	}

}
