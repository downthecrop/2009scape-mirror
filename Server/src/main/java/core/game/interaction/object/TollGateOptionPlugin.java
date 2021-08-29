package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.content.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import rs09.game.world.repository.Repository;
import core.plugin.Initializable;
import core.plugin.Plugin;

import static rs09.game.node.entity.player.info.stats.StatAttributeKeysKt.STATS_ALKHARID_GATE;
import static rs09.game.node.entity.player.info.stats.StatAttributeKeysKt.STATS_BASE;

@Initializable
public class TollGateOptionPlugin extends OptionHandler {

	@Override
	public boolean handle(Player player, Node node, String option) {
		if (option.equals("pay-toll(10gp)")) {
			if (player.getQuestRepository().getQuest("Prince Ali Rescue").getStage(player) > 50) {
				player.getPacketDispatch().sendMessage("The guards let you through for free.");
				DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
			} else {
				if (player.getInventory().contains(995, 10)) {
					player.getInventory().remove(new Item(995, 10));
					player.getPacketDispatch().sendMessage("You quickly pay the 10 gold toll and go through the gates.");
					DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
					player.incrementAttribute("/save:" + STATS_BASE + ":" + STATS_ALKHARID_GATE, 10);
					return true;
				} else {
					player.getPacketDispatch().sendMessage("You need 10 gold to pass through the gates.");
				}
			}
		} else {
			player.getDialogueInterpreter().open(925, Repository.findNPC(925), (Scenery) node);
			return true;
		}
		return true;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(35551).getHandlers().put("option:open", this);
		SceneryDefinition.forId(35551).getHandlers().put("option:pay-toll(10gp)", this);
		SceneryDefinition.forId(35549).getHandlers().put("option:open", this);
		SceneryDefinition.forId(35549).getHandlers().put("option:pay-toll(10gp)", this);
		SceneryDefinition.forId(2882).getHandlers().put("option:pay-toll(10gp)", this);
		return this;
	}

}
