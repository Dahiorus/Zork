package fr.zork.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

import fr.zork.character.Monster;
import fr.zork.character.Player;
import fr.zork.character.enums.Level;
import fr.zork.commands.execution.PreparedCommand;
import fr.zork.commands.parsers.BasicCommandParser;
import fr.zork.item.Armor;
import fr.zork.item.Equipment;
import fr.zork.item.Item;
import fr.zork.item.Weapon;
import fr.zork.item.enums.ArmorType;
import fr.zork.item.enums.Hand;
import fr.zork.item.enums.WeaponType;
import fr.zork.utils.reader.LoadXMLReader;
import fr.zork.utils.reader.WorldXMLReader;
import fr.zork.utils.writer.SaveXMLWriter;
import fr.zork.world.Room;
import fr.zork.world.World;
import fr.zork.world.enums.Exit;

public class Game {
	private static String startRoomName = "Entree du donjon";
	private static String dungeonBossRoom = "Etage de Zork";
	
	private static Player player = Player.getInstance();
	private static World world = World.getInstance();
	
	private String difficulty;
	private int stageNumber;
	private Monster zork;
	private Room currentRoom, previousRoom, zorkStage;
	
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
	
	
	public Room getPreviousRoom() {
		return this.previousRoom;
	}
	
	
	public void setPreviousRoom(Room previousRoom) {
		this.previousRoom = previousRoom;
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
		
		String entryLine;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			entryLine = reader.readLine();
			player.setName(entryLine.trim());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
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
	
	
	public void createZork(final int hp, final int power, final int defense) {
		this.zork = new Monster("Maitre Zork", hp, power, defense, Level.EXTREME);
		this.zork.setArmor(new Armor("Armure des ombres", 200, 1, 1, ArmorType.BODY, true));
		this.zork.setWeapon(new Weapon("Epee des ombres", 220, 1, 1, WeaponType.SWORD, Hand.BOTH));
	}
	
	
	public boolean displayMenu() {
		System.out.println("----------------------------  Zork MENU  ----------------------------");
		System.out.println("-                                                                   -");
		System.out.println("-  * Nouvelle partie :    'nouveau [facile | normal | difficile]'   -");
		System.out.println("-  * Charger une partie : 'charger [nom_partie]'                    -");
		System.out.println("-                                                                   -");
		System.out.println("---------------------------------------------------------------------");
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
						
						switch (difficulty) {
							case "facile":
								this.createZork(300, 89, 50);
								this.stageNumber = 10;
								break;
							case "normal":
								this.createZork(600, 136, 78);
								this.stageNumber = 20;
								break;
							case "difficile":
								this.createZork(999, 219, 170);
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
		BasicCommandParser parser = BasicCommandParser.getInstance();
		
		while (!end && !player.isDead() && !this.wins()) {
			System.out.println(this.currentRoom.getDescription());
			System.out.println();
			parser.printCommands();
			
			PreparedCommand command = parser.readEntry();
			end = parser.execute(command);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			
			System.out.println();
			System.out.println("    * * * * *    ");
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
	
	
	public Item getItem(String name, List<Item> list) {
		if (name == null) return null;
		
		for (Item item : list) {
			if (item.getName().equals(name)) return item;
		}
		
		return null;
	}
	
	
	public Equipment getEquipment(String name, boolean isUsable) {
		if (name == null) return null;
		
		for (Item item : player.getBag()) {
			if (item.getName().equals(name) && item instanceof Equipment) {
				Equipment equipment = (Equipment) item;
				
				if (isUsable) {
					if (equipment.isUsable()) return equipment;
				} else {
					if (!equipment.isUsable()) return equipment;
				}
			}
		}
		
		return null;
	}
	
}
