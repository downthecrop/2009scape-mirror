package plugin.skill.herblore;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.member.herblore.Herbs;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the cleaning of a dirty herb.
 * @author Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class HerbCleanPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.setOptionHandler("clean", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (!player.getQuestRepository().isComplete("Drudic Ritual")) {
			player.getPacketDispatch().sendMessage("You must complete the Druidic Ritual quest before you can use Herblore.");
			return true;
		}
		final Herbs herb = Herbs.forItem((Item) node);
		if (player.getSkills().getLevel(Skills.HERBLORE) < herb.getLevel()) {
			player.getPacketDispatch().sendMessage("You need a herblore level " + herb.getLevel() + " to clean this herb.");
			return true;
		}
		double exp = herb.getExperience();
		if (player.getInventory().replace(herb.getProduct(), ((Item) node).getSlot()) != null) {
		    player.getSkills().addExperience(Skills.HERBLORE, exp, true);
		    player.getPacketDispatch().sendMessage("You clean the dirt from the " + herb.getProduct().getName().toLowerCase().replace("clean", "").trim() + " leaf.");
		}
		player.lock(1);
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}

}
