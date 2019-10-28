package plugin.skill.slayer;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the plugin used to handle a bug lantern.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class BuglanternPlugin implements Plugin<Object> {

	/**
	 * Represents the unlit lantern item.
	 */
	private static final Item UNLIT_LANTERN = new Item(7051);

	/**
	 * Represents the lit latern item.
	 */
	private static final Item LIT_LANTERN = new Item(7053);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		new LightLanternPlugin().newInstance(arg);
		ItemDefinition.forId(LIT_LANTERN.getId()).getConfigurations().put("option:extinguish", new OptionHandler() {

			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				return this;
			}

			@Override
			public boolean handle(Player player, Node node, String option) {
				if (player.getInventory().remove(LIT_LANTERN)) {
					player.getInventory().add(UNLIT_LANTERN);
				}
				return true;
			}

			@Override
			public boolean isWalk() {
				return false;
			}
		});
		return this;
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	/**
	 * Represents the plugin used to light the lantern.
	 * @author 'Vexia
	 * @version 1.0
	 */
	public final class LightLanternPlugin extends UseWithHandler {

		/**
		 * Constructs a new {@code LightLanternPlugin} {@code Object}.
		 */
		public LightLanternPlugin() {
			super(590);
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			addHandler(UNLIT_LANTERN.getId(), ITEM_TYPE, this);
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			final Player player = event.getPlayer();
			if (player.getSkills().getLevel(Skills.FIREMAKING) < 33) {
				player.sendMessage("You need a Firemaking level of at least 33 in order to do this.");
				return true;
			}
			player.getInventory().replace(LIT_LANTERN, event.getUsedItem().getSlot());
			return true;
		}

	}
}
