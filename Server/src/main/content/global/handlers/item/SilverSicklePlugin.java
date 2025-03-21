package content.global.handlers.item;

import static core.api.ContentAPIKt.*;
import core.cache.def.impl.ItemDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;
import content.region.morytania.quest.naturespirit.NSUtils;
import content.data.Quests;

/**
 * Handles the Silver Sickle (b) to collect Mort Myre Fungus.
 * @author Splinter
 */
@Initializable
public final class SilverSicklePlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(2963).getHandlers().put("option:operate", this);
		ItemDefinition.forId(2963).getHandlers().put("option:cast bloom", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "operate":
		case "cast bloom":
			if(player.getQuestRepository().getQuest(Quests.NATURE_SPIRIT).getStage(player) >= 75) {
				player.getPacketDispatch().sendAnimation(9021);
				NSUtils.castBloom(player);
			} else {
				sendDialogue(player, "You must complete Nature Spirit to use this.");
			}
			return true;
		}
		return false;
	}

}
