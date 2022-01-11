package core.game.interaction.inter;

import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.plugin.Initializable;
import core.game.node.entity.skill.magic.MagicSpell;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import rs09.game.node.entity.skill.magic.SpellListener;
import rs09.game.node.entity.skill.magic.SpellListeners;
import rs09.game.node.entity.skill.magic.SpellUtils;
import rs09.game.world.GameWorld;
import core.plugin.Plugin;

/**
 * Represents the magic book interface handling of non-combat spells.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class MagicBookInterface extends ComponentPlugin {

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ComponentDefinition.put(192, this);
		ComponentDefinition.put(193, this);
		ComponentDefinition.put(430, this);
		return this;
	}

	@Override
	public boolean handle(final Player player, Component component, int opcode, int button, int slot, int itemId) {
		if (GameWorld.getTicks() < player.getAttribute("magic:delay", -1)) {
			return true;
		}
		SpellListeners.run(button, SpellListener.NONE, SpellUtils.getBookFromInterface(component.getId()),player,null);
		return MagicSpell.castSpell(player, component.getId() == 192 ? SpellBook.MODERN : component.getId() == 193 ? SpellBook.ANCIENT : SpellBook.LUNAR, button, player);
	}
}
