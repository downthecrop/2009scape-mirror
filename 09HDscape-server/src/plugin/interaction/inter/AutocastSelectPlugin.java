package plugin.interaction.inter;

import org.crandor.game.component.Component;
import org.crandor.game.component.ComponentDefinition;
import org.crandor.game.component.ComponentPlugin;
import org.crandor.game.node.entity.combat.equipment.WeaponInterface;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the auto casting component plugin.
 * @author Emperor
 * 
 */
@InitializablePlugin
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
