package core.game.node.entity;

import core.game.event.*;
import core.game.interaction.*;
import core.game.node.Node;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.DeathTask;
import core.game.node.entity.combat.ImpactHandler;
import core.game.node.entity.combat.equipment.ArmourSet;
import core.game.node.entity.impl.*;
import core.game.node.entity.impl.Properties;
import core.game.node.entity.lock.ActionLocks;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.TeleportManager;
import core.game.node.entity.skill.Skills;
import core.game.system.task.Pulse;
import core.game.world.map.zone.ZoneRestriction;
import org.jetbrains.annotations.NotNull;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.Viewport;
import core.game.world.map.path.Path;
import core.game.world.map.path.Pathfinder;
import core.game.world.map.zone.ZoneMonitor;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.game.world.update.flag.*;
import core.game.node.entity.combat.CombatSwingHandler;
import core.game.world.update.UpdateMasks;
import core.game.system.timer.TimerManager;
import core.game.system.timer.TimerRegistry;

import java.util.*;

/**
 * An entity is a movable node, such as players and NPCs.
 * @author Emperor
 */
public abstract class Entity extends Node {

	/**
	 * The entity's properties.
	 */
	private final Properties properties = new Properties(this);

	/**
	 * The entity's update masks.
	 */
	private final UpdateMasks updateMasks = new UpdateMasks(this);

	/**
	 * The walking queue.
	 */
	private final WalkingQueue walkingQueue = new WalkingQueue(this);

	/**
	 * The entity's skills.
	 */
	public Skills skills = new Skills(this);

	/**
	 * The entity's extension classes.
	 */
	private final Map<Class<?>, Object> extensions = new HashMap<Class<?>, Object>();

	/**
	 * The entity's attributes.
	 */
	private final GameAttributes attributes = new GameAttributes();

	/**
	 * The entity's viewport.
	 */
	private final Viewport viewport = new Viewport();

	/**
	 * The pulse manager.
	 */
	private final PulseManager pulseManager = new PulseManager();

	/**
	 * The impact handler.
	 */
	private final ImpactHandler impactHandler = new ImpactHandler(this);

	/**
	 * The animator.
	 */
	private final Animator animator = new Animator(this);

	/**
	 * The teleporter.
	 */
	private final TeleportManager teleporter = new TeleportManager(this);

	/**
	 * The zone monitor.
	 */
	private final ZoneMonitor zoneMonitor = new ZoneMonitor(this);

	/**
	 * The reward locks.
	 */
	private final ActionLocks locks = new ActionLocks();
	public ScriptProcessor scripts = new ScriptProcessor(this);
	public final int[] clocks = new int[10];

	public MovementPulse currentMovement;


	/**
	 * The mapping of event types to event hooks
	 */
	private HashMap<Class<?>, ArrayList<EventHook>> hooks = new HashMap<>();
        public TimerManager timers = new TimerManager(this);

	/**
	 * If the entity is invisible.
	 */
	private boolean invisible;



	/**
	 * Constructs a new {@code Entity} {@code Object}.
	 * @param name The name of the entity.
	 * @param location The location.
	 */
	public Entity(String name, Location location) {
		super(name, location);
		super.destinationFlag = DestinationFlag.ENTITY;
	}

	/**
	 * Attempts to make the entity move one step (priority order: west, south,
	 * east, north).
	 */
	public void moveStep() {
		if (locks.isMovementLocked()) {
			return;
		}
		Path path;
		if (!(path = Pathfinder.find(this, getLocation().transform(-1, 0, 0), false, Pathfinder.DUMB)).isSuccessful()) {
			if (!(path = Pathfinder.find(this, getLocation().transform(0, -1, 0), false, Pathfinder.DUMB)).isSuccessful()) {
				if (!(path = Pathfinder.find(this, getLocation().transform(1, 0, 0), false, Pathfinder.DUMB)).isSuccessful()) {
					if (!(path = Pathfinder.find(this, getLocation().transform(0, 1, 0), false, Pathfinder.DUMB)).isSuccessful()) {
						path = null;
					}
				}
			}
		}
		if (path != null) {
			path.walk(this);
		}
	}

