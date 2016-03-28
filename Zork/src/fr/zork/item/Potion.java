package fr.zork.item;

public class Potion extends Item {
	private final int hpGain;

	
	public Potion(String name, int hpGain) throws IllegalArgumentException {
		super(name);
		
		if (hpGain <= 0) throw new IllegalArgumentException("hpGain must be > 0");
		
		this.hpGain = hpGain;
	}

	
	/**
	 * @return the hpGain
	 */
	public int getHpGain() {
		return hpGain;
	}
	
	
	@Override
	public Object clone() {
		return (Potion) super.clone();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		result = prime * result + hpGain;
		
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		if (!(obj instanceof Potion)) return false;
		
		Potion other = (Potion) obj;
		
		if (hpGain != other.hpGain) return false;
		
		return true;
	}


	@Override
	public String toString() {
		return "Potion [hpGain=" + this.hpGain + ", name=" + this.name + "]";
	}
	
	
	@Override
	public String getDescription() {
		return this.name + ": " + this.getItemType() + " +" + this.hpGain + "PV";
	}


	@Override
	public String getItemType() {
		return "Potion";
	}
}
