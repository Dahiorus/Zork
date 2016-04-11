package fr.zork.game.swing;

import fr.zork.game.Game;

public class TestGameSwing {

	public static void main(String[] args) {
		Game game = GameSwing.getInstance();
		game.createPlayer();
	}

}
