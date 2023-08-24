package content.global.skill.magic.ancient;

import java.util.List;

import core.game.node.entity.combat.spell.Runes;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.spell.CombatSpell;
import core.game.node.entity.combat.spell.SpellType;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs09.consts.Sounds;

import static core.api.ContentAPIKt.playGlobalAudio;

/**
 * Handles the Blood spells from the Ancient spellbook.
 * @author Emperor
 * @version 1.0
 */
@Initializable
public final class BloodSpells extends CombatSpell {

	/**
	 * The projectile for Blood rush.
	 */
	private static final Projectile RUSH_PROJECTILE = Projectile.create((Entity) null, null, 372, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Blood rush.
	 */
	private static final Graphics RUSH_END = new Graphics(373, 96);

	/**
	 * The end graphic for Blood burst.
	 */
	private static final Graphics BURST_END = new Graphics(376, 0);

	/**
	 * The projectile for Blood blitz.
	 */
	private static final Projectile BLITZ_PROJECTILE = Projectile.create((Entity) null, null, 374, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Blood blitz.
	 */
	private static final Graphics BLITZ_END = new Graphics(375, 96);

	/**
	 * The end graphic for Blood barrage.
	 */
	private static final Graphics BARRAGE_END = new Graphics(377, 0);

	/**
	 * Constructs a new {@code BloodSpells} {@code Object}.
	 */
	public BloodSpells() {
		/*
		 * ( empty.
		 */
	}

	/**
	 * Constructs a new {@code BloodSpells} {@Code Object}
	 * @param type The spell type.
	 * @param impactSound The impact sound id.
	 * @param anim The animation.
	 * @param start The start graphics.
	 * @param projectile The projectile.
	 * @param end The end graphics.
	 */
	private BloodSpells(SpellType type, int level, double baseExperience, int sound, int impactSound, Animation anim, Graphics start, Projectile projectile, Graphics end, Item... runes) {
		super(type, SpellBook.ANCIENT, level, baseExperience, sound, impactSound, anim, start, projectile, end, runes);
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.ANCIENT.register(4, new BloodSpells(SpellType.RUSH, 56, 33.0, Sounds.BLOOD_RUSH_CASTING_108, Sounds.BLOOD_RUSH_IMPACT_110, new Animation(1978, Priority.HIGH), null, RUSH_PROJECTILE, RUSH_END, Runes.BLOOD_RUNE.getItem(1), Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(2)));
		SpellBook.ANCIENT.register(6, new BloodSpells(SpellType.BURST, 68, 39.0, Sounds.BLOOD_CAST_106, Sounds.BLOOD_BURST_IMPACT_105, new Animation(1979, Priority.HIGH), null, null, BURST_END, Runes.BLOOD_RUNE.getItem(2), Runes.DEATH_RUNE.getItem(2), Runes.CHAOS_RUNE.getItem(4)));
		SpellBook.ANCIENT.register(5, new BloodSpells(SpellType.BLITZ, 80, 45.0, Sounds.BLOOD_CAST_106, Sounds.BLOOD_BLITZ_IMPACT_104, new Animation(1978, Priority.HIGH), null, BLITZ_PROJECTILE, BLITZ_END, Runes.BLOOD_RUNE.getItem(4), Runes.DEATH_RUNE.getItem(2)));
		SpellBook.ANCIENT.register(7, new BloodSpells(SpellType.BARRAGE, 92, 51.0, Sounds.BLOOD_CAST_106, Sounds.BLOOD_BARRAGE_IMPACT_102, new Animation(1979, Priority.HIGH), null, null, BARRAGE_END, Runes.SOUL_RUNE.getItem(1), Runes.BLOOD_RUNE.getItem(4), Runes.DEATH_RUNE.getItem(4)));
		return this;
	}

	@Override
	public void visualize(Entity entity, Node target) {
		entity.graphics(graphic);
		if (projectile != null) {
			projectile.transform(entity, (Entity) target, false, 58, 10).send();
		}
		entity.animate(animation);
		playGlobalAudio(entity.getLocation(), audio.id, 20);
	}

	@Override
	public void fireEffect(Entity entity, Entity victim, BattleState state) {
		if (state.getEstimatedHit() > -1) {
			int heal = state.getEstimatedHit() / 4;
			if (heal > 0) {
				entity.getSkills().heal(heal);
				if (entity instanceof Player) {
					((Player) entity).getPacketDispatch().sendMessage("You drain some of your opponent's health.");
				}
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
		return getType().getImpactAmount(entity, victim, 3);
	}

}
