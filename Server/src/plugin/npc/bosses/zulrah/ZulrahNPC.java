package plugin.npc.bosses.zulrah;

import org.crandor.game.content.global.BossKillCounter;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatSwingHandler;
import org.crandor.game.node.entity.npc.AbstractNPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.tools.RandomFunction;

/**
 * Handles the Zulrah NPC.
 * @author Vexia
 *
 */
public class ZulrahNPC extends AbstractNPC {

	/**
	 * The animation used for shooting projectiles.
	 */
	public static final Animation SHOOT_ANIMAION = new Animation(11246);

	/**
	 * The animation used for going into water.
	 */
	public static final Animation DOWN_ANIMATION = new Animation(11247);

	/**
	 * The animation used for coming out of water.
	 */
	public static final Animation UP_ANIMATION = new Animation(11248);
	
	/**
	 * The snakeling projectile id.
	 */
	public static final int SNAKELING_PROJECTILE = 1997;
	
	/**
	 * The toxic cloud projectile id.
	 */
	public static final int TOXIC_PROJECTILE = 1995; 
	
	/**
	 * The swing handler.
	 */
	private final ZulrahCombatHandler swingHandler = new ZulrahCombatHandler();

	/**
	 * The pattern zulrah will use.
	 */
	private final ZulrahPattern pattern;

	/**
	 * The delay between switching patterns.
	 */
	private int patternDelay;

	/**
	 * The base.
	 */
	private Location base;

	/**
	 * Constructs a new @{Code ZulrahNPC} object.
	 */
	public ZulrahNPC() {
		this(-1, null, null);
	}

	/**
	 * Constructs a new @{Code ZulrahNPC} object.
	 * @param id The id.
	 * @param location The location.
	 */
	public ZulrahNPC(int id, Location location, ZulrahPattern pattern) {
		super(id, location);
		this.pattern = pattern;
	}

	@Override
	public void init() {
		super.init();
		setPatternDelay();
		animate(UP_ANIMATION);
		//pattern.toxic(this);
	}

	@Override
	public void handleTickActions() {
		super.handleTickActions();
		if (patternDelay < GameWorld.getTicks()) {
			setPatternDelay();
			pattern.next(this);
		}
	}

	@Override
	public void sendImpact(BattleState state) {
		super.sendImpact(state);
	}

	@Override
	public void checkImpact(BattleState state) {
		super.checkImpact(state);
	}

	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
		if (killer instanceof Player) {
			BossKillCounter.addtoKillcount((Player) killer, getId());
		}
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		ZulrahPattern pattern = objects.length > 0 && objects[0] instanceof ZulrahPattern ? (ZulrahPattern) objects[0] : ZulrahPattern.getPattern();
		return new ZulrahNPC(id, location, pattern);
	}
	
	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		return swingHandler;
	}

	@Override
	public int[] getIds() {
		return new int[] {8620, 8621, 8622};
	}
	
	/**
	 * Sets the pattern delay.
	 */
	private void setPatternDelay() {
		patternDelay = GameWorld.getTicks() + RandomFunction.random(20, 30);
	}
	
	/**
	 * Gets the type of zulrah.
	 * @return The zulrah type.
	 */
	public ZulrahType getType() {
		return pattern.getCurrentSpot().getType();
	}
	
	/**
	 * Gets the zulrah spot.
	 * @return The zulrah spot.
	 */
	public ZulrahSpot getSpot() {
		return pattern.getCurrentSpot();
	}

	/**
	 * @return the pattern
	 */
	public ZulrahPattern getPattern() {
		return pattern;
	}

	/**
	 * @return The base.
	 */
	public Location getBase() {
		return base;
	}

	/**
	 * Sets the base.
	 * @param location The location.
	 */
	public void setBase(Location location) {
		this.base = location;
	}

	/**
	 * @return the X offset.
	 */
	public int getXOffset() {
		return 2240 - base.getX();
	}

	/**
	 * @return The y offset.
	 */
	public int getYOffset() {
		return 3008 - base.getY();
	}

}