	/**
	 * Dispatches an event to this entity's event hooks
	 */
	public void dispatch(Event event)
	{
		if(this.hooks.containsKey(event.getClass()))
		{
			ArrayList<EventHook> processList = new ArrayList(this.hooks.get(event.getClass()));
			processList.forEach((hook) -> { hook.process(this, event); });
		}
	}

	/**
	 * Unhooks an eventhook from this entity
	 */
	public void unhook(EventHook hook)
	{
		for(ArrayList<EventHook> s : hooks.values()) s.remove(hook);
	}

	/**
	 * Hooks an eventhook to this entity
	 */
	public void hook(Event event, EventHook hook)
	{
		hook(event.getClass(), hook);
	}

	public void hook(Class<?> event, EventHook hook)
	{
		ArrayList<EventHook> hookList;
		if(hooks.get(event) != null)
		{
			hookList = hooks.get(event);
		}
		else
		{
			hookList = new ArrayList<EventHook>();
		}
		if (!hookList.contains(hook))
			hookList.add(hook);
		hooks.put(event, hookList);
	}

	/**
	 * Initializes the entity.
	 */
	public void init() {
		active = true;
                TimerRegistry.addAutoTimers (this);
	}

	/**
	 * This methods gets called before the {@link #update()} method.
	 */
	public void tick() {
		scripts.preMovement();
		dispatch(new TickEvent(GameWorld.getTicks()));
		skills.pulse();
		Location old = location != null ? location.transform(0, 0, 0) : Location.create(0,0,0);
		walkingQueue.update();
		scripts.postMovement(!Objects.equals(location, old));
                timers.processTimers();
		updateMasks.prepare(this);
	}

	/**
	 * Updates the entity, this gets called in a thread pool.
	 */
	public void update() {
	}

	/**
	 * Resets the entity's update flags, ... <br> Gets called after
	 * {@link #update()}.
	 */
	public void reset() {
		updateMasks.reset();
		properties.setTeleporting(false);
	}

	/**
	 * Removes the entity from the world.
	 */
	public void clear() {
		active = false;
		viewport.remove(this);
		pulseManager.clear();
	}

	/**
	 * Checks if the entity is in combat.
	 * @return {@code True} if so.
	 */
	public boolean inCombat() {
		return getAttribute("combat-time", 0L) > System.currentTimeMillis();
	}

	/**
	 * Fully restores the entity.
	 */
	public void fullRestore() {
		skills.restore();
                timers.removeTimer("poison");
                timers.removeTimer("poison:immunity");
	}

	/**
	 * Called when the death task gets submitted.
	 * @param killer The killer of this entity.
	 */
	public void commenceDeath(Entity killer) {
	}

	/**
	 * Finalizes the death task.
	 * @param killer The killer of this entity.
	 */
	public void finalizeDeath(Entity killer) {
		skills.restore();
		skills.rechargePrayerPoints();
		impactHandler.getImpactQueue().clear();
		impactHandler.setDisabledTicks(10);
		timers.onEntityDeath();
		removeAttribute("combat-time");
		face(null);
		//Check if it's a Loar shade and transform back into the shadow version.
		if(this.getId() == 1240 || this.getId() == 1241){
			this.asNpc().transform(1240);
		}
	}

	/**
	 * Updates the location of an entity.
	 * @param last the last location.
	 */
	public void updateLocation(Location last) {

	}

	/**
	 * Checks if multiway combat zone rules should be ignored.
	 * @param victim The victim.
	 * @return {@code True} if this entity can attack regardless of multiway
	 * combat zone.
	 */
	public boolean isIgnoreMultiBoundaries(Entity victim) {
		if (this instanceof NPC) {
			return ((NPC) this).behavior.shouldIgnoreMultiRestrictions((NPC) this, victim);
		}
		return false;
	}

    /**
     * Should this entity prevent the mover from moving through it?
     */
    public boolean shouldPreventStacking(Entity mover) {
        return false;
    }

