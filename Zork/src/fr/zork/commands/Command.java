package fr.zork.commands;

public interface Command {
	
	public boolean isValid(String entry);
	
	public int getOptionLength(String word);
	
	public void printCommands();
	
	public void printHelp();
	
}
