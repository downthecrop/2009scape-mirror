package plugin.skill.slayer;

import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.path.Pathfinder;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.PluginManager;

/**
 * Handles the interactions of a rock slug.
 * @author Vexia
 */
@InitializablePlugin
public final class RockSlugPlugin implements Plugin<Object> {

	/**
	 * The bag of salt item.
	 */
	private static final Item SALT = new Item(4161, 1);

	/**
	 * The rockslug npc ids.
	 */
	private static final int[] IDS = new int[] { 1631, 1632 };

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		PluginManager.definePlugin(new RockSlugNPC());
		PluginManager.definePlugin(new SaltBagHandler());
		return this;
	}

	@Override
	public Object fireEvent(String identifier, Object... args) {
		return null;
	}

	/**
	 * The use with handler for the bag of salt on a rock slug.
	 * @author Vexia
	 */
	public final class SaltBagHandler extends UseWithHandler {

		/**
		 * Constructs a new {@code SaltBagHandler} {@code Object}.
		 */
		public SaltBagHandler() {
			super(SALT.getId());
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			for (int id : IDS) {
				addHandler(id, NPC_TYPE, this);
			}
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			final Player player = event.getPlayer();
			final NPC npc = (NPC) event.getUsedWith();
			player.getInventory().remove(SALT);
			if (npc.getSkills().getLifepoints() < 10) {
				npc.getImpactHandler().manualHit(player, npc.getSkills().getLifepoints(), HitsplatType.NORMAL);
				player.getPacketDispatch().sendMessage("The rockslug shrivels up and dies.");
			} else {
				player.sendMessage("Your bag of salt is ineffective. The Rockslug is not weak enough.");
			}
			return true;
		}

	}

	/**
	 * The rock slug npc.
	 * @author Vexia
	 */
	public final class RockSlugNPC extends AbstractNPC {

		/**
		 * Constructs a new {@code RockSlugNPC} {@code Object}.
		 */
		public RockSlugNPC() {
			super(-1, null);
		}

		/**
		 * Constructs a new {@code RockSlugNPC} {@code Object}.
		 * @param id the id.
		 * @param location the location.
		 */
		public RockSlugNPC(int id, Location location) {
			super(id, location, true);
		}

		@Override
		public AbstractNPC construct(int id, Location location, Object... objects) {
			return new RockSlugNPC(id, location);
		}

		@Override
		public void checkImpact(BattleState state) {
			super.checkImpact(state);
			int lifepoints = getSkills().getLifepoints();
			boolean run = false;
			if (state.getEstimatedHit() > -1) {
				lifepoints -= state.getEstimatedHit();
				if (lifepoints < 1) {
					run = true;
					state.setEstimatedHit(lifepoints - 1);
				}
				if (state.getEstimatedHit() < 0) {
					state.setEstimatedHit(0);
				}
			}
			if (state.getSecondaryHit() > -1) {
				lifepoints -= state.getSecondaryHit();
				if (lifepoints < 1) {
					run = true;
					state.setSecondaryHit(lifepoints - 1);
				}
				if (state.getSecondaryHit() < 0) {
					state.setSecondaryHit(0);
				}
			}
			if (run) {
				getProperties().getCombatPulse().stop();
				Pathfinder.find(getLocation(), getProperties().getSpawnLocation());
			}
		}

		@Override
		public int[] getIds() {
			return IDS;
		}

	}

}
