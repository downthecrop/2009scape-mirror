package core.game.node.entity.combat.spell;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatSpell;
import core.game.node.entity.combat.equipment.SpellType;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Represents the sonic wave spell of an evil chicken.
 * @author 'Vexia
 * @date 24/11/2013
 */
@Initializable
public final class SonicWaveSpell extends CombatSpell {

	/**
	 * The projectile for Earth strike.
	 */
	private static final Projectile STRIKE_PROJECTILE = Projectile.create((Entity) null, null, 337, 8, 8, 52, 100, 15, 1);

	/**
	 * Constructs a new {@code EarthSpell} {@Code Object}
	 */
	public SonicWaveSpell() {
		/*
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
	private SonicWaveSpell(SpellType type, int level, int sound, Graphics start, Projectile projectile, Graphics end, Item... runes) {
		super(type, SpellBook.MODERN, level, 0.0, sound, sound + 1, null, start, projectile, end, runes);
	}

	@Override
	public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
		return getType().getImpactAmount(entity, victim, 5);
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType type) throws Throwable {
		SpellBook.MODERN.register(63232, new SonicWaveSpell(SpellType.STRIKE, 1, -1, new Graphics(337), STRIKE_PROJECTILE, null));
		return this;
	}

}
