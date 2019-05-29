package plugin.npc.familiar;

import java.util.List;

import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.game.content.skill.member.summoning.familiar.Familiar;
import org.crandor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.tools.RandomFunction;

/**
 * Represents the Smoke Devil familiar.
 * @author Aero
 * @author Vexia
 */
@InitializablePlugin
public class SmokeDevilNPC extends Familiar {

	/**
	 * Constructs a new {@code SmokeDevilNPC} {@code Object}.
	 */
	public SmokeDevilNPC() {
		this(null, 6865);
	}

	/**
	 * Constructs a new {@code SmokeDevilNPC} {@code Object}.
	 * @param owner The owner.
	 * @param id The id.
	 */
	public SmokeDevilNPC(Player owner, int id) {
		super(owner, id, 4800, 12085, 6);
	}

	@Override
	public Familiar construct(Player owner, int id) {
		return new SmokeDevilNPC(owner, id);
	}

	@Override
	public void configureFamiliar() {
		NPCDefinition.setOptionHandler("flames", new OptionHandler() {
			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				return this;
			}

			@Override
			public boolean handle(Player player, Node node, String option) {
				final Familiar familiar = (Familiar) node;
				if (!player.getFamiliarManager().isOwner(familiar)) {
					return true;
				}
				// TODO:
				return true;
			}
		});
	}

	@Override
	protected boolean specialMove(FamiliarSpecial special) {
		if (!isOwnerAttackable()) {
			return false;
		}
		final List<Entity> entitys = RegionManager.getLocalEntitys(this, 1);
		entitys.remove(this);
		entitys.remove(owner);
		visualize(Animation.create(7820), Graphics.create(1375));
		for (Entity e : entitys) {
			if (super.canCombatSpecial(e, false)) {
				e.getImpactHandler().manualHit(this, RandomFunction.random(6), HitsplatType.NORMAL);
			}
		}
		return true;
	}

	@Override
	public int[] getIds() {
		return new int[] { 6865, 6866 };
	}

}
