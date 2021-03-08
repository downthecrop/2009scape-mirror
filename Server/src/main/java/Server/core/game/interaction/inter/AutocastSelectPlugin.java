package core.game.interaction.inter;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the auto casting component plugin.
 * @author Emperor
 * 
 */
@Initializable
public final class AutocastSelectPlugin extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.forId(797).setPlugin(this);
		ComponentDefinition.forId(319).setPlugin(this);
		ComponentDefinition.forId(310).setPlugin(this);
		ComponentDefinition.forId(406).setPlugin(this);
		return this;
	}

	@Override
	public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
		if (!player.getAttribute("autocast_select", false)) {
			return true;
		}
		player.removeAttribute("autocast_select");
		final WeaponInterface w = player.getExtension(WeaponInterface.class);
		if (w != null) {
			w.selectAutoSpell(button, true);
			player.getInterfaceManager().openTab(w);
		}
		return true;
	}

}
