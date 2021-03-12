package core.game.node.entity.combat.spell;

import core.game.node.entity.skill.magic.Runes;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatSpell;
import core.game.node.entity.combat.equipment.SpellType;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the earth combat spells.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class EarthSpell extends CombatSpell {

	/**
	 * The start graphic for Earth strike.
	 */
	private static final Graphics STRIKE_START = new Graphics(96, 96);

	/**
	 * The projectile for Earth strike.
	 */
	private static final Projectile STRIKE_PROJECTILE = Projectile.create((Entity) null, null, 97, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Earth strike.
	 */
	private static final Graphics STRIKE_END = new Graphics(98, 96);

	/**
	 * The start graphic for Earth bolt.
	 */
	private static final Graphics BOLT_START = new Graphics(123, 96);

	/**
	 * The projectile for Earth bolt.
	 */
	private static final Projectile BOLT_PROJECTILE = Projectile.create((Entity) null, null, 124, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Earth bolt.
	 */
	private static final Graphics BOLT_END = new Graphics(125, 96);

	/**
	 * The start graphic for Earth blast.
	 */
	private static final Graphics BLAST_START = new Graphics(138, 96);

	/**
	 * The projectile for Earth blast.
	 */
	private static final Projectile BLAST_PROJECTILE = Projectile.create((Entity) null, null, 139, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Earth blast.
	 */
	private static final Graphics BLAST_END = new Graphics(140, 96);

	/**
	 * The start graphic for Earth wave.
	 */
	private static final Graphics WAVE_START = new Graphics(164, 96);

	/**
	 * The projectile for Earth wave.
	 */
	private static final Projectile WAVE_PROJECTILE = Projectile.create((Entity) null, null, 165, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Earth wave.
	 */
	private static final Graphics WAVE_END = new Graphics(166, 96);

	/**
	 * The cast animation.
	 */
	private static final Animation ANIMATION = new Animation(711, Priority.HIGH);

	/**
	 * Constructs a new {@code EarthSpell} {@Code Object}
	 */
	public EarthSpell() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code EarthSpell} {@Code Object}.
	 * @param type The spell type.
	 * @param level The level requirement.
	 * @param sound The cast sound.
	 * @param start The start graphics.
	 * @param projectile The projectile.
	 * @param end The end graphics.
	 * @param runes The rune requirements.
	 */
	private EarthSpell(SpellType type, int level, double baseExperience, int sound, Graphics start, Projectile projectile, Graphics end, Item... runes) {
		super(type, SpellBook.MODERN, level, baseExperience, sound, sound + 1, ANIMATION, start, projectile, end, runes);
	}

	@Override
	public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
		return getType().getImpactAmount(entity, victim, 3);
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType type) throws Throwable {
		SpellBook.MODERN.register(6, new EarthSpell(SpellType.STRIKE, 9, 9.5, 132, STRIKE_START, STRIKE_PROJECTILE, STRIKE_END, Runes.MIND_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(2), Runes.AIR_RUNE.getItem(1)));
		SpellBook.MODERN.register(17, new EarthSpell(SpellType.BOLT, 29, 19.5, 130, BOLT_START, BOLT_PROJECTILE, BOLT_END, Runes.CHAOS_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(3), Runes.AIR_RUNE.getItem(2)));
		SpellBook.MODERN.register(33, new EarthSpell(SpellType.BLAST, 53, 31.5, 128, BLAST_START, BLAST_PROJECTILE, BLAST_END, Runes.DEATH_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(4), Runes.AIR_RUNE.getItem(3)));
		SpellBook.MODERN.register(52, new EarthSpell(SpellType.WAVE, 70, 40.0, 134, WAVE_START, WAVE_PROJECTILE, WAVE_END, Runes.BLOOD_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(7), Runes.AIR_RUNE.getItem(5)));
		return this;
	}

}
