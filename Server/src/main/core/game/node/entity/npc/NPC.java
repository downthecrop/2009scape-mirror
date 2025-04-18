package core.game.node.entity.npc;

import core.game.event.NPCKillEvent;
import core.cache.def.impl.NPCDefinition;
import core.game.dialogue.DialoguePlugin;
import core.game.interaction.InteractPlugin;
import core.game.interaction.MovementPulse;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.spell.CombatSpell;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.spell.DefaultCombatSpell;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.npc.agg.AggressiveBehavior;
import core.game.node.entity.npc.agg.AggressiveHandler;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.entity.skill.Skills;
import content.global.skill.slayer.Tasks;
import content.global.skill.summoning.familiar.Familiar;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.build.DynamicRegion;
import core.game.world.map.path.Pathfinder;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.game.world.update.flag.*;
import core.tools.RandomFunction;
import core.api.utils.PlayerStatsCounter;
import core.api.utils.Vector;
import core.game.shops.Shops;
import core.game.node.entity.combat.CombatSwingHandler;
import core.game.system.config.NPCConfigParser;
import core.game.world.GameWorld;
import core.game.world.repository.Repository;

import static core.game.system.command.sets.StatAttributeKeysKt.STATS_BASE;
import static core.game.system.command.sets.StatAttributeKeysKt.STATS_ENEMIES_KILLED;

/**
 * Represents a non-player character.
 * @author Emperor
 */
public class NPC extends Entity {

	/**
	 * The non-player character's id.
	 */
	private int id;

	/**
	 * The original NPC id.
	 */
	private final int originalId;

	/**
	 * The NPC definitions.
	 */
	private NPCDefinition definition;

	/**
	 * If the NPC walks;
	 */
	private boolean walks;

	/**
	 * If the NPC is aggressive.
	 */
	private boolean aggressive;

	/**
	 * Represents if the NPC will respawn.
	 */
	private boolean respawn = true;

	/**
	 * The movement path of the NPC.
	 */
	protected Location[] movementPath;

	/**
	 * The walking radius.
	 */
	protected int walkRadius = 11;

	/**
	 * The movement index.
	 */
	protected int movementIndex;

	/**
	 * The respawn tick.
	 */
	private int respawnTick = -1;

	/**
	 * If the NPC walks bound to a path.
	 */
	protected boolean pathBoundMovement;

	/**
	 * The current dialogue.
	 */
	protected Player dialoguePlayer;

	/**
	 * The aggressive handler of the NPC.
	 */
	protected AggressiveHandler aggressiveHandler;

	/**
	 * The walk back radius.
	 */
	protected int nextWalk;

	/**
	 * The children NPCs.
	 */
	private NPC[] children;

	/**
	 * The amount of slayer experience received when killing this NPC.
	 */
	private double slayerExperience;

	/**
	 * The slayer task.
	 */
	private Tasks task;

	/**
	 * If the npc can never walk.
	 */
	private boolean neverWalks;
	
	/**
	 * The force talk string.
	 */
	private String forceTalk;

	public NPCBehavior behavior;

        public boolean isRespawning = false;

	/**
	 * Constructs a new {@code NPC} {@code Object}.
	 * @param id The NPC id.
	 */
	public NPC(int id) {
		this(id, null);
	}

	/**
	 * Constructs a new {@code NPC} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	protected NPC(int id, Location location, Direction direction) {
		super(NPCDefinition.forId(id).getName(), location);
		this.id = id;
		this.originalId = id;
		this.definition = NPCDefinition.forId(id);
		super.size = definition.size;
		super.direction = direction;
		super.interactPlugin = new InteractPlugin(this);
		this.behavior = NPCBehavior.forId(id);
	}

	/**
	 * Constructs a new {@code NPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public NPC(int id, Location location) {
		this(id, location, Direction.SOUTH);
	}

	/**
	 * Creates a new NPC object.
	 * @param id The NPC id.
	 * @param location The location.
	 * @param direction the dir.
	 * @param objects the objects.
	 * @return The NPC object.
	 */
	public static NPC create(int id, Location location, Direction direction, Object... objects) {
		NPC n = AbstractNPC.forId(id);

		if (n != null) {
			n = ((AbstractNPC) n).construct(id, location, objects);
		}
		if (n == null) {
			n = new NPC(id, location, direction);
		}
		return n;
	}

