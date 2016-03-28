package fr.zork.game;
import fr.zork.game.Game;

public class TestGame {

	public static void main(String[] args) {
		Game game = Game.getInstance();
		while (!game.displayMenu());
		game.run();
	}

}
