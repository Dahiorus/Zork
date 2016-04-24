package fr.zork.game.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

import fr.zork.character.Monster;
import fr.zork.commands.BasicCommand;
import fr.zork.commands.CombatCommand;
import fr.zork.commands.execution.PreparedCommand;
import fr.zork.commands.parsers.BasicCommandParser;
import fr.zork.commands.parsers.CombatCommandParser;
import fr.zork.game.Game;
import fr.zork.item.Equipment;
import fr.zork.item.Item;
import fr.zork.item.Potion;
import fr.zork.item.Spell;
import fr.zork.item.enums.ArmorType;
import fr.zork.item.enums.Hand;
import fr.zork.utils.reader.LoadXMLReader;
import fr.zork.utils.writer.SaveXMLWriter;
import fr.zork.world.Room;
import fr.zork.world.World;
import fr.zork.world.enums.Dice;
import fr.zork.world.enums.Exit;

public class GameConsole extends Game {
	private static BufferedReader reader;
	
	private static class GameHolder {
		private final static GameConsole instance = new GameConsole();
	}
	

	private GameConsole() {
		super();
		reader = new BufferedReader(new InputStreamReader(System.in));
	}


	public static GameConsole getInstance() {
		return GameHolder.instance;
	}
	
	
	public void run() {
		// display start menu
		this.executeStartMenu();
		System.out.println();
		
		this.displayWelcome();
		System.out.println();
		
		boolean end = false;
		commandParser = BasicCommandParser.getInstance();
		
		// game loop
		while (!end && !player.isDead() && !this.wins()) {
			System.out.println(this.currentRoom.getDescription());
			System.out.println();
			System.out.println(commandParser.getCommandsMessage());
			
			String entryLine = this.readLine();
			PreparedCommand command = commandParser.parseInput(entryLine);
			end = this.execute(command);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			
			System.out.println();
			System.out.println("    * * * * * * * * * *    ");
			System.out.println();
		}
		
		if (end) {
			this.displayQuit();
			System.exit(0);
		} else {
			if (player.isDead()) this.displayLose();
			else if (this.wins()) this.displayWin();
			
			System.out.println();
		}
	}
	
	
	/**
	 * Créer un nouveau joueur pour le jeu.
	 * Demande à l'utilisateur d'entrer un nom pour le joueur.
	 */
	public void createPlayer() {
		String entryLine = null;
		
		while (entryLine == null || entryLine.isEmpty()) {
			System.out.println("Entrez un nom de joueur.");
			entryLine = this.readLine();
		}
		
		player.newPlayer(entryLine.trim());
	}
	
	
	public void executeStartMenu() {
		boolean created = false;
		
		while (!created) {
			this.displayStartMenu();
			System.out.println();
			
			String entryLine = this.readLine();
			StringTokenizer tokenizer = new StringTokenizer(entryLine);
			
			if (tokenizer.hasMoreTokens()) {
				String response = tokenizer.nextToken();
				
				if (response.equals(NEW)) {
					if (tokenizer.hasMoreTokens()) {
						String difficulty = tokenizer.nextToken();
						created = this.newGame(difficulty.trim());
					} else {
						System.out.println("Entrez un niveau de difficulte.");
					}
				} else if (response.equals(LOAD)) {
					if (tokenizer.hasMoreTokens()) {
						String name = tokenizer.nextToken();
						created = this.loadGame(name.trim());
					} else {
						System.out.println("Entrez le nom d'une partie.");
					}
				} else if (response.equals(QUIT)) {
					System.out.println("Vous quittez le jeu.");
					System.exit(0);
				} else {
					System.out.println("Cette option ne se trouve pas dans le menu.");
					System.out.println();
					created = false;
				}
			} else {
				System.out.println("Entrez une commande.");
				created = false;
			}
			
			System.out.println();
		}
	}
	
	
	public boolean newGame(final String difficulty) {
		boolean result = true;
		
		switch (difficulty) {
			case EASY:
				this.stageNumber = 10;
				break;
			case NORMAL:
				this.stageNumber = 20;
				break;
			case HARD:
				this.stageNumber = 30;
				break;
			default:
				System.out.println("Quel niveau de difficulte ?");
				result = false;
				break;
		}
	
		if (result) {
			this.difficulty = difficulty;
			
			System.out.println("Nouvelle partie (" + this.difficulty + ")");
			System.out.println();
			this.createPlayer();
			this.createZork();
			this.createWorld();
		}
		
		return result;
	}
	
	
	public boolean loadGame(final String name) {
		if (!LoadXMLReader.getInstance().loadGame(name)) {
			System.out.println("Cette partie n'existe pas.");
			System.out.println();
			
			return false;
		}
		
		System.out.println("Chargement de la partie '" + name + "'...");
		
		return true;
	}
	
	
	public void displayStartMenu() {
		System.out.println("----------------------------  Zork MENU  ----------------------------");
		System.out.println("-                                                                   -");
		System.out.println("-  * Nouvelle partie :    'nouveau [facile | normal | difficile]'   -");
		System.out.println("-  * Charger une partie : 'charger [nom_partie]'                    -");
		System.out.println("-  * Quitter le jeu :     'quitter'                                 -");
		System.out.println("-                                                                   -");
		System.out.println("---------------------------------------------------------------------");
	}


