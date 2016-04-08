package fr.zork;

import fr.zork.game.GameConsole;

public class Zork {

	public static void main(String[] args) {
		GameConsole game = GameConsole.getInstance();
		
		while (!game.displayMenu());
		game.run();
	}

}