	/**
	 * Creates a new NPC object.
	 * @param id the id.
	 * @param location the location.
	 * @return the npc object.
	 */
	public static NPC create(int id, Location location, Object... objects) {
		return create(id, location, Direction.SOUTH, objects);
	}

	@Override
	public void init() {
		super.init();
		getProperties().setSpawnLocation(getLocation());
		initConfig();
		Repository.getNpcs().add(this);
		RegionManager.move(this);
		if (getViewport().getRegion().isActive()) {
			Repository.addRenderableNPC(this);
		}
		interactPlugin.setDefault();
		configure();
		setDefaultBehavior();
		if (definition.childNPCIds != null) {
			children = new NPC[definition.childNPCIds.length];
			for (int i = 0; i < children.length; i++) {
				NPC npc = children[i] = new NPC(definition.childNPCIds[i]);
				npc.interactPlugin.setDefault();
				npc.index = index;
				npc.size = size;
			}
		}
		behavior.onCreation(this);
        // FIXME: hack around MovementPulse's constructor getting run while behavior is null when behavior is set between NPC constructor and init.
		// FIXME: Commented out as a fix. most npcs were not being able to attack with range/magic due to setting the combat pulse.
        // getProperties().setCombatPulse(new CombatPulse(this));
	}

	@Override
	public void clear() {
		super.clear();
		Repository.removeRenderableNPC(this);
		Repository.getNpcs().remove(this);
		getViewport().setCurrentPlane(null);
		behavior.onRemoval(this);
		// getViewport().setRegion(null);
	}

	/**
	 * Configures default boss data.
	 */
	public void configureBossData() {
		getProperties().setNPCWalkable(true);
		getProperties().setCombatTimeOut(2);
		if (!isAggressive()) {
			setAggressive(true);
			setDefaultBehavior();
		}
		if (getAggressiveHandler() != null) {
			getAggressiveHandler().setChanceRatio(6);
			getAggressiveHandler().setAllowTolerance(false);
			getAggressiveHandler().setTargetSwitching(true);
			getAggressiveHandler().setRadius(64);
		}
		walkRadius = 64;
	}

	/**
	 * Initializes the configurations.
	 */
	public void initConfig() {
		int defaultLevel = definition.getCombatLevel() / 2;
		getProperties().setCombatLevel(definition.getCombatLevel());
		getSkills().setStaticLevel(Skills.ATTACK, definition.getConfiguration(NPCConfigParser.ATTACK_LEVEL, defaultLevel));
		getSkills().setStaticLevel(Skills.STRENGTH, definition.getConfiguration(NPCConfigParser.STRENGTH_LEVEL, defaultLevel));
		getSkills().setStaticLevel(Skills.DEFENCE, definition.getConfiguration(NPCConfigParser.DEFENCE_LEVEL, defaultLevel));
		getSkills().setStaticLevel(Skills.RANGE, definition.getConfiguration(NPCConfigParser.RANGE_LEVEL, defaultLevel));
		getSkills().setStaticLevel(Skills.MAGIC, definition.getConfiguration(NPCConfigParser.MAGIC_LEVEL, defaultLevel));
		getSkills().setStaticLevel(Skills.HITPOINTS, definition.getConfiguration(NPCConfigParser.LIFEPOINTS, defaultLevel));
		int lp = getSkills().getMaximumLifepoints();
		if (this instanceof Familiar) {
			lp = getAttribute("hp", lp);
		}
		getSkills().setLevel(Skills.HITPOINTS, lp);
		aggressive = definition.getConfiguration(NPCConfigParser.AGGRESSIVE, aggressive);
		Animation anim = null;
		for (int i = 0; i < 3; i++) {
			if (definition.getCombatAnimation(i) != null) {
				getProperties().setAttackAnimation(anim = definition.getCombatAnimation(i));
				break;
			}
		}
		if (definition.getCombatAnimation(3) != null) {
			getProperties().setDefenceAnimation(definition.getCombatAnimation(3));
		}
		if (definition.getCombatAnimation(4) != null) {
			getProperties().setDeathAnimation(definition.getCombatAnimation(4));
		}
		if (definition.getCombatAnimation(1) != null) {
			getProperties().setMagicAnimation(definition.getCombatAnimation(1));
		}
		if (definition.getCombatAnimation(2) != null) {
			getProperties().setRangeAnimation(definition.getCombatAnimation(2));
		}
		if (getProperties().getAttackAnimation() == null && anim != null) {
			getProperties().setAttackAnimation(anim);
		}
		if (getProperties().getMagicAnimation() == null && anim != null) {
			getProperties().setMagicAnimation(anim);
		}
		if (getProperties().getRangeAnimation() == null && anim != null) {
			getProperties().setRangeAnimation(anim);
		}
		definition.initCombatGraphics(definition.getHandlers());
		getProperties().setBonuses(definition.getConfiguration(NPCConfigParser.BONUSES, new int[15]));
		getProperties().setAttackSpeed(definition.getConfiguration(NPCConfigParser.ATTACK_SPEED, 5));
		forceTalk = definition.getConfiguration("force_talk", null);
		if (definition.getConfiguration("movement_radius") != null) {
			this.setWalkRadius(definition.getConfiguration("movement_radius"));
		}
		if(definition.getConfiguration("death_gfx") != null) {
			getProperties().deathGfx = new Graphics(definition.getConfiguration("death_gfx"));
		}
	}

