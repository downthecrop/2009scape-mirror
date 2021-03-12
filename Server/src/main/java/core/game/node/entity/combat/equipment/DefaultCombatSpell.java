package core.game.node.entity.combat.equipment;

import core.game.node.entity.skill.Skills;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatSpell;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.link.SpellBookManager;
import core.plugin.Plugin;

/**
 * Handles the default combat spell for NPCs.
 * @author Emperor
 */
public final class DefaultCombatSpell extends CombatSpell {

	/**
	 * The projectile id.
	 */
	private final int projectileId;

	/**
	 * The start height of the projectile.
	 */
	private final int startHeight;

	/**
	 * Constructs a new {@code DefaultCombatSpell} {@code Object}.
	 * @param npc The NPC.
	 */
	public DefaultCombatSpell(NPC npc) {
		super(SpellType.BOLT, SpellBookManager.SpellBook.MODERN, 0, 0.0, -1, -1, npc.getProperties().getMagicAnimation(), npc.getDefinition().getCombatGraphics()[0], null, npc.getDefinition().getCombatGraphics()[2]);
		if (npc.getDefinition().getCombatGraphics()[1] != null) {
			this.projectileId = npc.getDefinition().getCombatGraphics()[1].getId();
			this.startHeight = npc.getDefinition().getCombatGraphics()[1].getHeight();
		} else {
			this.projectileId = -1;
			this.startHeight = 42;
		}
	}

	@Override
	public void visualize(Entity entity, Node target) {
		if (projectileId != -1) {
			super.projectile = Projectile.magic(entity, (Entity) target, projectileId, startHeight, 36, 52, 15);
		}
		super.visualize(entity, target);
	}

	@Override
	public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
		int level = entity.getSkills().getLevel(Skills.MAGIC);
		int bonus = entity.getProperties().getBonuses()[13];
		return (int) ((14 + level + (bonus / 8) + ((level * bonus) * 0.016865))) / 10 + 1;
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		return null;
	}

}