	/**
	 * Checks an impact before receiving it.
	 */
	public void checkImpact(BattleState state) {
		getProperties().getCombatPulse().setLastReceivedAttack(GameWorld.getTicks());
		int ticks = GameWorld.getTicks() - getProperties().getCombatPulse().getLastSentAttack();
		if (ticks > 10 && this instanceof NPC && ((NPC) this).getDefinition().getConfiguration("safespot", false)) {
			Pathfinder.find(state.getAttacker(), getLocation()).walk(state.getAttacker());
			Pathfinder.find(state.getVictim(), state.getAttacker().getLocation()).walk(state.getVictim());
			if (ticks > 40) {
				state.getAttacker().moveStep();
				state.getAttacker().getProperties().getCombatPulse().stop();
				state.getVictim().moveStep();
				state.getVictim().getProperties().getCombatPulse().stop();
			}
		}
	}

	/**
	 * Handles an impact.
	 * @param entity The entity.
	 * @param state The battle state.
	 */
	public void onImpact(final Entity entity, BattleState state) {
		if (DeathTask.isDead(this))
			state.neutralizeHits();
		if (this instanceof NPC) {
			((NPC) this).behavior.afterDamageReceived((NPC) this, entity, state);
		}
		if (properties.isRetaliating() && !properties.getCombatPulse().isAttacking() && !getLocks().isInteractionLocked() && properties.getCombatPulse().getNextAttack() < GameWorld.getTicks()) {
			if (!getWalkingQueue().hasPath() && !getPulseManager().isMovingPulse() || (this instanceof NPC)) {
				properties.getCombatPulse().attack(entity);
			}
		}
	}

	/**
	 * Handles the first attack.
	 * @param target the target.
	 */
	public void onAttack(final Entity target) {

	}

	/**
	 * Method used to attack a node.
	 * @param node the node.
	 */
	public void attack(final Node node) {
		getProperties().getCombatPulse().attack(node);
	}

	/**
	 * Checks if the entity can move to the destination.
	 * @param destination the destination.
	 * @return {@code True} if so.
	 */
	public boolean canMove(Location destination) {
		return true;
	}

	/**
	 * Teleports the entity to a location.
	 * @param location the location.
	 */
	public void teleport(Location location) {
		getProperties().setTeleportLocation(location);
	}

	/**
	 * Teleports the entity.
	 * @param location the location.
	 * @param ticks the ticks.
	 */
	public void teleport(final Location location, int ticks) {
		GameWorld.getPulser().submit(new Pulse(ticks, this) {
			@Override
			public boolean pulse() {
				teleport(location);
				return true;
			}
		});
	}

	/**
	 * Locks the entity.
	 */
	public void lock() {
		locks.lock();
	}

	/**
	 * Locks the entity from using any actions.
	 * @param time The time (in game ticks) to lock.
	 */
	public void lock(int time) {
		locks.lock(time);
	}

	/**
	 * Unlocks the default reward locks.
	 */
	public void unlock() {
		locks.unlock();
	}

	/**
	 * Checks if the entity is using the protection prayer for the given style.
	 * @param style The combat style.
	 * @return {@code True} if so.
	 */
	public abstract boolean hasProtectionPrayer(CombatStyle style);

	/**
	 * Gets the dragonfire protection value.
	 * @param fire if a fire attack.
	 * @return The value.
	 */
	public abstract int getDragonfireProtection(boolean fire);

	/**
	 * Checks if this entity is attackable by the attacking entity.
	 * @param entity The attacking entity.
	 * @param style The combat style used.
     * @param message Whether to send the player a message indicating why the entity isn't attackable.
	 * @return {@code True} if the attacking entity can attack this entity.
	 */
	public boolean isAttackable(Entity entity, CombatStyle style, boolean message) {
		if (DeathTask.isDead(this)) {
			return false;
		}
		if (!entity.getZoneMonitor().continueAttack(this, style, message)) {
			return false;
		}
		return true;
	}

	/**
	 * Registers a new graphics update flag to the update masks.
	 * @param graphics The graphics.
	 * @return {@code True} if succesful.
	 */
	public boolean graphics(Graphics graphics) {
		return animator.graphics(graphics);
	}

	/**
	 * Registers a new animation update flag to the update masks.
	 * @param animation The animation.
	 * @return {@code True} if succesful.
	 */
	public boolean animate(Animation animation) {
		return animator.animate(animation);
	}

