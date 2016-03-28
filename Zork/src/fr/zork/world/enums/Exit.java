package fr.zork.world.enums;

public enum Exit {
	NORTH("nord"),
	WEST("ouest"),
	EAST("est");
	
	
	private final String direction;
	
	
	Exit(final String direction) {
		this.direction = direction;
	}

	
	public static Exit find(String direction) {
		if (direction == null) return null;
		
		for (Exit exit : Exit.values()) {
			if (exit.direction.equals(direction)) return exit;
		}
		
		return null;
	}
	
	
	public String toString() {
		return this.direction;
	}
}
