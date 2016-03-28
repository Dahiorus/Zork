package fr.zork.commands;


public class BasicCommand implements Command {
	
	public static final String GO	   = "aller";
	public static final String BACK	   = "retourner";
	public static final String LOOK	   = "voir";
	public static final String LOOT	   = "prendre";
	public static final String FIGHT   = "combattre";
	public static final String USE	   = "utiliser";
	public static final String EQUIP   = "equiper";
	public static final String UNEQUIP = "enlever";
	public static final String THROW   = "jeter";
	public static final String SAVE	   = "sauvegarder";
	public static final String QUIT	   = "quitter";
	public static final String HELP	   = "aide";
	
	public static final String PLAYER	  = "joueur";
	public static final String BAG		  = "inventaire";
	public static final String EQUIPMENTS = "equipements";
	public static final String ROOM		  = "salle";
	
	public static final String WEAPON = "arme";
	public static final String ARMOR  = "armure";
	
	public static final String RIGHT = "droite";
	public static final String LEFT  = "gauche";
	
	private enum KeyWord {
		GO(BasicCommand.GO, 1, "[nord | ouest | est] - Aller dans la salle dans la direction donnee"),
		BACK(BasicCommand.BACK, 0, "Retourner dans la salle precedente"),
		LOOK(BasicCommand.LOOK, 1, "[joueur | inventaire | equipements | salle] - Afficher le joueur, l'inventaire, les equipements, ou la salle courante"),
		LOOT(BasicCommand.LOOT, 1, "[nom_item] - Ramasser un item dans la salle courante"),
		FIGHT(BasicCommand.FIGHT, 0, "Commencer un combat avec les monstres de la salle"),
		USE(BasicCommand.USE, 1, "[nom_potion] - Consommer une potion de soin"),
		EQUIP(BasicCommand.EQUIP, 1, "[nom_equipement] - S'équiper d'un équipement de l'inventaire"),
		UNEQUIP(BasicCommand.UNEQUIP, 2, "[armure (tete | bras | corps | jambes | _) || arme (droite | gauche | _)] - Retirer un equipement actuellement porte"),
		THROW(BasicCommand.THROW, 1, "[nom_item] - Jeter un item (use pour un equipement) du sac (supprime definitivement)"),
		SAVE(BasicCommand.SAVE, 1, "[nom] - Sauvegarder la partie"),
		QUIT(BasicCommand.QUIT, 0, "Quitter le jeu"),
		HELP(BasicCommand.HELP, 0, "Afficher cette aide.");
		
		private final String name;
		private final int optionNumber;
		private final String help;
		
		KeyWord(final String name, final int optionNumber, final String help) {
			this.name = name;
			this.optionNumber = optionNumber;
			this.help = help;
		}
		
		public String toString() {
			return this.name;
		}
		
		public static KeyWord find(String word) {
			if (word == null) return null;
			
			for (KeyWord keyWord : KeyWord.values()) {
				if (keyWord.name.equals(word)) return keyWord;
			}
			
			return null;
		}
	}

	
	public BasicCommand() {}

	
	@Override
	public boolean isValid(String entry) {
		if (entry == null) return false;
		
		for (KeyWord word : KeyWord.values()) {
			if (entry.equals(word.toString())) return true;
		}
		
		return false;
	}
	
	
	public int getOptionLength(String word) {
		return KeyWord.find(word).optionNumber;
	}

	
	@Override
	public void printCommands() {
		System.out.println("Commandes:");
		
		for (KeyWord word : KeyWord.values()) {
			System.out.print("  " + word);
		}
		
		System.out.println();
	}
	

	@Override
	public void printHelp() {
		System.out.println("Aide:");
		
		for (KeyWord word : KeyWord.values()) {
			System.out.println("  " + word + "  =  " + word.help);
		}
		
		System.out.println();
	}

}
