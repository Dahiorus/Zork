package fr.zork.commands.parsers;

import fr.zork.character.Monster;
import fr.zork.character.Player;
import fr.zork.commands.CombatCommand;
import fr.zork.commands.execution.PreparedCommand;
import fr.zork.game.Game;
import fr.zork.item.Equipment;
import fr.zork.item.Item;
import fr.zork.item.Potion;
import fr.zork.item.Spell;
import fr.zork.world.enums.Dice;

public class CombatCommandParser extends CommandParser {
	public static final int PLAYER = 0;
	public static final int MONSTER = 1;
	public static final int END = 2;
	
	private static class CombatCommandParserHolder {
		private final static CombatCommandParser instance = new CombatCommandParser();
	}
	

	private CombatCommandParser() {
		this.command = new CombatCommand();
	}
	
	
	public static CombatCommandParser getInstance() {
		return CombatCommandParserHolder.instance;
	}


	public int execute(Monster monster, PreparedCommand preparedCommand) {
		if (preparedCommand.isUnknown()) {
			System.out.println("Cette action est inconnue.");
			return PLAYER;
		}
		
		String word = preparedCommand.getWord();
		int nextTurn = PLAYER;
		
		switch (word) {
			case CombatCommand.ATTACK:
				if (preparedCommand.hasOptions()) {
					System.out.println("Cette commande n'a pas d'option.");
					nextTurn = PLAYER;
				} else {
					this.attack(monster);
					nextTurn = MONSTER;
				}
				break;
			case CombatCommand.USE:
				nextTurn = this.use(preparedCommand) ? MONSTER : PLAYER;
				break;
			case CombatCommand.CAST:
				nextTurn = this.cast(preparedCommand, monster) ? MONSTER : PLAYER;
				break;
			case CombatCommand.LOOK:
				this.look(preparedCommand, monster);
				nextTurn = PLAYER;
				break;
			case CombatCommand.EQUIP:
				this.equip(preparedCommand);
				nextTurn = PLAYER;
				break;
			case CombatCommand.FLEE:
				if (preparedCommand.hasOptions()) {
					System.out.println("Cette commande n'a pas d'option");
					nextTurn = PLAYER;
				} else {
					nextTurn = this.flee() ? END : MONSTER;
				}
				break;
			case CombatCommand.HELP:
				if (preparedCommand.hasOptions()) System.out.println("Cette commande n'a pas d'option");
				else this.printHelp();
				nextTurn = PLAYER;
				break;
			default:
				System.out.println("Cette action est impossible.");
				nextTurn = PLAYER;
				break;
		}
		
		return nextTurn;
	}
	
	
	public void attack(Monster monster) {
		System.out.println("Vous attaquez...");
		
		Player player = Player.getInstance();
		
		int hpBeforeAttack = monster.getHp();
		boolean critical = player.attack(monster);
		
		if (critical) System.out.println("Coup critique !");
		if (monster.getHp() < hpBeforeAttack) System.out.println("Le monstre a perdu " + (hpBeforeAttack - monster.getHp()) + "PV.");
		else System.out.println("Votre attaque echoue.");
		
		if (player.hasUnusableWeapon()) {
			System.out.println("Vous avez une arme inutilisable");
			System.out.println("Votre force est reduite.");
		}
	}
	
	
	public boolean use(PreparedCommand command) {
		if (!command.hasOptions()) {
			System.out.println("Utiliser quel item ?");
			return false;
		}
		
		Player player = Player.getInstance();
		
		String potionName = command.getOptions()[0];
		Item toUse = Game.getInstance().getItem(potionName, player.getBag());
		
		if (toUse != null) {
			if (toUse instanceof Potion) {
				Potion potion = (Potion) toUse;
				player.heal(potion);
				
				System.out.println("Vous gagnez " + potion.getHpGain() + " PV.");
				
				return true;
			}
			
			System.out.println("Cet item n'est pas une potion.");
		} else {
			System.out.println("Cet item n'est pas dans votre inventaire.");
		}
		
		return false;
	}
	
	
	public boolean cast(PreparedCommand command, Monster monster) {
		if (!command.hasOptions()) {
			System.out.println("Lancer quel sort ?");
			return false;
		}
		
		Player player = Player.getInstance();
		
		String spellName = command.getOptions()[0];
		Item toCast = Game.getInstance().getItem(spellName, player.getBag());
		
		if (toCast != null) {
			if (toCast instanceof Spell) {
				Spell spell = (Spell) toCast;
				int damage = spell.getDamage();
				
				player.cast(spell, monster);
				System.out.println("Vous avez lance un sort.");
				System.out.println("Le monstre subit " + damage + " blessures");
				
				return true;
			}
			
			System.out.println("Cet item n'est pas un sort.");
		} else {
			System.out.println("Cet item n'est pas dans votre inventaire.");
		}
		
		return false;
	}
	
	
	public boolean look(PreparedCommand command, Monster monster) {
		if (!command.hasOptions()) {
			System.out.println("Vous pouvez voir :");
			System.out.println("  inventaire, equipements, joueur, monstres");
			return false;
		}
		
		Player player = Player.getInstance();
		
		String option = command.getOptions()[0];
		boolean result = true;
		
		switch (option) {
			case CombatCommand.BAG:
				System.out.println(player.getBagDescription());
				break;
			case CombatCommand.EQUIPMENTS:
				System.out.println(player.getEquipmentListDescription());
				break;
			case CombatCommand.PLAYER:
				System.out.println(player.getDescription());
				break;
			case CombatCommand.MONSTERS:
				System.out.println(monster.getDescription());
				break;
			default:
				System.out.println("Cette action est impossible.");
				result = false;
				break;
		}
		
		return result;
	}
	
	
	public boolean equip(PreparedCommand command) {
		if (!command.hasOptions()) {
			System.out.println("Equiper quel equipement ?");
			return false;
		}
		
		String equipmentName = command.getOptions()[0];
		Equipment toEquip = Game.getInstance().getEquipment(equipmentName, true);
		
		if (toEquip != null) {
			if (Player.getInstance().equip(toEquip)) {
				System.out.println("Vous vous etes equipe de : " + equipmentName);
				return true;
			}
			
			System.out.println("Vous ne pouvez pas vous equipe de cet item.");
		} else System.out.println("Vous ne possedez pas cet item.");
		
		return false;
	}
	
	
	public boolean flee() {
		System.out.println("Vous tentez de fuir...");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		if (Dice.D6.roll() >= 5) {
			System.out.println("Vous fuyez.");
			return true;
		}
		
		System.out.println("Vous n'y arrivez pas.");
		return false;
	}

}
