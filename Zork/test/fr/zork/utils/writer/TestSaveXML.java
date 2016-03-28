package fr.zork.utils.writer;
import fr.zork.character.Player;
import fr.zork.game.Game;
import fr.zork.item.Potion;
import fr.zork.utils.writer.SaveXMLWriter;

public class TestSaveXML {

	public static void main(String[] args) {
		Game.getInstance();
		
		Player player = Player.getInstance();
		
		player.getBag().add(new Potion("potion", 20));
		player.getBag().add(new Potion("potion+", 40));
		player.getBag().add(new Potion("potionX", 60));
		
		SaveXMLWriter.getInstance().saveGame("test");
	}

}
