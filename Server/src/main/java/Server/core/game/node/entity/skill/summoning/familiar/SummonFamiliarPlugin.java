package core.game.node.entity.skill.summoning.familiar;

import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.item.Item;
import core.game.world.map.zone.ZoneBorders;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.game.node.entity.npc.familiar.IbisNPC;

/**
 * Handles summoning a familiar.
 * @author Emperor
 */
@Initializable
public final class SummonFamiliarPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.setOptionHandler("summon", this);
		return null;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		Item item = (Item) node;
		if (!player.getQuestRepository().isComplete("Wolf Whistle") && player.getAttribute("in-cutscene", null) == null) {
			player.getPacketDispatch().sendMessage("You have to complete Wolf Whistle before you can summon a familiar.");
			return true;
		}
		player.getFamiliarManager().summon(item, false);

		// Achievement diary handlers
		if (player.getFamiliarManager().hasFamiliar()
				&& player.getFamiliarManager().getFamiliar() instanceof IbisNPC
				&& (new ZoneBorders(3011, 3222, 3017, 3229, 0).insideBorder(player)
				|| new ZoneBorders(3011, 3220, 3015, 3221, 0).insideBorder(player))) {
			player.getAchievementDiaryManager().finishTask(player,DiaryType.FALADOR, 2, 9);
		}
		return true;
	}

}
