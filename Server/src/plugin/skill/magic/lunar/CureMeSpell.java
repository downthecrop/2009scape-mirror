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
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * The cure me spell.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class CureMeSpell extends MagicSpell {

	/**
	 * Represents the animation of this graphics.
	 */
	private static final Animation ANIMATION = new Animation(4411);

	/**
	 * Repesents the graphick, next spells of this spell.
	 */
	private static final Graphics GRAPHIC = new Graphics(731, 90);

	/**
	 * Constructs a new {@code CureOtherSpell} {@code Object}.
	 */
	public CureMeSpell() {
		super(SpellBook.LUNAR, 71, 69, ANIMATION, null, null, new Item[] { new Item(Runes.ASTRAL_RUNE.getId(), 2), new Item(Runes.LAW_RUNE.getId(), 1), new Item(Runes.COSMIC_RUNE.getId(), 2) });
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.LUNAR.register(23, this);
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		Player p = (Player) entity;
		if (!p.getStateManager().hasState(EntityState.POISONED)) {
			p.getPacketDispatch().sendMessage("You are not poisoned.");
			return false;
		}
		if (!meetsRequirements(entity, true, true)) {
			return false;
		}
		p.animate(ANIMATION);
		p.graphics(GRAPHIC);
		p.getStateManager().remove(EntityState.POISONED);
		return true;
	}

}