	/**
	 * Registers a new graphics update flag.
	 * @param graphics the graphics.
	 * @param delay the delay.
	 * @return {@code True} if so.
	 */
	public boolean graphics(final Graphics graphics, int delay) {
		GameWorld.getPulser().submit(new Pulse(delay, this) {
			@Override
			public boolean pulse() {
				graphics(graphics);
				return true;
			}
		});
		return true;
	}

	/**
	 * Checks if an entity can continue its attack.
	 * @param target the target.
	 * @param style the style.
	 * @return {@code True} if so.
	 */
	public boolean continueAttack(Entity target, CombatStyle style, boolean message) {
		return true;
	}

	/**
	 * Registers a new animation update flag to the update masks.
	 * @param animation The animation.
	 * @param delay the delay
	 * @return {@code True} if succesful.
	 */
	public boolean animate(final Animation animation, int delay) {
		GameWorld.getPulser().submit(new Pulse(delay, this) {
			@Override
			public boolean pulse() {
				animate(animation);
				return true;
			}
		});
		return true;
	}

	/**
	 * Sends the impact.
	 * @param state the state.
	 */
	public void sendImpact(BattleState state) {
		getProperties().getCombatPulse().setLastSentAttack(GameWorld.getTicks());
	}

	/**
	 * Checks if the target can be selected.
	 * @param target the target.
	 * @return {@code True} if so.
	 */
	public boolean canSelectTarget(Entity target) {
		return true;
	}

	/**
	 * Registers a new animation & graphic update flag to the update masks.
	 * @param animation The animation.
	 * @param graphics The graphics.
	 * @return {@code True} if successful.
	 */
	public boolean visualize(Animation animation, Graphics graphics) {
		return animator.animate(animation, graphics);
	}

	/**
	 * Temporarily faces an entity.
	 * @param entity The entity to face.
	 * @param ticks The ticks to face the entity.
	 * @return {@code True} if successful.
	 */
	public boolean faceTemporary(Entity entity, int ticks) {
		return faceTemporary(entity, null, ticks);
	}

	/**
	 * Temporarily faces an entity.
	 * @param entity The entity to face.
	 * @param reset The entity to face when the pulse has passed.
	 * @param ticks The ticks to face the entity.
	 * @return {@code True} if successful.
	 */
	public boolean faceTemporary(Entity entity, final Entity reset, int ticks) {
		if (face(entity)) {
			GameWorld.getPulser().submit(new Pulse(ticks + 1, this) {
				@Override
				public boolean pulse() {
					face(reset);
					return true;
				}
			});
			return true;
		}
		return false;
	}

	/**
	 * Gets the formatted hit.
	 * @param state the state.
	 * @param hit the hit.
	 * @return {@code True} if so.
	 */
	public double getFormattedHit(BattleState state, int hit) {
		if (state.getAttacker() == null || state.getVictim() == null || state.getStyle() == null) {
			return hit;
		}
		Entity entity = state.getAttacker();
		Entity victim = state.getVictim();
		CombatStyle type = state.getStyle();
		if (state.getArmourEffect() != ArmourSet.VERAC && !entity.isIgnoreProtection(type) && victim.hasProtectionPrayer(type)) {
			return hit *= (entity instanceof Player && victim instanceof Player) ? 0.6 : 0;
		}
		return hit;
	}

	/**
	 * Checks if this entity ignores protection prayers for the given combat style.
	 * @param style The combat style used.
	 * @return {@code True} if the entity can hit through protection prayers.
	 */
	public boolean isIgnoreProtection(CombatStyle style) {
		return false;
	}

	/**
	 * Starts the death for an npc.
	 * @param killer the killer.
	 */
	public void startDeath(Entity killer) {
		if (zoneMonitor.startDeath(this, killer)) {
			DeathTask.startDeath(this, killer);
		}
	}

	/**
	 * Casts the player type.
	 * @return the player.
	 */
	public Player asPlayer() {
		return (Player) this;
	}

	/**
	 * Checks if the entity is instance of a player.
	 * @return {@code True} if so.
	 */
	public boolean isPlayer() {
		return this instanceof Player;
	}

