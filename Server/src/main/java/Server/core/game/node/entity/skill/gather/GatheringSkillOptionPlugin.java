package core.game.node.entity.skill.gather;

import core.cache.def.impl.ObjectDefinition;
import core.game.node.entity.skill.gather.mining.MiningSkillPulse;
import core.game.node.entity.skill.gather.woodcutting.WoodcuttingSkillPulse;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the gathering skill option handler plugin.
 * @author Emperor
 * @version 1.0
 */
@Initializable
public final class GatheringSkillOptionPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.setOptionHandler("chop-down", this);
		ObjectDefinition.setOptionHandler("chop down", this);
        ObjectDefinition.setOptionHandler("cut down", this);
		ObjectDefinition.setOptionHandler("mine", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		if(option.equals("mine")){
			player.getPulseManager().run(new MiningSkillPulse(player, node.asObject()));
		} else {
			player.getPulseManager().run(new WoodcuttingSkillPulse(player, node.asObject()));
		}
		return true;
	}

}
