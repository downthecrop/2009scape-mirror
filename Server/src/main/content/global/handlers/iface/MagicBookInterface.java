package content.global.handlers.iface;

import content.global.skill.magic.SpellListener;
import content.global.skill.magic.SpellListeners;
import content.global.skill.magic.SpellUtils;
import core.game.event.SpellCastEvent;
import core.game.component.Component;
import core.game.component.ComponentDefinition;
import core.game.component.ComponentPlugin;
import core.plugin.Initializable;
import core.game.node.entity.combat.spell.MagicSpell;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.world.GameWorld;
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

		SpellBook spellBook = component.getId() == 192
				? SpellBook.MODERN
				: component.getId() == 193
					? SpellBook.ANCIENT
					: SpellBook.LUNAR;

		SpellListeners.run(button, SpellListener.NONE, SpellUtils.getBookFromInterface(component.getId()),player,null);
		boolean result = MagicSpell.castSpell(player, spellBook, button, player);

		return result;
	}
}
