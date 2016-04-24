package fr.zork.character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.zork.item.Armor;
import fr.zork.item.Equipment;
import fr.zork.item.Item;
import fr.zork.item.Potion;
import fr.zork.item.Spell;
import fr.zork.item.Weapon;
import fr.zork.item.enums.ArmorType;
import fr.zork.item.enums.Hand;
import fr.zork.item.enums.WeaponType;
import fr.zork.world.Room;
import fr.zork.world.enums.Dice;

public class Player extends MortalCharacter {
	private List<Item> bag;
	private Armor head, body, arm, leg;
	private Weapon rightHand, leftHand;
	private int level;
	
	/* singleton pattern */
	private static class PlayerHolder {
		private final static Player instance = new Player();
	}

	
	private Player() {
		super();
		this.bag = new ArrayList<Item>();
	}
	
	
	public static Player getInstance() {
		return PlayerHolder.instance;
	}
	
	
	public void newPlayer(String name) {
		this.level = 1;
		this.maxHp = this.hp = 200;
		this.power = 30;
		this.defense = 25;

		this.setName(name);
		this.setStarterStuff();
	}
	

	/**
	 * @return the bag
	 */
	public List<Item> getBag() {
		return bag;
	}


	/**
	 * @return the head
	 */
	public Armor getHead() {
		return head;
	}


	/**
	 * @param head the head to set
	 */
	public void setHead(Armor head) {
		if (head != null && head.getType() != ArmorType.HEAD) return;
		this.head = head;
	}


	/**
	 * @return the body
	 */
	public Armor getBody() {
		return body;
	}


	/**
	 * @param body the body to set
	 */
	public void setBody(Armor body) {
		if (body != null && body.getType() != ArmorType.BODY) return;
		this.body = body;
	}


	/**
	 * @return the arm
	 */
	public Armor getArm() {
		return arm;
	}


	/**
	 * @param arm the arm to set
	 */
	public void setArm(Armor arm) {
		if (arm != null && arm.getType() != ArmorType.ARM) return;
		this.arm = arm;
	}


	/**
	 * @return the leg
	 */
	public Armor getLeg() {
		return leg;
	}


	/**
	 * @param leg the leg to set
	 */
	public void setLeg(Armor leg) {
		if (leg != null && leg.getType() != ArmorType.LEG) return;
		this.leg = leg;
	}


	/**
	 * @return the rightHand
	 */
	public Weapon getRightHand() {
		return rightHand;
	}


	/**
	 * @param rightHand the rightHand to set
	 */
	public void setRightHand(Weapon rightHand) {
		if (rightHand != null && rightHand.getHand() == Hand.LEFT) return;
		this.rightHand = rightHand;
	}


	/**
	 * @return the leftHand
	 */
	public Weapon getLeftHand() {
		return leftHand;
	}


