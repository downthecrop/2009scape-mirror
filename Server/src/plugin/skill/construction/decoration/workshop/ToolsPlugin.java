package plugin.skill.construction.decoration.workshop;


import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.dialogue.DialogueInterpreter;
import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the 5 types of tool stores
 * ToolsPlugin.java
 * @author Clayton Williams
 * @date Oct 29, 2015
 *
 */
@InitializablePlugin
public class ToolsPlugin extends OptionHandler {
	
	/**
	 * The different tool stores
	 * @author Clayton Williams
	 * @date Oct 29, 2015
	 */
	private enum ToolStore {
		
		TOOLSTORE_1(13699, 8794, 1755, 2347, 1735),
		TOOLSTORE_2(13700, 1925, 952, 590),
		TOOLSTORE_3(13701, 1757, 1785, 1733),
		TOOLSTORE_4(13702, 1595, 1597, 1592, 1599, 5523),
		TOOLSTORE_5(13703, 5341, 952, 676, 5343, 5331);
		
		/**
		 * The object id of the tool store
		 */
		private int objectId;
		
		/**
		 * The tools included
		 */
		private int[] tools;
		
		/**
		 * ToolStore
		 * @param objectId
		 * @param tools
		 */
		ToolStore(int objectId, int... tools) {
			this.objectId = objectId;
			this.tools = tools;
		}
		
		/**
		 * Gets the toolstore from an object id
		 * @param objectId - the object id
		 * @return
		 */
		private static ToolStore forId(int objectId) {
			for (ToolStore t : ToolStore.values()) {
				if (t.objectId == objectId) {
					return t;
				}
			}
			return null;
		}
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (ToolStore t : ToolStore.values()) {
			ObjectDefinition.forId(t.objectId).getConfigurations().put("option:search", this);
		}
		PluginManager.definePlugin(new ToolDialogue());
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		GameObject object = node.asObject();
		ToolStore ts = ToolStore.forId(object.getId());
		if (ts != null) {
			player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("con:tools"), ts);
		}
		return true;
	}
	
	/**
	 * Handles the tool dialogue
	 * @author Clayton Williams
	 * @date Oct 29, 2015
	 */
	private final class ToolDialogue extends DialoguePlugin {
		
		/**
		 * The tool store being used
		 */
		private ToolStore toolStore;
		
		/**
		 * ToolDialogue
		 */
		private ToolDialogue() {
			/**
			 * Empty
			 */
		}
		
		/**
		 * ToolDialogue
		 * @param player
		 */
		public ToolDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new ToolDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			toolStore = (ToolStore) args[0];
			List<String> itemNames = new ArrayList<String>();
			for(int itemId : toolStore.tools) {
				ItemDefinition n = ItemDefinition.forId(itemId);
				itemNames.add(n.getName());
			}		
			interpreter.sendOptions("Select a Tool", itemNames.toArray(new String[itemNames.size()]));
			stage = 1;
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
				case 1:
					Item item = new Item(toolStore.tools[buttonId - 1], 1);
					if (player.getInventory().freeSlots() <= 0) {
						interpreter.sendDialogue("You have no space in your inventory.");
						stage = 2;
						return true;
					}
					player.getInventory().add(item);
					end();
					return true;
				case 2:
					end();
					return true;
			}
			return false;
		}

		@Override
		public int[] getIds() {
			return new int[] { DialogueInterpreter.getDialogueKey("con:tools") };
		}
		
	}

}