	public void displayWelcome() {
		System.out.println("-----------------------------------------------------------");
		System.out.println("-                                                         -");
		System.out.println("-  Vous entrez dans la tour du terrifiant Maitre Zork.    -");
		System.out.println("-                                                         -");
		System.out.println("-  Votre but, en tant qu'aventurier, est de parcourir ce  -");
		System.out.println("-  donjon et de vaincre Maitre Zork.                      -");
		System.out.println("-                                                         -");
		System.out.println("-  Combattez les monstres sur votre chemin pour ramasser  -");
		System.out.println("-  des items et gagner de l'experience.                   -");
		System.out.println("-                                                         -");
		System.out.println("-----------------------------------------------------------");
		System.out.println();
		System.out.println("Dans ce jeu, vous devez entrer des commandes pour avancer");
		System.out.println("a travers le donjon.");
		System.out.println("Entrez la commande 'aide' si vous avez besoin d'aide.");
	}
	
	
	public void displayLose() {
		System.out.println("Vous vous etes battu vaillamment.");
		System.out.println("Mais le donjon de Maitre Zork a ete plus fort que vous.");
		System.out.println("Reposez en paix...");
	}
	
	
	public void displayWin() {
		System.out.println("Bravo !");
		System.out.println("Vous avez parcouru le donjon et defait le terrible Maitre Zork.");
		System.out.println("Vos exploits seront narres dans les quatre coins du monde.");
	}
	
	
	public void displayQuit() {
		System.out.println("Vous quittez la partie.");
		System.out.println();
		System.out.println("Voulez-vous sauvegarder votre progression ? ('oui' pour sauvegarder)");
		
		String response = this.readLine().trim();
		
		if (response != null && response.trim().equalsIgnoreCase("oui")) {			
			String name = null;
			while (name == null || name.isEmpty()) {
				System.out.println("Entrez un nom pour votre sauvegarde :");
				name = this.readLine();
			}
			
			if (SaveXMLWriter.getInstance().saveGame(name)) {
				System.out.println("Votre progression a ete sauvegarde dans " + name);
			}
		}
		
		System.out.println("Partie terminee.");
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
				else System.out.println(commandParser.getHelpMessage());
				break;
			default:
				System.out.println("Cette commande est inconnue.");
				break;
		}
		
