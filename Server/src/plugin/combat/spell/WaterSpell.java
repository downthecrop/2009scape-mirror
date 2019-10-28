package plugin.combat.spell;

import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatSpell;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.impl.Projectile;
import org.crandor.game.node.entity.impl.Animator.Priority;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the water spells.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class WaterSpell extends CombatSpell {

	/**
	 * The start graphic for Water strike.
	 */
	private static final Graphics STRIKE_START = new Graphics(93, 96);

	/**
	 * The projectile for Water strike.
	 */
	private static final Projectile STRIKE_PROJECTILE = Projectile.create((Entity) null, null, 94, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Water strike.
	 */
	private static final Graphics STRIKE_END = new Graphics(95, 96);

	/**
	 * The start graphic for Water bolt.
	 */
	private static final Graphics BOLT_START = new Graphics(120, 96);

	/**
	 * The projectile for Water bolt.
	 */
	private static final Projectile BOLT_PROJECTILE = Projectile.create((Entity) null, null, 121, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Water bolt.
	 */
	private static final Graphics BOLT_END = new Graphics(122, 96);

	/**
	 * The start graphic for Water blast.
	 */
	private static final Graphics BLAST_START = new Graphics(135, 96); // 129

	/**
	 * The projectile for Water blast.
	 */
	private static final Projectile BLAST_PROJECTILE = Projectile.create((Entity) null, null, 136, 40, 36, 52, 75, 15, 11); // 130

	/**
	 * The end graphic for Water blast.
	 */
	private static final Graphics BLAST_END = new Graphics(137, 96); // 131

	/**
	 * The start graphic for Water wave.
	 */
	private static final Graphics WAVE_START = new Graphics(161, 96);

	/**
	 * The projectile for Water wave.
	 */
	private static final Projectile WAVE_PROJECTILE = Projectile.create((Entity) null, null, 162, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Water wave.
	 */
	private static final Graphics WAVE_END = new Graphics(163, 96);

	/**
	 * The cast animation.
	 */
	private static final Animation ANIMATION = new Animation(711, Priority.HIGH);

	/**
	 * Constructs a new {@code WaterSpell} {@Code Object}
	 */
	public WaterSpell() {
		/*
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code WaterSpell} {@Code Object}
	 * @param type The spell type.
	 * @param level The level requirement.
	 * @param sound The cast sound.
	 * @param start The start graphics.
	 * @param projectile The projectile.
	 * @param end The end graphics.
	 * @param runes The rune requirements.
	 */
	private WaterSpell(SpellType type, int level, double baseExperience, int sound, Graphics start, Projectile projectile, Graphics end, Item... runes) {
		super(type, SpellBook.MODERN, level, baseExperience, sound, sound + 1, ANIMATION, start, projectile, end, runes);
	}

	@Override
	public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
		return getType().getImpactAmount(entity, victim, 2);
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType type) throws Throwable {
		SpellBook.MODERN.register(4, new WaterSpell(SpellType.STRIKE, 5, 7.5, 211, STRIKE_START, STRIKE_PROJECTILE, STRIKE_END, Runes.MIND_RUNE.getItem(1), Runes.WATER_RUNE.getItem(1), Runes.AIR_RUNE.getItem(1)));
		SpellBook.MODERN.register(14, new WaterSpell(SpellType.BOLT, 23, 16.5, 209, BOLT_START, BOLT_PROJECTILE, BOLT_END, Runes.CHAOS_RUNE.getItem(1), Runes.WATER_RUNE.getItem(2), Runes.AIR_RUNE.getItem(2)));
		SpellBook.MODERN.register(27, new WaterSpell(SpellType.BLAST, 47, 28.5, 207, BLAST_START, BLAST_PROJECTILE, BLAST_END, Runes.DEATH_RUNE.getItem(1), Runes.WATER_RUNE.getItem(3), Runes.AIR_RUNE.getItem(3)));
		SpellBook.MODERN.register(48, new WaterSpell(SpellType.WAVE, 65, 37.5, 213, WAVE_START, WAVE_PROJECTILE, WAVE_END, Runes.BLOOD_RUNE.getItem(1), Runes.WATER_RUNE.getItem(7), Runes.AIR_RUNE.getItem(5)));
		return this;
	}

}
