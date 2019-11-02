package plugin.interaction.object;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the dummy attack plugin.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class DummyAttackPlugin extends OptionHandler {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ObjectDefinition.forId(2038).getConfigurations().put("option:attack", this);
		ObjectDefinition.forId(15624).getConfigurations().put("option:attack", this);
		ObjectDefinition.forId(15625).getConfigurations().put("option:attack", this);
		ObjectDefinition.forId(15626).getConfigurations().put("option:attack", this);
		ObjectDefinition.forId(15627).getConfigurations().put("option:attack", this);
		ObjectDefinition.forId(15628).getConfigurations().put("option:attack", this);
		ObjectDefinition.forId(15629).getConfigurations().put("option:attack", this);
		ObjectDefinition.forId(15630).getConfigurations().put("option:attack", this);
		ObjectDefinition.forId(18238).getConfigurations().put("option:attack", this);
		ObjectDefinition.forId(25648).getConfigurations().put("option:attack", this);
		ObjectDefinition.forId(28912).getConfigurations().put("option:attack", this);
		ObjectDefinition.forId(823).getConfigurations().put("option:attack", this);
		ObjectDefinition.forId(23921).getConfigurations().put("option:attack", this);
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
