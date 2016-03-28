package fr.zork.commands.parsers;

import fr.zork.character.Monster;
import fr.zork.character.Player;
import fr.zork.commands.BasicCommand;
import fr.zork.commands.execution.PreparedCommand;
import fr.zork.game.Game;
import fr.zork.item.Equipment;
import fr.zork.item.Item;
import fr.zork.item.Potion;
import fr.zork.item.enums.ArmorType;
import fr.zork.item.enums.Hand;
import fr.zork.utils.writer.SaveXMLWriter;
import fr.zork.world.Room;
import fr.zork.world.enums.Dice;
import fr.zork.world.enums.Exit;

public class BasicCommandParser extends CommandParser {
	
	private static class BasicCommandParserHolder {
		private final static BasicCommandParser instance = new BasicCommandParser();
	}
	

	private BasicCommandParser() {
		this.command = new BasicCommand();
	}

	
	public static BasicCommandParser getInstance() {
		return BasicCommandParserHolder.instance;
	}
	
	
	public boolean execute(PreparedCommand preparedCommand) {
		if (preparedCommand.isUnknown()) {
			System.out.println("Cette action n'existe pas...");
			return false;
		}
		
		String word = preparedCommand.getWord();
		boolean result = false;
		
		switch (word) {
			case BasicCommand.GO:
				this.go(preparedCommand);
				break;
			case BasicCommand.BACK:
				if (preparedCommand.hasOptions()) System.out.println("Cette commande n'a pas d'option.");
				else this.goBack();
				break;
			case BasicCommand.LOOK:
				this.look(preparedCommand);
				break;
			case BasicCommand.LOOT:
				this.loot(preparedCommand);
				break;
			case BasicCommand.FIGHT:
				if (preparedCommand.hasOptions()) System.out.println("Cette commande n'a pas d'option.");
				else this.fight();
				break;
			case BasicCommand.EQUIP:
				this.equip(preparedCommand);
				break;
			case BasicCommand.UNEQUIP:
				this.unequip(preparedCommand);
				break;
			case BasicCommand.USE:
				this.use(preparedCommand);
				break;
			case BasicCommand.THROW:
				this.throwOut(preparedCommand);
				break;
			case BasicCommand.SAVE:
				this.save(preparedCommand);
				break;
			case BasicCommand.QUIT:
				if (preparedCommand.hasOptions()) System.out.println("Cette commande n'a pas d'option.");
				else result = true;
				break;
			case BasicCommand.HELP:
				if (preparedCommand.hasOptions()) System.out.println("Cette commande n'a pas d'option.");
				else this.printHelp();
				break;
			default:
				System.out.println("Cette commande est inconnue.");
				break;
		}
		
		return result;
	}
	
	
	public void go(PreparedCommand command) {
		if (!command.hasOptions()) {
			System.out.println("Aller ou ?");
			return;
		}
		
		Game game = Game.getInstance();
		
		if (game.getCurrentRoom().hasMonsters()) {
			System.out.println("Un monstre vous empeche d'avancer...");
			return;
		}
		
		String direction = command.getOptions()[0];
		Exit exit = Exit.find(direction);
		
		if (exit == null) {
			System.out.println("Cette direction est inconnue...");
			return;
		}
		
		Room nextRoom = game.getCurrentRoom().getNextRoom(exit);
		
		if (nextRoom == null) {
			System.out.println("Il n'y a pas de porte dans cette direction.");
			return;
		}
		
		game.setPreviousRoom(game.getCurrentRoom());
		game.setCurrentRoom(nextRoom);
		
		// trigger curse
		if (game.getCurrentRoom().hasCurse()) {
			int damage = game.getCurrentRoom().getCurse().getDamage();
			
			System.out.println("Cette salle est piegee !");
			System.out.println("Vous subissez " + damage + " blessures.");
			
			game.getCurrentRoom().triggerCurse(Player.getInstance());
		}
	}
	
	
	public void goBack() {
		Game game = Game.getInstance();
		
		if (game.getCurrentRoom() == null) {
			System.out.println("Vous ne pouvez pas retourner en arriere.");
			return;
		}
		
		Room nextRoom = game.getPreviousRoom();
		game.setPreviousRoom(game.getCurrentRoom());
		game.setCurrentRoom(nextRoom);
	}
	
	
	public boolean look(PreparedCommand command) {
		if (!command.hasOptions()) {
			System.out.println("Vous pouvez voir :");
			System.out.println("  joueur, inventaire, equipements, salle");
			return false;
		}
		
		Player player = Player.getInstance();
		
		String option = command.getOptions()[0];
		boolean result = true;
		
		switch (option) {
			case BasicCommand.PLAYER:
				System.out.println(player.getDescription());
				break;
			case BasicCommand.BAG:
				System.out.println(player.getBagDescription());
				break;
			case BasicCommand.EQUIPMENTS:
				System.out.println(player.getEquipmentListDescription());
				break;
			case BasicCommand.ROOM:
				System.out.println(Game.getInstance().getCurrentRoom().getDescription());
				break;
			default:
				System.out.println("Cette action est impossible.");
				result = false;
				break;
		}
		
		return result;
	}
	
	
	public boolean loot(PreparedCommand command) {
		if (!command.hasOptions()) {
			System.out.println("Prendre quel item ?");
			return false;
		}
		
		Game game = Game.getInstance();
		
		if (game.getCurrentRoom().getTreasures().isEmpty()) {
			System.out.println("La salle est vide.");
			return false;
		}
		
		String itemName = command.getOptions()[0];
		Item toLoot = game.getItem(itemName, game.getCurrentRoom().getTreasures());
		
		if (toLoot != null) {
			Player.getInstance().pickUpItem(toLoot, game.getCurrentRoom());
			System.out.println("Vous avez pris : " + itemName);
			
			return true;
		}
		
		System.out.println("Cet item ne se trouve pas dans cette salle");
		
		return false;
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
	
	
	public boolean unequip(PreparedCommand command) {
		if (!command.hasOptions()) {
			System.out.println("Vous pouvez enlever :");
			System.out.println("  arme (droite, gauche, _)");
			System.out.println("  armure (tete, corps, bras, jambes, _)");
			return false;
		}
		
		Player player = Player.getInstance();
		
		String className = command.getOptions()[0];
		String type = null;
		
		if (command.getOptionsLength() > 1) {
			type = command.getOptions()[1];
		}
		
		if (className.equals(BasicCommand.ARMOR)) {
			if (type != null && !type.equals("")) {
				ArmorType armorType = ArmorType.find(type);
				
				if (armorType != null) {
					player.unequip(armorType);
					System.out.println("L'armure se trouve dans votre inventaire.");
					return true;
				}
				
				System.out.println("Cette armure n'existe pas.");
			} else {
				player.unequip(ArmorType.HEAD);
				player.unequip(ArmorType.BODY);
				player.unequip(ArmorType.ARM);
				player.unequip(ArmorType.LEG);
				
				System.out.println("Toutes vos armures se trouvent dans votre inventaire.");
				return true;
			}
		} else if (className.equals(BasicCommand.WEAPON)) {
			if (type != null && !type.equals("")) {
				if (type.equals(BasicCommand.RIGHT)) {
					player.unequip(Hand.RIGHT);
				} else if (type.equals(BasicCommand.LEFT)) {
					player.unequip(Hand.LEFT);
				} else {
					System.out.println("Cette arme n'existe pas.");
					return false;
				}
				
				System.out.println("Votre arme se trouve dans votre inventaire.");
				return true;
			}
			
			player.unequip(Hand.BOTH);
			System.out.println("Vos armes se trouvent dans votre inventaire.");
			return true;
		}
		
		System.out.println("Ce type d'equipement n'existe pas.");
		return false;
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
	
	
	public boolean throwOut(PreparedCommand command) {
		if (!command.hasOptions()) {
			System.out.println("Jeter quel item ?");
			return false;
		}
		
		Player player = Player.getInstance();
		Game game = Game.getInstance();
		
		String itemName = command.getOptions()[0];
		Item toThrow = game.getItem(itemName, player.getBag());
		
		if (toThrow != null) {
			if (toThrow instanceof Equipment) {
				toThrow = game.getEquipment(itemName, false);
			}
			
			if (player.throwItem(toThrow)) {
				System.out.println("Vous vous etes debarasse de cet item.");
				return true;
			}
			
			System.out.println("Vous ne pouvez pas jeter cet item.");
			return false;
		}
		
		System.out.println("Cet item n'est pas dans votre inventaire.");	
		return false;
	}
	
	
	public void fight() {
		Game game = Game.getInstance();
		
		if (!game.getCurrentRoom().hasMonsters()) {
			System.out.println("Il n'y a pas de monstre dans cette salle.");
			return;
		}
		
		CombatCommandParser combatCommandParser = CombatCommandParser.getInstance();
		
		Player player = Player.getInstance();
		Monster opponent = game.getCurrentRoom().getMonsters().get(0);
		int turn = Dice.D10.roll() % 2;
		
		System.out.println("Vous affrontez : " + opponent.getName());
		
		
		
		// combat loop
		while (!player.isDead() && !opponent.isDead() && turn != CombatCommandParser.END) {
			if (turn == CombatCommandParser.PLAYER) {
				System.out.println("C'est votre tour.");
				combatCommandParser.printCommands();
				
				PreparedCommand command = combatCommandParser.readEntry();
				turn = combatCommandParser.execute(opponent, command);
			} else if (turn == CombatCommandParser.MONSTER && !opponent.isDead()) {
				System.out.println("C'est au tour du monstre.");
				try {
					Thread.sleep(1000); // simuling AI thinking
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				System.out.println("Le monstre vous attaque...");
				int hpBeforeAttack = player.getHp();
				boolean critical = opponent.attack(player);
				
				if (critical) System.out.println("Coup critique !");
				if (player.getHp() < hpBeforeAttack) {
					System.out.println("Vous avez perdu " + (hpBeforeAttack - player.getHp()) + "PV.");
					System.out.println("Il vous reste " + player.getHp() + "PV.");
				}
				else System.out.println("Son attaque echoue.");
				
				if (player.hasUnusableArmor()) {
					System.out.println("Vous avez une armure inutilisable");
					System.out.println("Votre defense est reduite.");
				}
				
				turn = CombatCommandParser.PLAYER;
			}
			System.out.println();
		}
		
		// combat results
		if (player.isDead()) {
			System.out.println("Vous etes mort.");
		} else if (opponent.isDead()) {
			System.out.println("Vous avez gagne contre ce monstre.");
			System.out.println("Vous gagnez un niveau.");
			
			player.levelUp();
			
			System.out.println(player.getDescription());
			System.out.println();
			
			if (!opponent.getLoots().isEmpty()) {
				System.out.println("Le monstre a laisse des items dans la salle.");
				opponent.giveLoots(game.getCurrentRoom());
			}
			
			game.getCurrentRoom().getMonsters().remove(opponent);
			opponent = null;
		} else {
			System.out.println("Vous avez pris la fuite");
			opponent.setHp(opponent.getMaxHp());
		}
	}
	
	
	public boolean save(PreparedCommand command) {
		if (!command.hasOptions()) {
			System.out.println("Entrez un nom pour la sauvegarde.");
			return false;
		}
		
		String name = command.getOptions()[0];
		
		if (SaveXMLWriter.getInstance().saveGame(name)) {
			System.out.println("Votre partie a été enregistrée dans " + name + ".");
			return true;
		}
		
		System.out.println("Un problème est survenu lors de la sauvegarde.");
		return false;
	}
	
}
