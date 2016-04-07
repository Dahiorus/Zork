package fr.zork.commands;
import fr.zork.commands.execution.PreparedCommand;
import fr.zork.commands.parsers.BasicCommandParser;
import fr.zork.commands.parsers.CombatCommandParser;
import fr.zork.commands.parsers.CommandParser;

public class TestCommand {

	public static void main(String[] args) {
		System.out.println("Test BasicCommandParser\n");
		
		CommandParser parser = BasicCommandParser.getInstance();
		PreparedCommand command = parser.parseInput("prendre potion verte");
		
		printCommand(command);
		
		System.out.println();
		
		System.out.println("Test CombatCommandParser\n");
		parser = CombatCommandParser.getInstance();
		command = parser.parseInput("lancer foudre");
		
		printCommand(command);
	}
	
	
	public static void printCommand(PreparedCommand command) {
		if (command.isUnknown()) {
			System.out.println("Unknown command");
		} else {
			System.out.println("word: " + command.getWord());
			
			if (command.hasOptions()) {
				for (int i = 0; i < command.getOptions().length; i++) {
					System.out.println("Option " + i + ": " + command.getOptions()[i]);
				}
			}
		}
	}

}
