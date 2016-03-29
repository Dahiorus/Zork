package fr.zork.character;

import fr.zork.item.Armor;
import fr.zork.item.Weapon;
import fr.zork.item.enums.ArmorType;
import fr.zork.item.enums.Hand;
import fr.zork.item.enums.WeaponType;

public class TestPlayer {

	public static void main(String[] args) {
		Player player = Player.getInstance();
		
		player.setName("Joueur niveau 1");
		System.out.println(player.getDescription()); // PV: 200/200 - Force: 50 - Defense: 25
		System.out.println();
		
		for (int i = 1; i < 20; i++) {
			player.levelUp();
		}
		
		player.setName("Joueur niveau 20");
		player.setRightHand(new Weapon("rapiere", 38, 18, WeaponType.SWORD, Hand.RIGHT));
		player.setLeftHand(new Weapon("epee magique", 36, 18, WeaponType.SWORD, Hand.LEFT));
		player.setHead(new Armor("heaume", 15, 20, ArmorType.HEAD, false));
		player.setBody(new Armor("armure magique", 38, 35, ArmorType.BODY, true));
		player.setArm(new Armor("bracelets en or", 25, 26, ArmorType.ARM, false));
		player.setLeg(new Armor("jambieres en plaques", 29, 28, ArmorType.LEG, false));
		
		System.out.println(player.getDescription()); // PV: 390/390 - Force: 132 - Defense: 160
		System.out.println();
		
		for (int i = 20; i < 50; i++) {
			player.levelUp();
		}
		
		player.setName("Joueur niveau 50");
		player.setRightHand(new Weapon("Excalibur", 150, 100, WeaponType.SWORD, Hand.BOTH));
		player.setLeftHand(null);
		player.setHead(new Armor("Casque de Mithril", 30, 200, ArmorType.HEAD, false));
		player.setBody(new Armor("Plastron de Mithril", 50, 200, ArmorType.BODY, true));
		player.setArm(new Armor("Gantelets de Mithril", 40, 200, ArmorType.ARM, false));
		player.setLeg(new Armor("Jambieres de Mithril", 40, 200, ArmorType.LEG, false));
		
		System.out.println(player.getDescription()); // PV: 690/690 - Force: 268 - Defense: 273
		System.out.println();
	}

}
