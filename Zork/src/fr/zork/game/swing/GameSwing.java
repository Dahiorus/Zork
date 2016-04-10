package fr.zork.game.swing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fr.zork.character.Monster;
import fr.zork.commands.execution.PreparedCommand;
import fr.zork.game.Game;

public class GameSwing extends Game {
	private static GameWindow window;
	
	private static class GameSwingHolder {
		private static final GameSwing instance = new GameSwing();
	}

	public GameSwing() {
		super();
		
		window = new GameWindow("Zork");
		
		window.addCommandTextFieldListener(new KeyListener() {
			GameSwing instance = GameSwing.getInstance();
			
			@Override
			public void keyTyped(KeyEvent e) {
				String entryLine = instance.readLine();
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
	}
	
	
	public static GameSwing getInstance() {
		return GameSwingHolder.instance;
	}
	

	@Override
	public void createPlayer() {
		// TODO Auto-generated method stub

	}

	
	@Override
	public boolean displayMenu() {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public void displayWelcome() {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void displayLose() {
		// TODO Auto-generated method stub

	}
	

	@Override
	public void displayWin() {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void displayQuit() {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	
	@Override
	public boolean execute(PreparedCommand command) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean go(PreparedCommand command) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public void goBack() {
		// TODO Auto-generated method stub

	}

	
	@Override
	public boolean look(PreparedCommand command) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean loot(PreparedCommand command) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean equip(PreparedCommand command) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean unequip(PreparedCommand command) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	@Override
	public boolean use(PreparedCommand command) {
		// TODO Auto-generated method stub
		return false;
	}
	

	@Override
	public boolean throwOut(PreparedCommand command) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public void fight() {
		// TODO Auto-generated method stub

	}

	
	@Override
	public int executeCombat(Monster monster, PreparedCommand command) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	@Override
	public void attack(Monster monster) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public boolean cast(PreparedCommand command, Monster monster) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean look(PreparedCommand command, Monster monster) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean flee() {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean save(PreparedCommand command) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	protected String readLine() {
		return window.getCommandText();
	}

}
