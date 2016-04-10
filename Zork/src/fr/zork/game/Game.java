package fr.zork.game;

import java.util.List;

import fr.zork.character.Monster;
import fr.zork.character.Player;
import fr.zork.character.enums.Level;
import fr.zork.commands.execution.PreparedCommand;
import fr.zork.commands.parsers.BasicCommandParser;
import fr.zork.commands.parsers.CombatCommandParser;
import fr.zork.game.console.GameConsole;
import fr.zork.item.Armor;
import fr.zork.item.Equipment;
import fr.zork.item.Item;
import fr.zork.item.Weapon;
import fr.zork.item.enums.ArmorType;
import fr.zork.item.enums.Hand;
import fr.zork.item.enums.WeaponType;
import fr.zork.utils.reader.LoadXMLReader;
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
		EASY(300, 75, 65),
		NORMAL(600, 175, 115),
		HARD(999, 195, 150);
		
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
	
	protected static BasicCommandParser basicCmdParser = BasicCommandParser.getInstance();
	protected static CombatCommandParser combatCmdParser = CombatCommandParser.getInstance();
	
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
	
	
	public void createWorld() {
		List<Room> rooms = WorldXMLReader.getInstance().getWorldMap(this.difficulty, this.stageNumber);
		
		for (Room room : rooms) {
			world.addRoom(room);
		}
		
		this.zorkStage = new Room(dungeonBossRoom);
		this.zorkStage.getMonsters().add(this.zork);
		
		Room room = world.getRoom(String.valueOf(this.stageNumber) + "eme etage");
		room.setExits(this.zorkStage, room.getNextRoom(Exit.EAST), room.getNextRoom(Exit.WEST));
		world.getWorldMap().add(this.zorkStage);
		
		this.currentRoom = world.getRoom(startRoomName);
	}
	
	
	public void createZork() {
		ZorkStats stats = ZorkStats.getByDifficulty(this.difficulty);
		
		this.zork = new Monster("Maitre Zork", stats.hp, 0, 0, Level.EXTREME);
		this.zork.setArmor(new Armor("Armure des ombres", stats.power, 1, 1, ArmorType.BODY, true));
		this.zork.setWeapon(new Weapon("Epee des ombres", stats.defense, 1, 1, WeaponType.SWORD, Hand.BOTH));
	}
	
	
	public boolean wins() {
		if (!this.currentRoom.equals(this.zorkStage)) return false;
		if (this.currentRoom.hasMonsters()) return false;
		if (player.isDead()) return false;
		
		return true;
	}
	
	
	public void newGame() {
		this.createPlayer();
		this.createZork();
		this.createWorld();
	}
	
	
	public void loadGame(final String name) {
		LoadXMLReader.getInstance().loadGame(name);
	}
	
	
	protected Item getItem(String name, List<Item> list) {
		if (name == null) return null;
		
		for (Item item : list) {
			if (item.getName().equals(name)) return item;
		}
		
		return null;
	}
	
	
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
	
	
	public abstract void createPlayer();
	
	public abstract boolean displayMenu();
	
	public abstract void displayWelcome();
	
	public abstract void displayLose();

	public abstract void displayWin();
	
	public abstract void displayQuit();
	
	public abstract void run();
	
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
