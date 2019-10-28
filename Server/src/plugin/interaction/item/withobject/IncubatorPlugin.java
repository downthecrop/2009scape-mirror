package plugin.interaction.item.withobject;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.member.summoning.pet.IncubatorEgg;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.state.EntityState;
import org.crandor.game.node.entity.state.impl.IncubatorStatePulse;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.PluginManager;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.StringUtils;

/**
 * Handles the incubator.
 * @author Vexia
 */
@InitializablePlugin
public class IncubatorPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		PluginManager.definePlugin(new IncubatorEggHandler());
		ObjectDefinition.forId(28359).getConfigurations().put("option:take-egg", this);
		ObjectDefinition.forId(28359).getConfigurations().put("option:inspect", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		int inc = player.getAttribute("inc", -1);
		switch (option) {
		case "take-egg":
			if (inc == -1) {
				player.sendMessage("The egg is still incubating.");
				return true;
			}
			IncubatorEgg egg = IncubatorEgg.values()[inc];
			if (!player.getInventory().hasSpaceFor(egg.getProduct())) {
				player.sendMessage("You don't have enough inventory space.");
				return true;
			}
			if (egg != null) {
				String name = egg.getProduct().getName().toLowerCase();
				player.getFamiliarManager().setConfig(-(1 << 4));
				player.sendMessage("You take your " + name + " out of the incubator.");
				player.getInventory().add(egg.getProduct());
				player.removeAttribute("inc");
			}
			return true;
		case "inspect":
			if (player.getStateManager().hasState(EntityState.INCUBATION) || inc != -1) {
				IncubatorStatePulse p = (IncubatorStatePulse) player.getStateManager().get(EntityState.INCUBATION);
				String name = p == null ? IncubatorEgg.values()[inc].getProduct().getName().toLowerCase() : p.getEgg().getProduct().getName().toLowerCase();
				player.sendMessage("There is " + (StringUtils.isPlusN(name) ? "an" : "a") + " " + name + " incubating in there.");
			}
			return true;
		}
		return true;
	}

	/**
	 * Handles the reward of using an egg on the incubator.
	 * @author Vexia
	 */
	public class IncubatorEggHandler extends UseWithHandler {

		/**
		 * Constructs a new {@Code IncubatorEggHandler} {@Code
		 * Object}
		 */
		public IncubatorEggHandler() {
			super(12483, 11964, 5077, 5076, 5078, 12494, 12477, 12480, 12478, 12479);
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			addHandler(28336, OBJECT_TYPE, this);
			addHandler(28352, OBJECT_TYPE, this);
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			final Player player = event.getPlayer();
			final IncubatorEgg egg = IncubatorEgg.forItem(event.getUsedItem());
			if (egg == null) {
				return false;
			}
			if (player.getStateManager().hasState(EntityState.INCUBATION)) {
				player.sendMessage("You already have an egg in there.");
				return false;
			}
			if (player.getSkills().getStaticLevel(Skills.SUMMONING) < egg.getLevel()) {
				player.getDialogueInterpreter().sendDialogue("You need a Summoning level of at least " + egg.getLevel() + " in order to do this.");
				return true;
			}
			player.getStateManager().register(EntityState.INCUBATION, true, egg);
			return true;
		}

	}
}
