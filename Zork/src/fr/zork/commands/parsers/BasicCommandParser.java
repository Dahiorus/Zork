package fr.zork.commands.parsers;

import fr.zork.commands.BasicCommand;

public class BasicCommandParser extends CommandParser {
	
	private static class BasicCommandParserHolder {
		private final static BasicCommandParser instance = new BasicCommandParser();
	}
	

	private BasicCommandParser() {
		this.command = new BasicCommand();
	}

	
	public static BasicCommandParser getInstance() {
		return BasicCommandParserHolder.instance;
	}
	
}
