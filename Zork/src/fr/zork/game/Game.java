package fr.zork.game;

import java.util.List;

import fr.zork.character.Monster;
import fr.zork.character.Player;
import fr.zork.character.enums.Level;
import fr.zork.commands.execution.PreparedCommand;
import fr.zork.commands.parsers.CommandParser;
import fr.zork.game.console.GameConsole;
import fr.zork.item.Armor;
import fr.zork.item.Equipment;
import fr.zork.item.Item;
import fr.zork.item.Weapon;
import fr.zork.item.enums.ArmorType;
import fr.zork.item.enums.Hand;
import fr.zork.item.enums.WeaponType;
import fr.zork.utils.reader.WorldXMLReader;
import fr.zork.world.Room;
import fr.zork.world.World;
import fr.zork.world.enums.Exit;

public abstract class Game {
	public static final String EASY   = "facile";
	public static final String NORMAL = "normal";
	public static final String HARD   = "difficile";
	
	public static final String NEW  = "nouveau";
	public static final String LOAD = "charger";
	public static final String QUIT = "quitter";
	
	public static final int PLAYER = 0;
	public static final int MONSTER = 1;
	public static final int END = 2;
	
	protected enum ZorkStats {
		EASY(500, 100, 80),
		NORMAL(1000, 175, 115),
		HARD(1500, 255, 170);
		
		private final int hp;
		private final int power;
		private final int defense;
		
		ZorkStats(final int hp, final int power, final int defense) {
			this.hp = hp;
			this.power = power;
			this.defense = defense;
		}
		
		public static ZorkStats getByDifficulty(final String difficulty) {
			if (difficulty == null) return null;
			
			ZorkStats stats = null;
			
			switch (difficulty) {
				case GameConsole.EASY:
					stats = ZorkStats.EASY;
					break;
				case GameConsole.NORMAL:
					stats = ZorkStats.NORMAL;
					break;
				case GameConsole.HARD:
					stats = ZorkStats.HARD;
					break;
			}
			
			return stats;
		}
	}
	
	protected static String startRoomName = "Entree du donjon";
	protected static String dungeonBossRoom = "Etage de Zork";
	
	protected static Player player = Player.getInstance();
	protected static World world = World.getInstance();
	
	protected static CommandParser commandParser;
	
	protected String difficulty;
	protected int stageNumber;
	protected Monster zork;
	protected Room currentRoom, previousRoom, zorkStage;
	

	protected Game() {}
	
	
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
	
	
	public String getDifficulty() {
		return difficulty;
	}
	
	
	public void setDifficulty(final String difficulty) {
		this.difficulty = difficulty;
	}
	
	
	/**
	 * <p>Read XML resources and instantiate the World.</p>
	 * <p>Depending on the difficulty, the world will be a tower with
	 * a predefined number of stage (10 if EASY, 20 if NORMAL, 30 if HARD).</p>
	 * <p>The player will be placed in the first room "Entree du donjon",
	 * Zork's stage will be placed in the North of the last tower's stage.</p>
	 */
	public void createWorld() {
		List<Room> rooms = WorldXMLReader.getInstance().getWorldMap(this.difficulty, this.stageNumber);
		
		for (Room room : rooms) {
			world.addRoom(room);
		}
		
		this.zorkStage = new Room(dungeonBossRoom);
		this.zorkStage.getMonsters().add(this.zork);
		
		Room room = world.getRoom(String.valueOf(this.stageNumber) + "eme etage");
		room.setExits(this.zorkStage, room.getNextRoom(Exit.EAST), room.getNextRoom(Exit.WEST));
		world.addRoom(this.zorkStage);
		
		this.currentRoom = world.getRoom(startRoomName);
	}
	
	
	/**
	 * <p>Create Zork, the final boss, depending on the difficulty.</p>
	 */
	public void createZork() {
		ZorkStats stats = ZorkStats.getByDifficulty(this.difficulty);
		
		this.zork = new Monster("Maitre Zork", stats.hp, 0, 0, Level.EXTREME);
		this.zork.setWeapon(new Weapon("Epee des ombres", stats.power, 1, 1, WeaponType.SWORD, Hand.BOTH));
		this.zork.setArmor(new Armor("Armure des ombres", stats.defense, 1, 1, ArmorType.BODY, true));
	}
	
	
	/**
	 * <p>Indicates if the player wins the game.</p>
	 * <p>The player wins the game if he or she is in Zork's stage and has defeated him.</p>
	 * 
	 * @return <code>true</code> if the player wins the game, <code>false</code> otherwise
	 */
	public boolean wins() {
		if (!this.currentRoom.equals(this.zorkStage)) return false;
		if (this.currentRoom.hasMonsters()) return false;
		if (player.isDead()) return false;
		
		return true;
	}
	
	
	/**
	 * Get an item with the specified name from the specified list.
	 * 
	 * @param name - the name of the item
	 * @param list - the list of items
	 * @return the item which has the specified name
	 */
	protected Item getItem(String name, List<Item> list) {
		if (name == null) return null;
		
		for (Item item : list) {
			if (item.getName().equals(name)) return item;
		}
		
		return null;
	}
	
	
	/**
	 * Get an equipment with the specified name from the player's bag.
	 * The returned equipment is usable or not depending on the specified
	 * boolean parameter.
	 *  
	 * @param name		- the name of the equipment
	 * @param isUsable	- indicates if the equipment is usable or not
	 * @return the equipment which has the specified name and has the specified usability
	 */
	protected Equipment getEquipment(String name, boolean isUsable) {
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
	
	
	public abstract void run();
	
	public abstract void createPlayer();
	
	public abstract void executeStartMenu();
	
	public abstract boolean newGame(final String difficulty);
	
	public abstract boolean loadGame(final String name);
	
	public abstract void displayStartMenu();
	
	public abstract void displayWelcome();
	
	public abstract void displayLose();

	public abstract void displayWin();
	
	public abstract void displayQuit();
	
	public abstract boolean execute(PreparedCommand command);
	
	public abstract boolean go(PreparedCommand command);
	
	public abstract void goBack();
	
	public abstract boolean look(PreparedCommand command);
	
	public abstract boolean loot(PreparedCommand command);
	
	public abstract boolean equip(PreparedCommand command);
	
	public abstract boolean unequip(PreparedCommand command);
	
	public abstract boolean use(PreparedCommand command);
	
	public abstract boolean throwOut(PreparedCommand command);
	
	public abstract void fight();
	
	public abstract int executeCombat(Monster monster, PreparedCommand command);
	
	public abstract void attack(Monster monster);
	
	public abstract boolean cast(PreparedCommand command, Monster monster);
	
	public abstract boolean look(PreparedCommand command, Monster monster);
	
	public abstract boolean flee();
	
	public abstract boolean save(PreparedCommand command);
	
	protected abstract String readLine();
	
}
