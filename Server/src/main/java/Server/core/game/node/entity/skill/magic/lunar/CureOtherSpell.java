package core.game.node.entity.skill.magic.lunar;

import core.game.node.entity.skill.magic.MagicSpell;
import core.game.node.entity.skill.magic.Runes;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.SpellType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.entity.state.EntityState;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * The cure other spell.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class CureOtherSpell extends MagicSpell {

	/**
	 * Represents the animation of this graphics.
	 */
	private static final Animation ANIMATION = new Animation(4411);

	/**
	 * Repesents the graphick, next spells of this spell.
	 */
	private static final Graphics GRAPHIC = new Graphics(738, 130);

	/**
	 * Constructs a new {@code CureOtherSpell} {@code Object}.
	 */
	public CureOtherSpell() {
		super(SpellBook.LUNAR, 68, 65, ANIMATION, null, null, new Item[] { new Item(Runes.ASTRAL_RUNE.getId(), 1), new Item(Runes.LAW_RUNE.getId(), 1), new Item(Runes.EARTH_RUNE.getId(), 10) });
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.LUNAR.register(1, this);
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		Player p = (Player) entity;
		if (!(target instanceof Player)) {
			p.getPacketDispatch().sendMessage("You can only cast this spell on other players.");
			return false;
		}
		Player o = (Player) target;
		if (!o.isActive() || o.getLocks().isInteractionLocked()) {
			p.getPacketDispatch().sendMessage("This player is busy.");
			return false;
		}
		if (!o.getSettings().isAcceptAid()) {
			p.getPacketDispatch().sendMessage("The player is not accepting any aid.");
			return false;
		}
		if (!o.getStateManager().hasState(EntityState.POISONED)) {
			p.getPacketDispatch().sendMessage("This player is not poisoned.");
			return false;
		}
		if (!meetsRequirements(entity, true, true)) {
			return false;
		}
		p.face(o);
		p.animate(ANIMATION);
		o.graphics(GRAPHIC);
		o.getStateManager().remove(EntityState.POISONED);
		return true;
	}

}
