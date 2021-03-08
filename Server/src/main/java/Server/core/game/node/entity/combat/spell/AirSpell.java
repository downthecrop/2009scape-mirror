package core.game.node.entity.combat.spell;

import core.plugin.Initializable;
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
import core.plugin.Plugin;

/**
 * Represents the combat spell plugin used to handle air spells.
 * @author Emperor
 * @version 1.0
 */
@Initializable
public final class AirSpell extends CombatSpell {

	/**
	 * The start graphic for Air strike.
	 */
	private static final Graphics STRIKE_START = new Graphics(90, 96);

	/**
	 * The projectile for Air strike.
	 */
	private static final Projectile STRIKE_PROJECTILE = Projectile.create((Entity) null, null, 91, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Air strike.
	 */
	private static final Graphics STRIKE_END = new Graphics(92, 96);

	/**
	 * The start graphic for Air bolt.
	 */
	private static final Graphics BOLT_START = new Graphics(117, 96);

	/**
	 * The projectile for Air bolt.
	 */
	private static final Projectile BOLT_PROJECTILE = Projectile.create((Entity) null, null, 118, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Air bolt.
	 */
	private static final Graphics BOLT_END = new Graphics(119, 96);

	/**
	 * The start graphic for Air blast.
	 */
	private static final Graphics BLAST_START = new Graphics(132, 96); // 129

	/**
	 * The projectile for Air blast.
	 */
	private static final Projectile BLAST_PROJECTILE = Projectile.create((Entity) null, null, 133, 40, 36, 52, 75, 15, 11); // 130

	/**
	 * The end graphic for Air blast.
	 */
	private static final Graphics BLAST_END = new Graphics(134, 96); // 131

	/**
	 * The start graphic for Air wave.
	 */
	private static final Graphics WAVE_START = new Graphics(158, 96);

	/**
	 * The projectile for Air wave.
	 */
	private static final Projectile WAVE_PROJECTILE = Projectile.create((Entity) null, null, 159, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Air wave.
	 */
	private static final Graphics WAVE_END = new Graphics(160, 96);

	/**
	 * The cast animation.
	 */
	private static final Animation ANIMATION = new Animation(711, Priority.HIGH);

	/**
	 * Constructs a new {@code AirSpell} {@Code Object}
	 */
	public AirSpell() {
		/*
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code AirSpell} {@Code Object}
	 * @param type The spell type.
	 * @param level The level requirement.
	 * @param sound The cast sound.
	 * @param start The start graphics.
	 * @param projectile The projectile.
	 * @param end The end graphics.
	 * @param runes The rune requirements.
	 */
	private AirSpell(SpellType type, int level, double baseExperience, int sound, Graphics start, Projectile projectile, Graphics end, Item... runes) {
		super(type, SpellBook.MODERN, level, baseExperience, sound, sound + 1, ANIMATION, start, projectile, end, runes);
	}

	@Override
	public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
		return getType().getImpactAmount(entity, victim, 1);
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType type) throws Throwable {
		SpellBook.MODERN.register(1, new AirSpell(SpellType.STRIKE, 1, 5.5, 220, STRIKE_START, STRIKE_PROJECTILE, STRIKE_END, Runes.MIND_RUNE.getItem(1), Runes.AIR_RUNE.getItem(1)));
		SpellBook.MODERN.register(10, new AirSpell(SpellType.BOLT, 17, 13.5, 218, BOLT_START, BOLT_PROJECTILE, BOLT_END, Runes.CHAOS_RUNE.getItem(1), Runes.AIR_RUNE.getItem(2)));
		SpellBook.MODERN.register(24, new AirSpell(SpellType.BLAST, 41, 25.5, 216, BLAST_START, BLAST_PROJECTILE, BLAST_END, Runes.DEATH_RUNE.getItem(1), Runes.AIR_RUNE.getItem(3)));
		SpellBook.MODERN.register(45, new AirSpell(SpellType.WAVE, 62, 36.0, 222, WAVE_START, WAVE_PROJECTILE, WAVE_END, Runes.BLOOD_RUNE.getItem(1), Runes.AIR_RUNE.getItem(5)));
		return this;
	}
}
