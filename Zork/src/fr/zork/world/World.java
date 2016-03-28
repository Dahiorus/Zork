package fr.zork.world;

import java.util.ArrayList;
import java.util.List;

public class World {
	private List<Room> worldMap;
	
	
	private static class WorldHolder {
		private final static World instance = new World();
	}
	
	
	private World() {
		this.worldMap = new ArrayList<Room>();
	}

	
	public static World getInstance() {
		return WorldHolder.instance;
	}
	
	
	public List<Room> getWorldMap() {
		return this.worldMap;
	}
	
	
	public void addRoom(Room room) {
		this.worldMap.add(room);
	}
	
	
	public Room getRoom(String name) {
		if (name == null) return null;
		
		for (Room room : this.worldMap) {
			if (room.getName().equals(name)) return room;
		}
		
		return null;
	}
}
