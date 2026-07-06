package content.data.consumables.effects;

import core.game.consumable.ConsumableEffect;
import core.game.node.entity.combat.ImpactHandler;
import core.game.node.entity.player.Player;

/**
 * The Zamorak mix shows the regular Zamorak brew hitsplat (10% of pre-drink lifepoints + 2, rounded down)
 * but also heals 6 lifepoints. The heal must be applied before the hit so the net change can never kill
 * the player, while the hitsplat still reflects the pre-heal lifepoints.
 */
public class ZamorakMixEffect extends ConsumableEffect {
	
	@Override
	public void activate(Player p) {
		int damage = (int) (p.getSkills().getLifepoints() * 0.10 + 2);
		p.getSkills().heal(6);
		p.getImpactHandler().manualHit(p, damage, ImpactHandler.HitsplatType.NORMAL);
	}
	
	@Override
	public int getHealthEffectValue(Player player) {
		return 6 - (int) (player.getSkills().getLifepoints() * 0.10 + 2);
	}
}
