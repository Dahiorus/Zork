package fr.zork;

import java.util.Scanner;

import fr.zork.game.Game;

public class Zork {

	public static void main(String[] args) {
		Game game = Game.getInstance();
		boolean stop = false;
		
		while (!stop) {
			while (!game.displayMenu());
			game.run();
			
			System.out.println("Voulez-vous recommencer la partie ? ('oui' pour accepter)");
			System.out.print("> ");
			
			Scanner in = new Scanner(System.in);
			String response = in.nextLine();
			
			if (response == null || !response.equalsIgnoreCase("oui")) {
				stop = true;
				in.close();
			}
		}
	}

}
