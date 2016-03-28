package fr.zork.commands.execution;

public class PreparedCommand {
	private final String word;
	private final String[] options;
	

	public PreparedCommand(String word, String[] options) {
		this.word = word;
		this.options = options;
	}


	public String getWord() {
		return word;
	}


	public String[] getOptions() {
		return options;
	}
	
	
	public boolean isUnknown() {
		return this.word == null;
	}
	
	
	public boolean hasOptions() {
		return this.options != null;
	}
	
	
	public int getOptionsLength() {
		return this.options.length;
	}

}
