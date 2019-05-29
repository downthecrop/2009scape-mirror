package plugin.skill.summoning.familiar;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.skill.member.summoning.pet.Pets;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the familiar items option plugin.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class FamiliarItemOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (Pets p : Pets.values()) {
			ItemDefinition def = ItemDefinition.forId(p.getBabyItemId());
			if (def == null) {
				continue;
			}
			def.getConfigurations().put("option:drop", this);
			def.getConfigurations().put("option:release", this);
			if (p.getGrownItemId() > -1) {
				ItemDefinition.forId(p.getGrownItemId()).getConfigurations().put("option:drop", this);
				ItemDefinition.forId(p.getGrownItemId()).getConfigurations().put("option:release", this);
			}
			if (p.getOvergrownItemId() > -1) {
				ItemDefinition.forId(p.getOvergrownItemId()).getConfigurations().put("option:drop", this);
				ItemDefinition.forId(p.getOvergrownItemId()).getConfigurations().put("option:release", this);
			}
		}
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "drop":
			player.getFamiliarManager().summon(((Item) node), true);
			return true;
		case "release":
			if (((Item) node).getId() == 7771) {
				player.getFamiliarManager().summon(((Item) node), true);
				return true;
			}
			if (player.getInventory().remove(((Item) node))) {
				player.getDialogueInterpreter().sendDialogues(player, null, "Run along; I'm setting you free.");
			}
			return true;
		}
		return true;
	}

	@Override
	public boolean isWalk() {
		return false;
	}
}
