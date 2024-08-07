package content.region.wilderness.handlers.revenants;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.DeathTask;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.entity.skill.Skills;
import content.global.skill.summoning.familiar.Familiar;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.impl.WildernessZone;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import core.game.node.entity.combat.CombatSwingHandler;
import core.game.system.config.NPCConfigParser;
import core.game.world.GameWorld;
import org.rs09.consts.Sounds;

import static core.api.ContentAPIKt.playAudio;
import static core.api.ContentAPIKt.playGlobalAudio;

/**
 * Handles a revenant NPC.
 * @author Ceikry-ish (mostly Vexia code still), Player Name
 */
@Initializable
public class RevenantNPC extends AbstractNPC {

	/**
	 * The safe zone borders.
	 */
	private static final ZoneBorders[] SAFE_ZONES = new ZoneBorders[] { new ZoneBorders(3074, 3651, 3193, 3774), new ZoneBorders(3264, 3672, 3279, 3695), new ZoneBorders(3081, 3909, 3129, 3954), new ZoneBorders(3350, 3869, 3391, 3900) };

	/**
	 * The possible PVP item drops.
	 */
	private static final int[] PVP_DROPS = new int[] { 13896, 13884, 13890, 13902, 13887, 13899, 13893, 13905, 13864, 13858, 13861, 13867, 13876, 13879, 13870, 13873, 13883, 13908, 13911, 13914, 13917, 13920, 13923, 13926, 13929, 13932, 13935, 13938, 13941, 13944, 13947, 13950, 13953, 13958, 13961, 13964, 13967, 13970, 13973, 13976, 13979, 13982, 13985, 13988 };

	/**
	 * The swing handler.
	 */
	private CombatSwingHandler swingHandler;

	/**
	 * The revenant type.
	 */
	private final RevenantType type;

	/**
	 * The routes this revenant can travel.
	 */
	private final Location[][] routes;

	/**
	 * Constructs a new {@code RevenantNPC} {@code Object}
	 * @param id the id.
	 * @param location the location.
	 */
	public RevenantNPC(int id, Location location, Location[][] routes) {
		super(id, location);
		this.routes = routes;
		this.type = RevenantType.forId(id);
	}

	/**
	 * Constructs a new {@code RevenantNPC} {@code Object}
	 */
	public RevenantNPC() {
		this(-1, null, null);
	}

	@Override
	public void configure() {
		setWalks(true);
		setRespawn(false);
		setAggressive(true);
		setRenderable(true);
		setDefaultBehavior();
		getAggressiveHandler().setRadius(64 * 2);
		getAggressiveHandler().setChanceRatio(9);
		getAggressiveHandler().setAllowTolerance(false);
		getProperties().setCombatTimeOut(120);
		configureBonuses();
		super.configure();
		this.swingHandler = new RevenantCombatHandler(getProperties().getAttackAnimation(), getProperties().getMagicAnimation(), getProperties().getRangeAnimation());
		setAttribute("food-items", 20);
	}

	@Override
	public void init() {
		super.init();
		RevenantController.registerRevenant(this);
		int spawnAnim = getDefinition().getConfiguration(NPCConfigParser.SPAWN_ANIMATION, -1);
		if (spawnAnim != -1) {
			animate(new Animation(spawnAnim));
		}
	}

