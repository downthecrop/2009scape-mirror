package plugin.quest.merlincrystal;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.quest.Quest;
import org.crandor.game.node.object.GameObject;
import org.crandor.plugin.Plugin;

/**
 * Represents the quest node plugin handler.
 * @author Splinter
 */
public class MerlinCrystalOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(247).getConfigurations().put("option:attack", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final Quest quest = player.getQuestRepository().getQuest("Merlin's Crystal");
		int id = node instanceof GameObject ? ((GameObject) node).getId() : ((NPC) node).getId();
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