	/**
	 * Registers a new face entity update flag to the update masks.
	 * @param entity The entity to face.
	 * @return {@code True} if succesful.
	 */
	public boolean face(Entity entity) {
            if (entity == null) {
                int ordinal = EntityFlags.getOrdinal(EFlagType.of(this), EntityFlag.FaceEntity); 
                if (getUpdateMasks().unregisterSynced(ordinal)) {
                    return getUpdateMasks().register(EntityFlag.FaceEntity, null);
                }
                return true;
            }
            return getUpdateMasks().register(EntityFlag.FaceEntity, entity, true);
        }

	/**
	 * Registers a new face location update flag to the update masks.
	 * @param location The location to face.
	 * @return {@code True} if succesful.
	 */
	public boolean faceLocation(Location location) {
            if (location == null) {
                int ordinal = EntityFlags.getOrdinal(EFlagType.of(this), EntityFlag.FaceLocation);
                getUpdateMasks().unregisterSynced(ordinal);
                return true;
            }
            return getUpdateMasks().register(EntityFlag.FaceLocation, location, true);
        }

	/**
	 * Registers a new force chat update flag to the update masks.
	 * @param string The string.
	 * @return {@code True} if successful.
	 */
	public boolean sendChat(String string) {
            return getUpdateMasks().register(EntityFlag.ForceChat, string);
        }

	/**
	 * Gets the current combat swing handler.
	 * @param swing If this method is called when actually performing a combat
	 * swing.
	 * @return The current combat swing handler to use.
	 */
	public abstract CombatSwingHandler getSwingHandler(boolean swing);

	/**
	 * Checks if the entity is immune to poison.
	 * @return {@code True} if the entity is immune to poison.
	 */
	public abstract boolean isPoisonImmune();

	/**
	 * Sends the chat on a tick.
	 * @param string the string.
	 * @param ticks the ticks.
	 */
	public void sendChat(final String string, int ticks) {
		GameWorld.getPulser().submit(new Pulse(ticks, this) {
			@Override
			public boolean pulse() {
				sendChat(string);
				return true;
			}
		});
	}

	/**
	 * Gets the level mod.
	 * @param entity the entity.
	 * @param victim the victim.
	 * @return the levelMod.
	 */
	public double getLevelMod(Entity entity, Entity victim) {
		return 0;
	}

	/**
	 * Gets the reward locks.
	 * @return The reward locks.
	 */
	public ActionLocks getLocks() {
		return locks;
	}

	/**
	 * Gets the client index of the entity.
	 * @return The client index.
	 */
	public int getClientIndex() {
		return index;
	}

	/**
	 * Gets the properties.
	 * @return The properties.
	 */
	public Properties getProperties() {
		return properties;
	}
	/**
	 * Gets the updateMasks.
	 * @return The updateMasks.
	 */
	public UpdateMasks getUpdateMasks() {
		return updateMasks;
	}

	/**
	 * Adds an extension.
	 * @param clazz The class type.
	 * @param object The object.
	 */
	public void addExtension(Class<?> clazz, Object object) {
		extensions.put(clazz, object);
	}

	/**
	 * Gets an extension.
	 * @param clazz The class type.
	 * @return The object.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getExtension(Class<?> clazz) {
		return (T) extensions.get(clazz);
	}

	/**
	 * Gets an extension.
	 * @param clazz The class type.
	 * @param fail The object to return if the extension wasn't added.
	 * @return The object.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getExtension(Class<?> clazz, T fail) {
		T extension = (T) extensions.get(clazz);
		if (extension == null) {
			return fail;
		}
		return extension;
	}

	/**
	 * Removes the extension.
	 * @param clazz The class type.
	 */
	public void removeExtension(Class<?> clazz) {
		extensions.remove(clazz);
	}

	/**
	 * Gets the players attributes.
	 * @return the attributes.
	 */
	public Map<String, Object> getAttributes() {
		return attributes.getAttributes();
	}

	public void clearAttributes() {
		this.attributes.getAttributes().clear();
		this.attributes.getSavedAttributes().clear();
	}

	/**
	 * Sets an attribute value.
	 * @param key The attribute name.
	 * @param value The attribute value.
	 */
	public void setAttribute(String key, Object value) {
		attributes.setAttribute(key, value);
		dispatch(new AttributeSetEvent(this, key, value));
	}