	/**
	 * Opens the shop for a player.
	 * @param player the player.
	 * @return {@code True} if so.
	 */
	public boolean openShop(Player player) {
		if (getName().contains("assistant")) {
			NPC n = RegionManager.getNpc(this, getId() - 1);
			if (n != null) {
				return n.openShop(player);
			}
		}

		if (Shops.getShopsByNpc().get(id) == null) {
			return false;
		}
		Shops.getShopsByNpc().get(id).openFor(player);
		
		//Fix for issue #11 for shops keeping dialogue open.
		DialoguePlugin dialogue = player.getDialogueInterpreter().getDialogue();
		if (dialogue != null)
			dialogue.end();
		return true;
	}

	@Override
	public boolean isInvisible() {
		if (!isActive() || getRespawnTick() > GameWorld.getTicks()) {
			return true;
		}
		return super.isInvisible();
	}

	/**
	 * Checks if the NPC is hidden for the player.
	 * @param player The player.
	 * @return {@code True} if so.
	 */
	public boolean isHidden(Player player) {
		return isInvisible();
	}

	/**
	 * Sets the default aggressive behavior of the NPC.
	 */
	public void setDefaultBehavior() {
		if (aggressive && definition.getCombatLevel() > 0) {
			aggressiveHandler = new AggressiveHandler(this, AggressiveBehavior.DEFAULT);
			aggressiveHandler.setRadius(definition.getConfiguration(NPCConfigParser.AGGRESSIVE_RADIUS, 4));
		}
	}

	/**
	 * Configures the movement path.
	 * @param movementPath The movement path.
	 */
	public void configureMovementPath(Location... movementPath) {
		this.movementPath = movementPath;
		this.movementIndex = 0;
		this.pathBoundMovement = true;
	}

	@Override
	public void checkImpact(BattleState state) {
		super.checkImpact(state);
		Entity entity = state.getAttacker();
		behavior.beforeDamageReceived(this, entity, state);
		if (task != null && entity instanceof Player && task.levelReq > entity.getSkills().getLevel(Skills.SLAYER)) {
			state.neutralizeHits();
		}
	}

	@Override
	public boolean isAttackable(Entity entity, CombatStyle style, boolean message) {
		if (isInvisible()) {
			return false;
		}
		if (task != null && entity instanceof Player && task.levelReq > entity.getSkills().getLevel(Skills.SLAYER)) {
            if(message) {
                ((Player) entity).getPacketDispatch().sendMessage("You need a higher slayer level to know how to wound this monster.");
            }
		}
		if (!behavior.canBeAttackedBy(this, entity, style, message))
			return false;
		return super.isAttackable(entity, style, message);
	}

