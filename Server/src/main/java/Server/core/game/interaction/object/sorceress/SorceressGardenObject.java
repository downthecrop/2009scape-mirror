package core.game.interaction.object.sorceress;

import core.cache.def.impl.ObjectDefinition;
import core.game.content.global.action.DoorActionHandler;
import core.game.node.entity.skill.Skills;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.object.GameObject;
import core.plugin.Plugin;

/**
 * Hanldes the sorceress garden gates.
 * @author 'Vexia
 */
public class SorceressGardenObject extends OptionHandler {

	@Override
	public boolean handle(Player player, Node node, String option) {
		GardenObjectsPlugin.SeasonDefinitions def = GardenObjectsPlugin.SeasonDefinitions.forGateId(((GameObject) node).getId());
		if (def != null) {
			if (player.getSkills().getStaticLevel(Skills.THIEVING) < def.getLevel()) {
				player.getDialogueInterpreter().sendItemMessage(10692, "You need Thieving level of " + def.getLevel() + " to pick the lock of this gate.");
				return true;
			}
			DoorActionHandler.handleAutowalkDoor(player, (GameObject) node);
		}
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(21709).getHandlers().put("option:open", this);
		ObjectDefinition.forId(21753).getHandlers().put("option:open", this);
		ObjectDefinition.forId(21731).getHandlers().put("option:open", this);
		ObjectDefinition.forId(21687).getHandlers().put("option:open", this);
		return this;
	}

}
