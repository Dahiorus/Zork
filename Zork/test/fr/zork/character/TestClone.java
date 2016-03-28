package fr.zork.character;

import fr.zork.character.enums.Level;
import fr.zork.item.Potion;
import fr.zork.item.Weapon;
import fr.zork.item.enums.Hand;
import fr.zork.item.enums.WeaponType;

public class TestClone {

	public static void main(String[] args) {
		Monster object = new Monster("Dragon", 500, 80, 60, Level.HARD);
		object.setWeapon(new Weapon("griffes", 50, 100, WeaponType.SWORD, Hand.BOTH));
		
		Monster clone = (Monster) object.clone();
		
		if (object == clone) {
			System.out.println("Meme reference");
		} else if (object.equals(clone)) {
			System.out.println("Meme objet");
		} else {
			System.out.println("Objets differents");
		}
		
		object.getLoots().add(new Potion("potion", 10));
		
		System.out.println(object.getLoots() == clone.getLoots());
	}
	
}
