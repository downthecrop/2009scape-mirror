package core.game.content.activity.gwd;

import core.game.content.global.BossKillCounter;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.DeathTask;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.npc.agg.AggressiveBehavior;
import core.game.node.entity.npc.agg.AggressiveHandler;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.prayer.PrayerType;
import core.game.world.map.Location;
import core.game.world.map.zone.ZoneBorders;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import rs09.game.node.entity.combat.CombatPulse;
import rs09.game.node.entity.combat.CombatSwingHandler;
import rs09.game.world.GameWorld;

/**
 * Handles a god wars boss NPC.
 * @author Emperor
 */
@Initializable
public final class GodwarsBossNPC extends AbstractNPC {

	/**
	 * Handles the combat swing of Commander Zilyana.
	 */
	private static final CombatSwingHandler ZILYANA_COMBAT = new GWDZilyanaSwingHandler();

	/**
	 * Handles the combat swing of Kree'arra.
	 */
	private static final CombatSwingHandler KREE_ARRA_COMBAT = new GWDKreeArraSwingHandler();

	/**
	 * Handles the combat swing of General Graardor.
	 */
	private static final CombatSwingHandler GRAARDOR_COMBAT = new GWDGraardorSwingHandler();

	/**
	 * Handles the combat swing of General Graardor.
	 */
	private static final CombatSwingHandler TSUTSAROTH_COMBAT = new GWDTsutsarothSwingHandler();

	/**
	 * The battle cries.
	 */
	private static final String[][] BATTLE_CRIES = { { // Zamorak
			"Attack them, you dogs!", "Forward!", "Death to Saradomin's dogs!", "Kill them, you cowards!", "The Dark One will have their souls!", "Zamorak curse them!", "Rend them limb from limb!", "No retreat!", "Flay them all!" }, { // Armadyl
			"Kraaaw!" }, { // Saradomin
			"Death to the enemies of the light!", "Slay the evil ones!", "Saradomin lend me strength!", "By the power of Saradomin!", "May Saradomin be my sword.", "Good will always triumph!", "Forward! Our allies are with us!", "Saradomin is with us!", "In the name of Saradomin!", "Attack! Find the Godsword!" }, { // Bandos
			"Death to our enemies!", "Brargh!", "Break their bones!", "For the glory of Bandos!", "Split their skulls!", "We feast on the bones of our enemies tonight!", "CHAAARGE!", "Crush them underfoot!", "All glory to Bandos!", "GRAAAAAAAAAAAR!", "FOR THE GLORY OF THE BIG HIGH WAR GOD!" } };

	/**
	 * The minions.
	 */
	private NPC[] minions;

	/**
	 * The boss chamber.
	 */
	private ZoneBorders chamber;

	/**
	 * The next battle cry tick.
	 */
	private int nextBattleCry;

	/**
	 * The combat swing handler.
	 */
	private CombatSwingHandler handler;

	/**
	 * If the NPC focuses on one target.
	 */
	private boolean targetFocus;

	/**
	 * Constructs a new {@code CommanderZilyanaNPC} {@code Object}.
	 */
	public GodwarsBossNPC() {
		super(6203, null);
	}

