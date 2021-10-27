package core.game.content.activity.pestcontrol;

import java.util.Random;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionPlane;
import core.game.world.update.flag.context.Animation;
import core.tools.RandomFunction;
import core.game.content.activity.pestcontrol.monsters.PCRavagerNPC;
import core.game.content.activity.pestcontrol.monsters.PCSpinnerNPC;

/**
 * Handles the portal NPC.
 * @author Emperor
 */
public final class PCPortalNPC extends AbstractNPC {

	/**
	 * The splatter NPCs.
	 */
	private static final int[][] SPLATTERS = { { 3727, 3728, 3729 }, // Novice
			{ 3728, 3729, 3730 }, // Intermediate
			{ 3729, 3730, 3731 }, // Veteran
	};

	/**
	 * The shifter NPCs.
	 */
	private static final int[][] SHIFTERS = { { 3732, 3733, 3734, 3735, 3736, 3737 }, // Novice
			{ 3734, 3735, 3736, 3737, 3738, 3739 }, // Intermediate
			{ 3736, 3737, 3738, 3739, 3740, 3741 }, // Veteran
	};

	/**
	 * The ravager NPCs.
	 */
	private static final int[][] RAVAGERS = { { 3742, 3743, 3744 }, // Novice
			{ 3743, 3744, 3745 }, // Intermediate
			{ 3744, 3745, 3746 }, // Veteran
	};

	/**
	 * The spinner NPCs.
	 */
	private static final int[][] SPINNERS = { { 3747, 3748, 3749 }, // Novice
			{ 3748, 3749, 3750 }, // Intermediate
			{ 3749, 3750, 3751 }, // Veteran
	};

	/**
	 * The torcher NPCs.
	 */
	private static final int[][] TORCHERS = { { 3752, 3753, 3754, 3755, 3756, 3757 }, // Novice
			{ 3754, 3755, 3756, 3757, 3758, 3759 }, // Intermediate
			{ 3756, 3757, 3758, 3759, 3760, 3761 }, // Veteran
	};

	/**
	 * The defiler NPCs.
	 */
	private static final int[][] DEFILERS = { { 3762, 3763, 3764, 3765, 3766, 3767 }, // Novice
			{ 3764, 3765, 3766, 3767, 3768, 3769 }, // Intermediate
			{ 3766, 3767, 3768, 3769, 3770, 3771 }, // Veteran
	};

	/**
	 * The brawler NPCs.
	 */
	private static final int[][] BRAWLERS = { { 3772, 3773, 3774 }, // Novice
			{ 3773, 3774, 3775 }, // Intermediate
			{ 3774, 3775, 3776 }, // Veteran
	};

	/**
	 * If the lifepoints should be updated.
	 */
	private boolean updateLifepoints = true;

	/**
	 * The pest control session.
	 */
	private PestControlSession session;

	/**
	 * The spinners.
	 */
	private PCSpinnerNPC[] spinners = new PCSpinnerNPC[3];

	/**
	 * The brawlers.
	 */
	private NPC[] brawlers = new NPC[2];


	/**
	 * The portal ids?
	 */
	final static Integer[] portalIds = new Integer[] { 6142, 6143, 6144, 6145, 6146, 6147, 6148, 6149,

			6150, 6151, 6152, 6153, 6154, 6155, 6156, 6157,

			7551, 7552, 7553, 7554, 7555, 7556, 7557, 7558 };

	/**
	 * Constructs a new {@code PCPortalNPC} {@code Object}.
	 */
	public PCPortalNPC() {
		this(6142, null);
	}

	/**
	 * Constructs a new {@code PCPortalNPC} {@code Object}.
	 * @param id The NPC id.
	 * @param l The location.
	 */
	public PCPortalNPC(int id, Location l) {
		super(id, l);
	}

	@Override
	public void init() {
		super.setWalks(false);
		super.setRespawn(false);
		super.getProperties().setRetaliating(false);
		super.init();
		getProperties().setAttackAnimation(Animation.create(-1));
		getProperties().setDefenceAnimation(Animation.create(-1));
		getProperties().setDeathAnimation(Animation.create(-1));
		session = getExtension(PestControlSession.class);
	}

	@Override
	public void tick() {
		super.tick();
		if (session != null) {
			RegionPlane plane = getViewport().getCurrentPlane();
			if (plane != null && session.getTicks() % 35 - plane.getPlayers().size() == 0 && plane.getNpcs().size() < 100) {
				spawnNPCs();
			}
			if (updateLifepoints && session.getTicks() % 10 == 0) {
				String color = getSkills().getLifepoints() > 50 ? "<col=00FF00>" : "<col=FF0000>";
				session.sendString(color + getSkills().getLifepoints(), 13 + getPortalIndex());
				updateLifepoints = false;
			}
		}
	}

