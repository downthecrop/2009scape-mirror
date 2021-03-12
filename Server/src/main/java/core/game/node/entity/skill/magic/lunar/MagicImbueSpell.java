package core.game.node.entity.skill.magic.lunar;

import core.plugin.Initializable;
import core.game.node.entity.skill.magic.MagicSpell;
import core.game.node.entity.skill.magic.Runes;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.SpellType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.item.Item;
import rs09.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;

/**
 * The magic imbue spell.
 * @author 'Vexia
 */
@Initializable
public class MagicImbueSpell extends MagicSpell {

	/**
	 * The graphic.
	 */
	private static final Graphics GRAPHIC = new Graphics(141, 96);

	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(722);

	/**
	 * Constructs a new {@code StatRestoreSpell} {@code Object}.
	 */
	public MagicImbueSpell() {
		super(SpellBook.LUNAR, 82, 86, null, null, null, new Item[] { new Item(Runes.ASTRAL_RUNE.getId(), 2), new Item(Runes.FIRE_RUNE.getId(), 7), new Item(Runes.WATER_RUNE.getId(), 7) });
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.LUNAR.register(13, this);
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		final Player player = ((Player) entity);
		if (player == null) {
			return false;
		}
		if (player.getAttribute("spell:imbue", 0) > GameWorld.getTicks()) {
			player.sendMessage("You already have this activated.");
			return false;
		}
		if (!super.meetsRequirements(player, true, true)) {
			return false;
		}
		player.setAttribute("spell:imbue", GameWorld.getTicks() + 20);
		player.lock(ANIMATION.getDuration() + 1);
		player.graphics(GRAPHIC);
		player.animate(ANIMATION);
		player.getPacketDispatch().sendMessage("You are charged to combine runes!");
		return true;
	}
}
