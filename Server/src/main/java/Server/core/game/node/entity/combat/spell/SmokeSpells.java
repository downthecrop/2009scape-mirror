package core.game.node.entity.combat.spell;

import java.util.List;

import core.game.node.entity.skill.magic.Runes;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatSpell;
import core.game.node.entity.combat.equipment.SpellType;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.entity.state.EntityState;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the Smoke spells from the Ancient spellbook.
 * @author Emperor
 */
@Initializable
public final class SmokeSpells extends CombatSpell {

	/**
	 * The projectile for Smoke rush.
	 */
	private static final Projectile RUSH_PROJECTILE = Projectile.create((Entity) null, null, 384, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Smoke rush.
	 */
	private static final Graphics RUSH_END = new Graphics(385, 96);

	/**
	 * The projectile for Smoke burst.
	 */
	private static final Projectile BURST_PROJECTILE = Projectile.create((Entity) null, null, 386, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Smoke burst.
	 */
	private static final Graphics BURST_END = new Graphics(387, 0);

	/**
	 * The projectile for Smoke blitz.
	 */
	private static final Projectile BLITZ_PROJECTILE = Projectile.create((Entity) null, null, 389, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Smoke blitz.
	 */
	private static final Graphics BLITZ_END = new Graphics(388, 96);

	/**
	 * The projectile for Smoke barrage.
	 */
	private static final Projectile BARRAGE_PROJECTILE = Projectile.create((Entity) null, null, 391, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Smoke barrage.
	 */
	private static final Graphics BARRAGE_END = new Graphics(390, 0);

	/**
	 * Constructs a new {@code SmokeSpells} {@code Object}.
	 */
	public SmokeSpells() {
		/*
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code SmokeSpells} {@Code Object}
	 * @param type The spell type.
	 * @param level The level requirement.
	 * @param sound The casting sound.
	 * @param impactSound The impact sound id.
	 * @param anim The animation.
	 * @param start The start graphics.
	 * @param projectile The projectile.
	 * @param end The end graphics.
	 */
	private SmokeSpells(SpellType type, int level, double baseExperience, int sound, int impactSound, Animation anim, Graphics start, Projectile projectile, Graphics end, Item... runes) {
		super(type, SpellBook.ANCIENT, level, baseExperience, sound, impactSound, anim, start, projectile, end, runes);
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.ANCIENT.register(8, new SmokeSpells(SpellType.RUSH, 50, 30.0, 176, 177, new Animation(1978, Priority.HIGH), null, RUSH_PROJECTILE, RUSH_END, Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(1), Runes.AIR_RUNE.getItem(1)));
		SpellBook.ANCIENT.register(10, new SmokeSpells(SpellType.BURST, 62, 36.0, 179, 180, new Animation(1979, Priority.HIGH), null, BURST_PROJECTILE, BURST_END, Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(4), Runes.FIRE_RUNE.getItem(2), Runes.AIR_RUNE.getItem(2)));
		SpellBook.ANCIENT.register(9, new SmokeSpells(SpellType.BLITZ, 74, 42.0, 183, 184, new Animation(1978, Priority.HIGH), null, BLITZ_PROJECTILE, BLITZ_END, Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(2), Runes.AIR_RUNE.getItem(2)));
		SpellBook.ANCIENT.register(11, new SmokeSpells(SpellType.BARRAGE, 86, 48.0, 183, 185, new Animation(1979, Priority.HIGH), null, BARRAGE_PROJECTILE, BARRAGE_END, Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(4), Runes.FIRE_RUNE.getItem(4), Runes.AIR_RUNE.getItem(4)));
		return this;
	}

	@Override
	public void visualize(Entity entity, Node target) {
		entity.graphics(graphic);
		if (projectile != null) {
			projectile.transform(entity, (Entity) target, false, 58, 10).send();
		}
		entity.animate(animation);
		sendAudio(entity, audio);
	}

	@Override
	public void fireEffect(Entity entity, Entity victim, BattleState state) {
		if (state.getEstimatedHit() > -1) {
			victim.getStateManager().register(EntityState.POISONED, false, type.ordinal() >= SpellType.BLITZ.ordinal() ? 48 : 28, entity);
		}
	}

	@Override
	public BattleState[] getTargets(Entity entity, Entity target) {
		if (animation.getId() == 1978 || !entity.getProperties().isMultiZone() || !target.getProperties().isMultiZone()) {
			return super.getTargets(entity, target);
		}
		List<Entity> list = getMultihitTargets(entity, target, 9);
		BattleState[] targets = new BattleState[list.size()];
		int index = 0;
		for (Entity e : list) {
			targets[index++] = new BattleState(entity, e);
		}
		return targets;
	}

	@Override
	public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
		return getType().getImpactAmount(entity, victim, 1);
	}

}