	@Override
	public int getDragonfireProtection(boolean fire) {
		return 0;
	}

	@Override
	public void tick() {
		if (!getViewport().getRegion().isActive()) {
			onRegionInactivity();
			return;
		}
		if (respawnTick > GameWorld.getTicks()) {
			if (respawnTick - GameWorld.getTicks() == 2)
				teleport(getProperties().getSpawnLocation());
			return;
		}
		if (isRespawning && respawnTick <= GameWorld.getTicks()) {
                        behavior.onRespawn(this);
			onRespawn();
                        fullRestore();
                        isRespawning = false;
		}
		handleTickActions();
		super.tick();
	}

	/**
	 * Called when the NPC respawns.
	 */
	protected void onRespawn() {}

	/**
	 * Handles the automatic actions of the NPC.
	 */
	public void handleTickActions() {
		if (!behavior.tick(this))
			return;
		if (!getLocks().isInteractionLocked()) {
			if (!getLocks().isMovementLocked()) {
				if (
						!pathBoundMovement
						&& walkRadius > 0
						&& walkRadius <= 20
						&& !getLocation().withinDistance(getProperties().getSpawnLocation(), (int)(walkRadius * 1.5))
						&& !getAttribute("no-spawn-return", false)
					)	
				{
					MovementPulse current = getAttribute("return-to-spawn-pulse");
					if (current != null && current.isRunning()) return;

					if(!isNeverWalks()){
						if(walkRadius == 0)
							walkRadius = 3;
					}
					if (aggressiveHandler != null) {
						aggressiveHandler.setPauseTicks(walkRadius + 1);
					}
					nextWalk = GameWorld.getTicks() + walkRadius + 1;
					getLocks().lockMovement(100);
					getImpactHandler().setDisabledTicks(100);
					setAttribute("return-to-spawn", true);

					MovementPulse returnPulse = new MovementPulse(this, getProperties().getSpawnLocation(), Pathfinder.SMART) {
						@Override
						public boolean pulse() {
							getProperties().getCombatPulse().stop();
							getLocks().unlockMovement();
							fullRestore();
							getImpactHandler().setDisabledTicks(0);
							removeAttribute("return-to-spawn");
							removeAttribute("return-to-spawn-pulse");
							return true;
						}
					};

					setAttribute("return-to-spawn-pulse", returnPulse);
					GameWorld.getPulser().submit(returnPulse);
					return;
				}
				if (dialoguePlayer == null || !dialoguePlayer.isActive() || !dialoguePlayer.getInterfaceManager().hasChatbox()) {
					dialoguePlayer = null;
					if (walks && !getPulseManager().hasPulseRunning() && !getProperties().getCombatPulse().isAttacking() && !getProperties().getCombatPulse().isInCombat() && nextWalk < GameWorld.getTicks()) {
						if (walkToNextDest()) return;
					}
				}
			}
			if (aggressive && aggressiveHandler != null && aggressiveHandler.selectTarget()) {
				return;
			}
			if (getProperties().getCombatPulse().isAttacking()) {
				return;
			}
		}
		if (forceTalk != null && getAttribute("lastForceTalk", 0) < GameWorld.getTicks()) {
			sendChat(forceTalk);
			setAttribute("lastForceTalk", GameWorld.getTicks() + RandomFunction.random(15, 30));
		}
	}

	private boolean walkToNextDest() {
		if (RandomFunction.nextBool()) return true;
		setNextWalk();
		Location l = getMovementDestination();
		if (canMove(l)) {
			if((Boolean) definition.getHandlers().getOrDefault("water_npc",false)){
				Pathfinder.findWater(this,l,true,Pathfinder.DUMB).walk(this);
			} else {
				Pathfinder.find(this, l, true, Pathfinder.DUMB).walk(this);
			}
		}
		return false;
	}

	public int getNextWalk() {
		return nextWalk;
	}
	/**
	 * Sets the next walk.
	 */
	public void setNextWalk() {
		nextWalk = GameWorld.getTicks() + 5 + RandomFunction.randomize(10);
	}

