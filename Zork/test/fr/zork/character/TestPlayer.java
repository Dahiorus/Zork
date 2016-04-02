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
		System.out.println(player.getDescription()); // PV: 200/200 - Force: 52 - Defense: 35
		System.out.println();
		
		for (int i = 1; i < 10; i++) {
			player.levelUp();
		}
		
		player.setName("Joueur niveau 10");
		player.setRightHand(new Weapon("rapiere", 38, 18, 7, WeaponType.SWORD, Hand.RIGHT));
		player.setLeftHand(new Weapon("epee magique", 36, 18, 7, WeaponType.SWORD, Hand.LEFT));
		player.setHead(new Armor("heaume", 15, 20, 1, ArmorType.HEAD, false));
		player.setBody(new Armor("armure magique", 38, 35, 10, ArmorType.BODY, true));
		player.setArm(new Armor("bracelets en or", 25, 26, 10, ArmorType.ARM, false));
		player.setLeg(new Armor("jambieres en plaques", 29, 28, 7, ArmorType.LEG, false));
		
		System.out.println(player.getDescription()); // PV: 290/290 - Force: 131 - Defense: 150
		System.out.println();
		
		for (int i = 10; i < 30; i++) {
			player.levelUp();
		}
		
		player.setName("Joueur niveau 30");
		player.setRightHand(new Weapon("flamberge", 62, 26, 13, WeaponType.SWORD, Hand.BOTH));
		player.setLeftHand(null);
		player.setHead(new Armor("heaume de chevalier", 24, 26, 15, ArmorType.HEAD, false));
		player.setBody(new Armor("armure magique", 38, 35, 16, ArmorType.BODY, true));
		player.setArm(new Armor("plaques en os", 15, 20, 20, ArmorType.ARM, false));
		player.setLeg(new Armor("jambieres en plaques", 27, 28, 7, ArmorType.LEG, false));
		
		System.out.println("Avec stuff normal");
		System.out.println(player.getDescription()); // PV: 490/490 - Force: 179 - Defense: 187
		System.out.println();
		
		player.setRightHand(new Weapon("Excalibur", 150, 100, 20, WeaponType.SWORD, Hand.BOTH));
		player.setLeftHand(null);
		player.setHead(new Armor("Casque de Mithril", 30, 200, 20, ArmorType.HEAD, false));
		player.setBody(new Armor("Plastron de Mithril", 50, 200, 20, ArmorType.BODY, true));
		player.setArm(new Armor("Gantelets de Mithril", 40, 200, 20, ArmorType.ARM, false));
		player.setLeg(new Armor("Jambieres de Mithril", 40, 200, 20, ArmorType.LEG, false));
		
		System.out.println("Avec stuff legendaire");
		System.out.println(player.getDescription()); // PV: 490/490 - Force: 267 - Defense: 243
		System.out.println();
	}

}
