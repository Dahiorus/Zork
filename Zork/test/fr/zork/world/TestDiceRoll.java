package fr.zork.world;
import fr.zork.world.enums.Dice;

public class TestDiceRoll {

	public static void main(String[] args) {
		Dice dice = Dice.D6;
		
		for (int n = 0; n < 50; n++) {
			int result = dice.roll();
			System.out.println(n + " result=" + result);
		}
	}

}
