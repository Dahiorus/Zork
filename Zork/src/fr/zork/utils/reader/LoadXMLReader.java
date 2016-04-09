package fr.zork.utils.reader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
import fr.zork.character.Player;
import fr.zork.game.console.GameConsole;
import fr.zork.item.Armor;
import fr.zork.item.Equipment;
import fr.zork.item.Item;
import fr.zork.item.Weapon;
import fr.zork.world.Curse;
import fr.zork.world.Room;
import fr.zork.world.World;

public class LoadXMLReader {
	private static final String SOURCE_DIRECTORY = ".game/saves/";
	private static final String PLAYER_FILE = "player.xml";
	private static final String ROOM_FILE = "rooms.xml";
	
	private File directory, xmlPlayerFile, xmlRoomFile;
	private InputStream exitFile;
	private static DocumentBuilder builder;
	
	private Map<String, Room> rooms;
	
	private static class LoadXMLReaderHolder {
		private static final LoadXMLReader instance = new LoadXMLReader();
	}
	

	private LoadXMLReader() {
		this.rooms = new HashMap<String, Room>();
		this.exitFile = LoadXMLReader.class.getClassLoader().getResourceAsStream("data/game/exits.xml");
		
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	
	public static LoadXMLReader getInstance() {
		return LoadXMLReaderHolder.instance;
	}
	
	
	public boolean loadGame(final String name) {
		this.directory = new File(SOURCE_DIRECTORY + name);
		
		if (!this.directory.exists()) return false;
		
		this.readRooms(name);
		this.readExits(name);
		this.readPlayer(name);
		
		World.getInstance().getWorldMap().addAll(this.rooms.values());
		
		return true;
	}
	
	
	private void readRooms(final String name) {
		this.xmlRoomFile = new File(SOURCE_DIRECTORY + name + "/" + ROOM_FILE);
		
		Map<String, Item> items = ItemXMLReader.getInstance().getAllItems();
		Map<String, Monster> monsters = MonsterXMLReader.getInstance().getAllMonsters();
		
		try {
			Document document = builder.parse(this.xmlRoomFile);
			document.getDocumentElement().normalize();
			
			// reading difficulty element
			NodeList difficultyNodes = document.getElementsByTagName("difficulty");
			if (difficultyNodes.getLength() != 0) {
				Node difficultyNode = difficultyNodes.item(0);
				String difficulty = difficultyNode.getTextContent();
				
				if (difficulty != null) {
					GameConsole.getInstance().setDifficulty(difficulty.trim());
					GameConsole.getInstance().createZork();
				}
			}
			
			// reading room elements
			NodeList roomNodes = document.getElementsByTagName("room");
			
			for (int i = 0; i < roomNodes.getLength(); i++) {
				Node roomNode = roomNodes.item(i);
				
				if (roomNode.getNodeType() == Node.ELEMENT_NODE) {
					Element roomElement = (Element) roomNode;
					
					// creating room from data
					String roomName = roomElement.getAttribute("name");
					Room room = new Room(roomName);
					
					//  setting room curse
					NodeList curseNodes = roomElement.getElementsByTagName("curse");
					
					if (curseNodes.getLength() != 0) {
						Node curseNode = curseNodes.item(0);
						
						if (curseNode.getNodeType() == Node.ELEMENT_NODE) {
							Element curseElement = (Element) curseNode;
							
							int curseDamage = Integer.parseInt(curseElement.getAttribute("damage"));
							Curse curse = new Curse(curseDamage);
							
							room.setCurse(curse);
						}
					}
					
					// setting treasures
					NodeList treasureNodes = roomElement.getElementsByTagName("treasures");
					
					if (treasureNodes.getLength() != 0) {
						Node treasureNode = treasureNodes.item(0);
						
						if (treasureNode.getNodeType() == Node.ELEMENT_NODE) {
							Element treasureElement = (Element) treasureNode;
							NodeList itemNodes = treasureElement.getElementsByTagName("item");
							
							for (int j = 0; j < itemNodes.getLength(); j++) {
								Node itemNode = itemNodes.item(j);
								
								if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
									Element itemElement = (Element) itemNode;
									String itemName = itemElement.getTextContent();
									Item item = items.get(itemName);
									
									if (item != null) room.getTreasures().add((Item) item.clone());
								}
							}
						}
					}
					
					// setting monsters
					NodeList monsterNodes = roomElement.getElementsByTagName("monster");
					
					for (int j = 0; j < monsterNodes.getLength(); j++) {
						Node monsterNode = monsterNodes.item(j);
						
						if (monsterNode.getNodeType() == Node.ELEMENT_NODE) {
							Element monsterElement = (Element) monsterNode;
							String monsterName = monsterElement.getAttribute("name");
							Monster monster = monsters.get(monsterName);
							
							if (monster != null) {
								// setting monster's loots
								monster.getLoots().clear();
								NodeList lootNodes = monsterElement.getElementsByTagName("loot");
								
								for (int k = 0; k < lootNodes.getLength(); k++) {
									Node lootNode = lootNodes.item(k);
									
									if (lootNode.getNodeType() == Node.ELEMENT_NODE) {
										Element lootElement = (Element) lootNode;
										String lootName = lootElement.getTextContent();
										Item loot = items.get(lootName);
										
										if (loot != null) monster.getLoots().add((Item) loot.clone());
									}
								}
								
								monster.setNbLoots(monster.getLoots().size());
								room.getMonsters().add((Monster) monster.clone());
							}
						}
					}
					
					this.rooms.put(roomName, room);
				} // END if (roomNode.getNodeType() == Node.ELEMENT_NODE)
			} // END for (int i = 0; i < roomNodes.getLength(); i++)
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void readExits(final String name) {
		try {
			Document document = builder.parse(this.exitFile);
			document.getDocumentElement().normalize();
			
			NodeList exitNodes = document.getElementsByTagName("exit");
			
			for (int i = 0; i < exitNodes.getLength(); i++) {
				Node exitNode = exitNodes.item(i);
				
				if (exitNode.getNodeType() == Node.ELEMENT_NODE) {
					Element exitElement = (Element) exitNode;
					String sourceName = exitElement.getAttribute("source");
					Room source = this.rooms.get(sourceName);
					
					if (source != null) {
						// getting room's exits
						Room north = null, west = null, east = null;
						
						NodeList northNodes = exitElement.getElementsByTagName("north");
						if (northNodes.getLength() != 0) {
							String northName = northNodes.item(0).getTextContent().trim();
							north = this.rooms.get(northName);
						}
						
						NodeList westNodes = exitElement.getElementsByTagName("west");
						if (westNodes.getLength() != 0) {
							String westName = westNodes.item(0).getTextContent().trim();
							west = this.rooms.get(westName);
						}
						
						NodeList eastNodes = exitElement.getElementsByTagName("east");
						if (eastNodes.getLength() != 0) {
							String eastName = eastNodes.item(0).getTextContent().trim();
							east = this.rooms.get(eastName);
						}
						
						source.setExits(north, east, west);
					}
				} // END if (exitNode.getNodeType() == Node.ELEMENT_NODE)
			} // END for (int i = 0; i < exitNodes.getLength(); i++)
			
			this.exitFile.close();
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void readPlayer(final String name) {
		this.xmlPlayerFile = new File(SOURCE_DIRECTORY + name + "/" + PLAYER_FILE);
		
		Map<String, Item> items = ItemXMLReader.getInstance().getItems();
		items.putAll(ItemXMLReader.getInstance().getUniqueItems());
		
		try {
			Document document = builder.parse(this.xmlPlayerFile);
			Player player = Player.getInstance();
			
			NodeList playerNodes = document.getElementsByTagName("player");
			
			if (playerNodes.getLength() != 0) {
				Node playerNode = playerNodes.item(0);
				
				if (playerNode.getNodeType() == Node.ELEMENT_NODE) {
					Element playerElement = (Element) playerNode;
					
					player.setName(playerElement.getAttribute("name").trim());
					player.setLevel(Integer.parseInt(playerElement.getAttribute("level").trim()));
					player.setPower(Integer.parseInt(playerElement.getAttribute("power").trim()));
					player.setDefense(Integer.parseInt(playerElement.getAttribute("defense").trim()));
					player.setMaxHp(Integer.parseInt(playerElement.getAttribute("maxHp").trim()));
					player.setHp(Integer.parseInt(playerElement.getAttribute("hp").trim()));
					
					// setting weapons
					NodeList rightHandNodes = playerElement.getElementsByTagName("rightHand");
					if (rightHandNodes.getLength() != 0) {
						Node rightHandNode = rightHandNodes.item(0);
						
						if (rightHandNode.getNodeType() == Node.ELEMENT_NODE) {
							Element rightHandElement = (Element) rightHandNode;
							String weaponName = rightHandElement.getTextContent().trim();
							int lifespawn = Integer.parseInt(rightHandElement.getAttribute("lifespawn").trim());
							Item item = items.get(weaponName);
							
							if (item != null && item instanceof Weapon) {
								Weapon rightHand = (Weapon) item.clone();
								rightHand.setLifespawn(lifespawn);
								player.setRightHand(rightHand);
							} 
						}
					}
					
					NodeList leftHandNodes = playerElement.getElementsByTagName("leftHand");
					if (leftHandNodes.getLength() != 0) {
						Node leftHandNode = leftHandNodes.item(0);
						
						if (leftHandNode.getNodeType() == Node.ELEMENT_NODE) {
							Element leftHandElement = (Element) leftHandNode;
							String weaponName = leftHandElement.getTextContent().trim();
							int lifespawn = Integer.parseInt(leftHandElement.getAttribute("lifespawn").trim());
							Item item = items.get(weaponName);
							
							if (item != null && item instanceof Weapon) {
								Weapon leftHand = (Weapon) item.clone();
								leftHand.setLifespawn(lifespawn);
								player.setLeftHand(leftHand);
							} 
						}
					}
					
					// setting armors
					NodeList headNodes = playerElement.getElementsByTagName("head");
					if (headNodes.getLength() != 0) {
						Node headNode = headNodes.item(0);
						
						if (headNode.getNodeType() == Node.ELEMENT_NODE) {
							Element headElement = (Element) headNode;
							String armorName = headElement.getTextContent().trim();
							int lifespawn = Integer.parseInt(headElement.getAttribute("lifespawn").trim());
							Item item = items.get(armorName);
							
							if (item != null && item instanceof Armor) {
								Armor head = (Armor) item.clone();
								head.setLifespawn(lifespawn);
								player.setHead(head);
							} 
						}
					}
					
					NodeList bodyNodes = playerElement.getElementsByTagName("body");
					if (bodyNodes.getLength() != 0) {
						Node bodyNode = bodyNodes.item(0);
						
						if (bodyNode.getNodeType() == Node.ELEMENT_NODE) {
							Element bodyElement = (Element) bodyNode;
							String armorName = bodyElement.getTextContent().trim();
							int lifespawn = Integer.parseInt(bodyElement.getAttribute("lifespawn").trim());
							Item item = items.get(armorName);
							
							if (item != null && item instanceof Armor) {
								Armor body = (Armor) item.clone();
								body.setLifespawn(lifespawn);
								player.setBody(body);
							} 
						}
					}
					
					NodeList armNodes = playerElement.getElementsByTagName("arm");
					if (armNodes.getLength() != 0) {
						Node armNode = armNodes.item(0);
						
						if (armNode.getNodeType() == Node.ELEMENT_NODE) {
							Element armElement = (Element) armNode;
							String armorName = armElement.getTextContent().trim();
							int lifespawn = Integer.parseInt(armElement.getAttribute("lifespawn").trim());
							Item item = items.get(armorName);
							
							if (item != null && item instanceof Armor) {
								Armor arm = (Armor) item.clone();
								arm.setLifespawn(lifespawn);
								player.setArm(arm);
							} 
						}
					}
					
					NodeList legNodes = playerElement.getElementsByTagName("leg");
					if (legNodes.getLength() != 0) {
						Node legNode = legNodes.item(0);
						
						if (legNode.getNodeType() == Node.ELEMENT_NODE) {
							Element legElement = (Element) legNode;
							String armorName = legElement.getTextContent().trim();
							int lifespawn = Integer.parseInt(legElement.getAttribute("lifespawn").trim());
							Item item = items.get(armorName);
							
							if (item != null && item instanceof Armor) {
								Armor leg = (Armor) item.clone();
								leg.setLifespawn(lifespawn);
								player.setLeg(leg);
							} 
						}
					}
					
					// setting bag
					NodeList bagNodes = playerElement.getElementsByTagName("bag");
					if (bagNodes.getLength() != 0) {
						Node bagNode = bagNodes.item(0);
						
						if (bagNode.getNodeType() == Node.ELEMENT_NODE) {
							Element bagElement = (Element) bagNode;
							NodeList itemNodes = bagElement.getElementsByTagName("item");
							
							for (int j = 0; j < itemNodes.getLength(); j++) {
								Node itemNode = itemNodes.item(j);
								
								if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
									Element itemElement = (Element) itemNode;
									String itemName = itemElement.getTextContent();
									Item item = items.get(itemName);
									
									if (item != null) {
										if (item instanceof Equipment) {
											Equipment equipment = (Equipment) item;
											int lifespawn = Integer.parseInt(itemElement.getAttribute("lifespawn").trim());
											equipment.setLifespawn(lifespawn);
											
											player.getBag().add((Equipment) equipment.clone());
										} else {
											player.getBag().add((Item) item.clone());
										}
									}
								}
							}
						}
					}
					
					// setting currentRoom
					NodeList currentRoomNodes = playerElement.getElementsByTagName("currentRoom");
					if (currentRoomNodes.getLength() != 0) {
						Node currentRoomNode = currentRoomNodes.item(0);
						
						if (currentRoomNode.getNodeType() == Node.ELEMENT_NODE) {
							Element currentRoomElement = (Element) currentRoomNode;
							String roomName = currentRoomElement.getTextContent().trim();
							Room currentRoom = this.rooms.get(roomName);
							if (currentRoom != null) GameConsole.getInstance().setCurrentRoom(currentRoom);
						}
					}
					
					// setting previousRoom
					NodeList previousRoomNodes = playerElement.getElementsByTagName("previousRoom");
					if (previousRoomNodes.getLength() != 0) {
						Node previousRoomNode = previousRoomNodes.item(0);
						
						if (previousRoomNode.getNodeType() == Node.ELEMENT_NODE) {
							Element previousRoomElement = (Element) previousRoomNode;
							String roomName = previousRoomElement.getTextContent().trim();
							Room previousRoom = this.rooms.get(roomName);
							if (previousRoom != null) GameConsole.getInstance().setPreviousRoom(previousRoom);
						}
					}
				} // END if (playerNode.getNodeType() == Node.ELEMENT_NODE)
			} // END if (playerNodes.getLength() != 0)
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
	}

}
