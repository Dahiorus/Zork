package fr.zork.world;

public class Curse {
	private final int damage;
	
	
	public Curse(int damage) throws IllegalArgumentException {
		if (damage <= 0) throw new IllegalArgumentException("damage is negative");
		
		this.damage = damage;
	}


	public int getDamage() {
		return damage;
	}


	@Override
	public String toString() {
		return "Curse [damage=" + damage + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + damage;
		
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Curse)) return false;
		
		Curse other = (Curse) obj;
		if (damage != other.damage) return false;
		
		return true;
	}
	
	
	
}
