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
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.entity.state.EntityState;
import core.game.node.item.Item;
import rs09.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the Ice spells from the Ancient spellbook.
 * @author Emperor
 * @version 1.0
 */
@Initializable
public final class IceSpells extends CombatSpell {

	/**
	 * The barrage orb GFX.
	 */
	private static final Graphics BARRAGE_ORB = new Graphics(1677, 96); //119

	/**
	 * The projectile for Ice rush.
	 */
	private static final Projectile RUSH_PROJECTILE = Projectile.create((Entity) null, null, 360, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Ice rush.
	 */
	private static final Graphics RUSH_END = new Graphics(361, 96);

	/**
	 * The projectile for Ice barrage.
	 */
	private static final Projectile BURST_PROJECTILE = Projectile.create((Entity) null, null, 362, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Ice barrage.
	 */
	private static final Graphics BURST_END = new Graphics(363, 0);

	/**
	 * The start graphic for Ice rush.
	 */
	private static final Graphics BLITZ_START = new Graphics(366, 96);

	/**
	 * The end graphic for Ice rush.
	 */
	private static final Graphics BLITZ_END = new Graphics(367, 96);

	/**
	 * The projectile for Ice barrage.
	 */
	private static final Projectile BARRAGE_PROJECTILE = Projectile.create((Entity) null, null, 368, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Ice barrage.
	 */
	private static final Graphics BARRAGE_END = new Graphics(369, 0);

	/**
	 * Constructs a new {@code IceSpells} {@code Object}.
	 */
	public IceSpells() {
		/*
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code IceSpells} {@Code Object}
	 * @param type The spell type.
	 * @param impactSound The impact sound id.
	 * @param anim The animation.
	 * @param start The start graphics.
	 * @param projectile The projectile.
	 * @param end The end graphics.
	 */
	private IceSpells(SpellType type, int level, double baseExperience, int impactSound, Animation anim, Graphics start, Projectile projectile, Graphics end, Item... runes) {
		super(type, SpellBook.ANCIENT, level, baseExperience, 171, impactSound, anim, start, projectile, end, runes);
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
	public void visualizeImpact(Entity entity, Entity target, BattleState state) {
		if (state.isFrozen()) {
			if (target == entity.getProperties().getCombatPulse().getVictim()) {
				sendAudio(target, new Audio(impactAudio, 1, 20));
			}
			target.graphics(BARRAGE_ORB);
			return;
		}
		super.visualizeImpact(entity, target, state);
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.ANCIENT.register(0, new IceSpells(SpellType.RUSH, 58, 34.0, 173, new Animation(1978, Priority.HIGH), null, RUSH_PROJECTILE, RUSH_END, Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(2), Runes.WATER_RUNE.getItem(2)));
		SpellBook.ANCIENT.register(2, new IceSpells(SpellType.BURST, 70, 40.0, 170, new Animation(1979, Priority.HIGH), null, BURST_PROJECTILE, BURST_END, Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(4), Runes.WATER_RUNE.getItem(4)));
		SpellBook.ANCIENT.register(1, new IceSpells(SpellType.BLITZ, 82, 46.0, 169, new Animation(1978, Priority.HIGH), BLITZ_START, null, BLITZ_END, Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(2), Runes.WATER_RUNE.getItem(3)));
		SpellBook.ANCIENT.register(3, new IceSpells(SpellType.BARRAGE, 94, 52.0, 168, new Animation(1979, Priority.HIGH), null, BARRAGE_PROJECTILE, BARRAGE_END, Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(4), Runes.WATER_RUNE.getItem(6)));
		return this;
	}

	@Override
	public void fireEffect(Entity entity, Entity victim, BattleState state) {
		if (state.getEstimatedHit() == -1) {
			return;
		}
		int ticks = (1 + (type.ordinal() - SpellType.RUSH.ordinal())) * 8;
		if (state.getEstimatedHit() > -1) {
			if (victim.getAttribute("freeze_immunity", -1) < GameWorld.getTicks()) {
				victim.getStateManager().set(EntityState.FROZEN, ticks);
			} else if (type == SpellType.BARRAGE) {
				state.setFrozen(true);
			}
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
		return getType().getImpactAmount(entity, victim, 4);
	}

}
