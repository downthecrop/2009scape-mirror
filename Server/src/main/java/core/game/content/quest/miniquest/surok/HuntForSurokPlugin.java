package core.game.content.quest.miniquest.surok;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Plugin;
import rs09.plugin.ClassScanner;

/**
 * Handles the hunt for surok mini quest.
 * @author Vexia
 */

public class HuntForSurokPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ClassScanner.definePlugin(new DakhThoulanAegisDialogue());
		ClassScanner.definePlugin(new MishkalunDornDialogue());
		ClassScanner.definePlugin(new SilasDahcsnuDialogue());
		ClassScanner.definePlugin(new SurokMagisDialogue());
		SceneryDefinition.forId(28780).getHandlers().put("option:use", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "use":
			switch (node.getId()) {
			case 28780:
				player.teleport(new Location(3326, 5469, 0));
				break;
			}
			break;
		}
		return true;
	}

}
