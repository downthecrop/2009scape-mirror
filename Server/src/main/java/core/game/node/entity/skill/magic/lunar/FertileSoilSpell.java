package core.game.node.entity.skill.magic.lunar;

import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.equipment.SpellType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.entity.skill.magic.MagicSpell;
import core.game.node.entity.skill.magic.Runes;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import rs09.game.node.entity.skill.farming.CompostType;
import rs09.game.node.entity.skill.farming.FarmingPatch;
import rs09.game.node.entity.skill.farming.Patch;

/**
 * Represents the fertile soil spell plugin.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class FertileSoilSpell extends MagicSpell {

	/**
	 * Represents the graphic to use.
	 */
	private static final Graphics GRAPHIC = new Graphics(141, 96);

	/**
	 * Represents the animaton to use.
	 */
	private static final Animation ANIMATION = new Animation(722);

	/**
	 * Constructs a new {@code FertileSoilSpell} {@code Object}.
	 */
	public FertileSoilSpell() {
		super(SpellBook.LUNAR, 83, 87, null, null, null, new Item[] { new Item(Runes.NATURE_RUNE.getId(), 2), new Item(Runes.ASTRAL_RUNE.getId(), 3), new Item(Runes.EARTH_RUNE.getId(), 15) });
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.LUNAR.register(2, this);
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		final Player player = ((Player) entity);
		final Scenery object = (Scenery) target;
		final FarmingPatch fPatch = FarmingPatch.forObject(object);
		if(fPatch == null){
			player.sendMessage("Um... I don't want to fertilize that!");
			return false;
		}
		final Patch patch = fPatch.getPatchFor(player);
		if(patch.getCompost() != CompostType.NONE){
			player.sendMessage("This patch has already been composted.");
			return false;
		}
		if (!super.meetsRequirements(player, true, true)) {
			return false;
		}
		patch.setCompost(CompostType.SUPER);
		player.sendMessage("You fertilize the soil.");
		player.graphics(GRAPHIC);
		player.animate(ANIMATION);
		return true;
	}
}
