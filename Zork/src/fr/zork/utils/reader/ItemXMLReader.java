package fr.zork.utils.reader;

import java.io.File;
import java.io.IOException;
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

import fr.zork.item.Armor;
import fr.zork.item.Item;
import fr.zork.item.Potion;
import fr.zork.item.Spell;
import fr.zork.item.Weapon;
import fr.zork.item.enums.ArmorType;
import fr.zork.item.enums.Hand;
import fr.zork.item.enums.WeaponType;

public class ItemXMLReader {
	private static File xmlItemFile, xmlUniqueItemFile;
	private static DocumentBuilder builder;
	private static Document document;
	
	private Map<String, Item> items, uniqueItems;
	
	private static class ItemXMLReaderHolder {
		private final static ItemXMLReader instance = new ItemXMLReader();
	}

	
	private ItemXMLReader() {
		String path = "resources/data/game/";
		
		xmlItemFile = new File(path + "items.xml");
		xmlUniqueItemFile = new File(path + "uniqueItems.xml");
		
		this.items = new HashMap<String, Item>();
		this.uniqueItems = new HashMap<String, Item>();
		
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	public static ItemXMLReader getInstance() {
		return ItemXMLReaderHolder.instance;
	}
	
	
	public Map<String, Item> getAllItems() {
		Map<String, Item> items = new HashMap<String, Item>(this.getItems());
		items.putAll(this.getUniqueItems());
		
		return items;
	}
	
	
	public Map<String, Item> getItems() {
		if (this.items.isEmpty()) {
			this.readPotions(xmlItemFile, this.items);
			this.readSpells(xmlItemFile, this.items);
			this.readArmors(xmlItemFile, this.items);
			this.readWeapons(xmlItemFile, this.items);
		}
		
		return this.items;
	}
	
	
	public Map<String, Item> getUniqueItems() {
		if (this.uniqueItems.isEmpty()) {
			this.readArmors(xmlUniqueItemFile, this.uniqueItems);
			this.readWeapons(xmlUniqueItemFile, this.uniqueItems);
		}
		
		return this.uniqueItems;
	}
	
	
	public List<Item> getPotions() {
		List<Item> potions = new ArrayList<Item>();
		
		if (this.items.isEmpty()) {
			this.getItems();
		}
		
		for (Item item : this.items.values()) {
			if (item instanceof Potion) potions.add(item);
		}
		
		return potions;
	}
	
	
	public List<Item> getSpells() {
		List<Item> spells = new ArrayList<Item>();
		
		if (this.items.isEmpty()) {
			this.getItems();
		}
		
		for (Item item : this.items.values()) {
			if (item instanceof Spell) spells.add(item);
		}
		
		return spells;
	}
	
	
	public List<Item> getArmors() {
		List<Item> armors = new ArrayList<Item>();
		
		if (this.items.isEmpty()) {
			this.getItems();
		}
		
		for (Item item : this.items.values()) {
			if (item instanceof Armor) armors.add(item);
		}
		
		return armors;
	}
	
	
	public List<Item> getWeapons() {
		List<Item> weapons = new ArrayList<Item>();
		
		if (this.items.isEmpty()) {
			this.getItems();
		}
		
		for (Item item : this.items.values()) {
			if (item instanceof Weapon) weapons.add(item);
		}
		
		return weapons;
	}
	
	
	private void readPotions(File xmlFile, Map<String, Item> map) {
		try {
			document = builder.parse(xmlFile);
			document.getDocumentElement().normalize();
			
			NodeList nodes = document.getElementsByTagName("potion");
			
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					
					String name = element.getAttribute("name").trim();
					int hpGain = Integer.parseInt(element.getAttribute("hpGain").trim());
					
					map.put(name, new Potion(name, hpGain));
				}
			}
		} catch (IOException | SAXException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	
	private void readSpells(File xmlFile, Map<String, Item> map) {
		try {
			document = builder.parse(xmlFile);
			document.getDocumentElement().normalize();
			
			NodeList nodes = document.getElementsByTagName("spell");
			
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					
					String name = element.getAttribute("name").trim();
					int damage = Integer.parseInt(element.getAttribute("damage").trim());
					
					map.put(name, new Spell(name, damage));
				}
			}
		} catch (IOException | SAXException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	private void readWeapons(File xmlFile, Map<String, Item> map) {
		try {
			document = builder.parse(xmlFile);
			document.getDocumentElement().normalize();
			
			NodeList nodes = document.getElementsByTagName("weapon");
			
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					
					String name = element.getAttribute("name").trim();
					int bonus = Integer.parseInt(element.getAttribute("bonus").trim());
					int lifespawn = Integer.parseInt(element.getAttribute("lifespawn").trim());
					int levelMin = Integer.parseInt(element.getAttribute("levelMin").trim());
					WeaponType type = WeaponType.valueOf(element.getElementsByTagName("type").item(0).getTextContent().trim());
					Hand hands = Hand.valueOf(element.getElementsByTagName("hands").item(0).getTextContent().trim());
					
					map.put(name, new Weapon(name, bonus, lifespawn, levelMin, type, hands));
				}
			}
		} catch (IOException | SAXException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
	
	
	private void readArmors(File xmlFile, Map<String, Item> map) {
		try {
			document = builder.parse(xmlFile);
			document.getDocumentElement().normalize();
			
			NodeList nodes = document.getElementsByTagName("armor");
			
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					
					String name = element.getAttribute("name").trim();
					int bonus = Integer.parseInt(element.getAttribute("bonus").trim());
					int lifespawn = Integer.parseInt(element.getAttribute("lifespawn").trim());
					int levelMin = Integer.parseInt(element.getAttribute("levelMin").trim());
					boolean isBig = Boolean.parseBoolean(element.getAttribute("isBig").trim());
					ArmorType type = ArmorType.valueOf(element.getElementsByTagName("type").item(0).getTextContent().trim());
					
					map.put(name, new Armor(name, bonus, lifespawn, levelMin, type, isBig));
				}
			}
		} catch (IOException | SAXException e) {
			e.printStackTrace();
			System.exit(-1);
		}	
	}
	
}
