package fr.zork.item;
import fr.zork.character.Player;
import fr.zork.item.Armor;
import fr.zork.item.Weapon;
import fr.zork.item.enums.ArmorType;
import fr.zork.item.enums.Hand;
import fr.zork.item.enums.WeaponType;

public class TestEquipmentDescription {

	public static void main(String[] args) {
		Player player = Player.getInstance();
				
		Weapon rightSword = new Weapon("épée bâtarde", 9, 13, 6, WeaponType.SWORD, Hand.RIGHT);
		Weapon leftSword = new Weapon("sabre laser", 15, 10, 1, WeaponType.SWORD, Hand.LEFT);
		Weapon katana = new Weapon("katana", 11, 20, 8, WeaponType.SWORD, Hand.BOTH);
		
		Armor helmet = new Armor("casque ailé", 6, 10, 5, ArmorType.HEAD, false);
		Armor body = new Armor("plastron bronze", 15, 13, 8, ArmorType.BODY, true);
		Armor leg  = new Armor("jambières or", 20, 20, 6, ArmorType.LEG, true);
		Armor hat = new Armor("chapeau", 2, 10, 1, ArmorType.HEAD, false);
		
		katana.setLifespawn(0);
		leg.setLifespawn(0);
		
		player.setName("Toto");
		player.getBag().add(rightSword);
		player.getBag().add(leftSword);
		player.getBag().add(katana);
		player.getBag().add(helmet);
		player.getBag().add(body);
		player.getBag().add(leg);
		player.getBag().add(hat);
		player.getBag().add(katana);
		player.getBag().add(katana);
		player.getBag().add(leg);
		player.getBag().add(leg);
		player.getBag().add(leg);
		
		System.out.println(player.getEquipmentListDescription());
	}
}
