package fr.zork.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

import fr.zork.character.Monster;
import fr.zork.character.Player;
import fr.zork.character.enums.Level;
import fr.zork.commands.BasicCommand;
import fr.zork.commands.CombatCommand;
import fr.zork.commands.execution.PreparedCommand;
import fr.zork.commands.parsers.BasicCommandParser;
import fr.zork.commands.parsers.CombatCommandParser;
import fr.zork.item.Armor;
import fr.zork.item.Equipment;
import fr.zork.item.Item;
import fr.zork.item.Potion;
import fr.zork.item.Spell;
import fr.zork.item.Weapon;
import fr.zork.item.enums.ArmorType;
import fr.zork.item.enums.Hand;
import fr.zork.item.enums.WeaponType;
import fr.zork.utils.reader.LoadXMLReader;
import fr.zork.utils.reader.WorldXMLReader;
import fr.zork.utils.writer.SaveXMLWriter;
import fr.zork.world.Room;
import fr.zork.world.World;
import fr.zork.world.enums.Dice;
import fr.zork.world.enums.Exit;

public class Game {
	private static String startRoomName = "Entree du donjon";
	private static String dungeonBossRoom = "Etage de Zork";
	
	private static final int PLAYER = 0;
	private static final int MONSTER = 1;
	private static final int END = 2;
	
	private static Player player = Player.getInstance();
	private static World world = World.getInstance();
	
	private String difficulty;
	private int stageNumber;
	private Monster zork;
	private Room currentRoom, previousRoom, zorkStage;
	
	private static CombatCommandParser combatCommandParser = CombatCommandParser.getInstance();
	private static BasicCommandParser basicCommandParser = BasicCommandParser.getInstance();
	
	private static class GameHolder {
		private final static Game instance = new Game();
	}
	

