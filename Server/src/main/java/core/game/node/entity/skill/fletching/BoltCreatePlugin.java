/*
package core.game.node.entity.skill.fletching;

import rs09.game.content.dialogue.SkillDialogueHandler;
import rs09.game.content.dialogue.SkillDialogueHandler.SkillDialogue;
import core.game.node.entity.skill.fletching.items.bolts.Bolt;
import core.game.node.entity.skill.fletching.items.bolts.BoltPulse;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.player.Player;
import org.crandor.net.packet.PacketRepository;
import org.crandor.net.packet.context.ChildPositionContext;
import org.crandor.net.packet.out.RepositionChild;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

*/
/**
 * Represents the bolt creating plugin.
 * @author 'Vexia
 * @version 1.0
 *//*

@InitializablePlugin
public final class BoltCreatePlugin extends UseWithHandler {

	*/
/**
	 * Constructs a new {@code BoltCreatePlugin} {@code Object}.
	 *//*

	public BoltCreatePlugin() {
		super(314);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		for (Bolt bolt : Bolt.values()) {
			addHandler(bolt.getItem().getId(), ITEM_TYPE, this);
		}
		return this;
	}

	@Override
	public boolean handle(final NodeUsageEvent event) {
		final Player player = event.getPlayer();

	}

}
*/
