package fr.zork.commands.parsers;

import fr.zork.commands.CombatCommand;

public class CombatCommandParser extends CommandParser {
		
	private static class CombatCommandParserHolder {
		private final static CombatCommandParser instance = new CombatCommandParser();
	}
	

	private CombatCommandParser() {
		this.command = new CombatCommand();
	}
	
	
	public static CombatCommandParser getInstance() {
		return CombatCommandParserHolder.instance;
	}

}
