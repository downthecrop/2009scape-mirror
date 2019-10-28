package plugin.skill.magic.lunar;

import org.crandor.game.content.skill.free.magic.MagicSpell;
import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the energy transfer spell.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class EnergyTransferSpell extends MagicSpell {

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(4411);

	/**
	 * Repesents the graphic, next spells of this spell.
	 */
	private static final Graphics GRAPHICS = new Graphics(738, 90);

	/**
	 * Constructs a new {@code EnergyTransferSpell} {@code Object}.
	 */
	public EnergyTransferSpell() {
		super(SpellBook.LUNAR, 91, 100, null, null, null, new Item[] { new Item(Runes.ASTRAL_RUNE.getId(), 3), new Item(Runes.LAW_RUNE.getId(), 2), new Item(Runes.NATURE_RUNE.getId(), 1) });
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.LUNAR.register(5, this);
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		if (!(target instanceof Player)) {
			return false;
		}
		final Player player = ((Player) entity);
		if (!super.meetsRequirements(player, true, true)) {
			return false;
		}
		final Player o = ((Player) target);
		int hp = (int) Math.floor(player.getSkills().getLifepoints() * 0.10);
		int run = hp;
		if (run > (100 - o.getSettings().getRunEnergy())) {
			run = (int) (100 - o.getSettings().getRunEnergy());
		}
		if (run < 0) {
			run = 0;
		}
		o.getSettings().updateRunEnergy(-run);
		player.getImpactHandler().manualHit(player, hp, HitsplatType.NORMAL, 2);
		int energy = 100;
		energy -= o.getSettings().getSpecialEnergy();
		if (energy < 0) {
			energy = 0;
		}
		if (energy > player.getSettings().getSpecialEnergy()) {
			energy = player.getSettings().getSpecialEnergy();
		}
		o.getSettings().setSpecialEnergy(o.getSettings().getSpecialEnergy() + energy);
		player.getSettings().setSpecialEnergy(player.getSettings().getSpecialEnergy() - energy);
		player.animate(ANIMATION);
		o.graphics(GRAPHICS);
		return true;
	}
}
