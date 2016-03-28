package fr.zork.commands;

public class CombatCommand implements Command {
	public static final String ATTACK = "attaquer";
	public static final String USE	  = "utiliser";
	public static final String CAST	  = "lancer";
	public static final String EQUIP  = "equiper";
	public static final String LOOK	  = "voir";
	public static final String FLEE	  = "fuir";
	public static final String HELP	  = "aide";
	
	public static final String BAG		  = "inventaire";
	public static final String EQUIPMENTS = "equipements";
	public static final String PLAYER	  = "joueur";
	public static final String MONSTERS   = "monstres";
	
	private enum KeyWord {
		ATTACK(CombatCommand.ATTACK, 0, "Lancer une attaque vers les monstres"),
		USE(CombatCommand.USE, 1, "[nom_potion] - Consommer une potion de soin"),
		CAST(CombatCommand.CAST, 1, "[nom_sort] - Lancer un sort"),
		EQUIP(CombatCommand.EQUIP, 1, "[nom_equipement] - S'équiper d'un équipement de l'inventaire"),
		LOOK(CombatCommand.LOOK, 1, "[inventaire | equipements | joueur | monstres] - Afficher l'inventaire, les équipements, les sorts disponibles, ou les monstres dans ce combat"),
		FLEE(CombatCommand.FLEE, 0, "Tenter de fuir le combat"),
		HELP(CombatCommand.HELP, 0, "Afficher cette aide.");
		
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

	public CombatCommand() {}

	@Override
	public boolean isValid(String entry) {
		if (entry == null) return false;
		
		for (KeyWord word : KeyWord.values()) {
			if (entry.equals(word.toString())) return true;
		}
		
		return false;
	}
	
	
	@Override
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
		System.out.println("Aide:\n");
		
		for (KeyWord word : KeyWord.values()) {
			System.out.println(word + " = " + word.help);
		}
		
		System.out.println();
	}

}
