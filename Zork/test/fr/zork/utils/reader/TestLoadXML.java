package fr.zork.utils.reader;
import java.util.List;

import fr.zork.utils.reader.LoadXMLReader;
import fr.zork.world.Room;
import fr.zork.world.World;

public class TestLoadXML {

	public static void main(String[] args) {
		LoadXMLReader.getInstance().loadGame("test");
		
		List<Room> rooms = World.getInstance().getWorldMap();
		
		for (Room room : rooms) {
			System.out.println(room.getTreasures());
		}
	}

}
