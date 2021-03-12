package core.game.content.zone.wbisland;

import java.util.List;

import core.game.content.global.BossKillCounter;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.tools.RandomFunction;

/**
 * Represents a dagannoth king npc.
 * @author Vexia
 */
public final class DagannothKingNPC extends AbstractNPC {

	/**
	 * The type of dag.
	 */
	private DagType type;

	/**
	 * Constructs a new {@Code DagannothKingNPC} {@Code Object}
	 */
	public DagannothKingNPC() {
		super(-1, null);
	}

	/**
	 * Constructs a new {@Code DagannothKingNPC} {@Code Object}
	 * @param id the id.
	 * @param location the location.
	 */
	public DagannothKingNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public void init() {
		type = DagType.forId(getId());
		super.init();
	}

	@Override
	public void checkImpact(BattleState state) {
		CombatStyle style = state.getStyle();
		if (style == null) {
			style = state.getAttacker().getProperties().getCombatPulse().getStyle();
		}
		if (type.isImmune(style)) {
			state.neutralizeHits();
		}
	}

	@Override
	public double getLevelMod(Entity entity, Entity victim) {
		if (type == DagType.PRIME) {
			return 3.5;
		}
		return 0.0;
	}

	@Override
	public void sendImpact(BattleState state) {
		if (state.getEstimatedHit() > type.getMaxHit()) {
			state.setEstimatedHit(RandomFunction.random(type.getMaxHit() - 5, type.getMaxHit()));
		}
		if (type != DagType.REX && RandomFunction.random(5) <= 2) {
			List<Player> players = RegionManager.getLocalPlayers(this, 9);
			if (players.size() <= 1) {
				return;
			}
			Player newP = players.get(RandomFunction.random(players.size()));
			if (newP != null) {
				getProperties().getCombatPulse().stop();
				getAggressiveHandler().setPauseTicks(2);
				attack(newP);
			}
		}
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new DagannothKingNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return new int[] { 2881, 2882, 2883 };
	}

	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
		if (getId() == 2881 || getId() == 2882 || getId() == 2883) {
			BossKillCounter.addtoKillcount((Player) killer, this.getId());
		}
	}

	/**
	 * ß A king type.
	 * @author Vexia
	 */
	public enum DagType {
		SUPREME(2881, CombatStyle.RANGE, CombatStyle.MELEE, CombatStyle.MAGIC, 30), PRIME(2882, CombatStyle.MAGIC, CombatStyle.RANGE, CombatStyle.MELEE, 61), REX(2883, CombatStyle.MELEE, CombatStyle.MAGIC, CombatStyle.RANGE, 28);

		/**
		 * The npc id.
		 */
		private final int id;

		/**
		 * The combat style.
		 */
		private final CombatStyle style;

		/**
		 * The style he is weak to.
		 */
		private final CombatStyle weakStyle;

		/**
		 * The style he is immunned to.
		 */
		private final CombatStyle immuneStyle;

		/**
		 * The max hit.
		 */
		private final int maxHit;

		/**
		 * Constructs a new {@Code DagType} {@Code Object}
		 * @param id the id.
		 * @param style the style.
		 * @param weakStyle the weak style.
		 * @parmam immuneStyle the style.
		 * @param maxHit the max hit.
		 */
		private DagType(int id, CombatStyle style, CombatStyle weakStyle, CombatStyle immuneStyle, int maxHit) {
			this.id = id;
			this.style = style;
			this.weakStyle = weakStyle;
			this.immuneStyle = immuneStyle;
			this.maxHit = maxHit;
		}

		/**
		 * Gets the maxHit.
		 * @return the maxHit
		 */
		public int getMaxHit() {
			return maxHit;
		}

		/**
		 * Gets a dag type.
		 * @param id the id.
		 * @return the type.
		 */
		public static DagType forId(int id) {
			for (DagType type : values()) {
				if (type.getId() == id) {
					return type;
				}
			}
			return null;
		}

		/**
		 * Checks if the dag is immune to a style.
		 * @param style the style.
		 * @return {@code True} if so.
		 */
		public boolean isImmune(CombatStyle style) {
			return style == immuneStyle || style == this.style;
		}

		/**
		 * Gets the id.
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * Gets the style.
		 * @return the style
		 */
		public CombatStyle getStyle() {
			return style;
		}

		/**
		 * Gets the immuneStyle.
		 * @return the immuneStyle
		 */
		public CombatStyle getImmuneStyle() {
			return immuneStyle;
		}

		/**
		 * Gets the weakStyle.
		 * @return the weakStyle
		 */
		public CombatStyle getWeakStyle() {
			return weakStyle;
		}

	}

}
