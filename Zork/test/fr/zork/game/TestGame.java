package fr.zork.game;
import fr.zork.game.GameConsole;

public class TestGame {

	public static void main(String[] args) {
		GameConsole game = GameConsole.getInstance();
		while (!game.displayMenu());
		game.run();
	}

}