    public void resetWalk() {
        nextWalk = GameWorld.getTicks() - 1;
        getWalkingQueue().reset();
    }

	/**
	 * Called when the region goes inactive.
	 */
	public void onRegionInactivity() {
		getWalkingQueue().reset();
		getPulseManager().clear();
		getUpdateMasks().reset();
		if (getAttribute("return-to-spawn", false)) {
			this.location = getProperties().getSpawnLocation();
			MovementPulse returnPulse = getAttribute("return-to-spawn-pulse");
			if (returnPulse != null) {
				returnPulse.pulse();
				returnPulse.stop();
			}
		}
		Repository.removeRenderableNPC(this);
		if (getViewport().getRegion() instanceof DynamicRegion) {
			clear();
		}
	}

	@Override
	public boolean isPoisonImmune() {
		return definition.getConfiguration(NPCConfigParser.POISON_IMMUNE, false);
	}

	@Override
	public void finalizeDeath(Entity killer) {
		super.finalizeDeath(killer);
		if (getZoneMonitor().handleDeath(killer)) {
			return;
		}
		Player p = !(killer instanceof Player) ? null : (Player) killer;
		if (p != null) {
			p.incrementAttribute("/save:" + STATS_BASE + ":" + STATS_ENEMIES_KILLED);
            PlayerStatsCounter.incrementKills(p, originalId);
		}
		handleDrops(p, killer);
		if (!isRespawn())
			clear();
		isRespawning = true;
		behavior.onDeathFinished(this, killer);
		killer.dispatch(new NPCKillEvent(this));
		setRespawnTick(GameWorld.getTicks() + definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 17));
	}

        public void setRespawnTicks (int ticks) {
            definition.getHandlers().put(NPCConfigParser.RESPAWN_DELAY, ticks);
        }

	@Override
	public void commenceDeath(Entity killer) {
		behavior.onDeathStarted(this, killer);
	}

	/**
	 * Handles the drops of the npc.
	 * @param p the p.
	 * @param killer the killer.
	 */
	public void handleDrops(Player p, Entity killer) {
		if (getAttribute("disable:drop", false)) {
			return;
		}
		if (killer instanceof Player && p != null && p.getIronmanManager().isIronman() && getImpactHandler().getPlayerImpactLog().size() > 1) {
			return;
		}
		if (definition == null || definition.getDropTables() == null) {
			return;
		}
		definition.getDropTables().drop(this, killer);
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void reset() {
		super.reset();
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		CombatSwingHandler original = getProperties().getCombatPulse().getStyle().getSwingHandler();
		return behavior.getSwingHandlerOverride(this, original);
	}

	/**
	 * Gets an audio piece.
	 * @param index the index.
	 * @return the audio.
	 */
	public Audio getAudio(int index) {
		int[] audios = getDefinition().getConfiguration(NPCConfigParser.COMBAT_AUDIO, null);
		if (audios != null) {
			Audio audio = new Audio(audios[index]);
			if (audio != null && audio.id != 0) {
				return audio;
			}
		}
		return null;
	}

	/**
	 * Configures the NPC. <br> <b>Override this instead of {@link #init()} when
	 * creating a sub class.</b>
	 */
	public void configure() {
		int[] bonus = definition.getConfiguration(NPCConfigParser.BONUSES, new int[3]);
		int highest = 0;
		int index = 0;
		for (int i = 0; i < 3; i++) {
			if (bonus[i] > highest) {
				highest = bonus[i];
				index = i;
			}
		}
		CombatStyle protectStyle = definition.getConfiguration(NPCConfigParser.PROTECT_STYLE, null);
		if (protectStyle !=  null) {
			getProperties().setProtectStyle(protectStyle);
		}
		getProperties().setAttackStyle(new WeaponInterface.AttackStyle(WeaponInterface.STYLE_CONTROLLED, index));
		CombatStyle style = getDefinition().getConfiguration(NPCConfigParser.COMBAT_STYLE);
        if (style != null) {
            getProperties().getCombatPulse().setStyle(style);
        }
		if (style == CombatStyle.MAGIC) {
			getProperties().setAutocastSpell(new DefaultCombatSpell(this));
			int spell = definition.getConfiguration("spell_id", -1);
			if (spell != -1) {
				getProperties().setSpell((CombatSpell) SpellBookManager.SpellBook.MODERN.getSpell(spell));
				getProperties().setAutocastSpell((CombatSpell) SpellBookManager.SpellBook.MODERN.getSpell(spell));
			}
		} 
		task = Tasks.forId(getId());
	}

	/**
	 * Method used to check if the NPC can attack the victim. This method is
	 * used for aggressive behavior.
	 * @return <code>True</code> if so.
	 */
	public boolean canAttack(final Entity victim) {
		return true;
	}

	@Override
	public boolean hasProtectionPrayer(CombatStyle style) {
		return getProperties().getProtectStyle() == style;
	}

	/**
	 * Checks if the npc should ignore victim attack restrictions(i.e You can't
	 * attack this npc.")
	 * @param victim the victim.
	 * @return {@code True} if ignore.
	 */
	public boolean isIgnoreAttackRestrictions(Entity victim) {
		return false;
	}

	@Override
	public String toString() {
		return "NPC " + id + ", " + getLocation() + ", " + getIndex();
	}

	/**
	 * Transforms this NPC.
	 * @param id The new NPC id.
	 */
	public NPC transform(int id) {
		this.id = id;
		this.definition = NPCDefinition.forId(id);
		super.name = definition.getName();
		super.size = definition.size;
		super.interactPlugin = new InteractPlugin(this);
		initConfig();
		configure();
		interactPlugin.setDefault();
		if (id == originalId) {
                    int ordinal = EntityFlags.getOrdinal (EFlagType.NPC, EntityFlag.TypeSwap);
                    getUpdateMasks().unregisterSynced(ordinal);
		}
                getUpdateMasks().register(EntityFlag.TypeSwap, id, id != originalId);
		return this;
	}

	/**
	 * Transforms this NPC back to original.
	 */
	public void reTransform() {
		if (originalId == this.id) {
			return;
		}
		transform(originalId);
	}

	/**
	 * Gets the movement destination of the NPC.
	 * @return The movement destination.
	 */
	protected Location getMovementDestination() {
		if (!pathBoundMovement || movementPath == null || movementPath.length < 1) {
			Location returnToSpawnLocation = getProperties().getSpawnLocation().transform(-5 + RandomFunction.random(getWalkRadius()), -5 + RandomFunction.random(getWalkRadius()), 0);
			int dist = (int) Location.getDistance(location, returnToSpawnLocation);
			int pathLimit = 14;
                        if (dist > pathLimit) {
                            Vector normalizedDir = Vector.betweenLocs(this.location, returnToSpawnLocation).normalized();
                            returnToSpawnLocation = this.location.transform (normalizedDir.times(pathLimit));
                        }
			return returnToSpawnLocation;
		}
		Location l = movementPath[movementIndex++];
		if (movementIndex == movementPath.length) {
			movementIndex = 0;
		}
		return l;
	}
	
	/**
	 * Gets the drop location.
	 * @return The drop location.
	 */
	public Location getDropLocation() {
		return getLocation();
	}

	/**
	 * Checks if the npc can start combat.
	 * @param victim the victim.
	 * @return {@code True} if so.
	 */
	public boolean canStartCombat(Entity victim) {
		return true;
	}

	/**
	 * Gets the definition.
	 * @return The definition.
	 */
	public NPCDefinition getDefinition() {
		return definition;
	}

	/**
	 * Sets the definition.
	 * @param definition The definition to set.
	 */
	public void setDefinition(NPCDefinition definition) {
		this.definition = definition;
	}

	/**
	 * Get the id.
	 * @return The non-player character's id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the id.
	 * @param id The non-player character's id.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the walks.
	 * @return The walks.
	 */
	public boolean isWalks() {
		return walks;
	}

	/**
	 * Sets the walks.
	 * @param walks The walks to set.
	 */
	public void setWalks(boolean walks) {
		this.walks = walks;
	}

	/**
	 * Gets the aggressive.
	 * @return The aggressive.
	 */
	public boolean isAggressive() {
		return aggressive;
	}

	/**
	 * Sets the aggressive.
	 * @param aggressive The aggressive to set.
	 */
	public void setAggressive(boolean aggressive) {
		this.aggressive = aggressive;
	}

	/**
	 * Sets the name.
	 * @param @name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the movementPath.
	 * @return The movementPath.
	 */
	public Location[] getMovementPath() {
		return movementPath;
	}

	/**
	 * @return the respawn.
	 */
	public boolean isRespawn() {
		return respawn;
	}

	/**
	 * @param respawn the respawn to set.
	 */
	public void setRespawn(boolean respawn) {
		this.respawn = respawn;
	}

	/**
	 * Gets the pathBoundMovement.
	 * @return The pathBoundMovement.
	 */
	public boolean isPathBoundMovement() {
		return pathBoundMovement;
	}

	/**
	 * Sets the pathBoundMovement.
	 * @param pathBoundMovement The pathBoundMovement.
	 */
	public void setPathBoundMovement(boolean pathBoundMovement) {
		this.pathBoundMovement = pathBoundMovement;
	}

	/**
	 * Gets the dialoguePlayer.
	 * @return The dialoguePlayer.
	 */
	public Player getDialoguePlayer() {
		return dialoguePlayer;
	}

	/**
	 * Sets the dialoguePlayer.
	 */
	public void setDialoguePlayer(Player dialoguePlayer) {
		this.dialoguePlayer = dialoguePlayer;
	}

	/**
	 * Gets the aggressive handler.
	 * @return The aggressive handler.
	 */
	public AggressiveHandler getAggressiveHandler() {
		return aggressiveHandler;
	}

	/**
	 * Sets the aggressive handler.
	 * @param handler The handler.
	 */
	public void setAggressiveHandler(AggressiveHandler handler) {
		this.aggressiveHandler = handler;
	}

	/**
	 * Gets the respawnTick.
	 * @return The respawnTick.
	 */
	public int getRespawnTick() {
		return respawnTick;
	}

	/**
	 * Sets the walking radius.
	 * @param radius The walking radius.
	 */
	public void setWalkRadius(int radius) {
		this.walkRadius = radius;
	}

	/**
	 * Gets the walking radius.
	 * @return the walking radius.
	 */
	public int getWalkRadius() {
		return walkRadius;
	}

	/**
	 * Sets the respawnTick.
	 * @param respawnTick The respawnTick to set.
	 */
	public void setRespawnTick(int respawnTick) {
		this.respawnTick = respawnTick;
	}

	/**
	 * Gets the originalId.
	 * @return The originalId.
	 */
	public int getOriginalId() {
		return originalId;
	}

	/**
	 * Gets the currently shown NPC.
	 * @param player The player to check for.
	 * @return The shown NPC.
	 */
	public NPC getShownNPC(Player player) {
		if (children == null) {
			return this;
		}
		int npcId = definition.getChildNPC(player).getId();
		for (NPC npc : children) {
			if (npc.getId() == npcId) {
				return npc;
			}
		}
		return this;
	}

	/**
	 * Gets the slayerExperience.
	 * @return The slayerExperience.
	 */
	public double getSlayerExperience() {
		return slayerExperience;
	}

	/**
	 * Sets the slayerExperience.
	 * @param slayerExperience The slayerExperience to set.
	 */
	public void setSlayerExperience(double slayerExperience) {
		this.slayerExperience = slayerExperience;
	}

	/**
	 * Gets the task.
	 * @return The task.
	 */
	public Tasks getTask() {
		return task;
	}

	/**
	 * Sets the task.
	 * @param task The task to set.
	 */
	public void setTask(Tasks task) {
		this.task = task;
	}

	/**
	 * Gets the neverWalks.
	 * @return the neverWalks
	 */
	public boolean isNeverWalks() {
		return neverWalks;
	}

	/**
	 * Sets the baneverWalks.
	 * @param neverWalks the neverWalks to set.
	 */
	public void setNeverWalks(boolean neverWalks) {
		this.neverWalks = neverWalks;
	}

}