		return result;
	}
	
	
	public boolean go(PreparedCommand command) {
		if (!command.hasOptions()) {
			System.out.println("Aller ou ?");
			return false;
		}
		
		if (this.currentRoom.hasMonsters()) {
			System.out.println("Un monstre vous empeche d'avancer...");
			return false;
		}
		
		String direction = command.getOptions()[0];
		Exit exit = Exit.find(direction);
		
		if (exit == null) {
			System.out.println("Cette direction est inconnue...");
			return false;
		}
		
		Room nextRoom = this.currentRoom.getNextRoom(exit);
		
		if (nextRoom == null) {
			System.out.println("Il n'y a pas de porte dans cette direction.");
			return false;
		}
		
		this.setPreviousRoom(this.currentRoom);
		this.setCurrentRoom(nextRoom);
		
		// trigger curse
		if (this.currentRoom.hasCurse()) {
			int damage = this.currentRoom.getCurse().getDamage();
			
			System.out.println("Cette salle est piegee !");
			System.out.println("Vous subissez " + damage + " blessures.");
			
			this.currentRoom.triggerCurse(player);
		}
		
		return true;
	}
	
	
	public void goBack() {
		if (this.getCurrentRoom() == null) {
			System.out.println("Vous ne pouvez pas retourner en arriere.");
			return;
		}
		
		Room nextRoom = this.previousRoom;
		this.setPreviousRoom(this.currentRoom);
		this.setCurrentRoom(nextRoom);
	}
	
	
	public boolean look(PreparedCommand command) {
		if (!command.hasOptions()) {
			System.out.println("Vous pouvez voir :");
			System.out.println("  joueur, inventaire, equipements, salle");
			return false;
		}
		
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
				System.out.println(GameConsole.getInstance().getCurrentRoom().getDescription());
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
		
		if (this.currentRoom.getTreasures().isEmpty()) {
			System.out.println("La salle est vide.");
			return false;
		}
		
		String itemName = command.getOptions()[0];
		Item toLoot = this.getItem(itemName, this.currentRoom.getTreasures());
		
		if (toLoot != null) {
			player.pickUpItem(toLoot, this.currentRoom);
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
		Equipment toEquip = this.getEquipment(equipmentName, true);
		
		if (toEquip != null) {
			if (player.equip(toEquip)) {
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
					System.out.println("L'armure a ete retire.");
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
				
				System.out.println("L'arme a ete retire.");
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
		
		String potionName = command.getOptions()[0];
		Item toUse = GameConsole.getInstance().getItem(potionName, player.getBag());
		
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
		
		String itemName = command.getOptions()[0];
		Item toThrow = this.getItem(itemName, player.getBag());
		
		if (toThrow != null) {
			if (toThrow instanceof Equipment) {
				toThrow = this.getEquipment(itemName, false);
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
		if (!this.currentRoom.hasMonsters()) {
			System.out.println("Il n'y a pas de monstre dans cette salle.");
			return;
		}
		
		Monster opponent = this.currentRoom.getMonsters().get(0);
		int turn = Dice.D10.roll() % 2;
		commandParser = CombatCommandParser.getInstance();
		
		System.out.println("Vous affrontez : " + opponent.getName());
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			Thread.currentThread().interrupt();
		}
		
		// combat loop
		while (!player.isDead() && !opponent.isDead() && turn != END) {
			if (turn == PLAYER) {
				System.out.println("C'est votre tour.");
				System.out.println(commandParser.getCommandsMessage());
				
				String entryLine = this.readLine();
				PreparedCommand preparedCommand = commandParser.parseInput(entryLine);
				turn = this.executeCombat(opponent, preparedCommand);
			} else if (turn == MONSTER && !opponent.isDead()) {
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
				
				turn = PLAYER;
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
			
			// each 5 levels, increase all monsters power and defense
			if (player.getLevel() % 5 == 0) {
				List<Room> rooms = World.getInstance().getWorldMap();
				
				for (Room room : rooms) {
					room.increaseMonstersStats();
				}
			}
			
			if (!opponent.getLoots().isEmpty()) {
				System.out.println("Le monstre a laisse des items dans la salle.");
				opponent.giveLoots(this.currentRoom);
			}
			
			this.currentRoom.getMonsters().remove(opponent);
			opponent = null;
		} else {
			System.out.println("Vous avez pris la fuite");
			opponent.setHp(opponent.getMaxHp());
		}
		
		commandParser = BasicCommandParser.getInstance();
	}
	
	
	public int executeCombat(Monster monster, PreparedCommand preparedCommand) {
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
				else System.out.println(commandParser.getHelpMessage());
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
	
	
	public boolean cast(PreparedCommand command, Monster monster) {
		if (!command.hasOptions()) {
			System.out.println("Lancer quel sort ?");
			return false;
		}
		
		String spellName = command.getOptions()[0];
		Item toCast = this.getItem(spellName, player.getBag());
		
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
	
	
	public boolean flee() {
		System.out.println("Vous tentez de fuir...");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		int monsterRoll = Dice.D8.roll();
		int playerRoll = Dice.D8.roll();
		
		if (playerRoll > monsterRoll) {
			System.out.println("Vous fuyez.");
			return true;
		}
		
		System.out.println("Vous n'y arrivez pas.");
		return false;
	}
	
	
	public boolean save(PreparedCommand command) {
		if (!command.hasOptions()) {
			System.out.println("Entrez un nom pour la sauvegarde.");
			return false;
		}
		
		String name = command.getOptions()[0];
		
		if (SaveXMLWriter.getInstance().saveGame(name)) {
			System.out.println("Votre partie a ete enregistree dans " + name + ".");
			return true;
		}
		
		System.out.println("Un probleme est survenu lors de la sauvegarde.");
		return false;
	}
	
	
	protected String readLine() {
		String entryLine = null;
		
		System.out.print("> ");
		
		try {
			entryLine = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return entryLine;
	}
	
}
