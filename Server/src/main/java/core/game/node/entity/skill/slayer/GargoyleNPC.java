package core.game.node.entity.skill.slayer;

import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs09.consts.Items;
import rs09.plugin.ClassScanner;

/**
 * Handles the gargoyle npc.
 * @author Vexia
 */
@Initializable
public final class GargoyleNPC extends AbstractNPC {

	/**
	 * Constructs a new {@code GargoyleNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public GargoyleNPC(int id, Location location) {
		super(id, location);
	}

	/**
	 * Constructs a new {@code GargoyleNPC} {@code Object}.
	 */
	public GargoyleNPC() {
		super(0, null);
	}

	@Override
	public void checkImpact(BattleState state) {
		super.checkImpact(state);
		int lp = getSkills().getLifepoints();
		if (state.getEstimatedHit() > -1) {
			if (lp - state.getEstimatedHit() < 1) {
				state.setEstimatedHit(0);
				if (lp > 1) {
					state.setEstimatedHit(lp - 1);
				}
			}
		}
		if (state.getSecondaryHit() > -1) {
			if (lp - state.getSecondaryHit() < 1) {
				state.setSecondaryHit(0);
				if (lp > 1) {
					state.setSecondaryHit(lp - 1);
				}
			}
		}
		int totalHit = state.getEstimatedHit() + state.getSecondaryHit();
		if (lp - totalHit < 1) {
			state.setEstimatedHit(0);
			state.setSecondaryHit(0);
		}
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ClassScanner.definePlugin(new RockHammerHandler());
		return super.newInstance(arg);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new GargoyleNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return Tasks.GARGOYLES.getNpcs();
	}

	/**
	 * The rock hammer handler plugin.
	 * @author Vexia
	 */
	public final class RockHammerHandler extends UseWithHandler {

		/**
		 * Constructs a new {@code RockHammerHandler} {@code Object}.
		 */
		public RockHammerHandler() {
			super(Items.ROCK_HAMMER_4162);
		}

		@Override
		public Plugin<Object> newInstance(Object arg) throws Throwable {
			for (int id : getIds()) {
				addHandler(id, NPC_TYPE, this);
			}
			return this;
		}

		@Override
		public boolean handle(NodeUsageEvent event) {
			final Player player = event.getPlayer();
			final NPC npc = (NPC) event.getUsedWith();
			if (npc.getSkills().getLifepoints() > 10) {
				player.getPacketDispatch().sendMessage("The gargoyle isn't weak enough to be harmed by the hammer.");
			} else {
				player.getPacketDispatch().sendMessage("The gargoyle cracks apart.");
				npc.getImpactHandler().manualHit(player, npc.getSkills().getLifepoints(), HitsplatType.NORMAL);
			}
			return true;
		}

	}

}
