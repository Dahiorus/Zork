package fr.zork.character;

public abstract class MortalCharacter extends Character {
	protected int maxHp;
	protected int hp;
	protected int power;
	protected int defense;
	
	
	protected MortalCharacter() {
		super();
	}
	
	
	protected MortalCharacter(String name, int maxHp, int power, int defense) throws IllegalArgumentException {
		super(name);
		
		if (maxHp <= 0) throw new IllegalArgumentException("maxHp is negative");
		
		
		this.maxHp = this.hp = maxHp;
		this.power = power;
		this.defense = defense;
	}
	
	
	/**
	 * @return the maxHp
	 */
	public int getMaxHp() {
		return maxHp;
	}

	/**
	 * @param maxHp the maxHp to set
	 */
	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}
	

	/**
	 * @return the hp
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * @param hp the hp to set
	 */
	public void setHp(int hp) {
		if (hp > this.maxHp) hp = this.maxHp;
		else if (hp < 0) hp = 0;
		
		this.hp = hp;
	}
	

	/**
	 * @return the power
	 */
	public int getPower() {
		return power;
	}

	/**
	 * @param power the power to set
	 */
	public void setPower(int power) {
		this.power = power;
	}
	

	/**
	 * @return the defense
	 */
	public int getDefense() {
		return defense;
	}

	/**
	 * @param defense the defense to set
	 */
	public void setDefense(int defense) {
		this.defense = defense;
	}
	

	/**
	 * Indicates if this MortalCharater is dead.
	 * 
	 * @return <code>true</code> if this MortalCharater is dead
	 */
	public boolean isDead() {
		return this.hp == 0;
	}
	
	
	/**
	 * Indicates if the hp of this MortalCharacter are full.
	 * 
	 * @return <code>true</code> if the hp of this MortalCharacter are full
	 */
	public boolean isFullHp() {
		return this.hp == this.maxHp;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		result = prime * result + defense;
		result = prime * result + hp;
		result = prime * result + maxHp;
		result = prime * result + power;
		
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		if (!(obj instanceof MortalCharacter)) return false;
		
		MortalCharacter other = (MortalCharacter) obj;
		
		if (defense != other.defense) return false;
		if (hp != other.hp) return false;
		if (maxHp != other.maxHp) return false;
		if (power != other.power) return false;
		
		return true;
	}
	
	
	public void receiveDamage(int damage) {
		if (damage <= 0) return;
		this.setHp(this.hp - damage);
	}
	
	
	public abstract boolean attack(MortalCharacter target) throws IllegalArgumentException;
	
	public abstract int getEffectivePower();
	
	public abstract int getEffectiveDefense();
}
