package fr.zork;

import fr.zork.game.Game;

public class Zork {

	public static void main(String[] args) {
		Game game = Game.getInstance();
		
		while (!game.displayMenu());
		game.run();
	}

}
