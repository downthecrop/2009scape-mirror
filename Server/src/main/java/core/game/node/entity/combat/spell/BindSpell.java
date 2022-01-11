package core.game.node.entity.combat.spell;

import core.plugin.Initializable;
import core.game.node.entity.skill.magic.Runes;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatSpell;
import core.game.node.entity.combat.equipment.SpellType;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.item.Item;
import rs09.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;

/**
 * Represents the binding combat spell.
 * @author 'Vexia
 * @author Emperor
 * @version 1.0
 */
@Initializable
public final class BindSpell extends CombatSpell {

	/**
	 * The start graphic for Earth strike.
	 */
	private static final Graphics BIND_START = new Graphics(177, 96);

	/**
	 * The projectile for Earth strike.
	 */
	private static final Projectile BIND_PROJECTILE = Projectile.create((Entity) null, null, 178, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Earth strike.
	 */
	private static final Graphics BIND_END = new Graphics(181, 96);

	/**
	 * The start graphic for Earth strike.
	 */
	private static final Graphics SNARE_START = new Graphics(177, 96);

	/**
	 * The projectile for Earth strike.
	 */
	private static final Projectile SNARE_PROJECTILE = Projectile.create((Entity) null, null, 178, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Earth strike.
	 */
	private static final Graphics SNARE_END = new Graphics(180, 96);

	/**
	 * The start graphic for Earth strike.
	 */
	private static final Graphics ENTANGLE_START = new Graphics(177, 96);

	/**
	 * The projectile for Earth strike.
	 */
	private static final Projectile ENTANGLE_PROJECTILE = Projectile.create((Entity) null, null, 178, 40, 36, 52, 75, 15, 11);

	/**
	 * The end graphic for Earth strike.
	 */
	private static final Graphics ENTANGLE_END = new Graphics(179, 96);

	/**
	 * The cast animation.
	 */
	private static final Animation ANIMATION = new Animation(710, Priority.HIGH);

	/**
	 * Constructs a new {@code BindSpell} {@Code Object}
	 */
	public BindSpell() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code BindSpell} {@code Object}.
	 * @param type the type.
	 * @param level the level.
	 * @param sound the sound.
	 * @param start the start.
	 * @param projectile the projectile.
	 * @param end the end.
	 * @param runes the runes.
	 */
	private BindSpell(SpellType type, int level, double baseExperience, int sound, Graphics start, Projectile projectile, Graphics end, Item... runes) {
		super(type, SpellBook.MODERN, level, baseExperience, sound, -1, ANIMATION, start, projectile, end, runes);
	}

	@Override
	public void fireEffect(Entity entity, Entity victim, BattleState state) {
		if (victim instanceof NPC) {
			NPC npc = (NPC) victim;
			if (npc.getName().contains("impling")) {
				state.setEstimatedHit(-2);
			}
		}
		if (state.getEstimatedHit() == -1) {
			return;
		}
		int tick = 9;
		if (getType() == SpellType.BIND) {
			state.setEstimatedHit(-2);
		}
		if (state.getSpell().getSpellId() == 30) {
			tick = 17;
		} else if (state.getSpell().getSpellId() == 56) {
			tick = 25;
		}
		if (!victim.getLocks().isMovementLocked() && victim instanceof Player) {
			((Player) victim).getPacketDispatch().sendMessage("A magical force stops you from moving!");
		}
		victim.getWalkingQueue().reset();
		victim.getLocks().lockMovement(tick);
		entity.setAttribute("entangleDelay", GameWorld.getTicks() + tick + 2);
	}

	@Override
	public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
		return getType() == SpellType.ENTANGLE ? 5 : 3;
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType type) throws Throwable {
        // TODO: bind/snare/entangle have separate casting and onhit components 
        // to their sound effects, in order for splashes to sound distinct, currently 
        // these just sound like the spells should on success, even if they splash
		SpellBook.MODERN.register(12, new BindSpell(SpellType.BIND, 20, 30.0, 101, BIND_START, BIND_PROJECTILE, BIND_END, Runes.NATURE_RUNE.getItem(2), Runes.EARTH_RUNE.getItem(3), Runes.WATER_RUNE.getItem(3)));
		SpellBook.MODERN.register(30, new BindSpell(SpellType.SNARE, 50, 60.0, 186, SNARE_START, SNARE_PROJECTILE, SNARE_END, Runes.NATURE_RUNE.getItem(3), Runes.EARTH_RUNE.getItem(4), Runes.WATER_RUNE.getItem(4)));
		SpellBook.MODERN.register(56, new BindSpell(SpellType.ENTANGLE, 79, 89.0, 152, ENTANGLE_START, ENTANGLE_PROJECTILE, ENTANGLE_END, Runes.NATURE_RUNE.getItem(4), Runes.EARTH_RUNE.getItem(5), Runes.WATER_RUNE.getItem(5)));
		return this;
	}

}
