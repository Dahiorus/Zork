package fr.zork.commands;

public interface Command {
	
	public boolean isValid(String entry);
	
	public int getOptionLength(String word);
	
	public String commandsMessage();
	
	public String helpMessage();
	
}
