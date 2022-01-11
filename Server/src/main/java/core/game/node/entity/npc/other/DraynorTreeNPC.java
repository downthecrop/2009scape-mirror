package core.game.node.entity.npc.other;

import java.util.List;

import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.tools.RandomFunction;

/**
 * Represents the abstract draynor tree npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class DraynorTreeNPC extends AbstractNPC {

	/**
	 * Represents the NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 5208, 152, 5207 };

	/**
	 * Represents the animation of the tree.
	 */
	private static final Animation ANIMATION = new Animation(73, Priority.HIGH);

	/**
	 * Represents the attack delay.
	 */
	private int attackDelay;

	/**
	 * Constructs a new {@code DraynorTreeNPC} {@code Object}.
	 */
	public DraynorTreeNPC() {
		super(0, null, false);
	}

	/**
	 * Constructs a new {@code DraynorTreeNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	private DraynorTreeNPC(int id, Location location) {
		super(id, location, false);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new DraynorTreeNPC(id, location);
	}

	@Override
	public void tick() {
		final List<Player> players = RegionManager.getLocalPlayers(this, 1);
		if (players.size() != 0) {
			if (attackDelay < GameWorld.getTicks()) {
				for (Player p : players) {
					faceTemporary(p, 2);
					getAnimator().forceAnimation(ANIMATION);
					int hit = RandomFunction.random(2);
					p.getImpactHandler().manualHit(this, hit, hit > 0 ? HitsplatType.NORMAL : HitsplatType.MISS);
					attackDelay = GameWorld.getTicks() + 3;
					p.animate(p.getProperties().getDefenceAnimation());
					return;
				}
			}
		}
		super.tick();
	}

	@Override
	public int[] getIds() {
		return ID;
	}

}
