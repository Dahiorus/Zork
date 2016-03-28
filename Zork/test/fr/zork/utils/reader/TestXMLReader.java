package fr.zork.utils.reader;
import java.util.List;

import fr.zork.utils.reader.WorldXMLReader;
import fr.zork.world.Room;

public class TestXMLReader {

	public static void main(String[] args) {
		List<Room> rooms = WorldXMLReader.getInstance().getWorldMap("difficile", 30);
		
		for (Room room : rooms) {
			System.out.println(room.getDescription());
			//System.out.println(room.getMonsters());
			//System.out.println(room.getTreasures());
			//System.out.println(room.getCurse());
			System.out.println();
		}
		
		System.out.println("Nombre de salles lues: " + rooms.size());
	}

}
