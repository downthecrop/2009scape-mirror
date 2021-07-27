package core.game.content.global.presets;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PresetManager {

	private Player player;

	private final Map<String, Preset> current_presets;

	public PresetManager() {
		current_presets = new HashMap<>();
	}

	public PresetManager storeSet(String name_key) {
		final Preset set = current_presets.get(name_key);
		if (set != null) {
			player.sendMessage("You were unable to store the set " + name_key + " as it already exists.");
		}
		name_key = name_key.toLowerCase();
		ArrayList<Item> equipment = new ArrayList<Item>(Arrays.asList(player.getEquipment().getEvent().getItems())), inventory = new ArrayList<Item>(Arrays.asList(player.getInventory().getEvent().getItems()));
		current_presets.put(name_key, new Preset(equipment, inventory));
		return this;
	}

	public void printAvailableSetups() {
		final int size = current_presets.size();
		player.sendMessage("You have used "+size+"/"+maxSize()+" available setups.");
		if (size > 0) {
			player.sendMessage("<col=ff0000>Your available setups are:");
			for (final String key : current_presets.keySet()) {
				player.sendMessage(key);
			}
		}
	}
	
	public int maxSize() {
		return 6;
	}
}
