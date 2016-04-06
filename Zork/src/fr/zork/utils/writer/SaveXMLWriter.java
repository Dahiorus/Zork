package fr.zork.utils.writer;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fr.zork.character.Monster;
import fr.zork.character.Player;
import fr.zork.game.Game;
import fr.zork.item.Equipment;
import fr.zork.item.Item;
import fr.zork.world.Room;
import fr.zork.world.World;

public class SaveXMLWriter {
	private static final String TARGET_DIRECTORY = "game/saves/";
	private static final String PLAYER_FILE = "player.xml";
	private static final String ROOM_FILE = "rooms.xml";
	
	private File directory, xmlPlayerFile, xmlRoomFile;
	
	private static DocumentBuilder builder;
	
	private static class SaveXMLWriterHolder {
		private static final SaveXMLWriter instance = new SaveXMLWriter();
	}
	
	
	private SaveXMLWriter() {
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	
	public static SaveXMLWriter getInstance() {
		return SaveXMLWriterHolder.instance;
	}
	
	
	public boolean saveGame(final String name) {
		this.directory = new File(TARGET_DIRECTORY + name);
		
		if (!this.directory.exists()) {
			this.directory.mkdir();
		}
		
		this.writePlayer(name);
		this.writeRooms(name);
		
		return this.directory.list().length > 0;
	}
	
	
	private void writePlayer(final String name) {
		Player player = Player.getInstance();
		Document document = builder.newDocument();
		
		this.xmlPlayerFile = new File(TARGET_DIRECTORY + name + "/" + PLAYER_FILE);
		
		if (this.xmlPlayerFile.exists()) this.xmlPlayerFile.delete();
		
		// root element "player"
		Element root = document.createElement("player");
		document.appendChild(root);
		
		// player attributes (name, level, hp, maxHp, power, defense)
		root.setAttribute("name", player.getName());
		root.setAttribute("level", String.valueOf(player.getLevel()));
		root.setAttribute("hp", String.valueOf(player.getHp()));
		root.setAttribute("maxHp", String.valueOf(player.getMaxHp()));
		root.setAttribute("power", String.valueOf(player.getPower()));
		root.setAttribute("defense", String.valueOf(player.getDefense()));
		
		// weapon attributes "rightHand", "leftHand"
		if (player.getRightHand() != null) {
			Element rightHand = document.createElement("rightHand");
			rightHand.appendChild(document.createTextNode(player.getRightHand().getName()));
			root.appendChild(rightHand);
			rightHand.setAttribute("lifespawn", String.valueOf(player.getRightHand().getLifespawn()));
		}
		
		if (player.getLeftHand() != null) {
			Element leftHand = document.createElement("leftHand");
			leftHand.appendChild(document.createTextNode(player.getLeftHand().getName()));
			root.appendChild(leftHand);
			leftHand.setAttribute("lifespawn", String.valueOf(player.getLeftHand().getLifespawn()));
		}
		
		// armor attributes "head", "body", "arm", "leg"
		if (player.getHead() != null) {
			Element head = document.createElement("head");
			head.appendChild(document.createTextNode(player.getHead().getName()));
			root.appendChild(head);
			head.setAttribute("lifespawn", String.valueOf(player.getHead().getLifespawn()));
		}
		
		if (player.getBody() != null) {
			Element body = document.createElement("body");
			body.appendChild(document.createTextNode(player.getBody().getName()));
			root.appendChild(body);
			body.setAttribute("lifespawn", String.valueOf(player.getBody().getLifespawn()));
		}
		
		if (player.getArm() != null) {
			Element arm = document.createElement("arm");
			arm.appendChild(document.createTextNode(player.getArm().getName()));
			root.appendChild(arm);
			arm.setAttribute("lifespawn", String.valueOf(player.getArm().getLifespawn()));
		}
		
		if (player.getLeg() != null) {
			Element leg = document.createElement("leg");
			leg.appendChild(document.createTextNode(player.getLeg().getName()));
			root.appendChild(leg);
			leg.setAttribute("lifespawn", String.valueOf(player.getLeg().getLifespawn()));
		}
		
		// bag attribute
		if (!player.getBag().isEmpty()) {
			Element bagElement = document.createElement("bag");
			root.appendChild(bagElement);
			
			for (Item item : player.getBag()) {
				Element itemElement = document.createElement("item");
				bagElement.appendChild(itemElement);
				itemElement.appendChild(document.createTextNode(item.getName()));
				
				if (item instanceof Equipment) {
					Equipment equipment = (Equipment) item;
					itemElement.setAttribute("lifespawn", String.valueOf(equipment.getLifespawn()));
				}
			}
		}
		
		// element "currentRoom"
		Element currentRoomElement = document.createElement("currentRoom");
		root.appendChild(currentRoomElement);
		currentRoomElement.appendChild(document.createTextNode(Game.getInstance().getCurrentRoom().getName()));
		
		
		// element "previousRoom"
		if (Game.getInstance().getPreviousRoom() != null) {
			Element previousRoomElement = document.createElement("previousRoom");
			root.appendChild(previousRoomElement);
			previousRoomElement.appendChild(document.createTextNode(Game.getInstance().getPreviousRoom().getName()));
		}
		
		document.getDocumentElement().normalize();
		
		// write the document into file
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(this.xmlPlayerFile);
			
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(source, result);
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	
	private void writeRooms(final String name) {
		World world = World.getInstance();
		Document document = builder.newDocument();
		
		this.xmlRoomFile = new File(TARGET_DIRECTORY + name + "/" + ROOM_FILE);
		
		if (this.xmlRoomFile.exists()) this.xmlRoomFile.delete();
		
		// root element "map"
		Element root = document.createElement("map");
		document.appendChild(root);
		
		// difficulty element "difficulty"
		Element difficulty = document.createElement("difficulty");
		root.appendChild(difficulty);
		difficulty.setTextContent(Game.getInstance().getDifficulty());
		
		// room elements "room"
		Element rooms = document.createElement("rooms");
		root.appendChild(rooms);
		
		for (Room room : world.getWorldMap()) {
			Element roomElement = document.createElement("room");
			rooms.appendChild(roomElement);
			
			// attribute "name"
			roomElement.setAttribute("name", room.getName());
			
			// element "curse"
			if (room.hasCurse()) {
				Element curse = document.createElement("curse");
				curse.setAttribute("damage", String.valueOf(room.getCurse().getDamage()));
				roomElement.appendChild(curse);
			}
			
			// element "treasures"
			if (!room.getTreasures().isEmpty()) {
				Element treasures = document.createElement("treasures");
				roomElement.appendChild(treasures);
				
				for (Item item : room.getTreasures()) {
					Element itemElement = document.createElement("item");
					itemElement.appendChild(document.createTextNode(item.getName()));
					treasures.appendChild(itemElement);
				}
			}
			
			// element "monsters"
			if (!room.getMonsters().isEmpty()) {
				Element monsters = document.createElement("monsters");
				roomElement.appendChild(monsters);
				
				for (Monster monster : room.getMonsters()) {
					Element monsterElement = document.createElement("monster");
					monsterElement.setAttribute("name", monster.getName());
					
					// element "loots"
					if (!monster.getLoots().isEmpty()) {
						Element loots = document.createElement("loots");
						monsterElement.appendChild(loots);
						
						for (Item loot : monster.getLoots()) {
							Element lootElement = document.createElement("loot");
							lootElement.appendChild(document.createTextNode(loot.getName()));
							loots.appendChild(lootElement);
						}
					}
					
					monsters.appendChild(monsterElement);
				}
			}
			
			document.getDocumentElement().normalize();
			
			// write the document into file
			try {
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(this.xmlRoomFile);
				
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				transformer.transform(source, result);
			} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
		}
	}

}
