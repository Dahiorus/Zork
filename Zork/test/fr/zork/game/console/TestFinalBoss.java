package fr.zork.game.console;

import fr.zork.character.Player;
import fr.zork.game.Game;
import fr.zork.item.Armor;
import fr.zork.item.Potion;
import fr.zork.item.Spell;
import fr.zork.item.Weapon;
import fr.zork.item.enums.ArmorType;
import fr.zork.item.enums.Hand;
import fr.zork.item.enums.WeaponType;
import fr.zork.world.World;

public class TestFinalBoss {

	// penser a commenter l'appel a Game.executeStartMenu()
	public static void main(String[] args) {
		Game game = GameConsole.getInstance();
		Player player = Player.getInstance();
		
		game.newGame(Game.EASY);
		game.setCurrentRoom(World.getInstance().getRoom("Etage de Zork"));
		
		for (int i = 1; i < 38; i++) {
			player.levelUp();
		}
		
		player.setHead(new Armor("heaume de chevalier", 24, 26, 15, ArmorType.HEAD, false));
		player.setBody(new Armor("plastron de chevalier", 34, 25, 15, ArmorType.BODY, false));
		player.setArm(new Armor("gantelets en plaques", 32, 24, 15, ArmorType.ARM, false));
		player.setLeg(new Armor("jambieres enchantees", 35, 32, 15, ArmorType.LEG, true));
		
		player.setRightHand(new Weapon("epee batarde", 70, 29, 15, WeaponType.SWORD, Hand.BOTH));
		player.setLeftHand(null);
		
		player.getBag().add(new Potion("potion majenta", 400));
		player.getBag().add(new Potion("potion or", 1000));
		player.getBag().add(new Potion("potion jaune", 250));
		player.getBag().add(new Potion("potion jaune", 250));
		player.getBag().add(new Potion("potion jaune", 250));
		player.getBag().add(new Potion("potion majenta", 400));
		player.getBag().add(new Spell("boule de feu", 120));
		player.getBag().add(new Potion("tempete", 150));
		
		game.run();
	}

}
