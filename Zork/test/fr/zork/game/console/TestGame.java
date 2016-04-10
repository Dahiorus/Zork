package fr.zork.game.console;
import fr.zork.game.console.GameConsole;

public class TestGame {

	public static void main(String[] args) {
		GameConsole game = GameConsole.getInstance();
		while (!game.displayMenu());
		game.run();
	}

}