    @Override
    public boolean shouldPreventStacking(Entity mover) {
        return true;
    }

	@Override
	public void onImpact(final Entity entity, BattleState state) {
		updateLifepoints = true;
		super.onImpact(entity, state);
		if (session != null && state != null && entity instanceof Player) {
			int total = 0;
			if (state.getEstimatedHit() > 0) {
				total += state.getEstimatedHit();
			}
			if (state.getSecondaryHit() > 0) {
				total += state.getSecondaryHit();
			}
			session.addZealGained((Player) entity, total);
		}
	}

	/**
	 * Spawns the NPCs.
	 */
	public void spawnNPCs() {
		Direction dir = Direction.getLogicalDirection(getLocation(), session.getSquire().getLocation());
		int index = getDifficultyIndex();
		Random r = RandomFunction.RANDOM;
		int amount = index + 1;
		if (getViewport().getCurrentPlane() != null) {
			amount += getViewport().getCurrentPlane().getPlayers().size() / 10;
		}
		if (getDifficultyIndex() == 0) {
			amount += ((getDifficultyIndex() + 1) * 3) / 2;
		}
		for (int i = 0; i < amount; i++) {
			int[] ids = SHIFTERS[index];
			switch (r.nextInt(7)) {
			case 0:
				ids = SPLATTERS[index];
				break;
			case 1:
				ids = SHIFTERS[index];
				break;
			case 2:
				ids = RAVAGERS[index];
				break;
			case 3:
				boolean spawn = false;
				for (PCSpinnerNPC npc : spinners) {
					if (npc == null || !npc.isActive()) {
						spawn = true;
						break;
					}
				}
				if (spawn) {
					ids = SPINNERS[index];
				} else {
					ids = TORCHERS[index];
				}
				break;
			case 4:
				ids = TORCHERS[index];
				break;
			case 5:
				ids = DEFILERS[index];
				break;
			case 6:
				spawn = false;
				for (NPC npc : brawlers) {
					if (npc == null || !npc.isActive()) {
						spawn = true;
						break;
					}
				}
				if (spawn) {
					ids = BRAWLERS[index];
				} else {
					ids = DEFILERS[index];
				}
				break;
			}
			int x = dir.getStepX() * 3;
			int y = dir.getStepY() * 3;
			if (x == 0) {
				x = i;
			} else {
				y = i;
			}
			NPC n = session.addNPC(NPC.create(ids[r.nextInt(ids.length)], getLocation().transform(x, y, 0)));
			if (ids == RAVAGERS[index]) {
				((PCRavagerNPC) n).setPortalIndex(getPortalIndex());
			} else if (ids == SPINNERS[index]) {
				for (int j = 0; j < spinners.length; j++) {
					if (spinners[j] == null || !spinners[j].isActive()) {
						spinners[j] = ((PCSpinnerNPC) n).setPortalIndex(getPortalIndex());
						break;
					}
				}
			}
			n.setWalks(true);
			n.setRespawn(false);
		}
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new PCPortalNPC(id, location);
	}

	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
		if (session != null) {
			int value = 0;
			boolean endSession = true;
			for (int i = 0; i < 4; i++) {
				if (!session.getPortals()[i].isActive()) {
					value |= 1 << i;
				} else {
					endSession = false;
				}
			}
			if (value > 0) {
				session.sendConfig(value << 28);
				if (endSession) {
					session.getActivity().end(session, true);
					return;
				}
			}
			for (PCSpinnerNPC npc : spinners) {
				if (npc != null && npc.isActive()) {
					npc.explode();
				}
			}
			session.sendString("<col=FF0000>0", 13 + getPortalIndex());
			updateLifepoints = false;
			session.getSquire().getSkills().heal(50);
			session.getSquire().onImpact(this, null);
		}
	}

	@Override
	public int[] getIds() {
		return new int[] { 6142, 6143, 6144, 6145, 6146, 6147, 6148, 6149,

                6150, 6151, 6152, 6153, 6154, 6155, 6156, 6157,

                7551, 7552, 7553, 7554, 7555, 7556, 7557, 7558 };
	}

	/**
	 * The difficulty index.
	 * @return The index.
	 */
	public int getDifficultyIndex() {
		if (getId() > 7550) {
			return 2;
		}
		if (getId() > 6149) {
			return 1;
		}
		return 0;
	}

	/**
	 * The portal index.
	 * @return The index.
	 */
	public int getPortalIndex() {
		if (getId() > 7550) {
			return (getId() - 7551) % 4;
		}
		if (getId() > 6149) {
			return (getId() - 6150) % 4;
		}
		return (getId() - 6142) % 4;
	}
}
