package fr.zork.item;

public abstract class Item implements Cloneable {
	protected String name;
	
	
	protected Item(String name) throws IllegalArgumentException {
		if (name == null || name.equals("")) throw new IllegalArgumentException("name is empty");
		
		this.name = name;
	}
	
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	@Override
	public Object clone() {
		Item item = null;
		try {
			item = (Item) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return item;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + this.name.hashCode();
		
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Item)) {
			return false;
		}
		
		Item other = (Item) obj;
		
		return this.name.equals(other.name);
	}
	
	
	public abstract String getDescription();
	
	public abstract String getItemType();
}
