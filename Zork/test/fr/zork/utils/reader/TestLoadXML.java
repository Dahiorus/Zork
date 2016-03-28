package fr.zork.utils.reader;
import fr.zork.game.Game;
import fr.zork.utils.reader.LoadXMLReader;

public class TestLoadXML {

	public static void main(String[] args) {
		LoadXMLReader.getInstance().loadGame("test");
		Game.getInstance().run();
	}

}