	/**
	 * @param leftHand the leftHand to set
	 */
	public void setLeftHand(Weapon leftHand) {
		if (leftHand != null && leftHand.getHand() != Hand.LEFT) return;
		this.leftHand = leftHand;
	}


	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}


	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}


	@Override
	public String toString() {
		return "Player [head=" + head + ", body=" + body + ", arm=" + arm + ", leg=" + leg + ", rightHand=" + rightHand
				+ ", leftHand=" + leftHand + ", level=" + level + ", maxHp=" + maxHp + ", hp=" + hp + ", power=" + power
				+ ", defense=" + defense + ", name=" + name + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		result = prime * result + ((arm == null) ? 0 : arm.hashCode());
		result = prime * result + ((bag == null) ? 0 : bag.hashCode());
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((head == null) ? 0 : head.hashCode());
		result = prime * result + ((leftHand == null) ? 0 : leftHand.hashCode());
		result = prime * result + ((leg == null) ? 0 : leg.hashCode());
		result = prime * result + level;
		result = prime * result + ((rightHand == null) ? 0 : rightHand.hashCode());
		
		return result;
	}
	
	
	public void setStarterStuff() {
		Weapon epeeCourte = new Weapon("epee courte", 22, 18, 1, WeaponType.SWORD, Hand.LEFT);
		Armor plastronCuir = new Armor("plastron de cuir", 10, 15, 1, ArmorType.BODY, false);
		Potion potionVerte = new Potion("potion verte", 50);
		Spell foudre = new Spell("foudre", 30);
		
		this.setLeftHand((Weapon) epeeCourte.clone());
		this.setBody((Armor) plastronCuir.clone());
		
		this.getBag().add((Item) potionVerte.clone());
		this.getBag().add((Item) potionVerte.clone());
		this.getBag().add((Item) foudre.clone());
		this.getBag().add((Item) epeeCourte.clone());
	}
	
	
	/**
	 * <p>Indique si le joueur a une arme usée.</p>
	 * @return <code>true</code> si le joueur a une arme usée,
	 * 			<code>false</code> sinon
	 */
	public boolean hasUnusableWeapon() {
		if (this.rightHand != null && !this.rightHand.isUsable()) return true;
		if (this.leftHand != null && !this.leftHand.isUsable()) return true;
		return false;
	}
	
	
	/**
	 * <p>Indique si le joueur a une armure usée.</p>
	 * @return <code>true</code> si le joueur a une armure valide,
	 * 			<code>false</code> sinon
	 */
	public boolean hasUnusableArmor() {
		if (this.head != null && !this.head.isUsable()) return true;
		if (this.body != null && !this.body.isUsable()) return true;
		if (this.arm != null && !this.arm.isUsable()) return true;
		if (this.leg != null && !this.leg.isUsable()) return true;
		return false;
	}


	/**
	 * <p>Le joueur lance une attaque sur une cible spécifiée.</p>
	 * <p>Lors de l'attaque, un jet de D20 détermine les dégats de base
	 * auxquels on ajoute sa force effective. Puis on soustrait les dégâts
	 * par la défense effective de la cible. Si l'attaque est un coup critique,
	 * alors on ajoute un bonus d'attaque.</p>
	 * <p>On diminue la durée de vie des armes après l'attaque.</p>
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
		int basicDmg = Dice.D20.roll();
		totalDmg = basicDmg + this.getEffectivePower();
		
		// critical damage calculation
		int ccProba = Dice.D100.roll();
		if (ccProba >= 85) {
			if (ccProba < 90) {
				totalDmg += Dice.D10.roll() + this.level;
			} else if (ccProba < 95) {
				totalDmg = (int) Math.floor(totalDmg * 1.5);
			} else {
				totalDmg *= 2;
			}
			
			critical = true;
		}
		
		// target defense calculation
		totalDmg -= target.getEffectiveDefense();
		
		target.receiveDamage(totalDmg);
		if (totalDmg < 0) critical = false;
		
		this.decrementWeaponLifespawn();
		
		return critical;
	}
	

	/**
	 * Le joueur reçoie des blessures, et une de ses
	 * pièces d'armure choisie par un lancé de D4 diminue
	 * sa durée de vie.
	 * 
	 * @param damage - les blessures infligées
	 */
	@Override
	public void receiveDamage(int damage) {
		super.receiveDamage(damage);
		
		if (this.head != null || this.body != null || this.arm != null || this.leg != null) {
			boolean done = false;
			
			while (!done) {
				int choice = Dice.D4.roll();
				
				switch (choice) {
					case 1:
						if (this.head != null) {
							this.head.decrementLifespawn();
							done = true;
						}
						break;
					case 2:
						if (this.body != null) {
							this.body.decrementLifespawn();
							done = true;
						}
						break;
					case 3:
						if (this.arm != null) {
							this.arm.decrementLifespawn();
							done = true;
						}
						break;
					case 4:
						if (this.leg != null) {
							this.leg.decrementLifespawn();
							done = true;
						}
						break;
				}
			}
		}
	}
	
	
	/**
	 * Le joueur ramasse l'item spécifié se trouvant dans la pièce spécifiée.
	 * @param item - l'item à ramasser
	 * @param room - la pièce où se trouve l'item
	 * @throws IllegalArgumentException si item == null ou room == null
	 */
	public void pickUpItem(Item item, Room room) throws IllegalArgumentException {
		if (item == null) throw new IllegalArgumentException("item is null");
		if (room == null) throw new IllegalArgumentException("room is null");
		
		if (room.getTreasures().contains(item)) {
			this.bag.add(item);
			room.getTreasures().remove(item);
		}
	}
	
	
	/**
	 * S'équipe de l'équipement spécifié. Il remplace l'équipement courant
	 * de même type en le mettant dans le sac du joueur.
	 * 
	 * @param equipment - l'équipement à équiper
	 * @return <code>true</code> si l'équipement est bien équipé,
	 * 			<code>false</code> sinon
	 * @throws IllegalArgumentException
	 */
	public boolean equip(Equipment equipment) throws IllegalArgumentException {
		if (equipment == null) throw new IllegalArgumentException("equipment is null");
		
		if (!this.bag.contains(equipment)) return false;
		if (!equipment.isUsable()) return false;
		if (this.level < equipment.getLevelMin()) return false;
		
		if (equipment instanceof Armor) {
			Armor armor = (Armor) equipment;
			ArmorType type = armor.getType();
			
			if (!this.checkBigArmor(armor)) return false;
			
			this.unequip(type);
			
			switch (type) {
				case HEAD:
					this.setHead(armor);
					break;
				case BODY:
					this.setBody(armor);
					break;
				case ARM:
					this.setArm(armor);
					break;
				case LEG:
					this.setLeg(armor);
					break;
			}
		} else if (equipment instanceof Weapon) {
			Weapon weapon = (Weapon) equipment;
			
			switch (weapon.getHand()) {
				case RIGHT:
					this.unequip(Hand.RIGHT);
					this.rightHand = weapon;
					break;
				case LEFT:
					if (this.rightHand != null && this.rightHand.isTwoHanded()) {
						this.unequip(Hand.BOTH);
					}
					this.unequip(Hand.LEFT);
					this.leftHand = weapon;
					break;
				case BOTH:
					this.unequip(Hand.BOTH);
					this.rightHand = weapon;
			}
		}
		
		return this.bag.remove(equipment);
	}
	
	
	public void unequip(Hand hand) {
		switch (hand) {
			case LEFT:
				if (this.leftHand != null) {
					if (this.leftHand.isUsable()) this.bag.add(this.leftHand);
					this.leftHand = null;	
				}
				break;
			case RIGHT:
				if (this.rightHand != null) {
					if (this.rightHand.isUsable()) this.bag.add(this.rightHand);
					this.rightHand = null;
				}
				break;
			case BOTH:
				if (this.leftHand != null) {
					if (this.leftHand.isUsable()) this.bag.add(this.leftHand);
					this.leftHand = null;	
				}
				if (this.rightHand != null) {
					if (this.rightHand.isUsable()) this.bag.add(this.rightHand);
					this.rightHand = null;
				}
				break;
		}
			
	}
	
	
	public void unequip(ArmorType type) {
		switch (type) {
			case HEAD:
				if (this.head != null) {
					if (this.head.isUsable()) this.bag.add(this.head);
					this.head = null;
				}
				break;
			case BODY:
				if (this.body != null) {
					if (this.body.isUsable()) this.bag.add(this.body);
					this.body = null;
				}
				break;
			case ARM:
				if (this.arm != null) {
					if (this.arm.isUsable()) this.bag.add(this.arm);
					this.arm = null;
				}
				break;
			case LEG:
				if (this.leg != null) {
					if (this.leg.isUsable()) this.bag.add(this.leg);
					this.leg = null;
				}
				break;
		}
	}
	
	
	public void heal(Potion potion) {
		if (!this.bag.contains(potion)) return;
		
		this.setHp(this.hp + potion.getHpGain());
		this.bag.remove(potion);
	}
	
	
	public void cast(Spell spell, Monster monster) {
		if (!this.bag.contains(spell)) return;
		
		monster.receiveDamage(spell.getDamage());
		this.bag.remove(spell);
	}
	
	
	public boolean throwItem(Item item) {
		if (!this.bag.contains(item)) return false;
		
		if (item instanceof Equipment) {
			Equipment equipment = (Equipment) item;
			if (equipment.isUsable()) return false;
		}
		
		return this.bag.remove(item);
	}
	
	
	public void levelUp() {
		this.level++;
		this.power += 2;
		this.defense += 1;
		this.maxHp += 10;
		this.setHp(this.hp + 10);
	}
	

	@Override
	public int getEffectivePower() {
		int totalPower = this.power;
		
		if (this.isUsable(this.rightHand)) totalPower += this.rightHand.getBonus();
		if (this.isUsable(this.leftHand))  totalPower += this.leftHand.getBonus();
		
		return totalPower;
	}

	@Override
	public int getEffectiveDefense() {
		int totalDefense = this.defense;
		
		if (this.isUsable(this.head)) totalDefense += this.head.getBonus();
		if (this.isUsable(this.body)) totalDefense += this.body.getBonus();
		if (this.isUsable(this.arm))  totalDefense += this.arm.getBonus();
		if (this.isUsable(this.leg))  totalDefense += this.leg.getBonus();
		
		return totalDefense;
	}

	@Override
	public String getDescription() {
		String description = this.name + ": Joueur de niveau " + this.level + "\n";
		
		description += "PV: " + this.hp + "/" + this.maxHp 
				+ " - Force: " + this.getEffectivePower() 
				+ " - Defense: " + this.getEffectiveDefense() + "\n";
		description += this.getArmorDescription() + "\n";
		description += this.getWeaponDescription();
		
		return description.trim();
	}
	
	
	public String getBagDescription() {
		String description = "Sac:";
		Map<String, Integer> copies = this.getItemCopies();
		
		for (String itemName : copies.keySet()) {
			Item item = this.getByName(itemName);
			if (item instanceof Potion || item instanceof Spell) {
				description += "\n  - (x" + copies.get(itemName) + ") " + item.getDescription();
			}
		}
		
		return description;
	}
	
	
	public String getEquipmentListDescription() {
		String description = "Sac:";
		Map<String, Integer> copies = this.getItemCopies();
		
		for (String itemName : copies.keySet()) {
			Item item = this.getByName(itemName);
			if (item instanceof Equipment) {
				description += "\n  - (x" + copies.get(itemName) + ") " + item.getDescription();
			}
		}
		
		return description;
	}

	
	private void decrementWeaponLifespawn() {
		if (this.rightHand != null) this.rightHand.decrementLifespawn();
		if (this.leftHand != null)  this.leftHand.decrementLifespawn();
	}
	
	
	private boolean isUsable(Equipment equipment) {
		return equipment != null && equipment.isUsable();
	}
	
	
	private String getArmorDescription() {
		String description = "Armure:";
		
		if (this.head != null) description += "\n  - Tête: " + this.head.getName() + " +" + this.head.getBonus() + 
				(!this.head.isUsable() ? " [usé]" : " [" + this.head.getLifespawn() + " utilisations]");
		
		if (this.body != null) description += "\n  - Corps: " + this.body.getName() + " +" + this.body.getBonus() + 
				(!this.body.isUsable() ? " [usé]" : " [" + this.body.getLifespawn() + " utilisations]");
		
		if (this.arm != null)  description += "\n  - Bras: " + this.arm.getName() + " +" + this.arm.getBonus() + 
				(!this.arm.isUsable() ? " [usé]" : " [" + this.arm.getLifespawn() + " utilisations]");
		
		if (this.leg != null)  description += "\n  - Jambes: " + this.leg.getName() + " +" + this.leg.getBonus() + 
				(!this.leg.isUsable() ? " [usé]" : " [" + this.leg.getLifespawn() + " utilisations]");
		
		return description;
	}
	
	
	private String getWeaponDescription() {
		String description = "Arme:";
		
		if (this.rightHand != null) {
			description += this.rightHand.isTwoHanded() ? "\n  - 2 mains: " : "\n  - Droite: ";
			description += this.rightHand.getName() + " +" + this.rightHand.getBonus() + 
					(!this.rightHand.isUsable() ? " [usé]" : " [" + this.rightHand.getLifespawn() + " utilisations]");
		}
		if (this.leftHand != null) {
			description += "\n  - Gauche: " + this.leftHand.getName() + " +" + this.leftHand.getBonus() +
					(!this.leftHand.isUsable() ? " [usé]" : " [" + this.leftHand.getLifespawn() + " utilisations]");
		}
		
		return description;
	}
	
	
	/**
	 * <p>Vérifie s'il est possible de s'équiper de l'armure spécifiée.</p>
	 * <p>Si l'armure est grosse, on vérifie si le joueur n'a pas déjà
	 * une autre grosse armure.</p>
	 * @param armor - l'armure spécifiée
	 * @return <code>true</code> s'il est possible de porter l'armure,
	 * 			<code>false</code> sinon
	 */
	private boolean checkBigArmor(Armor armor) {
		if (!armor.isBig()) return true;
		
		boolean result = false;
		
		switch (armor.getType()) {
		case HEAD:
			if (this.body == null && this.arm == null && this.leg == null) result = true;
			else if (this.body != null && this.body.isBig()) result = false;
			else if (this.arm != null && this.arm.isBig()) result = false;
			else if (this.leg != null && this.leg.isBig()) result = false;
			else result = true;
			break;
		case BODY:
			if (this.head == null && this.arm == null && this.leg == null) result = true;
			else if (this.head != null && this.head.isBig()) result = false;
			else if (this.arm != null && this.arm.isBig()) result = false;
			else if (this.leg != null && this.leg.isBig()) result = false;
			else result = true;
			break;
		case ARM:
			if (this.body == null && this.head == null && this.leg == null) result = true;
			else if (this.body != null && this.body.isBig()) result = false;
			else if (this.head != null && this.head.isBig()) result = false;
			else if (this.leg != null && this.leg.isBig()) result = false;
			else result = true;
			break;
		case LEG:
			if (this.body == null && this.arm == null && this.head == null) result = true;
			else if (this.body != null && this.body.isBig()) result = false;
			else if (this.arm != null && this.arm.isBig()) result = false;
			else if (this.head != null && this.head.isBig()) result = false;
			else result = true;
			break;
		default:
			break;
		}
		
		return result;
	}
	
	
	private Map<String, Integer> getItemCopies() {
		Map<String, Integer> copies = new HashMap<String, Integer>();
		
		for (Item item : this.bag) {
			if (!copies.containsKey(item.getName())) copies.put(item.getName(), 1);
			else copies.put(item.getName(), copies.get(item.getName()) + 1);
		}
		
		return copies;
	}
	
	
	private Item getByName(String name) {
		if (name == null) return null;
		
		for (Item item : this.bag) {
			if (item.getName().equals(name)) return item;
		}
		
		return null;
	}
	
}
