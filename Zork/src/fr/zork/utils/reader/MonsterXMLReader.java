package fr.zork.utils.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import fr.zork.item.Armor;
import fr.zork.item.Item;
import fr.zork.item.Potion;
import fr.zork.item.Spell;
import fr.zork.item.Weapon;
import fr.zork.world.enums.Dice;

public class MonsterXMLReader {
	private Document monsterDocument, bossDocument;
	private Map<String, Monster> monsters, bosses;
	
	private static class MonsterXMLReaderHolder {
		private final static MonsterXMLReader instance = new MonsterXMLReader();
	}
	
	
	private MonsterXMLReader() {
		String path = "data/game/";
		
		InputStream monsterFile = MonsterXMLReader.class.getClassLoader().getResourceAsStream(path + "monsters.xml");
		InputStream bossFile	= MonsterXMLReader.class.getClassLoader().getResourceAsStream(path + "boss.xml");
		
		this.monsters = new HashMap<String, Monster>();
		this.bosses   = new HashMap<String, Monster>();
		
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			this.monsterDocument = builder.parse(monsterFile);
			this.bossDocument	 = builder.parse(bossFile);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	public static MonsterXMLReader getInstance() {
		return MonsterXMLReaderHolder.instance;
	}
	
	
	public Map<String, Monster> getAllMonsters() {
		Map<String, Monster> monsters = new HashMap<String, Monster>(this.getMonsters());
		monsters.putAll(this.getBosses());
		
		return monsters;
	}
	
	
	public Map<String, Monster> getMonsters() {
		if (this.monsters.isEmpty()) {
			this.readMonsters(this.monsterDocument, this.monsters);
		}
		
		return this.monsters;
	}
	
	
	public List<Monster> getMonsters(Level level) {
		List<Monster> monsters = new ArrayList<Monster>();
		
		if (this.monsters.isEmpty()) {
			this.readMonsters(this.monsterDocument, this.monsters);
		}
		
		for (Monster monster : this.monsters.values()) {
			if (monster.getLevel() == level) monsters.add(monster);
		}
		
		return monsters;
	} 
	
	
	public Map<String, Monster> getBosses() {
		if (this.bosses.isEmpty()) {
			this.readMonsters(this.bossDocument, this.bosses);
		}
		
		return this.bosses;
	}

	
	private void readMonsters(Document document, Map<String, Monster> map) {
		Map<String, Item> uniqueItems = ItemXMLReader.getInstance().getUniqueItems();
		Map<String, Item> allItems = ItemXMLReader.getInstance().getAllItems();

		document.getDocumentElement().normalize();
		
		NodeList nodes = document.getElementsByTagName("monster");
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				
				// creating a monster from read data
				String name = element.getAttribute("name").trim();
				int maxHp = Integer.parseInt(element.getAttribute("maxHp").trim());
				int power = Integer.parseInt(element.getAttribute("power").trim());
				int defense = Integer.parseInt(element.getAttribute("defense").trim());
				Level level = Level.valueOf(element.getAttribute("level").trim());
				
				Monster monster = new Monster(name, maxHp, power, defense, level);
				
				// adding weapon and armor to the monster
				NodeList weaponNodes = element.getElementsByTagName("weapon");
				if (weaponNodes.getLength() != 0) {
					String weaponName = weaponNodes.item(0).getTextContent().trim();
					
					// Retrieving Weapon from name
					Item item = allItems.get(weaponName);
					if (item != null) {
						if (item instanceof Weapon) {
							Weapon weapon = (Weapon) item;
							monster.setWeapon(weapon);
						}
					}
				}
				
				NodeList armorNodes = element.getElementsByTagName("armor");
				if (armorNodes.getLength() != 0) {
					String armorName = armorNodes.item(0).getTextContent().trim();
					
					// retrieving Armor from name
					Item item = allItems.get(armorName);
					if (item != null) {
						if (item instanceof Armor) {
							Armor armor = (Armor) item;
							monster.setArmor(armor);
						}
					}
				}
				
				// adding random loots to the monster
				int nbLoots = 0;
				
				if (document.equals(this.monsterDocument)) {
					nbLoots = Dice.D4.roll();
				} else {
					NodeList lootNodes = element.getElementsByTagName("loots");
					
					if (lootNodes.getLength() != 0) {
						Node lootNode = lootNodes.item(0);
						
						if (lootNode.getNodeType() == Node.ELEMENT_NODE) {
							Element lootElement = (Element) lootNode;
							nbLoots = Integer.parseInt(lootElement.getAttribute("number").trim());
						}
					}
				}
				
				for (int j = 0; j < nbLoots; j++) {
					List<Item> itemList = this.getRandomItemsByClass(this.chooseClass());
					int index = Dice.D100.roll() % itemList.size();
					monster.getLoots().add((Item) itemList.get(index).clone());
				}
				
				// setting unique items loot to the monster's loots
				NodeList uniqueNodes = element.getElementsByTagName("unique");
				for (int k = 0; k < uniqueNodes.getLength(); k++) {
					String itemName = uniqueNodes.item(k).getTextContent().trim();
					
					// retrieving unique item from name
					Item item = uniqueItems.get(itemName);
					if (item != null) monster.getLoots().add(item);
				}
				
				map.put(name, monster); // adding the new created monster
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
		if (className.equals(Spell.class)) return ItemXMLReader.getInstance().getSpells();
		if (className.equals(Armor.class)) return ItemXMLReader.getInstance().getArmors();
		if (className.equals(Weapon.class)) return ItemXMLReader.getInstance().getWeapons();
		
		return null;
	}

}
