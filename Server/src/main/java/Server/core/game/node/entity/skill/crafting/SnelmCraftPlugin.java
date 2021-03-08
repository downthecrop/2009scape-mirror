package core.game.node.entity.skill.crafting;

import core.plugin.Initializable;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Plugin;

/**
 * Handles the crafting of a snelm helmet.
 * @author Vexia
 */
@Initializable
public class SnelmCraftPlugin extends UseWithHandler {

	/**
	 * The snelm data.
	 */
	private static final int[][] DATA = new int[][] { { 3345, 3327 }, { 3355, 3337 },// blamish
			{ 3349, 3341 }, { 3341, 3359 },// ochre
			{ 3347, 3329 }, { 3357, 3339 },// blood
			{ 3351, 3333 }, { 3361, 3343 },// blue
			{ 3353, 3335 },// bark
	};

	/**
	 * Constructs a new {@Code SnelmCraftPlugin} {@Code Object}
	 */
	public SnelmCraftPlugin() {
		super(1755);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (int i = 0; i < DATA.length; i++) {
			for (int k = 0; k < DATA[i].length; k++) {
				addHandler(DATA[i][0], ITEM_TYPE, this);
			}
		}
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		int[] snelm = null;
		for (int i = 0; i < DATA.length; i++) {
			for (int k = 0; k < DATA[i].length; k++) {
				if (DATA[i][0] == event.getUsedItem().getId()) {
					snelm = DATA[i];
					break;
				}
			}
		}
		if (snelm == null) {
			return false;
		}
		player.lock(1);
		player.getPulseManager().run(new SnelmCraftPulse(player, event.getUsedItem(), snelm));
		return true;
	}

	/**
	 * Handles the crafting of a snelm helmet.
	 * @author Vexia
	 */
	public static final class SnelmCraftPulse extends SkillPulse<Item> {

		/**
		 * The snelm data.
		 */
		private final int[] data;

		/**
		 * Constructs a new {@Code SnelmCraftPulse} {@Code Object}
		 * @param player the player.
		 * @param node the node.
		 */
		public SnelmCraftPulse(Player player, Item node, int[] data) {
			super(player, node);
			this.setDelay(1);
			this.data = data;
		}

		@Override
		public boolean checkRequirements() {
			if (player.getSkills().getStaticLevel(Skills.CRAFTING) < 15) {
				player.sendMessage("You need a Crafting level of at least 15 in order to do this.");
				return false;
			}
			return true;
		}

		@Override
		public void animate() {

		}

		@Override
		public boolean reward() {
			player.sendMessage("You craft the shell into a helmet.");
			player.getInventory().replace(new Item(data[1]), node.getSlot());
			player.getSkills().addExperience(Skills.CRAFTING, 32.5, true);
			return true;
		}

	}
}
