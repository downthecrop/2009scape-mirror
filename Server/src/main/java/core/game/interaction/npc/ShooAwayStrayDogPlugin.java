package core.game.interaction.npc;

import core.cache.def.impl.NPCDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the option plugin used to shoo away a dog.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class ShooAwayStrayDogPlugin extends OptionHandler {

	/**
	 * Represents the animatio to use.
	 */
	private static final Animation ANIMATION = new Animation(2110);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		NPCDefinition.forId(5917).getHandlers().put("option:shoo-away", this);
		NPCDefinition.setOptionHandler("shoo-away", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.sendChat("Thbbbbt!");
		player.animate(ANIMATION);
		NPC dog = (NPC) node;
		dog.sendChat("Whine!");
		dog.moveStep();
		dog.getPulseManager().clear();
		return true;
	}

}
