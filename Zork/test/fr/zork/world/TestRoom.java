package fr.zork.world;
import fr.zork.character.Monster;
import fr.zork.item.Potion;
import fr.zork.world.Room;
import fr.zork.world.enums.Exit;

public class TestRoom {

	public static void main(String[] args) {
		Room enter = new Room("Entrée");
		Room north = new Room("Pièce nord");
		Room west = new Room("Pièce ouest");
		
		enter.setExits(north, null, west);
		enter.getMonsters().add(new Monster("gluant", 15, 3, 5, null));
		enter.getTreasures().add(new Potion("potion", 30));
		
		System.out.println(enter.getDescription());
		System.out.println();
		System.out.println(enter.getNextRoom(Exit.NORTH).getDescription());
		System.out.println();
		
		System.out.println(enter.getMonsters().get(0).getDescription());
	}
}
