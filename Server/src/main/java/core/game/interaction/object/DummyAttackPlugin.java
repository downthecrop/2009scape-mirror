package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.node.entity.skill.Skills;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the dummy attack plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class DummyAttackPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(2038).getHandlers().put("option:attack", this);
		SceneryDefinition.forId(15624).getHandlers().put("option:attack", this);
		SceneryDefinition.forId(15625).getHandlers().put("option:attack", this);
		SceneryDefinition.forId(15626).getHandlers().put("option:attack", this);
		SceneryDefinition.forId(15627).getHandlers().put("option:attack", this);
		SceneryDefinition.forId(15628).getHandlers().put("option:attack", this);
		SceneryDefinition.forId(15629).getHandlers().put("option:attack", this);
		SceneryDefinition.forId(15630).getHandlers().put("option:attack", this);
		SceneryDefinition.forId(18238).getHandlers().put("option:attack", this);
		SceneryDefinition.forId(25648).getHandlers().put("option:attack", this);
		SceneryDefinition.forId(28912).getHandlers().put("option:attack", this);
		SceneryDefinition.forId(823).getHandlers().put("option:attack", this);
		SceneryDefinition.forId(23921).getHandlers().put("option:attack", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		player.lock(3);
		player.animate(player.getProperties().getAttackAnimation());
		if (player.getProperties().getCurrentCombatLevel() < 8) {
			final Player p = player;
			double experience = 5;
			switch (p.getProperties().getAttackStyle().getStyle()) {
			case WeaponInterface.STYLE_ACCURATE:
				p.getSkills().addExperience(Skills.ATTACK, experience);
				break;
			case WeaponInterface.STYLE_AGGRESSIVE:// strength.
				p.getSkills().addExperience(Skills.STRENGTH, experience);
				break;
			case WeaponInterface.STYLE_DEFENSIVE:// defence.
				p.getSkills().addExperience(Skills.DEFENCE, experience);
				break;
			case WeaponInterface.STYLE_CONTROLLED:// shared.
				experience /= 3;
				p.getSkills().addExperience(Skills.ATTACK, experience);
				p.getSkills().addExperience(Skills.STRENGTH, experience);
				p.getSkills().addExperience(Skills.DEFENCE, experience);
				break;
			}
		} else {
			player.getPacketDispatch().sendMessage("You swing at the dummy...");
			player.getPacketDispatch().sendMessage("There is nothing more you can learn from hitting a dummy.");
		}
		return true;
	}

}
