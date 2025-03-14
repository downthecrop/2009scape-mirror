package content.region.kandarin.seers.quest.merlinsquest;

import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.scenery.Scenery;
import core.plugin.Plugin;
import content.data.Quests;

/**
 * Represents the quest node plugin handler.
 * @author Splinter
 */
public class MerlinCrystalOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(247).getHandlers().put("option:attack", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final Quest quest = player.getQuestRepository().getQuest(Quests.MERLINS_CRYSTAL);
		int id = node instanceof Scenery ? ((Scenery) node).getId() : ((NPC) node).getId();
		switch (id) {
		case 247:
			if (quest.getStage(player) < 40) {
				player.getProperties().getCombatPulse().stop();
				player.getPacketDispatch().sendMessage("You have no reason to attack this valiant knight.");
				return true;
			}
			if (quest.getStage(player) == 40) {
				player.getProperties().getCombatPulse().attack(node);
			}
			if (quest.getStage(player) > 40) {
				player.getProperties().getCombatPulse().stop();
				player.getPacketDispatch().sendMessage("You've already bested Sir Mordred in combat.");
				return true;
			}
			break;
		}
		return true;
	}

}
