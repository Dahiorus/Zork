package fr.zork.game.swing;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class GameWindow extends JFrame {
	private static final long serialVersionUID = -8707882858468805480L;
	
	private JPanel mainContainer = new JPanel();
	
	private JPanel textContainer = new JPanel();
	private JTextArea textZone = new JTextArea(32, 69);
	
	private JPanel commandContainer = new JPanel();
	private JTextField commandTextField = new JTextField();
	private JButton enterBtn = new JButton("Entrer");
	
	private String printedText = "";
	private String entryLine = "";
	
	
	public GameWindow(String title) throws HeadlessException {
		super(title);
		
		// JFrame basic configuration
		this.setTitle(title);
		this.setSize(800, 600);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		// add global container
		this.setContentPane(this.mainContainer);
		
		// add sub panels into main panel
		this.mainContainer.setLayout(new BoxLayout(this.mainContainer, BoxLayout.PAGE_AXIS));
		this.mainContainer.add(this.textContainer);
		this.mainContainer.add(this.commandContainer);
		
		// add container for text area
		JScrollPane scrollPane = new JScrollPane(this.textZone);
		this.textZone.setEditable(false);
		this.textContainer.add(scrollPane);
		this.textContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		// add container for command input field
		this.commandContainer.setLayout(new BoxLayout(this.commandContainer, BoxLayout.LINE_AXIS));
		this.commandContainer.add(this.commandTextField);
		this.commandContainer.add(this.enterBtn);
		this.commandContainer.setBorder(new EmptyBorder(5, 13, 10, 13));
		this.commandTextField.requestFocusInWindow();
		
		this.addEventListeners();
	}
	
	
	public void printText(String text) {
		StringBuilder builder = new StringBuilder(this.printedText);
		builder
			.append(text)
			.append("\n")
		;
		this.printedText = builder.toString();
		this.textZone.setText(this.printedText);
	}
	
	
	public void clear() {
		this.textZone.setText("");
		this.printedText = "";
	}
	
	
	public String getCommandText() {
		return this.commandTextField.getText();
	}
	
	
	public String getEntryLine() {
		return this.entryLine;
	}
	
	
	public void addEventListeners() {
		final GameWindow window = this;
		
		this.commandTextField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					window.entryLine = window.getCommandText();
					window.printText(">> " + window.entryLine);
					window.commandTextField.setText("");
				}
			}
		});
		
		this.enterBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				window.entryLine = window.getCommandText();
				window.printText(">> " + window.entryLine);
				window.commandTextField.setText("");
				window.commandTextField.requestFocusInWindow();
			}
		});
	}
	
	
	private String menuMessage() {
		StringBuilder builder = new StringBuilder();
		
		builder
			.append("----------------------------  Zork MENU  ----------------------------\n")
			.append("-                                                                   -\n")
			.append("-  * Nouvelle partie :    'nouveau [facile | normal | difficile]'   -\n")
			.append("-  * Charger une partie : 'charger [nom_partie]'                    -\n")
			.append("-                                                                   -\n")
			.append("---------------------------------------------------------------------\n")
		;
		
		return builder.toString();
	}
	
	
	private String welcomeMessage() {
		StringBuilder builder = new StringBuilder();
		
		builder
			.append("--------------------------------------------------------------------\n")
			.append("-                                                                  -\n")
			.append("-       Vous entrez dans la tour du terrifiant Maitre Zork.        -\n")
			.append("-                                                                  -\n")
			.append("-       Votre but, en tant qu'aventurier, est de parcourir ce      -\n")
			.append("-       donjon et de vaincre Maitre Zork.                          -\n")
			.append("-                                                                  -\n")
			.append("--------------------------------------------------------------------\n")
			.append("\n")
			.append("Dans ce jeu, vous devez entrer des commandes pour avancer à travers\n")
			.append("le donjon.\n")
			.append("Entrez la commande 'aide' si vous avez besoin d'aide.")
		;
		
		return builder.toString();
	}
	
}