	/**
	 * Constructs a new {@code CommanderZilyanaNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	public GodwarsBossNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public void init() {
		setAggressive(false);
		super.init();
		switch (getId()) {
		case 6203:
			chamber = new ZoneBorders(2918, 5318, 2936, 5331);
			handler = TSUTSAROTH_COMBAT;
			targetFocus = true;
			break;
		case 6222:
			chamber = new ZoneBorders(2824, 5296, 2842, 5308);
			handler = KREE_ARRA_COMBAT;
			break;
		case 6247:
			chamber = new ZoneBorders(2889, 5258, 2907, 5276);
			handler = ZILYANA_COMBAT;
			break;
		case 6260:
			chamber = new ZoneBorders(2864, 5351, 2876, 5369);
			handler = GRAARDOR_COMBAT;
			targetFocus = true;
			break;
		}
		AggressiveBehavior behavior = null;
		if (chamber != null) {
			final ZoneBorders borders = chamber;
			behavior = new AggressiveBehavior() {
				@Override
				public boolean canSelectTarget(Entity entity, Entity target) {
					if (!target.isActive() || DeathTask.isDead(target)) {
						return false;
					}
					if (!target.getProperties().isMultiZone() && target.inCombat()) {
						return false;
					}
					return borders.insideBorder(target.getLocation());
				}
			};
			super.setAggressiveHandler(new AggressiveHandler(this, behavior));
			if (chamber.insideBorder(getLocation())) {
				minions = new NPC[3];
				for (int i = 0; i < 3; i++) {
					int npcId = getId() + 1 + (i << 1);
					AbstractNPC npc = (AbstractNPC) (minions[i] = NPC.create(npcId, getSpawnLocation(npcId)));
					npc.init();
					npc.fireEvent("set_boss", this);
					if (behavior != null) {
						npc.setAggressiveHandler(new AggressiveHandler(npc, behavior));
						npc.getAggressiveHandler().setChanceRatio(6);
						npc.getAggressiveHandler().setAllowTolerance(false);
						npc.getAggressiveHandler().setRadius(28);
					}
				}
			}
		}
		getProperties().setNPCWalkable(true);
		getProperties().setCombatTimeOut(2);
		getAggressiveHandler().setChanceRatio(6);
		getAggressiveHandler().setRadius(28);
		getAggressiveHandler().setAllowTolerance(false);
		walkRadius = 28;
	}

	@Override
	public void tick() {
		CombatPulse pulse = getProperties().getCombatPulse();
		if (chamber != null && pulse.isAttacking()) {
			Entity e = pulse.getVictim();
			if (!targetFocus) {
				Entity target = getImpactHandler().getMostDamageEntity(null);
				if (target != null && target != e && target instanceof Player) {
					pulse.setVictim(target);
				}
			}
			if (!chamber.insideBorder(e.getLocation().getX(), e.getLocation().getY())) {
				getPulseManager().clear();
			}
			if (nextBattleCry < GameWorld.getTicks()) {
				String[] cries = BATTLE_CRIES[(getId() - 6203) >> 4];
				sendChat(cries[RandomFunction.randomize(cries.length)]);
				nextBattleCry = GameWorld.getTicks() + 7 + RandomFunction.randomize(20);
			}
		}
		super.tick();
		if (getRespawnTick() == GameWorld.getTicks() && minions != null) {
			for (NPC npc : minions) {
				npc.setRespawnTick(-1);
			}
		}
	}

	@Override
	public void onImpact(final Entity entity, BattleState state) {
		if (targetFocus) {
			if (getProperties().getCombatPulse().getNextAttack() < GameWorld.getTicks() - 3) {
				getProperties().getCombatPulse().attack(entity);
				return;
			}
		}
		super.onImpact(entity, state);
	}

	@Override
	public void sendImpact(BattleState state) {
		if (state.getVictim() == null || getId() != 6222) {
			return;
		}
		int max = 0;
		if (state.getVictim().isPlayer() && state.getEstimatedHit() > 0) {
			switch (state.getStyle()) {
			case MAGIC:
				max = 21;
				break;
			case MELEE:
				max = 26;
				break;
			case RANGE:
				max = 71;
				break;

			}
		}
		if (state.getEstimatedHit() > max) {
			state.setEstimatedHit(RandomFunction.random(max - 10));
		}
		if (state.getStyle() == CombatStyle.RANGE && state.getVictim().asPlayer().getPrayer().get(PrayerType.PROTECT_FROM_MISSILES)) {
			state.neutralizeHits();
		}
	}

	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
		if (getId() == 6222 || getId() == 6260 || getId() == 6247 || getId() == 6203) {
			BossKillCounter.addtoKillcount((Player) killer, this.getId());
		}
		if (minions == null) {
			return;
		}
		for (NPC minion : minions) {
			if (minion.getRespawnTick() >= GameWorld.getTicks()) {
				minion.setRespawnTick(getRespawnTick());
			}
		}
	}

	@Override
	public boolean isAttackable(Entity entity, CombatStyle style, boolean message) {
		if (getId() == 6222 && style == CombatStyle.MELEE && entity instanceof Player) {
            if(message) {
                ((Player) entity).getPacketDispatch().sendMessage("The aviansie is flying too high for you to attack using melee.");
            }
			return false;
		}
		return super.isAttackable(entity, style, message);
	}

	/**
	 * Gets the spawn location for the given NPC id.
	 * @param id The NPC id.
	 * @return The spawn location.
	 */
	private Location getSpawnLocation(int id) {
		switch (id) {
		case 6208:
			return Location.create(2920, 5320, 2);
		case 6206:
			return Location.create(2919, 5327, 2);
		case 6204:
			return Location.create(2927, 5325, 2);
		case 6223:
			return Location.create(2828, 5298, 2);
		case 6225:
			return Location.create(2837, 5299, 2);
		case 6227:
			return Location.create(2834, 5302, 2);
		case 6248:
			return Location.create(2898, 5267, 0);
		case 6250:
			return Location.create(2901, 5268, 0);
		case 6252:
			return Location.create(2895, 5260, 0);
		case 6265:
			return Location.create(2866, 5363, 2);
		case 6261:
			return Location.create(2867, 5354, 2);
		case 6263:
			return Location.create(2873, 5354, 2);
		}
		return null;
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		if (handler != null) {
			return handler;
		}
		return super.getSwingHandler(swing);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new GodwarsBossNPC(id, location);
	}

	@Override
	public int[] getIds() {
		return new int[] { 6203, 6222, 6247, 6260 };
	}

}
