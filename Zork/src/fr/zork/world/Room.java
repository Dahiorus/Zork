package fr.zork.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.zork.character.Monster;
import fr.zork.character.Player;
import fr.zork.item.Item;
import fr.zork.world.enums.Exit;

public class Room {
	private final String name;
	private Curse curse;
	private List<Item> treasures;
	private List<Monster> monsters;
	private Map<Exit, Room> exits;

	
	public Room(String name) throws IllegalArgumentException {
		if (name == null || name.equals("")) throw new IllegalArgumentException("name is empty");
		
		this.name = name;
		this.treasures = new ArrayList<Item>();
		this.monsters = new ArrayList<Monster>();
		this.exits = new HashMap<Exit, Room>(3);
	}


	public Curse getCurse() {
		return curse;
	}


	public void setCurse(Curse curse) {
		this.curse = curse;
	}


	public List<Item> getTreasures() {
		return treasures;
	}


	public List<Monster> getMonsters() {
		return monsters;
	}


	public Map<Exit, Room> getExits() {
		return exits;
	}


	public String getName() {
		return name;
	}
	
	
	@Override
	public String toString() {
		return "Room [name=" + name + ", curse=" + curse + ", treasures=" + treasures + ", monsters=" + monsters
				+ ", exits=" + exits + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((curse == null) ? 0 : curse.hashCode());
		result = prime * result + ((exits == null) ? 0 : exits.hashCode());
		result = prime * result + ((monsters == null) ? 0 : monsters.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((treasures == null) ? 0 : treasures.hashCode());
		
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Room)) return false;
		
		Room other = (Room) obj;
		
		if (curse == null) {
			if (other.curse != null) return false;
		} else if (!curse.equals(other.curse)) return false;
		
		if (exits == null) {
			if (other.exits != null) return false;
		} else if (!exits.equals(other.exits)) return false;
		
		if (monsters == null) {
			if (other.monsters != null) return false;
		} else if (!monsters.equals(other.monsters)) return false;
		
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		
		if (treasures == null) {
			if (other.treasures != null) return false;
		} else if (!treasures.equals(other.treasures)) return false;
		
		return true;
	}


	public boolean hasCurse() {
		return this.curse != null;
	}
	
	
	public boolean hasMonsters() {
		return !this.monsters.isEmpty();
	}
	
	
	public boolean hasExit(Exit exit) {
		return this.exits.get(exit) != null;
	}
	
	
	public Room getNextRoom(Exit exit) {
		return this.exits.get(exit);
	}
	
	
	public void setExits(Room north, Room east, Room west) {
		this.exits.put(Exit.NORTH, north);
		this.exits.put(Exit.EAST, east);
		this.exits.put(Exit.WEST, west);
	}
	
	
	public void triggerCurse(Player player) {
		if (this.curse != null) {
			player.receiveDamage(this.curse.getDamage());
			this.curse = null;
		}
	}
	
	
	public String getDescription() {
		String description = "Vous vous trouvez dans : " + this.name + "\n";
		
		description += "Il y a " + this.monsters.size() + " monstre" + (this.monsters.size() > 1 ? "s\n" : "\n");
		description += "Il y a " + this.treasures.size() + " item" + (this.treasures.size() > 1 ? "s\n" : "\n");
		
		if (!this.treasures.isEmpty()) {
			for (Item treasure : this.treasures) {
				description += "  - " + treasure.getName() + "\n";
			}
		}
		
		description += "Sorties: ";
		for (Exit exit : this.exits.keySet()) {
			if (this.hasExit(exit)) description += exit + "  ";
		}
		
		return description.trim();
	}
	
	
	public void increaseMonstersStats() {
		for (Monster monster : this.monsters) {
			monster.setPower(monster.getPower() + 1);
			monster.setDefense(monster.getDefense() + 1);
		}
	}

}
