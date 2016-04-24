package fr.zork.utils.reader;
import java.util.List;

import fr.zork.character.Monster;
import fr.zork.game.console.GameConsole;
import fr.zork.item.Item;
import fr.zork.utils.reader.WorldXMLReader;
import fr.zork.world.Room;

public class TestXMLReader {

	public static void main(String[] args) {
		List<Room> rooms = WorldXMLReader.getInstance().getWorldMap(GameConsole.EASY, 10);
		System.out.println("Nombre de salles lues: " + rooms.size());
		
		int nbMonsters = 0;
		
		for (Room room : rooms) {
			System.out.println(room.getName());
			System.out.println("Nombre de tresors : " + room.getTreasures().size());
			nbMonsters += room.getMonsters().size();
			
			for (Monster monster : room.getMonsters()) {
				System.out.println("  " + monster.getName() + " " + monster.getLevel());
				System.out.println("  HP=" + monster.getHp() + ", Force=" + monster.getEffectivePower() + ", Defense=" + monster.getEffectiveDefense());
				
				System.out.print("Loots=[");
				for (Item loot : monster.getLoots()) {
					System.out.print(loot.getName() + ", ");
				}
				System.out.println("]");
				
				System.out.println();
			}
			System.out.println();
		}
		
		System.out.println("Nombre de monstres total : " + nbMonsters);
	}

}
