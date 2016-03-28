package fr.zork.world;
import fr.zork.world.enums.Dice;

public class TestDiceRoll {

	public static void main(String[] args) {
		Dice dice = Dice.D20;
		
		for (int n = 0; n < 20; n++) {
			int result = dice.roll();
			System.out.println(n + " result=" + result);
		}
	}

}
