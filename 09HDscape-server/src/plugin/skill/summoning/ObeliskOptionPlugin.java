package plugin.skill.summoning;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.member.summoning.SummoningCreator;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the option used on the summoning obelisk.
 * @author 'Vexia
 * @author Emperor
 */
@InitializablePlugin
public final class ObeliskOptionPlugin extends OptionHandler {

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (option) {
		case "infuse-pouch":
			SummoningCreator.open(player, true);
			return true;
		case "renew-points":
			if (player.getSkills().getLevel(Skills.SUMMONING) == player.getSkills().getStaticLevel(Skills.SUMMONING)) {
				player.getPacketDispatch().sendMessage("You already have full summoning points.");
				return true;
			}
			player.visualize(Animation.create(8502), Graphics.create(1308));
			player.getSkills().setLevel(Skills.SUMMONING, player.getSkills().getStaticLevel(Skills.SUMMONING));
			player.getPacketDispatch().sendMessage("You renew your summoning points.");
			return true;
		}
		return false;
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.setOptionHandler("infuse-pouch", this);
		ObjectDefinition.setOptionHandler("renew-points", this);
		return this;
	}

}
