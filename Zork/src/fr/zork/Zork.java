package fr.zork;

import fr.zork.game.Game;
import fr.zork.game.console.GameConsole;

public class Zork {

	public static void main(String[] args) {
		Game game = GameConsole.getInstance();
		
		while (!game.displayMenu());
		game.run();
	}

}
