package core.game.content.global.presets;

import core.game.node.item.Item;

import java.util.ArrayList;

public class Preset {
	
	private ArrayList<Item> equipment;
	
	private ArrayList<Item> inventory;
	
	public Preset(ArrayList<Item> equipment, ArrayList<Item> inventory) {
		setEquipment(equipment);
		setInventory(inventory);
	}

	public ArrayList<Item> getEquipment() {
		return equipment;
	}

	public void setEquipment(ArrayList<Item> equipment) {
		this.equipment = equipment;
	}

	public ArrayList<Item> getInventory() {
		return inventory;
	}

	public void setInventory(ArrayList<Item> inventory) {
		this.inventory = inventory;
	}
}