	private Game() {}
	
	
	public Room getCurrentRoom() {
		return this.currentRoom;
	}
	
	
	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}
	
	
	public static Game getInstance() {
		return GameHolder.instance;
	}
	
	
	/**
	 * Créer un nouveau joueur pour le jeu.
	 * Demande à l'utilisateur d'entrer un nom pour le joueur.
	 */
	public void createPlayer() {
		System.out.println("Entrez un nom de joueur.");
		System.out.print("> ");
		
		String entryLine = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			entryLine = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		// set player attributes
		player.setName(entryLine.trim());
		player.setRightHand(new Weapon("epee longue", 16, 15, WeaponType.SWORD, Hand.RIGHT));
		player.setBody(new Armor("plastron cuir", 10, 15, ArmorType.BODY, false));
	}
	
	
	public void createWorld() {
		List<Room> rooms = WorldXMLReader.getInstance().getWorldMap(this.difficulty, this.stageNumber);
		
		for (Room room : rooms) {
			world.addRoom(room);
		}
		
		this.zorkStage = new Room(dungeonBossRoom);
		this.zorkStage.getMonsters().add(this.zork);
		
		Room room = world.getRoom(String.valueOf(this.stageNumber) + "eme etage");
		room.setExits(this.zorkStage, room.getNextRoom(Exit.EAST), room.getNextRoom(Exit.WEST));
		
		this.currentRoom = world.getRoom(startRoomName);
	}
	
	
	public boolean displayMenu() {
		System.out.println("--------------------  Zork MENU  -----------------------------------");
		System.out.println("-                                                                  -");
		System.out.println("-  * Nouvelle partie :    'nouveau [facile | normal | difficile]'  -");
		System.out.println("-  * Charger une partie : 'charger [nom_partie]'                   -");
		System.out.println("-                                                                  -");
		System.out.println("--------------------------------------------------------------------");
		System.out.println();
		System.out.print("> ");
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		boolean result = true;
		
		try {
			String entryLine = reader.readLine().trim();
			StringTokenizer tokenizer = new StringTokenizer(entryLine);
			
			if (tokenizer.hasMoreTokens()) {
				String response = tokenizer.nextToken();
				
				if (response.equals("nouveau")) {
					if (tokenizer.hasMoreTokens()) {
						String difficulty = tokenizer.nextToken();
						int zorkHp = 0, zorkPower = 0, zorkDefense = 0;
						
						switch (difficulty) {
							case "facile":
								zorkHp = 300;
								zorkPower = 89;
								zorkDefense = 50;
								this.stageNumber = 10;
								break;
							case "normal":
								zorkHp = 600;
								zorkPower = 136;
								zorkDefense = 78;
								this.stageNumber = 20;
								break;
							case "difficile":
								zorkHp = 999;
								zorkPower = 219;
								zorkDefense = 170;
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

							this.zork = new Monster("Maitre Zork", zorkHp, zorkPower, zorkDefense, Level.EXTREME);
							this.zork.setArmor(new Armor("Armure des ombres", 110, 1, ArmorType.BODY, true));
							this.zork.setWeapon(new Weapon("Epee des ombres", 120, 1, WeaponType.SWORD, Hand.BOTH));
							
							this.newGame();
						}
					} else {
						System.out.println("Entrez un niveau de difficulte.");
						System.out.println();
						result = false;
					}
				} else if (response.equals("charger")) {
					if (tokenizer.hasMoreTokens()) {
						String name = tokenizer.nextToken();
						
						System.out.println("Chargement de la partie '" + name + "'...");
						
						if (!LoadXMLReader.getInstance().loadGame(name)) {
							System.out.println("Cette partie n'existe pas.");
							System.out.println();
							result = false;
						}
					}
				} else {
					System.out.println("Cette option ne se trouve pas dans le menu.");
					System.out.println();
					result = false;
				}
			} else {
				System.out.println("Entrez une commande.");
				result = false;
			}

			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public void displayWelcome() {
		System.out.println("-----------------------------------------------------------");
		System.out.println("-                                                         -");
		System.out.println("-  Vous entrez dans le donjon du terrifiant Maitre Zork.  -");
		System.out.println("-                                                         -");
		System.out.println("-  Votre but, en tant qu'aventurier, est de parcourir ce  -");
		System.out.println("-  donjon et de vaincre Maitre Zork.                      -");
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
		System.out.print("> ");
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			String response = reader.readLine().trim();
			
			if (response.equalsIgnoreCase("oui")) {
				System.out.println("Entrez un nom pour votre sauvegarde :");
				System.out.print("> ");
				
				String name = reader.readLine().trim();
				if (SaveXMLWriter.getInstance().saveGame(name)) {
					System.out.println("Votre progression a ete sauvegarde dans " + name);
				}
			}
			
			System.out.println("Partie terminee.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean wins() {
		if (!this.currentRoom.equals(this.zorkStage)) return false;
		if (this.currentRoom.hasMonsters()) return false;
		if (player.isDead()) return false;
		
		return true;
	}
	
	
	public void newGame() {
		this.createPlayer();
		this.createWorld();
	}
	
	
	public void loadGame(final String name) {
		LoadXMLReader.getInstance().loadGame(name);
	}
	
	
	public void run() {
		this.displayWelcome();
		System.out.println();
		
		boolean end = false;
		
		while (!end && !player.isDead() && !this.wins()) {
			System.out.println(this.currentRoom.getDescription());
			System.out.println();
			basicCommandParser.printCommands();
			
			PreparedCommand command = basicCommandParser.readEntry();
			end = this.execute(command);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			
			System.out.println();
			System.out.println("  * * * * *  ");
			System.out.println();
		}
		
		if (end) {
			this.displayQuit();
		} else if (player.isDead()) {
			this.displayLose();
		} else if (this.wins()) {
			this.displayWin();
		}
		
		System.out.println();
	}
	
	
	public boolean execute(PreparedCommand command) {
		if (command.isUnknown()) {
			System.out.println("Cette action n'existe pas...");
			return false;
		}
		
		String word = command.getWord();
		boolean result = false;
		
		switch (word) {
			case BasicCommand.GO:
				this.go(command);
				break;
			case BasicCommand.BACK:
				this.goBack();
				break;
			case BasicCommand.LOOK:
				this.look(command);
				break;
			case BasicCommand.LOOT:
				this.loot(command);
				break;
			case BasicCommand.FIGHT:
				if (command.hasOptions()) System.out.println("Cette commande n'a pas d'option.");
				else this.fight();
				break;
			case BasicCommand.EQUIP:
				this.equip(command);
				break;
			case BasicCommand.UNEQUIP:
				this.unequip(command);
				break;
			case BasicCommand.USE:
				this.use(command);
				break;
			case BasicCommand.THROW:
				this.throwOut(command);
				break;
			case BasicCommand.SAVE:
				this.save(command);
				break;
			case BasicCommand.QUIT:
				if (command.hasOptions()) System.out.println("Cette commande n'a pas d'option.");
				else result = true;
				break;
			case BasicCommand.HELP:
				if (command.hasOptions()) System.out.println("Cette commande n'a pas d'option.");
				else basicCommandParser.printHelp();
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
		
		if (currentRoom.hasMonsters()) {
			System.out.println("Un monstre vous empeche d'avancer...");
			return;
		}
		
		String direction = command.getOptions()[0];
		Exit exit = Exit.find(direction);
		
		if (exit == null) {
			System.out.println("Cette direction est inconnue...");
			return;
		}
		
		Room nextRoom = currentRoom.getNextRoom(exit);
		
		if (nextRoom == null) {
			System.out.println("Il n'y a pas de porte dans cette direction.");
			return;
		}
		
		this.previousRoom = this.currentRoom;
		this.currentRoom = nextRoom;
		
		// trigger curse
		if (this.currentRoom.hasCurse()) {
			int damage = this.currentRoom.getCurse().getDamage();
			
			System.out.println("Cette salle est piegee !");
			System.out.println("Vous subissez " + damage + " blessures.");
			
			this.currentRoom.triggerCurse(player);
		}
	}
	
	
	public void goBack() {
		if (this.previousRoom == null) {
			System.out.println("Vous ne pouvez pas retourner en arriere.");
			return;
		}
		
		Room nextRoom = this.previousRoom;
		this.previousRoom = this.currentRoom;
		this.currentRoom = nextRoom;
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
				System.out.println(this.currentRoom.getDescription());
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
		Item toEquip = this.getItem(equipmentName, player.getBag());
		
		if (toEquip != null) {
			if (toEquip instanceof Equipment) {
				Equipment equipment = (Equipment) toEquip;
				
				if (player.equip(equipment)) {
					System.out.println("Vous vous etes equipe de : " + equipmentName);
					return true;
				}
				
				System.out.println("Vous ne pouvez pas vous equipe de cet equipement.");
			} else System.out.println("Cet item n'est pas un equipement.");
		} else System.out.println("Vous ne possedez pas cet equipement.");
		
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
		
		String potionName = command.getOptions()[0];
		Item toUse = this.getItem(potionName, player.getBag());
		
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
			player.throwItem(toThrow);
			System.out.println("Vous vous etes debarasse de cet item");
			return true;
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
		
		System.out.println("Vous affrontez : " + opponent.getName());
		
		// combat loop
		while (!player.isDead() && !opponent.isDead() && turn != END) {
			if (turn == PLAYER) {
				System.out.println("C'est votre tour.");
				combatCommandParser.printCommands();
				
				PreparedCommand command = combatCommandParser.readEntry();
				turn = this.executeCombat(opponent, command);
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
	}
	
	
	public int executeCombat(Monster monster, PreparedCommand command) {
		if (command.isUnknown()) {
			System.out.println("Cette action est inconnue.");
			return PLAYER;
		}
		
		String word = command.getWord();
		int nextTurn = PLAYER;
		
		switch (word) {
			case CombatCommand.ATTACK:
				if (command.hasOptions()) {
					System.out.println("Cette commande n'a pas d'option.");
					nextTurn = PLAYER;
				} else {
					this.attack(monster);
					nextTurn = MONSTER;
				}
				break;
			case CombatCommand.USE:
				nextTurn = this.use(command) ? MONSTER : PLAYER;
			case CombatCommand.CAST:
				nextTurn = this.cast(command, monster) ? MONSTER : PLAYER;
				break;
			case CombatCommand.LOOK:
				this.lookCombat(command, monster);
				nextTurn = PLAYER;
				break;
			case CombatCommand.EQUIP:
				this.equip(command);
				nextTurn = PLAYER;
				break;
			case CombatCommand.FLEE:
				if (command.hasOptions()) {
					System.out.println("Cette commande n'a pas d'option");
					nextTurn = PLAYER;
				} else {
					nextTurn = this.flee() ? END : MONSTER;
				}
				break;
			case CombatCommand.HELP:
				if (command.hasOptions()) System.out.println("Cette commande n'a pas d'option");
				else combatCommandParser.printHelp();
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
	
	
	public boolean lookCombat(PreparedCommand command, Monster monster) {
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
		System.out.println("Vous tentez de fuire...");
		
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
	
	
	private Item getItem(String name, List<Item> items) {
		if (name == null) return null;
		
		for (Item item : items) {
			if (item.getName().equals(name)) return item;
		}
		
		return null;
	}
}
