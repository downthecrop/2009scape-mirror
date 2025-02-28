package content.global.travel.glider;

import content.data.Quests;
import core.api.ContentAPIKt;
import core.cache.def.impl.NPCDefinition;
import core.game.component.Component;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;
import static core.api.ContentAPIKt.isQuestComplete;

/**
 * Represents the plugin used for gliders.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class GliderPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(3809).getHandlers().put("option:glider", this);
		NPCDefinition.forId(3810).getHandlers().put("option:glider", this);
		NPCDefinition.forId(3811).getHandlers().put("option:glider", this);
		NPCDefinition.forId(3812).getHandlers().put("option:glider", this);
		NPCDefinition.forId(3813).getHandlers().put("option:glider", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if(isQuestComplete(player, Quests.THE_GRAND_TREE)){
			player.getInterfaceManager().open(new Component(138));
			Gliders.sendConfig(node.asNpc(), player);
		} else {
			ContentAPIKt.sendMessage(player,"You must complete The Grand Tree Quest to access the gnome glider.");
		}
		return true;
	}

}
