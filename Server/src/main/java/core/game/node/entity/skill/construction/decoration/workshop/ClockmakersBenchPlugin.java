package core.game.node.entity.skill.construction.decoration.workshop;


import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.content.dialogue.DialogueInterpreter;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.construction.BuildingUtils;
import core.game.node.entity.skill.construction.Decoration;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;

/**
 * Handles the clockmakers bench in the workshop
 * ClockmakersBenchPlugin.java
 * @author Clayton Williams (hope)
 * @date Oct 29, 2015
 */
@Initializable
public class ClockmakersBenchPlugin extends OptionHandler {
	
	/**
	 * Stores data for things created by the clockmakers bench
	 * ClockmakersBenchPlugin.java
	 * @author Clayton Williams
	 * @date Oct 30, 2015
	 */
	private enum Craftable {
		
		TOY_HORSEY(2520, 10, BuildingUtils.PLANK),
		CLOCKWORK(8792, 8, new Item(2353)),
		TOY_SOLDIER(7759, 13, BuildingUtils.PLANK, new Item(8792)),
		TOY_DOLL(7763, 18, BuildingUtils.PLANK, new Item(8792)),
		TOY_MOUSE(7767, 33, BuildingUtils.PLANK, new Item(8792)),
		TOY_CAT(7771, 85, BuildingUtils.PLANK, new Item(8792)),
		WATCH(2575, 28, new Item(8792), new Item(2353)),
		SEXTANT(2574, 23, new Item(2353));
		
		/**
		 * The itemId to give after making this
		 */
		private int itemId;
		
		/**
		 * The materials needed to make this
		 */
		private Item[] materials;
		
		/**
		 * The crafting level required to make this
		 */
		private int craftingLevel;
		
		/**
		 * Craftable
		 * @param itemId
		 * @param craftingLevel
		 * @param materials
		 */
		Craftable(int itemId, int craftingLevel, Item... materials) {
			this.itemId = itemId;
			this.craftingLevel = craftingLevel;
			this.materials = materials;
		}
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(13709).getHandlers().put("option:craft", this);
		SceneryDefinition.forId(13710).getHandlers().put("option:craft", this);
		SceneryDefinition.forId(13711).getHandlers().put("option:craft", this);
		SceneryDefinition.forId(13712).getHandlers().put("option:craft", this);
		ClassScanner.definePlugin(new ClockmakerBenchDialogue());
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("con:clockbench"), node.asScenery());
		return true;
	}
	
	/**
	 * Handles the dialogue for clock bench
	 * @author Clayton Williams
	 * @date Oct 29, 2015
	 */
	private final class ClockmakerBenchDialogue extends DialoguePlugin {
		
		/**
		 * The crafting table decoration
		 */
		Decoration decoration;
		
		/**
		 * ClockmakerBenchDialogue
		 */
		public ClockmakerBenchDialogue() {
			/**
			 * Empty
			 */
		}
		
		/**
		 * ClockmakerBenchDialogue
		 * @param player
		 */
		public ClockmakerBenchDialogue(Player player) {
			super(player);
		}

		@Override
		public DialoguePlugin newInstance(Player player) {
			return new ClockmakerBenchDialogue(player);
		}

		@Override
		public boolean open(Object... args) {
			Scenery object = (Scenery) args[0];
			decoration = Decoration.forObjectId(object.getId());
			if (decoration != null) {
				switch (decoration) {
					case CRAFTING_TABLE_1:
						interpreter.sendOptions("Select an Option", "Toy Horsey", "Nevermind");
						break;
					case CRAFTING_TABLE_2:
						interpreter.sendOptions("Select an Option", "Toy Horsey", "Clockwork Mechanism");
						break;
					case CRAFTING_TABLE_3:
						interpreter.sendOptions("Select an Option", "Toy Horsey", "Clockwork Mechanism", "Clockwork Devices");
						break;
					case CRAFTING_TABLE_4:
						interpreter.sendOptions("Select an Option", "Toy Horsey", "Clockwork Mechanism", "Clockwork Devices", "Watch", "Sextant");
						break;
					default:
						break;
				}
			}
			stage = 1;
			return true;
		}

		@Override
		public boolean handle(int interfaceId, int buttonId) {
			switch (stage) {
				case 1:
					switch (buttonId) {
						case 1:
						case 2:
							if (decoration == Decoration.CRAFTING_TABLE_1 && buttonId == 2) {
								end();
								return true;
							}
							craftItem(buttonId == 1 ? Craftable.TOY_HORSEY : Craftable.CLOCKWORK);
							stage = 3;
							break;
						case 3:
							if (decoration == Decoration.CRAFTING_TABLE_3) {
								interpreter.sendOptions("Select an Option", "Clockwork Soldier", "Clockwork Doll");
							} else if (decoration == Decoration.CRAFTING_TABLE_4) {
								interpreter.sendOptions("Select an Option", "Clockwork Soldier", "Clockwork Doll", "Clockwork Mouse", "Clockwork Cat");
							}
							stage = 2;
							break;
						case 4:
						case 5:
							craftItem(buttonId == 4 ? Craftable.WATCH : Craftable.SEXTANT);
							stage = 3;
							break;
					}
					break;
				case 2:
					switch (buttonId) {
						case 1:
							craftItem(Craftable.TOY_SOLDIER);
							break;
						case 2:
							craftItem(Craftable.TOY_DOLL);
							break;
						case 3:
							craftItem(Craftable.TOY_MOUSE);
							break;
						case 4:
							craftItem(Craftable.TOY_CAT);
							break;					
					}
					stage = 3;
					break;
				case 3:
					end();
					break;
			}
			return true;
		}
		
		/**
		 * Attempts to craft an item
		 * @param c - the craftable to make
		 */
		private void craftItem(Craftable c) {
			if (c != null) {
				if (player.getSkills().getLevel(Skills.CRAFTING) < c.craftingLevel) {
					interpreter.sendDialogue("You need level " + c.craftingLevel + " crafting to make that.");
					return;
				}
				for (Item n : c.materials) {
					if (!player.getInventory().containsItem(n)) {
						interpreter.sendDialogue("You need a " + ItemDefinition.forId(n.getId()).getName() + " to make that!");
						return;
					}
				}
				for (Item n : c.materials) {
					n.setAmount(1);
					player.getInventory().remove(n);
				}
				player.getSkills().addExperience(Skills.CRAFTING, 15);
				player.getInventory().add(new Item(c.itemId, 1));
				player.animate(BuildingUtils.BUILD_MID_ANIM);
				interpreter.sendDialogue("You made a " + ItemDefinition.forId(c.itemId).getName() + "!");
			}
		}

		@Override
		public int[] getIds() {
			return new int[] { DialogueInterpreter.getDialogueKey("con:clockbench") };
		}	
	}
}
