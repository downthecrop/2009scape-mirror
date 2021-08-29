package core.game.interaction.object.sorceress;

import core.cache.def.impl.SceneryDefinition;
import core.game.content.global.action.DoorActionHandler;
import core.game.node.entity.skill.Skills;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import core.plugin.Plugin;

/**
 * Hanldes the sorceress garden gates.
 * @author 'Vexia
 */
public class SorceressGardenObject extends OptionHandler {

	@Override
	public boolean handle(Player player, Node node, String option) {
		GardenObjectsPlugin.SeasonDefinitions def = GardenObjectsPlugin.SeasonDefinitions.forGateId(((Scenery) node).getId());
		if (def != null) {
			if (player.getSkills().getStaticLevel(Skills.THIEVING) < def.getLevel()) {
				player.getDialogueInterpreter().sendItemMessage(10692, "You need Thieving level of " + def.getLevel() + " to pick the lock of this gate.");
				return true;
			}
			DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
		}
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(21709).getHandlers().put("option:open", this);
		SceneryDefinition.forId(21753).getHandlers().put("option:open", this);
		SceneryDefinition.forId(21731).getHandlers().put("option:open", this);
		SceneryDefinition.forId(21687).getHandlers().put("option:open", this);
		return this;
	}

}
