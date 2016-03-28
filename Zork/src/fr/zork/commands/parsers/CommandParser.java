package fr.zork.commands.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import fr.zork.commands.Command;
import fr.zork.commands.execution.PreparedCommand;

public abstract class CommandParser {
	protected Command command;
	protected BufferedReader reader;
	
	
	public CommandParser() {
		this.reader = new BufferedReader(new InputStreamReader(System.in));
	}

	
	public PreparedCommand readEntry() {
		String entryLine = null, cmdWord = null;
		String[] options = null;
		
		System.out.print("> ");
		
		try { // read line
			entryLine = this.reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
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
	
	
	public void printCommands() {
		this.command.printCommands();
	}


	public void printHelp() {
		this.command.printHelp();
	}
	
	
	private boolean checkOptions(String[] options) {
		if (options == null || options.length == 0) return false;
		
		for (String option : options) {
			if (option != null && !option.equals("")) return true;
		}
		
		return false;
	}
	
	
	protected void finalize() {
		try {
			this.reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
