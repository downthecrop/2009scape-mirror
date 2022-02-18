package core.game.node.entity.skill.magic;

import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.SpellType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.entity.state.EntityState;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the charge spell magic spell.
 * @author Emperor
 * @version 1.0
 */
@Initializable
public final class ChargeSpell extends MagicSpell {

	/**
	 * Constructs a new {@code ChargeSpell} {@code Object}.
	 */
	public ChargeSpell() {
		super(SpellBook.MODERN, 80, 180, Animation.create(811), new Graphics(6, 96), new Audio(1651), new Item[] { Runes.FIRE_RUNE.getItem(3), Runes.BLOOD_RUNE.getItem(3), Runes.AIR_RUNE.getItem(3) });
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.MODERN.register(58, this);
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		final Player p = (Player) entity;
		if (p.getLocks().isLocked("charge_cast")) {
			p.getPacketDispatch().sendMessage("You need to wait for the spell to recharge.");
			return false;
		}
		if (!meetsRequirements(entity, true, true)) {
			return false;
		}
		p.getLocks().lock("charge_cast", 100);
		visualize(entity, target);
		p.registerState("godcharge").init();
		p.getPacketDispatch().sendMessage("You feel charged with magic power.");
		return true;
	}

}
