package fr.zork.character;

import java.util.ArrayList;
import java.util.List;

import fr.zork.character.enums.Level;
import fr.zork.item.Armor;
import fr.zork.item.Item;
import fr.zork.item.Weapon;
import fr.zork.world.Room;
import fr.zork.world.enums.Dice;

public class Monster extends MortalCharacter implements Cloneable {
	private int nbLoots;
	private List<Item> loots;
	private Weapon weapon;
	private Armor armor;
	private Level level;
	

	public Monster(String name, int maxHp, int power, int defense, Level level) throws IllegalArgumentException {
		super(name, maxHp, power, defense);
		
		if (level == null) throw new IllegalArgumentException("level is null");
		
		this.loots = new ArrayList<Item>();
		this.level = level;
	}
	
	
	/**
	 * @return the loots
	 */
	public List<Item> getLoots() {
		return loots;
	}
	

	public int getNbLoots() {
		return nbLoots;
	}


	public void setNbLoots(int nbLoots) {
		this.nbLoots = nbLoots;
	}


	/**
	 * @return the weapon
	 */
	public Weapon getWeapon() {
		return weapon;
	}


	/**
	 * @param weapon the weapon to set
	 */
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}


	/**
	 * @return the armor
	 */
	public Armor getArmor() {
		return armor;
	}


	/**
	 * @param armor the armor to set
	 */
	public void setArmor(Armor armor) {
		this.armor = armor;
	}


	public Level getLevel() {
		return level;
	}


	public void setLevel(Level level) {
		this.level = level;
	}
	
	
	@Override
	public Object clone() {
		Monster monster = null;
		
		try {
			monster = (Monster) super.clone();
			if (this.hasArmor()) monster.setArmor((Armor) this.getArmor().clone());
			if (this.hasWeapon()) monster.setWeapon((Weapon) this.getWeapon().clone());
			
			monster.loots = new ArrayList<Item>();
			for (Item item : this.getLoots()) {
				monster.getLoots().add((Item) item.clone());
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return monster;
	}


	@Override
	public String toString() {
		return "Monster [level=" + level + ", weapon=" + weapon + ", armor=" + armor + ", maxHp=" + maxHp + ", hp=" + hp + ", power=" + power
				+ ", defense=" + defense + ", name=" + name + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((armor == null) ? 0 : armor.hashCode());
		result = prime * result + ((loots == null) ? 0 : loots.hashCode());
		result = prime * result + ((weapon == null) ? 0 : weapon.hashCode());
		
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		if (!(obj instanceof Monster)) return false;
		
		Monster other = (Monster) obj;
		
		if (level != other.level) return false;
		if (armor == null) {
			if (other.armor != null) return false;
		} else if (!armor.equals(other.armor)) return false;
		
		if (loots == null) {
			if (other.loots != null) return false;
		} else if (!loots.equals(other.loots)) return false;
		
		if (weapon == null) {
			if (other.weapon != null) return false;
		} else if (!weapon.equals(other.weapon)) return false;
		
		return true;
	}


	public boolean hasWeapon() {
		return this.weapon != null;
	}
	
	
	public boolean hasArmor() {
		return this.armor != null;
	}
	
	
	@Override
	public String getDescription() {
		String description = this.name + ": Monstre";
		
		if (this.armor != null)  description += "\n  Arme: " + this.armor.getName();
		if (this.weapon != null) description += "\n  Armure: " + this.weapon.getName();
		
		return description;
	}
	
	
	public int getEffectivePower() {
		return this.power + (this.weapon == null ? 0 : this.weapon.getBonus());
	}
	
	
	public int getEffectiveDefense() {
		return this.defense + (this.armor == null ? 0 : this.armor.getBonus());
	}
	
	
	/**
	 * <p>Ce monstre lance une attaque sur une cible spécifiée.</p>
	 * <p>Lors de l'attaque, un jet de D12 détermine les dégats de base
	 * auxquels on ajoute sa force effective. Puis on soustrait les dégâts
	 * par la défense effective de la cible. Si l'attaque est un coup critique,
	 * alors on ajoute un bonus d'attaque.</p>
	 * 
	 * @param target la cible de l'attaque
	 * @return <code>true</code> si l'attaque est un coup critique,
	 * 			<code>false</code> sinon
	 * @throws IllegalArgumentException si target est null
	 */
	@Override
	public boolean attack(MortalCharacter target) throws IllegalArgumentException {
		if (target == null) throw new IllegalArgumentException("target is null");
		
		boolean critical = false;
		int totalDmg;
		
		// damage generation
		int basicDmg = Dice.D12.roll();
		totalDmg = basicDmg + this.getEffectivePower();
		
		// critical damage calculation
		int ccProba = Dice.D100.roll();
		if (ccProba >= 80) {
			int bonus = Dice.D6.roll();
			totalDmg += bonus;
			critical = true;
		} else if (ccProba < 10) {
			int malus = Dice.D6.roll();
			totalDmg -= malus;
		}
		
		// target defense calculation
		totalDmg -= target.getEffectiveDefense();
		
		target.receiveDamage(totalDmg);
		if (totalDmg < 0) critical = false;
		
		return critical;
	}
	
	
	public void giveLoots(Room room) {
		room.getTreasures().addAll(this.loots);
		this.loots.clear();
	}

}
