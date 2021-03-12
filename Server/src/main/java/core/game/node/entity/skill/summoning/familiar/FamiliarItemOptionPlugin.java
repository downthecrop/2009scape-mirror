package core.game.node.entity.skill.summoning.familiar;

import core.cache.def.impl.ItemDefinition;
import core.game.node.entity.skill.summoning.pet.Pets;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the familiar items option plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class FamiliarItemOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (Pets p : Pets.values()) {
			ItemDefinition def = ItemDefinition.forId(p.getBabyItemId());
			if (def == null) {
				continue;
			}
			def.getHandlers().put("option:drop", this);
			def.getHandlers().put("option:release", this);
			if (p.getGrownItemId() > -1) {
				ItemDefinition.forId(p.getGrownItemId()).getHandlers().put("option:drop", this);
				ItemDefinition.forId(p.getGrownItemId()).getHandlers().put("option:release", this);
			}
			if (p.getOvergrownItemId() > -1) {
				ItemDefinition.forId(p.getOvergrownItemId()).getHandlers().put("option:drop", this);
				ItemDefinition.forId(p.getOvergrownItemId()).getHandlers().put("option:release", this);
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
