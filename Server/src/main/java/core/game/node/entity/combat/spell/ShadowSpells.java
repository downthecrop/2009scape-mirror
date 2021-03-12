package core.game.node.entity.combat.spell;

import java.util.List;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.magic.Runes;
import core.game.node.Node;
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
 * Handles the Shadow spells from the Ancient spellbook.
 * @author Emperor
 */
@Initializable
public final class ShadowSpells extends CombatSpell {

	/**
	 * The projectile for Shadow rush.
	 */
	private static final Projectile RUSH_PROJECTILE = Projectile.create((Entity) null, null, 378, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Shadow rush.
	 */
	private static final Graphics RUSH_END = new Graphics(379, 96);

	/**
	 * The projectile for Shadow burst.
	 */
	private static final Projectile BURST_PROJECTILE = Projectile.create((Entity) null, null, 380, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Shadow burst.
	 */
	private static final Graphics BURST_END = new Graphics(381, 0);

	/**
	 * The end graphic for Shadow blitz.
	 */
	private static final Graphics BLITZ_END = new Graphics(382, 96);

	/**
	 * The end graphic for Shadow barrage.
	 */
	private static final Graphics BARRAGE_END = new Graphics(383, 0);

	/**
	 * Constructs a new {@code ShadowSpells} {@code Object}.
	 */
	public ShadowSpells() {
		/*
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code ShadowSpells} {@Code Object}
	 * @param type The spell type.
	 * @param level The level requirement.
	 * @param sound The casting sound.
	 * @param impactSound The impact sound id.
	 * @param anim The animation.
	 * @param start The start graphics.
	 * @param projectile The projectile.
	 * @param end The end graphics.
	 */
	private ShadowSpells(SpellType type, int level, double baseExperience, int sound, int impactSound, Animation anim, Graphics start, Projectile projectile, Graphics end, Item... runes) {
		super(type, SpellBook.ANCIENT, level, baseExperience, sound, impactSound, anim, start, projectile, end, runes);
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.ANCIENT.register(12, new ShadowSpells(SpellType.RUSH, 52, 31.0, 175, 176, new Animation(1978, Priority.HIGH), null, RUSH_PROJECTILE, RUSH_END, Runes.SOUL_RUNE.getItem(1), Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(2), Runes.AIR_RUNE.getItem(1)));
		SpellBook.ANCIENT.register(14, new ShadowSpells(SpellType.BURST, 64, 37.0, 177, 178, new Animation(1979, Priority.HIGH), null, BURST_PROJECTILE, BURST_END, Runes.SOUL_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(4), Runes.AIR_RUNE.getItem(1)));
		SpellBook.ANCIENT.register(13, new ShadowSpells(SpellType.BLITZ, 76, 43.0, 181, 182, new Animation(1978, Priority.HIGH), null, null, BLITZ_END, Runes.SOUL_RUNE.getItem(2), Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(2), Runes.AIR_RUNE.getItem(2)));
		SpellBook.ANCIENT.register(15, new ShadowSpells(SpellType.BARRAGE, 88, 48.0, 181, 185, new Animation(1979, Priority.HIGH), null, null, BARRAGE_END, Runes.SOUL_RUNE.getItem(3), Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(4), Runes.AIR_RUNE.getItem(4)));
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
			int level = victim.getSkills().getStaticLevel(Skills.ATTACK);
			victim.getSkills().updateLevel(Skills.ATTACK, (int) -(level * 0.1), (int) (level - (level * 0.1)));
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
		return getType().getImpactAmount(entity, victim, 2);
	}

}
