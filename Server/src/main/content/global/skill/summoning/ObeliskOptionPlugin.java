package content.global.skill.summoning;

import core.game.event.SummoningPointsRechargeEvent;
import core.cache.def.impl.SceneryDefinition;
import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;
import org.rs09.consts.Sounds;

import static core.api.ContentAPIKt.playAudio;

/**
 * Represents the option used on the summoning obelisk.
 * @author 'Vexia
 * @author Emperor
 */
@Initializable
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
            playAudio(player, Sounds.DREADFOWL_BOOST_4214);
			player.getSkills().setLevel(Skills.SUMMONING, player.getSkills().getStaticLevel(Skills.SUMMONING));
			player.dispatch(new SummoningPointsRechargeEvent(node));
			player.getPacketDispatch().sendMessage("You renew your summoning points.");
			return true;
		}
		return false;
	}
	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.setOptionHandler("infuse-pouch", this);
		SceneryDefinition.setOptionHandler("renew-points", this);
		return this;
	}

}
