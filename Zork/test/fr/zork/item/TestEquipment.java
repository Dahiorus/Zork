package fr.zork.item;
import fr.zork.character.Player;
import fr.zork.item.Armor;
import fr.zork.item.Weapon;
import fr.zork.item.enums.ArmorType;
import fr.zork.item.enums.Hand;
import fr.zork.item.enums.WeaponType;

public class TestEquipment {

	public static void main(String[] args) {
		Player player = Player.getInstance();
		
		Weapon rightSword = new Weapon("épée bâtarde", 9, 13, 6, WeaponType.SWORD, Hand.RIGHT);
		Weapon leftSword = new Weapon("sabre laser", 15, 10, 1, WeaponType.SWORD, Hand.LEFT);
		Weapon katana = new Weapon("katana", 11, 20, 8, WeaponType.SWORD, Hand.BOTH);
		
		Armor helmet = new Armor("casque ailé", 6, 10, 5, ArmorType.HEAD, false);
		Armor body = new Armor("plastron bronze", 15, 13, 8, ArmorType.BODY, true);
		Armor leg  = new Armor("jambières or", 20, 20, 6, ArmorType.LEG, true);
		Armor hat = new Armor("chapeau", 2, 10, 1, ArmorType.HEAD, false);
		
		player.setName("Toto");
		player.getBag().add(rightSword);
		player.getBag().add(leftSword);
		player.getBag().add(katana);
		player.getBag().add(helmet);
		player.getBag().add(body);
		player.getBag().add(leg);
		player.getBag().add(hat);
		
		System.out.println(player.getEquipmentListDescription());
		System.out.println();
		
		player.equip(rightSword);
		System.out.println(player.getDescription());
		System.out.println();
		
		player.equip(leftSword);
		System.out.println(player.getDescription());
		System.out.println();
		
		player.equip(katana);
		System.out.println(player.getDescription());
		System.out.println();
		
		player.equip(helmet);
		player.equip(body);
		System.out.println(player.getDescription());
		System.out.println();
		
		player.equip(leg);
		player.equip(hat);
		System.out.println(player.getDescription());
		System.out.println();
		
		player.equip(leftSword);
		player.getLeftHand().setLifespawn(0);
		System.out.println(player.getDescription());
		System.out.println();
		
		System.out.println(player.getEquipmentListDescription());
	}
}
