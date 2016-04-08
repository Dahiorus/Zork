package fr.zork.utils.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.zork.character.Monster;
import fr.zork.character.enums.Level;
import fr.zork.game.GameConsole;
import fr.zork.item.Armor;
import fr.zork.item.Item;
import fr.zork.item.Potion;
import fr.zork.item.Spell;
import fr.zork.item.Weapon;
import fr.zork.world.Curse;
import fr.zork.world.Room;
import fr.zork.world.enums.Dice;

public class WorldXMLReader {
	private Document curseDocument, exitDocument, roomDocument;
	private List<Room> worldMap;
	private Map<String, Room> rooms;
	private List<Curse> curses;
	
	private static class WorlXMLReaderHolder {
		private static final WorldXMLReader instance = new WorldXMLReader();
	}
	

	private WorldXMLReader() {
		String path = "data/game/";
		
		InputStream xmlCurseFile = WorldXMLReader.class.getClassLoader().getResourceAsStream(path + "curses.xml");
		InputStream xmlRoomFile = WorldXMLReader.class.getClassLoader().getResourceAsStream(path + "rooms.xml");
		InputStream xmlExitFile = WorldXMLReader.class.getClassLoader().getResourceAsStream(path + "exits.xml");
		
		this.worldMap = new ArrayList<Room>();
		this.rooms 	  = new HashMap<String, Room>();
		this.curses	  = new ArrayList<Curse>();
		
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			this.curseDocument = builder.parse(xmlCurseFile);
			this.exitDocument  = builder.parse(xmlExitFile);
			this.roomDocument  = builder.parse(xmlRoomFile);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	public static WorldXMLReader getInstance() {
		return WorlXMLReaderHolder.instance;
	}

	
	public List<Room> getWorldMap(final String difficulty, final int stageNumber) {
		if (this.worldMap.isEmpty()) {
			this.readCurses();
			this.readRooms(difficulty, stageNumber);
			this.readExits();
			
			this.worldMap.addAll(this.rooms.values());
		}
		
		return this.worldMap;
	}
	
	
	private void readCurses() {
		this.curseDocument.getDocumentElement().normalize();
		
		NodeList nodes = this.curseDocument.getElementsByTagName("curse");
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				
				int damage = Integer.parseInt(element.getAttribute("damage").trim());
				this.curses.add(new Curse(damage));
			}
		}
	}
	
	
	private void readRooms(final String difficulty, final int stageNumber) {
		int stages = 0;
		
		this.roomDocument.getDocumentElement().normalize();
		
		NodeList nodes = this.roomDocument.getElementsByTagName("room");
		
		for (int i = 0; i < nodes.getLength() && stages < stageNumber; i++) {
			Node node = nodes.item(i);
			
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				
				String name = element.getAttribute("name").trim();
				Room room = new Room(name);
				
				// setting curse in the room
				NodeList curseNodes = element.getElementsByTagName("curse");
				if (curseNodes.getLength() != 0) {
					boolean cursed = Boolean.parseBoolean(curseNodes.item(0).getTextContent());
					
					if (cursed) {
						Random random = new Random();
						int index = random.nextInt(this.curses.size());
						room.setCurse(this.curses.get(index));
					}
				}
				
				// setting random treasures
				if (difficulty.equals(GameConsole.EASY) || difficulty.equals(GameConsole.NORMAL)) {
					int nbTreasures = 0;
					
					// if "facile" then read <treasures> line in the XML file
					if (difficulty.equals(GameConsole.EASY)) {
						NodeList treasureNodes = element.getElementsByTagName("treasures");
						if (treasureNodes.getLength() != 0) {
							Node treasureNode = treasureNodes.item(0);
							
							if (treasureNode.getNodeType() == Node.ELEMENT_NODE) {
								Element treasureElement = (Element) treasureNode;
								nbTreasures = Integer.parseInt(treasureElement.getAttribute("number").trim());
							}
						}
					} else { // else random treasure number
						nbTreasures = Dice.D4.roll() - 1;
					}
					
					for (int j = 0; j < nbTreasures; j++) {
						List<Item> itemList = this.getRandomItemsByClass(this.chooseClass());
						int index = Dice.D100.roll() % itemList.size();
						room.getTreasures().add((Item) itemList.get(index).clone());
					}
				}
				
				// setting unique items
				NodeList uniqueNodes = element.getElementsByTagName("unique");
				
				if (uniqueNodes.getLength() != 0) {
					
					Node uniqueNode = uniqueNodes.item(0);
					if (uniqueNode.getNodeType() == Node.ELEMENT_NODE) {
						Element uniqueElement = (Element) uniqueNode;
						String itemName = uniqueElement.getTextContent();
						Item uniqueItem = ItemXMLReader.getInstance().getUniqueItems().get(itemName);
						
						room.getTreasures().add(uniqueItem);
					}
				}
				
				// setting random monsters
				int nbMonsters = 0;
				NodeList monsterNodes = element.getElementsByTagName("monsters");
				
				if (monsterNodes.getLength() != 0) {
					Node monsterNode = monsterNodes.item(0);
					
					if (monsterNode.getNodeType() == Node.ELEMENT_NODE) {
						Element monsterElement = (Element) monsterNode;
						Level roomLevel = Level.find(monsterElement.getAttribute("level").trim());
						
						if (difficulty.equals(GameConsole.HARD)) {
							nbMonsters = Dice.D6.roll();
						} else {
							nbMonsters = Integer.parseInt(monsterElement.getAttribute("number").trim());
						}
						
						List<Monster> monsterList = MonsterXMLReader.getInstance().getMonsters(roomLevel);
						
						for (int j = 0; j < nbMonsters; j++) {
							int index = (Dice.D100.roll() * Dice.D100.roll()) % monsterList.size();
							Monster monster = (Monster) monsterList.get(index).clone();
							
							// adding monster's loots
							for (int n = 0; n < monster.getNbLoots(); n++) {
								List<Item> itemList = this.getRandomItemsByClass(this.chooseClass());
								int m = Dice.D100.roll() % itemList.size();
								monster.getLoots().add((Item) itemList.get(m).clone());
							}
							
							room.getMonsters().add(monster);
						}
					}
				}
				
				// setting room boss
				NodeList bossNodes = element.getElementsByTagName("boss");
				if (bossNodes.getLength() != 0) {
					String bossName = bossNodes.item(0).getTextContent().trim();
					Map<String, Monster> bosses = MonsterXMLReader.getInstance().getBosses();
					
					Monster boss = bosses.get(bossName);
					if (boss != null) room.getMonsters().add(boss);
				}
				
				this.rooms.put(name, room);
				if (name.contains("etage")) stages++;
			} // END if (node.getNodeType() == Node.ELEMENT_NODE)
		} // END for (int i = 0; i < nodes.getLength(); i++)
	}
	
	
	private void readExits() {
		this.exitDocument.getDocumentElement().normalize();
		
		NodeList nodes = this.exitDocument.getElementsByTagName("exit");
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				
				// getting room to set
				String sourceName = element.getAttribute("source").trim();
				Room source = this.rooms.get(sourceName);
				
				if (source != null) {
					// getting room's exits
					Room north = null, west = null, east = null;
					
					NodeList northNodes = element.getElementsByTagName("north");
					if (northNodes.getLength() != 0) {
						String northName = northNodes.item(0).getTextContent().trim();
						north = this.rooms.get(northName);
					}
					
					NodeList westNodes = element.getElementsByTagName("west");
					if (westNodes.getLength() != 0) {
						String westName = westNodes.item(0).getTextContent().trim();
						west = this.rooms.get(westName);
					}
					
					NodeList eastNodes = element.getElementsByTagName("east");
					if (eastNodes.getLength() != 0) {
						String eastName = eastNodes.item(0).getTextContent().trim();
						east = this.rooms.get(eastName);
					}
					
					source.setExits(north, east, west);
				}
			} // END if (node.getNodeType() == Node.ELEMENT_NODE)
		} // END for (int i = 0; i < nodes.getLength(); i++)
	}
	
	
	private Class<? extends Item> chooseClass() {
		int choice = Dice.D4.roll();
		Class<? extends Item> className = null;
		
		switch (choice) {
			case 1:
				className = Potion.class;
				break;
			case 2:
				className = Spell.class;
				break;
			case 3:
				className = Armor.class;
				break;
			case 4:
				className = Weapon.class;
				break;
		}
		
		return className;
	}
	
	
	private List<Item> getRandomItemsByClass(Class<? extends Item> className) {
		if (className == null) return null;
		
		if (className.equals(Potion.class)) return ItemXMLReader.getInstance().getPotions();
		if (className.equals(Spell.class))  return ItemXMLReader.getInstance().getSpells();
		if (className.equals(Armor.class))  return ItemXMLReader.getInstance().getArmors();
		if (className.equals(Weapon.class)) return ItemXMLReader.getInstance().getWeapons();
		
		return null;
	}
	
}
