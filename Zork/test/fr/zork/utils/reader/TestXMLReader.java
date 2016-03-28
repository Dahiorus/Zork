package fr.zork.utils.reader;
import java.util.List;

import fr.zork.character.Monster;
import fr.zork.utils.reader.WorldXMLReader;
import fr.zork.world.Room;

public class TestXMLReader {

	public static void main(String[] args) {
		List<Room> rooms = WorldXMLReader.getInstance().getWorldMap("facile", 10);
		
		for (Room room : rooms) {
			//System.out.println(room.getDescription());
			//System.out.println(room.getMonsters());
			//System.out.println(room.getTreasures());
			//System.out.println(room.getCurse());
			
			for (Monster monster : room.getMonsters()) {
				System.out.println(monster.getLoots());
			}
			
			System.out.println();
		}
		
		System.out.println("Nombre de salles lues: " + rooms.size());
	}

}