	/**
	 * Gets an attribute.
	 * @param key The attribute name.
	 * @return The attribute value.
	 */
	public <T> T getAttribute(String key) {
		return attributes.getAttribute(key);
	}

	/**
	 * Increments an attribute
	 * @param key The attribute name.
	 */
	public void incrementAttribute(String key) {
		incrementAttribute(key, 1);
	}

	/**
	 * Increments an attribute
	 * @param key The attribute name.
	 */
	public void incrementAttribute(String key, int amount) {
		attributes.setAttribute(key, attributes.getAttribute(key, 0) + amount);
	}

	/**
	 * Gets an attribute.
	 * @param string The attribute name.
	 * @param fail The value to return if the attribute is null.
	 * @return The attribute value, or the fail argument when null.
	 */
	public <T> T getAttribute(String string, T fail) {
		return attributes.getAttribute(string, fail);
	}

	/**
	 * Removes an attribute.
	 * @param string The attribute name.
	 */
	public void removeAttribute(String string) {
		attributes.removeAttribute(string);
		dispatch(new AttributeRemoveEvent(this, string));
	}

	/**
	 * Gets the game attributes instance.
	 * @return The game attributes.
	 */
	public GameAttributes getGameAttributes() {
		return attributes;
	}

	/**
	 * Gets the walkingQueue.
	 * @return The walkingQueue.
	 */
	public WalkingQueue getWalkingQueue() {
		return walkingQueue;
	}

	/**
	 * Gets the viewport.
	 * @return The viewport.
	 */
	public Viewport getViewport() {
		return viewport;
	}

	/**
	 * Gets the skills.
	 * @return The skills.
	 */
	public Skills getSkills() {
		return skills;
	}

	/**
	 * Gets the pulseManager.
	 * @return The pulseManager.
	 */
	public PulseManager getPulseManager() {
		return pulseManager;
	}

	/**
	 * Gets the impactHandler.
	 * @return The impactHandler.
	 */
	public ImpactHandler getImpactHandler() {
		return impactHandler;
	}

	/**
	 * @return the animator.
	 */
	public Animator getAnimator() {
		return animator;
	}

	/**
	 * Gets the Teleporter
	 * @return the Teleporter
	 */
	public TeleportManager getTeleporter() {
		return teleporter;
	}

	/**
	 * Gets the zoneMonitor.
	 * @return The zoneMonitor.
	 */
	public ZoneMonitor getZoneMonitor() {
		return zoneMonitor;
	}

	/**
	 * Checks if we have fire resistance.
	 * @return {@code True} if so.
	 */
	public boolean hasFireResistance() {
		return getAttribute("fire:immune",0) >= GameWorld.getTicks();
	}

	/**
	 * Checks if the entity is teleblocked.
	 * @return {@code True} if so.
	 */
	public boolean isTeleBlocked() {
                return timers.getTimer("teleblock") != null || getLocks().isTeleportLocked() || getZoneMonitor().isRestricted(ZoneRestriction.TELEPORT);
	}

	/**
	 * Gets the invisible value.
	 * @return The invisible.
	 */
	public boolean isInvisible() {
		return invisible;
	}

	/**
	 * Sets the invisible value.
	 * @param invisible The invisible to set.
	 */
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

    public Location getClosestOccupiedTile(@NotNull Location other) {
		List<Location> occupied = getOccupiedTiles();

		Location closest = location;
		if (occupied.size() > 1) {
			double lowest = 9999;
			for (Location tile : occupied) {
				double dist = tile.getDistance(other);
				if (dist < lowest) {
					lowest = dist;
					closest = tile;
				}
			}
		}

		return closest;
    }

	public List<Location> getOccupiedTiles() {
		ArrayList<Location> occupied = new ArrayList<>();

		Location northEast = location.transform(size, size, 0);

		for (int x = location.getX(); x < northEast.getX(); x++) {
			for (int y = location.getY(); y < northEast.getY(); y++) {
				occupied.add(Location.create(x, y, location.getZ()));
			}
		}
		return occupied;
	}

	public boolean delayed() {
		return scripts.getDelay() > GameWorld.getTicks();
	}

        public boolean isTeleporting() {
            return getAttribute("tele-pulse", null) != null || properties.getTeleportLocation() != null;
        }
}
