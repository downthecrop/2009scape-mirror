package plugin.interaction.item;

import java.util.concurrent.TimeUnit;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.dialogue.DialogueInterpreter;
import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.TeleportManager.TeleportType;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.PluginManager;

/**
 * Handles the operation of skillcapes.
 * @author Empathy.
 *
 */
@InitializablePlugin
public class SkillcapeHandlerPlugin extends OptionHandler {

	/**
	 * The skillcapes you can operate.
	 */
	public static final int[] SKILLCAPES = new int[] { 9948, 9949, 9750, 9751, 9780, 9781, 9774, 9775, 9783, 9784, 9762, 9763 };

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		PluginManager.definePlugin(new FishingCapePlugin(), new SkillcapeOperateDialogue());
		for (int id : SKILLCAPES) {
			ItemDefinition.forId(id).getConfigurations().put("option:operate", this);
		}
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		int item = node.asItem().getId();
		switch (item) {
		case 9750:
		case 9751:
			player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("skill-perks"), true, new Location(2876, 3546, 0), "Warriors Guild");
			return true;
		case 9780:
		case 9781:
			player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("skill-perks"), true, new Location(2933, 3288, 0), "Crafting Guild");
			return true;
		case 9774:
		case 9775:
			player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("skill-perks"), false, new Item(233));
			return true;
		case 9783:
		case 9784:
			player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("skill-perks"), false, new Item(946));
			return true;
		case 9948:
		case 9949:
			if (player.getSavedData().getGlobalData().getHunterCapeDelay() < System.currentTimeMillis()) {
				player.getSavedData().getGlobalData().setHunterCapeDelay(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
				player.getSavedData().getGlobalData().setHunterCapeCharges(0);
			}
			int charges = player.getSavedData().getGlobalData().getHunterCapeCharges();
			if (charges >= 5) {
				player.sendMessage("You have already used the effect of your hunter cape today.");
				return true;
			}
			player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("skill-perks"), true, new Location(2556, 2914, 0), "Red Chinchompas");
			player.getSavedData().getGlobalData().setHunterCapeCharges(charges + 1);
			return true;
		case 9762:
		case 9763:
			if (player.getSavedData().getGlobalData().getMagicSkillCapeDelay() < System.currentTimeMillis()) {
				if (player.inCombat() || player.getLocks().isInteractionLocked() || player.getSkullManager().isWilderness() || player.getAttribute("activity", null) != null) {
					player.getPacketDispatch().sendMessage("You can't do that right now.");
					return true;
				}
				player.getSavedData().getGlobalData().setMagicSkillCapeDelay(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));		
				player.animate(new Animation(6299));
				player.graphics(new Graphics(1062));
				player.getDialogueInterpreter().open(3264731, true, true);
				return true;
			}
			player.sendMessage("You have already used the effect of your magic cape today.");
			return true;
		}
		return false;
	}

	/**
	 * Handles the operating of a skillcape.
	 * @author Empathy
	 *
	 */
	public class SkillcapeOperateDialogue extends DialoguePlugin {
		
		/**
		 * The location to teleport to.
		 */
		private Location location;
		
		/**
		 * The item to receive.
		 */
		private Item item;
		
		/**
		 * 
		 * Constructs a new {@code SkillcapeUse} object.
		 */
		public SkillcapeOperateDialogue() {
			/**
			 * empty.
			 */
		}
		
		/**
		 * 
		 * Constructs a new {@code SkillcapeUse} object.
		 * @param player the player.
		 */
		public SkillcapeOperateDialogue(Player player) {
			super(player);
		}
		
		@Override
		public DialoguePlugin newInstance(Player player) {
			return new SkillcapeOperateDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			if ((boolean) args[0]) {
				location = (Location) args[1];
				interpreter.sendOptions("Teleport to the " + args[2] + "?", "Yes", "No");
				stage = 1;
				return true;
			}
			item = (Item) args[1];
			interpreter.sendOptions("Take a " + item.getName() + "?", "Yes", "No");
			stage = 5;
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
			case 1:
				switch (buttonId) {
				case 1:
					end();
					player.getTeleporter().send(location, TeleportType.NORMAL);
					return true;
				case 2:
					end();
					break;
				}
				break;
			case 5:
				switch (buttonId) {
				case 1:
					end();
					if (!player.getInventory().isFull()) {
						player.getInventory().add(item);
						return true;
					}
					player.sendMessage("Your inventory is too full to do that.");
					break;
				case 2:
					end();
					break;
				}
			}
			return true;
		}

		@Override
		public int[] getIds() {
			return new int[] { DialogueInterpreter.getDialogueKey("skill-perks") };
		}
		
	}
	
	/**
	 * Handles the fishing skillcape perk.
	 * @author Empathy
	 *
	 */
	public class FishingCapePlugin implements Plugin<Object> {

		/**
		 * The cape Ids.
		 */
		public final int[] capeIds = new int[] { 9798, 9799, 14831, 14833, 14835, 14839, 14840 };
		
		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			for (int id : capeIds) {
				ItemDefinition.forId(id).getConfigurations().put("equipment", this);
			}
			return this;
		}

		@Override
		public Object fireEvent(String identifier, Object... args) {
			final Player player = (Player) args[0];
			switch (identifier) {
			case "equip":
				player.getSkills().updateLevel(Skills.FISHING, 100, 100);
				player.sendMessage("You feel the power of your cape boost your fishing ability.");
				break;
			case "unequip":
				final Item other = args.length == 2 ? null : (Item) args[2];
				if (other != null) {
					for (int id : capeIds) {
						if (id == other.getId()) {
							System.out.println(id);
							return true;
						}
					}
				}
				player.getSkills().setLevel(Skills.FISHING, 99);
				break;
			}
			return true;
		}

	}

}
