package fr.zork.item;

public abstract class Equipment extends Item implements Damageable {
	protected int bonus;
	protected int lifespawn;
	

	public Equipment(String name, int bonus, int lifespawn) throws IllegalArgumentException {
		super(name);
		
		if (lifespawn <= 0) throw new IllegalArgumentException("lifespawn is negative");
		if (bonus < 0) throw new IllegalArgumentException("bonus is negative");
		
		this.bonus = bonus;
		this.lifespawn = lifespawn;
	}

	
	/**
	 * @return the bonus
	 */
	public int getBonus() {
		return bonus;
	}


	/**
	 * @param bonus the bonus to set
	 */
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
	
	
	@Override
	public int getLifespawn() {
		return this.lifespawn;
	}

	
	@Override
	public void setLifespawn(int lifespawn) {
		this.lifespawn = lifespawn;
	}

	
	@Override
	public void decrementLifespawn() {
		if (this.isUsable()) this.lifespawn--;
	}
	

	@Override
	public boolean isUsable() {
		return this.lifespawn > 0;
	}
	
	
	@Override
	public Object clone() {
		return (Equipment) super.clone();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		result = prime * result + bonus;
		result = prime * result + lifespawn;
		
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		if (!(obj instanceof Equipment)) return false;
		
		Equipment other = (Equipment) obj;
		if (this.bonus != other.bonus) return false;
		if (this.lifespawn != other.lifespawn) return false;
		
		return true;
	}

	
	
}