	@Override
	public void clear() {
		super.clear();
		RevenantController.unregisterRevenant(this, true);
	}

	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
		playGlobalAudio(killer.getLocation(),4063);
	}

	@Override
	public void tick() {
		skills.pulse();
		getWalkingQueue().update();
		if (this.getViewport().getRegion().isActive()) {
			getUpdateMasks().prepare(this);
		}
		if (!DeathTask.isDead(this)) {
			int curhp = getSkills().getLifepoints();
			int maxhp = getSkills().getStaticLevel(Skills.HITPOINTS);
			int fooditems = getAttribute("food-items", 0);
			if (curhp < maxhp / 2 && fooditems > 0 && getAttribute("eat-delay", 0) < GameWorld.getTicks()) {
				lock(3);
				getProperties().getCombatPulse().delayNextAttack(3);
				getSkills().heal(maxhp / 6);
				for (Player p : RegionManager.getLocalPlayers(this)) {
					playAudio(p, Sounds.EAT_2393);
				}
				setAttribute("eat-delay", GameWorld.getTicks() + 6);
				setAttribute("food-items", fooditems - 1);
			}
		}
		behavior.tick(this);
		if (aggressiveHandler != null && aggressiveHandler.selectTarget()) {
			return;
		}
	}

	@Override
	public void sendImpact(BattleState state) {
		if (state.getEstimatedHit() > type.getMaxHit()) {
			state.setEstimatedHit(RandomFunction.random(type.getMaxHit() - 5, type.getMaxHit()));
		}
	}

	@Override
	public Audio getAudio(int index) {
		return null;
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new RevenantNPC(id, location, null);
	}

	@Override
	public void setNextWalk() {
		nextWalk = GameWorld.getTicks() + RandomFunction.random(7, 15);
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return swingHandler;
	}

	@Override
	public boolean canMove(Location location) {
		for (ZoneBorders zone : SAFE_ZONES) {
			if (zone.insideBorder(location)) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected Location getMovementDestination() {
		if (!pathBoundMovement || movementPath == null || movementPath.length < 1) {
			return getLocation().transform(-5 + RandomFunction.random(getWalkRadius()), -5 + RandomFunction.random(getWalkRadius()), 0);
		}
		Location l = movementPath[movementIndex++];
		if (movementIndex == movementPath.length) {
			movementIndex = 0;
		}
		return l;
	}

	@Override
	public int getWalkRadius() {
		return 20;
	}

	@Override
	public boolean continueAttack(Entity target, CombatStyle style, boolean message) {
		return target instanceof Player ? hasAcceptableCombatLevel(target.asPlayer()) : true;
	}

	@Override
	public boolean isAttackable(Entity entity, CombatStyle style, boolean message) {
		if (entity instanceof Player) {
			if (!hasAcceptableCombatLevel(entity.asPlayer()) && !entity.asPlayer().isAdmin()) {
				if (message) {
					entity.asPlayer().sendMessage("The level difference between you and your opponent is too great.");
				}
				return false;
			}
		}
		if (entity instanceof Familiar) {
			Player owner = ((Familiar) entity).getOwner();
			if (owner == null) {
				return false;
			}
			if (!hasAcceptableCombatLevel(owner)) {
				return false;
			}
		}
		return super.isAttackable(entity, style, message);
	}

	@Override
	public boolean canSelectTarget(Entity target) {
		if (!(target instanceof Player)) {
			return false;
		}
		return hasAcceptableCombatLevel(target.asPlayer());
	}

	@Override
	public int[] getIds() {
		return new int[] { 6604, 6635, 6655, 6666, 6677, 6697, 6703, 6715, 6605, 6612, 6616, 6620, 6636, 6637, 6638, 6639, 6651, 6656, 6657, 6658, 6667, 6678, 6679, 6680, 6681, 6693, 6698, 6699, 6704, 6705, 6706, 6707, 6716, 6717, 6718, 6719, 6606, 6621, 6628, 6640, 6659, 6682, 6694, 6708, 6720, 6622, 6631, 6641, 6660, 6668, 6683, 6709, 6721, 6608, 6642, 6661, 6684, 6710, 6722, 6727, 6613, 6623, 6643, 6652, 6662, 6669, 6671, 6674, 6685, 6695, 6700, 6711, 6723, 6607, 6609, 6614, 6617, 6625, 6632, 6644, 6663, 6675, 6686, 6701, 6712, 6724, 6728, 6645, 6687, 6646, 6688, 6647, 6689, 6610, 6615, 6618, 6624, 6626, 6629, 6633, 6648, 6653, 6664, 6670, 6672, 6690, 6696, 6702, 6713, 6725, 6729, 6649, 6691, 6611, 6619, 6627, 6630, 6634, 6650, 6654, 6665, 6673, 6676, 6692, 6714, 6726, 6730, 6998, 6999 };
	}

	/**
	 * Configures the bonuses,
	 */
	@SuppressWarnings("deprecation")
	private void configureBonuses() {
		for (int i = 0; i < getProperties().getBonuses().length; i++) {
			getProperties().getBonuses()[i] = 40 + (4 * (getProperties().getCombatLevel() / 2));
		}
	}

	/**
	 * Configures a route for the revenant.
	 */
	private void configureRoute() {
		if (routes == null || routes.length == 0) {
			return;
		}
		configureMovementPath(RandomFunction.getRandomElement(routes));
	}

	/**
	 * Checks if the combat level of a revenant is acceptable to attack the
	 * player.
	 * @param player the player.
	 * @return {@code True} if so.
	 */
	private boolean hasAcceptableCombatLevel(Player player) {
		int level = WildernessZone.getWilderness(this);
		if (player.getSkullManager().getLevel() < level) {
			level = player.getSkullManager().getLevel();
		}
		int combat = getProperties().getCurrentCombatLevel();
		int targetCombat = player.getProperties().getCurrentCombatLevel();
		return Math.abs(combat - targetCombat) <= level;
	}

	/**
	 * Gets the routes.
	 * @return the routes
	 */
	public Location[][] getRoutes() {
		return routes;
	}

	/**
	 * Gets the type.
	 * @return the type
	 */
	public RevenantType getType() {
		return type;
	}

}
