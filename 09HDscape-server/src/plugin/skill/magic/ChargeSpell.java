package plugin.skill.magic;

import org.crandor.game.content.skill.free.magic.MagicSpell;
import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.entity.state.EntityState;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the charge spell magic spell.
 * @author Emperor
 * @version 1.0
 */
@InitializablePlugin
public final class ChargeSpell extends MagicSpell {

	/**
	 * Constructs a new {@code ChargeSpell} {@code Object}.
	 */
	public ChargeSpell() {
		super(SpellBook.MODERN, 80, 180, Animation.create(811), new Graphics(6, 96), null, new Item[] { Runes.FIRE_RUNE.getItem(3), Runes.BLOOD_RUNE.getItem(3), Runes.AIR_RUNE.getItem(3) });
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
		p.getStateManager().set(EntityState.CHARGED);
		p.getPacketDispatch().sendMessage("You feel charged with magic power.");
		return true;
	}

}
