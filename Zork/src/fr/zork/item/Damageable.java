package fr.zork.item;

public interface Damageable {
	
	public int getLifespawn();
	
	public void setLifespawn(int lifespawn);
	
	public void decrementLifespawn();
	
	public boolean isUsable();
}
