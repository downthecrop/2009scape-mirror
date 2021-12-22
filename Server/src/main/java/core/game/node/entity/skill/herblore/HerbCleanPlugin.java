package core.game.node.entity.skill.herblore;

import api.Container;
import static api.ContentAPIKt.*;
import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;

/**
 * Represents the cleaning of a dirty herb.
 * @author Vexia
 * @version 1.0
 */
@Initializable
public final class HerbCleanPlugin extends OptionHandler {
    private static final int[] SFX_IDS = new int[] { 5153, 5155, 5157 };

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.setOptionHandler("clean", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (!player.getQuestRepository().isComplete("Druidic Ritual")) {
			player.getPacketDispatch().sendMessage("You must complete the Druidic Ritual quest before you can use Herblore.");
			return true;
		}
		final Herbs herb = Herbs.forItem((Item) node);
		if (player.getSkills().getLevel(Skills.HERBLORE) < herb.getLevel()) {
			player.getPacketDispatch().sendMessage("You need level " + herb.getLevel() + " Herblore to clean the " + herb.getProduct().getName().replace("Clean", "Grimy") + ".");
			return true;
		}
		double exp = herb.getExperience();
		if (removeItem(player, node.asItem(), Container.INVENTORY)){
			player.getSkills().addExperience(Skills.HERBLORE, exp, true);
			addItem(player, herb.getProduct().getId(), 1, Container.INVENTORY);
            player.getAudioManager().send(SFX_IDS[RandomFunction.random(SFX_IDS.length)], 1);
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
