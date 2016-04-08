package fr.zork.game;

import fr.zork.character.Player;
import fr.zork.item.Potion;
import fr.zork.item.Spell;

public class TestCast {

	public static void main(String[] args) {
		GameConsole game = GameConsole.getInstance();
		Player player = Player.getInstance();
		
		Potion potion = new Potion("potion", 15);
		Spell spell = new Spell("foudre", 50);
		
		player.getBag().add(potion);
		player.getBag().add(spell);
		
		while (!game.displayMenu());
		game.run();
	}

}
