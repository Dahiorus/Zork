package fr.zork.item;

import fr.zork.item.enums.Hand;
import fr.zork.item.enums.WeaponType;

public class TestClone {

	public static void main(String[] args) {
		Weapon object = new Weapon("katana", 100, 10, 6, WeaponType.SWORD, Hand.BOTH);
		Weapon clone = (Weapon) object.clone();
		
		if (object == clone) {
			System.out.println("Meme reference");
		} else if (object.equals(clone)) {
			System.out.println("Meme objet");
		} else {
			System.out.println("Objets differents");
		}
	}

}
