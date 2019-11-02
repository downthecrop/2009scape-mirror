package plugin.skill.magic.lunar;

import org.crandor.game.content.skill.free.magic.MagicSpell;
import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.entity.state.EntityState;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * The cure group spell.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class CureGroupSpell extends MagicSpell {

	/**
	 * Represents the animation of this graphics.
	 */
	private static final Animation ANIMATION = new Animation(4409);

	/**
	 * Represents the graphic to use.
	 */
	private static final Graphics GRAPHIC = new Graphics(751, 130);

	/**
	 * Constructs a new {@code CureGroupSpell} {@code Object}.
	 */
	public CureGroupSpell() {
		super(SpellBook.LUNAR, 74, 74, ANIMATION, null, null, new Item[] { new Item(Runes.ASTRAL_RUNE.getId(), 2), new Item(Runes.LAW_RUNE.getId(), 2), new Item(Runes.COSMIC_RUNE.getId(), 2) });
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.LUNAR.register(3, this);
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		Player p = (Player) entity;
		if (!meetsRequirements(entity, true, true)) {
			return false;
		}
		p.animate(ANIMATION);
		p.graphics(GRAPHIC);
		p.getStateManager().remove(EntityState.POISONED);
		for (Player players : RegionManager.getLocalPlayers(p, 1)) {
			Player o = (Player) players;
			if (!o.isActive() || o.getLocks().isInteractionLocked()) {
				continue;
			}
			if (!o.getSettings().isAcceptAid()) {
				continue;
			}
			o.getStateManager().remove(EntityState.POISONED);
			o.graphics(GRAPHIC);
		}
		return true;
	}

}
