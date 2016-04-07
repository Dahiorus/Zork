package fr.zork.commands.parsers;

import java.util.StringTokenizer;

import fr.zork.commands.Command;
import fr.zork.commands.execution.PreparedCommand;

public abstract class CommandParser {
	protected Command command;
	
	
	public CommandParser() {}

	
	public PreparedCommand parseInput(String entryLine) {
		if (entryLine == null) return new PreparedCommand(null, null);
		
		String cmdWord = null;
		String[] options = null;
		StringTokenizer tokenizer = new StringTokenizer(entryLine, " ");
		
		// get word
		if (tokenizer.hasMoreTokens()) cmdWord = tokenizer.nextToken();
		
		if (this.command.isValid(cmdWord)) {
			// get first options (word [option1 option2 ... optionN-1] optionN)
			int optionLength = this.command.getOptionLength(cmdWord);
			
			if (optionLength > 0) {
				options = new String[optionLength];
				
				for (int i = 1; i < optionLength; i++) {
					if (tokenizer.hasMoreTokens()) options[i-1] = tokenizer.nextToken();
				}
				
				// get last options (name of the item)
				String name = "";
				while (tokenizer.hasMoreTokens()) {
					name += tokenizer.nextToken() + " ";
				}
				options[options.length-1] = name.trim();
			}
		} else cmdWord = null;
		
		if (!this.checkOptions(options)) options = null;
		
		return new PreparedCommand(cmdWord, options);
	}
	
	
	public String getCommandsMessage() {
		return this.command.commandsMessage();
	}


	public String getHelpMessage() {
		return this.command.helpMessage();
	}
	
	
	private boolean checkOptions(String[] options) {
		if (options == null || options.length == 0) return false;
		
		for (String option : options) {
			if (option != null && !option.equals("")) return true;
		}
		
		return false;
	}
	
}
