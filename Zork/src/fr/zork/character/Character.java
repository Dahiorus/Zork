package fr.zork.character;

public abstract class Character {
	protected String name;
	
	
	protected Character() {}
	
	
	protected Character(String name) throws IllegalArgumentException {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("name is empty");
		}
		
		this.name = name;
	}


	/**
	 * Get the name of this character.
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Set the name of this character.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * Returns a description of this Character, based on his attributes.
	 * 
	 * @return a description of this Character, based on his attributes
	 */
	public abstract String getDescription();
	
	
	@Override
	public String toString() {
		return "Character [name=" + name + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + name.hashCode();
		
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Character)) {
			return false;
		}
		
		Character other = (Character) obj;
		
		return this.name.equals(other.name);
	}
}
