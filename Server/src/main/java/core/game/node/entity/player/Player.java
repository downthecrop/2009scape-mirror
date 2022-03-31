package core.game.node.entity.player;

import core.game.component.Component;
import core.game.container.Container;
import core.game.container.ContainerType;
import core.game.container.impl.BankContainer;
import core.game.container.impl.EquipmentContainer;
import core.game.container.impl.InventoryListener;
import core.game.content.activity.pyramidplunder.PlunderObjectManager;
import core.game.content.dialogue.DialogueInterpreter;
import core.game.content.quest.miniquest.barcrawl.BarcrawlManager;
import core.game.content.ttrail.TreasureTrailManager;
import core.game.interaction.Interaction;
import core.game.interaction.item.brawling_gloves.BrawlingGlovesManager;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.DeathTask;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.combat.handlers.ChinchompaSwingHandler;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.info.PlayerDetails;
import core.game.node.entity.player.info.RenderInfo;
import core.game.node.entity.player.info.Rights;
import core.game.node.entity.player.info.UIDInfo;
import core.game.node.entity.player.info.login.LoginConfiguration;
import core.game.node.entity.player.link.*;
import core.game.node.entity.player.link.appearance.Appearance;
import core.game.node.entity.player.link.audio.AudioManager;
import core.game.node.entity.player.link.diary.AchievementDiaryManager;
import core.game.node.entity.player.link.emote.EmoteManager;
import core.game.node.entity.player.link.grave.GraveManager;
import core.game.node.entity.player.link.music.MusicPlayer;
import core.game.node.entity.player.link.prayer.Prayer;
import core.game.node.entity.player.link.prayer.PrayerType;
import core.game.node.entity.player.link.quest.QuestRepository;
import core.game.node.entity.player.link.request.RequestManager;
import core.game.node.entity.player.link.skillertasks.SkillerTasks;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.construction.HouseManager;
import core.game.node.entity.skill.hunter.HunterManager;
import rs09.game.node.entity.skill.slayer.SlayerManager;
import core.game.node.entity.skill.summoning.familiar.FamiliarManager;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.system.communication.CommunicationInfo;
import core.game.system.monitor.PlayerMonitor;
import core.game.system.task.Pulse;
import core.game.world.map.*;
import core.game.world.map.build.DynamicRegion;
import core.game.world.map.path.Pathfinder;
import core.game.world.map.zone.ZoneType;
import core.game.world.update.flag.PlayerFlags;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.game.world.update.flag.player.FaceEntityFlag;
import core.game.world.update.flag.player.FaceLocationFlag;
import core.game.world.update.flag.player.ForceChatFlag;
import core.net.IoSession;
import core.net.packet.PacketRepository;
import core.net.packet.context.DynamicSceneContext;
import core.net.packet.context.SceneGraphContext;
import core.net.packet.context.SkillContext;
import core.net.packet.out.BuildDynamicScene;
import core.net.packet.out.SkillLevel;
import core.net.packet.out.UpdateSceneGraph;
import core.tools.RandomFunction;
import core.tools.StringUtils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.rs09.consts.Items;
import rs09.GlobalStats;
import rs09.ServerConstants;
import rs09.game.VarpManager;
import rs09.game.content.ame.RandomEventManager;
import rs09.game.ge.GrandExchangeRecords;
import rs09.game.node.entity.combat.CombatSwingHandler;
import rs09.game.node.entity.combat.equipment.EquipmentDegrader;
import rs09.game.node.entity.player.info.login.PlayerSaver;
import rs09.game.node.entity.skill.runecrafting.PouchManager;
import rs09.game.node.entity.state.newsys.State;
import rs09.game.node.entity.state.newsys.StateRepository;
import rs09.game.system.SystemLogger;
import rs09.game.world.GameWorld;
import rs09.game.world.repository.Repository;
import rs09.game.world.update.MapChunkRenderer;
import rs09.game.world.update.NPCRenderer;
import rs09.game.world.update.PlayerRenderer;
import rs09.game.world.update.UpdateSequence;
import rs09.tools.TickUtilsKt;

import java.util.*;

import static api.ContentAPIKt.*;
import static rs09.game.node.entity.player.info.stats.StatAttributeKeysKt.STATS_BASE;
import static rs09.game.node.entity.player.info.stats.StatAttributeKeysKt.STATS_DEATHS;
import static rs09.tools.stringtools.GlobalsKt.colorize;

/**
 * Represents a player entity.
 * @author Emperor
 * @author Vexia
 */
public class Player extends Entity {

	/**
	 * The details of the player.
	 */
	private PlayerDetails details;

	public boolean inWardrobe = false;

	public Location startLocation = null;

	private final Graphics wardrobe_hold_graphics = new Graphics(1182,0,0);

	public boolean newPlayer = getSkills().getTotalLevel() < 50;

	public BankContainer dropLog = new BankContainer(this);

	public EquipmentDegrader degrader = new EquipmentDegrader();

	public PouchManager pouchManager = new PouchManager(this);

	public VarpManager varpManager = new VarpManager(this);

	public HashMap<String,State> states = new HashMap<>();

	public HashMap<String,Function1<Player, Unit>> logoutListeners = new HashMap<>();

	/**
	 * The inventory.
	 */
	private final Container inventory = new Container(28).register(new InventoryListener(this));

	/**
	 * The equipment.
	 */
	private final EquipmentContainer equipment = new EquipmentContainer(this);

	/**
	 * The bank container.
	 */
	private final BankContainer bank = new BankContainer(this);

	/**
	 * The secondary bank container.
	 */
	private final BankContainer bankSecondary = new BankContainer(this);

	/**
	 * Is secondary bank in use or not
	 */
	public boolean useSecondaryBank = false;

	/**
	 * The Blast Furnace Coal Container.
	 */
	public final Container blastCoal = new Container(225, ContainerType.NEVER_STACK);

	/**
	 * The Blast Furnace Ore Container.
	 */
	public final Container blastOre = new Container(28, ContainerType.NEVER_STACK);

	/**
	 * The Blast Furnace Bars Container.
	 */
	public final Container blastBars = new Container(28, ContainerType.NEVER_STACK);

	/**
	 * The packet dispatcher.
	 */
	private final PacketDispatch packetDispatch = new PacketDispatch(this);

	/**
	 * The spell book manager.
	 */
	private final SpellBookManager spellBookManager = new SpellBookManager();

	/**
	 * The rendering info.
	 */
	private final RenderInfo renderInfo = new RenderInfo(this);

	/**
	 * The interface manager.
	 */
	private final InterfaceManager interfaceManager = new InterfaceManager(this);

	/**
	 * The emote manager.
	 */
	private final EmoteManager emoteManager = new EmoteManager(this);

	/**
	 * The player flags.
	 */
	private final PlayerFlags playerFlags = new PlayerFlags();

	/**
	 * The appearance.
	 */
	private final Appearance appearance = new Appearance(this);

	/**
	 * The settings of the player.
	 */
	private final Settings settings = new Settings(this);

	/**
	 * The dialogue interpreter.
	 */
	private final DialogueInterpreter dialogueInterpreter = new DialogueInterpreter(this);

	/**
	 * The hint icon manager.
	 */
	private final HintIconManager hintIconManager = new HintIconManager();

	/**
	 * The slayer manager.
	 */
	private final SlayerManager slayer = new SlayerManager(this);

	/**
	 * The quest repository.
	 */
	private QuestRepository questRepository = new QuestRepository(this);

	/**
	 * The prayer manager.
	 */
	private final Prayer prayer = new Prayer(this);

	/**
	 * The skull manager.
	 */
	private final SkullManager skullManager = new SkullManager(this);

	/**
	 * The grave stone manager.
	 */
	private final GraveManager graveManager = new GraveManager(this);

	/**
	 * The new grand exchange interface manager.
	 */
	public final GrandExchangeRecords exchangeRecords = new GrandExchangeRecords(this);

	/**
	 * The familiar manager.
	 */
	private final FamiliarManager familiarManager = new FamiliarManager(this);

	/**
	 * Pyramid Plunder Object Manager
	 */
	private final PlunderObjectManager plunderObjectManager = new PlunderObjectManager();

	/**
	 * The config manager.
	 */
	private final ConfigurationManager configManager = new ConfigurationManager(this);

	/**
	 * The saved data.
	 */
	private SavedData savedData = new SavedData(this);

	/**
	 * The request manager.
	 */
	private final RequestManager requestManager = new RequestManager(this);

	/**
	 * The farming manager.
	 */

	/**
	 * The monitor which monitors player actions.
	 */
	private final PlayerMonitor monitor = new PlayerMonitor();

	/**
	 * Represents the players warning messages.
	 */
	private final WarningMessages warningMessages = new WarningMessages();

	/**
	 * The music player instance.
	 */
	private final MusicPlayer musicPlayer = new MusicPlayer(this);

	/**
	 * The house manager.
	 */
	private final HouseManager houseManager = new HouseManager();

	/**
	 * The barcrawl miniquest manager.
	 */
	private final BarcrawlManager barcrawlManager = new BarcrawlManager(this);

	/**
	 * The anti macro handler.
	 */
	private final RandomEventManager antiMacroHandler = new RandomEventManager(this);

	/**
	 * The hunter manager.
	 */
	private final HunterManager hunterManager = new HunterManager(this);

	/**
	 * The treasure trail manager.
	 */
	private final TreasureTrailManager treasureTrailManager = new TreasureTrailManager(this);

	/**
	 * The audio manager.
	 */
	private final AudioManager audioManager = new AudioManager(this);

	/**
	 * The bank pin manager.
	 */
	private final BankPinManager bankPinManager = new BankPinManager(this);

	/**
	 * The achievement diary manager.
	 */
	private final AchievementDiaryManager achievementDiaryManager = new AchievementDiaryManager(this);

	/**
	 * The Ironman manager.
	 */
	private final IronmanManager ironmanManager = new IronmanManager(this);


	/**
	 * Brawling Gloves manager
	 */
	private final BrawlingGlovesManager brawlingGlovesManager = new BrawlingGlovesManager(this);

	/**
	 * The boolean for the player playing.
	 */
	private boolean playing;

	/**
	 * If the player is invisible.
	 */
	private boolean invisible;

	/**
	 * If the player is artificial.
	 */
	protected boolean artificial;

	/**
	 * The skiller tasks.
	 */
	protected SkillerTasks skillTasks = new SkillerTasks();

	/**
	 * A custom state for bot debugging
	 */
	private String customState = "";

	/**
	 * The amount of targets that the player can shoot left for the archery minigame.
	 */
	private int archeryTargets = 0;

	private int archeryTotal = 0;

	/**
	 * Constructs a new {@code Player} {@code Object}.
	 * @param details The player's details.
	 */
	public Player(PlayerDetails details) {
		super(details.getUsername(), ServerConstants.START_LOCATION);
		super.active = false;
		super.interaction = new Interaction(this);
		this.details = details;
		this.direction = Direction.SOUTH;
	}

	@Override
	public void init() {
		if(!artificial)
			SystemLogger.logInfo(getUsername() + " initialising...");
		if (!artificial) {
			getProperties().setSpawnLocation(ServerConstants.HOME_LOCATION);
			getDetails().getSession().setObject(this);
			getDetails().getSession().setLastPing(System.currentTimeMillis() + 10_000L);
		}
		super.init();
		LoginConfiguration.configureLobby(this);
	}

	@Override
	public void clear() {
		clear(false);
	}

	/**
	 * Clears the player from the game.
	 * @param force If we should force removal, a player engaged in combat will otherwise remain active until out of combat.
	 */
	public void clear(boolean force) {
		/*if (!force && allowRemoval()) {
			Repository.getDisconnectionQueue().add(this, true);
			return;
		}*/
		if (force) {
			Repository.getDisconnectionQueue().remove(getName());
		}
		GameWorld.getLogoutListeners().forEach((it) -> it.logout(this));
		setPlaying(false);
		getWalkingQueue().reset();
		if(!logoutListeners.isEmpty()){
			logoutListeners.forEach((key,method) -> method.invoke(this));
		}
		if (familiarManager.hasFamiliar()) {
			familiarManager.getFamiliar().clear();
		}
		interfaceManager.close();
		interfaceManager.closeSingleTab();
		super.clear();
		getZoneMonitor().clear();
		CommunicationInfo.notifyPlayers(this, false, false);
		hunterManager.logout();
		HouseManager.leave(this);
		UpdateSequence.getRenderablePlayers().remove(this);
		Repository.getDisconnectionQueue().add(this);
	}

	public void toggleWardrobe(boolean intoWardrobe){
		class wardrobePulse extends Pulse{
			final Player player;
			boolean first = true;
			wardrobePulse(Player player){
				this.player = player;
			}
			@Override
			public boolean pulse() {
				if(first){
					player.visualize(new Animation(1241), new Graphics(1181,0,0));
					first = false;
					return !player.inWardrobe;
				}
				if(player.inWardrobe) {
					player.visualize(new Animation(1241),wardrobe_hold_graphics);
				} else {
					player.visualize(new Animation(1241), new Graphics(1183,0,0));
					player.getPulseManager().run(new Pulse(1){
						@Override
						public boolean pulse() {
							player.getAnimator().reset();
							player.packetDispatch.sendInterfaceConfig(548,69,false);
							return true;
						}
					});
				}
				return !player.inWardrobe;
			}
		}
		if(intoWardrobe){
			packetDispatch.sendInterfaceConfig(548,69,true);
			GameWorld.getPulser().submit(new wardrobePulse(this));
			inWardrobe = true;
		} else {
			inWardrobe = false;
		}
	}

	@Override
	public void tick() {
		super.tick();
		antiMacroHandler.tick();
		hunterManager.pulse();
		musicPlayer.tick();
		if(getAttribute("fire:immune",0) > 0){
			int time = getAttribute("fire:immune",0) - GameWorld.getTicks();
			if(time == TickUtilsKt.secondsToTicks(30)){
				sendMessage(colorize("%RYou have 30 seconds remaining on your antifire potion."));
				getAudioManager().send(3120);
			}
			if(time == 0){
				sendMessage(colorize("%RYour antifire potion has expired."));
				removeAttribute("fire:immune");
				getAudioManager().send(2607);
			}
		}
		if(getAttribute("poison:immunity",0) > 0){
			int time = getAttribute("poison:immunity",0) - GameWorld.getTicks();
			debug(time + "");
			if(time == TickUtilsKt.secondsToTicks(30)){
				sendMessage(colorize("%RYou have 30 seconds remaining on your antipoison potion."));
				getAudioManager().send(3120);
			}
			if(time == 0){
				sendMessage(colorize("%RYour antipoison potion has expired."));
				removeAttribute("poison:immunity");
				getAudioManager().send(2607);
			}
		}
		if (!artificial && (System.currentTimeMillis() - getSession().getLastPing()) > 20_000L) {
			details.getSession().disconnect();
			getSession().setLastPing(Long.MAX_VALUE);
		}
		if(getAttribute("infinite-special", false)) {
			settings.setSpecialEnergy(100);
		}

		//Decrements prayer points
		getPrayer().tick();
	}

	@Override
	public void update() {
		super.update();
		if (playerFlags.isUpdateSceneGraph()) {
			updateSceneGraph(false);
		}
		PlayerRenderer.render(this);
		NPCRenderer.render(this);
		MapChunkRenderer.render(this);
	}

	@Override
	public void reset() {
		super.reset();
		playerFlags.setUpdateSceneGraph(false);
		renderInfo.updateInformation();
		if (getSkills().isLifepointsUpdate()) {
			PacketRepository.send(SkillLevel.class, new SkillContext(this, Skills.HITPOINTS));
			getSkills().setLifepointsUpdate(false);
		}
	}

	@Override
	public boolean face(Entity entity) {
		if (entity == null) {
			if (getUpdateMasks().unregisterSynced(FaceEntityFlag.getOrdinal())) {
				return getUpdateMasks().register(new FaceEntityFlag(entity));
			}
			return true;
		}
		return getUpdateMasks().register(new FaceEntityFlag(entity), true);
	}

	@Override
	public boolean faceLocation(Location location) {
		if (location == null) {
			getUpdateMasks().unregisterSynced(FaceLocationFlag.getOrdinal());
			return true;
		}
		return getUpdateMasks().register(new FaceLocationFlag(location), true);
	}

	@Override
	public boolean sendChat(String string) {
		return getUpdateMasks().register(new ForceChatFlag(string));
	}

	@Override
	public int getClientIndex() {
		return this.getIndex() | 0x8000;
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean swing) {
		CombatStyle style = getProperties().getCombatPulse().getStyle();
		if (swing) {
			int weaponId = equipment.getNew(3).getId();
			if (getProperties().getSpell() != null || getProperties().getAutocastSpell() != null) {
				return CombatStyle.MAGIC.getSwingHandler();
			}
			if (settings.isSpecialToggled()) {
				CombatSwingHandler handler;
				if ((handler = style.getSwingHandler().getSpecial(weaponId)) != null) {
					return handler;
				}
				packetDispatch.sendMessage("Unhandled special attack for item " + weaponId + "!");
			}
		}
		if (style == CombatStyle.RANGE && equipment.getNew(3).getId() == 10033 || equipment.getNew(3).getId() == 10034) {
			return ChinchompaSwingHandler.getInstance();
		}
		return style.getSwingHandler();
	}

	@Override
	public void commenceDeath(Entity killer) {
		super.commenceDeath(killer);
		if (prayer.get(PrayerType.RETRIBUTION)) {
			prayer.startRetribution(killer);
		}
	}

	@Override
	public void finalizeDeath(Entity killer) {
		GlobalStats.incrementDeathCount();
		settings.setSpecialEnergy(100);
		settings.updateRunEnergy(settings.getRunEnergy() - 100);
		Player k = killer instanceof Player ? (Player) killer : this;
		if (!k.isActive()) {
			k = this;
		}
		if (this.isArtificial() && killer instanceof Player){
			setAttribute("dead", true);
		}
		if (this.isArtificial() && killer instanceof NPC) {
			return;
		}
		getPacketDispatch().sendMessage("Oh dear, you are dead!");
		incrementAttribute("/save:"+STATS_BASE+":"+STATS_DEATHS);

		packetDispatch.sendTempMusic(90);
		if (!getZoneMonitor().handleDeath(killer) && (!getProperties().isSafeZone() && getZoneMonitor().getType() != ZoneType.SAFE.getId()) && getDetails().getRights() != Rights.ADMINISTRATOR) {
			//If player was a Hardcore Ironman, announce that they died
			if (this.getIronmanManager().getMode().equals(IronmanMode.HARDCORE)){ //if this was checkRestriction, ultimate irons would be moved to HARDCORE_DEAD as well
				String gender = this.isMale() ? "man " : "woman ";
				if (getAttributes().containsKey("permadeath")) {
					Repository.sendNews("Permadeath Hardcore Iron" + gender + " " + this.getUsername() + " has fallen. Total Level: " + this.getSkills().getTotalLevel()); // Not enough room for XP
					equipment.clear();
					inventory.clear();
					bank.clear();
					skills = new Skills(this);
					clearAttributes();
					setAttribute("/save:permadeath", true);
					savedData = new SavedData(this);
					questRepository = new QuestRepository(this);
					new PlayerSaver(this).save();
					clear(true);
					return;
				} else {
					Repository.sendNews("Hardcore Iron " + gender + " " + this.getUsername() + " has fallen. Total Level: " + this.getSkills().getTotalLevel()); // Not enough room for XP
					this.getIronmanManager().setMode(IronmanMode.STANDARD);
					asPlayer().getSavedData().getActivityData().setHardcoreDeath(true);
					this.sendMessage("You have fallen as a Hardcore Iron Man, your Hardcore status has been revoked.");
				}
			}
			GroundItemManager.create(new Item(526), getLocation(), k);
			final Container[] c = DeathTask.getContainers(this);
			boolean gravestone = graveManager.generateable() && getIronmanManager().getMode() != IronmanMode.ULTIMATE;
			int seconds = graveManager.getType().getDecay() * 60;
			int ticks = (1000 * seconds) / 600;
			List<GroundItem> items = new ArrayList<>(20);
			for (Item item : c[1].toArray()) {
				if (item != null) {
					GroundItem ground;
					if (item.hasItemPlugin()) {
						item = item.getPlugin().getDeathItem(item);
					}
					if (gravestone || !item.getDefinition().isTradeable()) {
						ground = new GroundItem(item, getLocation(), gravestone ? ticks + 100 : 200, this);
					} else {
						ground = new GroundItem(item.getDropItem(), getLocation(), k);
					}
					items.add(ground);
					ground.setDropper(this); //Checking for ironman mode in any circumstance for death items is inaccurate to how it works in both 2009scapes.
					GroundItemManager.create(ground);
				}
			}
			equipment.clear();
			inventory.clear();
			inventory.addAll(c[0]);
			if (gravestone) {
				graveManager.create(ticks, items);
				sendMessages("<col=990000>Because of your current gravestone, you have "+graveManager.getType().getDecay()+" minutes to get your items and", "<col=990000>equipment back after dying in combat.");
			}
			familiarManager.dismiss();

		}
		skullManager.setSkulled(false);
		removeAttribute("combat-time");
		getPrayer().reset();
		super.finalizeDeath(killer);
		appearance.sync();
		if (killer instanceof Player && !GameWorld.isEconomyWorld() && getSkullManager().isWilderness() && killer.asPlayer().getSkullManager().isWilderness()) {
			killer.asPlayer().getSavedData().getSpawnData().onDeath(killer.asPlayer(), this);
		}
		if (GameWorld.isEconomyWorld() && !getSavedData().getGlobalData().isDeathScreenDisabled()) {
			getInterfaceManager().open(new Component(153));
		}
		if (!getSavedData().getGlobalData().isDeathScreenDisabled()) {
			getInterfaceManager().open(new Component(153));
		}
	}

	@Override
	public boolean hasProtectionPrayer(CombatStyle style) {
		if (style == null) {
			return false;
		}
		return prayer.get(style.getProtectionPrayer());
	}

	@Override
	public int getDragonfireProtection(boolean fire) {
		int value = 0;
		if (fire) {
			if (hasFireResistance()) {
				value |= 0x2;
			}
		}
		Item item = equipment.get(EquipmentContainer.SLOT_SHIELD);
		if (item != null && (item.getId() == 11283 || item.getId() == 11284 || (fire && (item.getId() == 1540) || (!fire && (item.getId() == 2890 || item.getId() == 9731))))) {
			value |= 0x4;
		}
		if (prayer.get(PrayerType.PROTECT_FROM_MAGIC)) {
			value |= 0x8;
		}
		setAttribute("fire_resistance", value);
		return value;
	}

	@Deprecated
	@Override
	public void setLocation(Location location) {
		super.setLocation(location);
	}

	@Override
	public void fullRestore() {
		prayer.reset();
		settings.setSpecialEnergy(100);
		settings.updateRunEnergy(-100);
		super.fullRestore();
	}

	@Override
	public boolean isAttackable(Entity entity, CombatStyle style, boolean message) {
		if (entity instanceof NPC && !((NPC) entity).getDefinition().hasAction("attack") && !((NPC) entity).isIgnoreAttackRestrictions(this)) {
			return false;
		}
		return super.isAttackable(entity, style, message);
	}

	@Override
	public boolean isPoisonImmune() {
		return getAttribute("poison:immunity", -1) > GameWorld.getTicks();
	}

	@Override
	public void onImpact(final Entity entity, BattleState state) {
		super.onImpact(entity, state);
		boolean recoil = getEquipment().getNew(EquipmentContainer.SLOT_RING).getId() == 2550;
		if (state.getEstimatedHit() > 0) {
			if (getAttribute("vengeance", false)) {
				removeAttribute("vengeance");
				final int hit = (int) (state.getEstimatedHit() * 0.75);
				sendChat("Taste vengeance!");
				if (hit > -1) {
					entity.getImpactHandler().manualHit(Player.this, hit, HitsplatType.NORMAL);
				}
			}
			if (recoil) {
				getImpactHandler().handleRecoilEffect(entity, state.getEstimatedHit());
			}
		}
		if (state.getSecondaryHit() > 0) {
			if (recoil) {
				getImpactHandler().handleRecoilEffect(entity, state.getSecondaryHit());
			}
		}
		degrader.checkArmourDegrades(this);
	}

	public void randomWalk(int radiusX, int radiusY) {
		Pathfinder.find(this, this.getLocation().transform(RandomFunction.random(radiusX, (radiusX * -1)), RandomFunction.random(radiusY, (radiusY * -1)), 0), false, Pathfinder.SMART).walk(this);
	}

	/**
	 * Initializes the player for reconnection.
	 */
	public void initReconnect() {
		getInterfaceManager().setChatbox(null);
		getPulseManager().clear();
		getZoneMonitor().getZones().clear();
		getViewport().setCurrentPlane(RegionManager.forId(66666).getPlanes()[3]);
		configManager.reset();
		playerFlags.setLastSceneGraph(null);
		playerFlags.setUpdateSceneGraph(false);
		playerFlags.setLastViewport(new RegionChunk[Viewport.CHUNK_SIZE][Viewport.CHUNK_SIZE]);
		renderInfo.getLocalNpcs().clear();
		renderInfo.getLocalPlayers().clear();
		renderInfo.setLastLocation(null);
		renderInfo.setOnFirstCycle(true);
		Arrays.fill(renderInfo.getAppearanceStamps(),0);
	}

	/**
	 * Checks if the player is wearing void.
	 */
	public boolean isWearingVoid(CombatStyle style) {
		int helm;
		if(style == CombatStyle.MELEE) {
			helm = Items.VOID_MELEE_HELM_11665;
		} else if(style == CombatStyle.RANGE) {
			helm = Items.VOID_RANGER_HELM_11664;
		} else if(style == CombatStyle.MAGIC) {
			helm = Items.VOID_MAGE_HELM_11663;
		} else {
			return false;
		}
		boolean legs = inEquipment(this, Items.VOID_KNIGHT_ROBE_8840, 1);
		boolean top = inEquipment(this, Items.VOID_KNIGHT_TOP_8839, 1)
				|| inEquipment(this, Items.VOID_KNIGHT_TOP_10611, 1);
		boolean gloves = inEquipment(this, Items.VOID_KNIGHT_GLOVES_8842, 1);
		return inEquipment(this, helm, 1) && legs && top && gloves;
	}

	/**
	 * Updates the player's scene graph.
	 * @param login If the player is logging in.
	 */
	public void updateSceneGraph(boolean login) {
		Region region = getViewport().getRegion();
		if (region instanceof DynamicRegion || region == null && (region = RegionManager.forId(location.getRegionId())) instanceof DynamicRegion) {
			PacketRepository.send(BuildDynamicScene.class, new DynamicSceneContext(this, login));
		} else {
			PacketRepository.send(UpdateSceneGraph.class, new SceneGraphContext(this, login));
		}
	}

	/**
	 * Toggles the debug mode.
	 */
	public void toggleDebug() {
		boolean debug = getAttribute("debug", false);
		setAttribute("debug", !debug);
		getPacketDispatch().sendMessage("Your debug mode is toggled to " + !debug + ".");
	}

	/**
	 * Wrapper method for sending a message.
	 * @param messages the messages.
	 */
	public void sendMessages(String... messages) {
		packetDispatch.sendMessages(messages);
	}

	/**
	 * Wrapper method for sending a message.
	 * @param message the message.
	 */
	public void sendMessage(String message) {
		sendMessages(message);
	}

	/**
	 * Sends a notification message.
	 * @param message The message.
	 */
	public void sendNotificationMessage(String message) {
		sendMessages("<col=ff0000>" + message + "</col>");
	}

	/**
	 * Checks if the player can spawn. & Location
	 * @return {@code True} if so.
	 */
	public boolean spawnZone() {
		return (getLocation().getX() > 3090 && getLocation().getY() < 3500
				&& getLocation().getX() < 3099 && getLocation().getY() > 3487);
	}

	public boolean canSpawn() {
		if (!spawnZone()) {
			sendMessage("You can only spawn items inside the edgeville bank.");
			return true;
		}
		if (inCombat() || getLocks().isInteractionLocked() || getSkullManager().isWilderness() || getAttribute("activity", null) != null) {
			sendMessage("<col=FF0000>You can't spawn items at the moment.");
			return true;
		}
		return false;
	}

	/**
	 * Sends a message on a tick.
	 * @param message the message.
	 * @param ticks the ticks.
	 */
	public void sendMessage(String message, int ticks) {
		packetDispatch.sendMessage(message, ticks);
	}

	/**
	 * Sends a message to the player if it's an administrator.
	 * @param string The message.
	 */
	public void debug(String string) {
		if (getAttribute("debug",false)) {
			packetDispatch.sendMessage(string);
		}
	}

	/**
	 * Grabs a players gender, using shorter amount of code
	 */

	public boolean isMale() {
		return this.getAppearance().getGender().ordinal() == 0;
	}

	/**
	 * Sets the player details.
	 * @param details The player details.
	 */
	@SuppressWarnings("deprecation")
	public void updateDetails(PlayerDetails details) {
		if (this.details != null) {
			details.setBanTime(this.details.getBanTime());
			details.setMuteTime(this.details.getMuteTime());
			details.setIcon(this.details.getIcon());
		}
		details.getSession().setObject(this);
		this.details = details;
	}

	/**
	 * Checks if the player is allowed to be removed from the game.
	 * @return {@code True} if so.
	 */
	public boolean allowRemoval() {
		return inCombat() || getSkills().getLifepoints() < 1 || DeathTask.isDead(this);
	}

	/**
	 * Checks if the containers have this item.
	 * @param item the item.
	 * @return {@code True} if so.
	 */
	public boolean hasItem(Item item) {
		return getInventory().containsItem(item) || getBank().containsItem(item) || getEquipment().containsItem(item);
	}

	/**
	 * Gets the player extra experience mod.
	 * @return the mod.
	 */
	public double getExperienceMod() {
		return getSavedData().getGlobalData().hasDoubleExp() ? 2 : 1;
	}

	/**
	 * Checks if the player is a staff member.
	 * @return {@code True} if so.
	 */
	public boolean isStaff() {
		return getDetails().getRights() != Rights.REGULAR_PLAYER;
	}

	/**
	 * Checks if the player is an admin.
	 * @return true if so.
	 */
	public boolean isAdmin() {
		return getDetails().getRights() == Rights.ADMINISTRATOR;
	}

	/**
	 * Checks if the player is debugging.
	 * @return {@code True} if so.
	 */
	public boolean isDebug() {
		return details.getRights() == Rights.ADMINISTRATOR && getAttribute("debug", false);
	}

	/**
	 * Gets the uid info.
	 * @return the info.
	 */
	public UIDInfo getUidInfo() {
		return details.getInfo();
	}

	/**
	 * Gets the {@code PlayerDetails}.
	 * @return the details.
	 */
	public PlayerDetails getDetails() {
		return details;
	}

	/**
	 * Gets the name.
	 * @return the name.
	 */
	public String getName() {
//		return display ? details.getDisplayName() : super.getName();
		return super.getName();
	}

	/**
	 * Gets the players {@code Session}.
	 * @return the {@code PlayerDetails} {@code Object} session.
	 */
	public IoSession getSession() {
		return details.getSession();
	}

	/**
	 * Gets the {@code Equipment}
	 * @return {@code Equipment}.
	 */
	public EquipmentContainer getEquipment() {
		return equipment;
	}

	/**
	 * Gets the current active bank.
	 * @return Current active bank.
	 */
	public BankContainer getBank() {
		return useSecondaryBank ? bankSecondary : bank;
	}

	/**
	 * Gets the primary bank.
	 * @return Primary bank
	 */
	public BankContainer getBankPrimary() {
		return bank;
	}

	/**
	 * Gets the Secondary bank.
	 * @return Secondary bank
	 */
	public BankContainer getBankSecondary() {
		return bankSecondary;
	}

	public BankContainer getDropLog() {return dropLog;}

	/**
	 * @return the inventory
	 */
	public Container getInventory() {
		return inventory;
	}

	/**
	 * Sets the playing flag.
	 * @param playing the flag to set.
	 */
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	/**
	 * Checks if the player is playing.
	 * @return {@code True} if so.
	 */
	public boolean isPlaying() {
		return playing;
	}

	/**
	 * Gets the rights of the player.
	 * @return the rights.
	 */
	public Rights getRights() {
		return details.getRights();
	}

	/**
	 * Gets the renderInfo.
	 * @return The renderInfo.
	 */
	public RenderInfo getRenderInfo() {
		return renderInfo;
	}

	/**
	 * Gets the appearance.
	 * @return The appearance.
	 */
	public Appearance getAppearance() {
		return appearance;
	}

	/**
	 * Gets the playerFlags.
	 * @return The playerFlags.
	 */
	public PlayerFlags getPlayerFlags() {
		return playerFlags;
	}

	/**
	 * Gets the {@code PacketDispatch}.
	 * @return the packet dispatch.
	 */
	public PacketDispatch getPacketDispatch() {
		return packetDispatch;
	}


	/**
	 * @return the spellBookManager
	 */
	public SpellBookManager getSpellBookManager() {
		return spellBookManager;
	}

	/**
	 * Gets the settings.
	 * @return The settings.
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * @return the interface manager.
	 */
	public InterfaceManager getInterfaceManager() {
		return interfaceManager;
	}

	/**
	 * Gets the dialogue interpreter.
	 * @return The dialogue interpreter.
	 */
	public DialogueInterpreter getDialogueInterpreter() {
		return dialogueInterpreter;
	}

	/**
	 * @return the hintIconManager
	 */
	public HintIconManager getHintIconManager() {
		return hintIconManager;
	}

	/**
	 * Gets the slayer.
	 * @return The slayer.
	 */
	public SlayerManager getSlayer() {
		return slayer;
	}

	/**
	 * Checks if the player is artifical (AIPlayer).
	 * @return {@code True} if so.
	 */
	public boolean isArtificial() {
		return artificial;
	}

	/**
	 * @return the questRepository.
	 */
	public QuestRepository getQuestRepository() {
		return questRepository;
	}

	/**
	 * @return the prayer.
	 */
	public Prayer getPrayer() {
		return prayer;
	}

	/**
	 * @return the skullManager.
	 */
	public SkullManager getSkullManager() {
		return skullManager;
	}

	/**
	 * @return the familiarManager.
	 */
	public FamiliarManager getFamiliarManager() {
		return familiarManager;
	}

	/**
	 * Gets the configManager.
	 * @return The configManager.
	 */
	public ConfigurationManager getConfigManager() {
		return configManager;
	}

	/**
	 * Gets the communication.
	 * @return The communication.
	 */
	public CommunicationInfo getCommunication() {
		return details.getCommunication();
	}

	/**
	 * Gets the requestManager.
	 * @return The requestManager.
	 */
	public RequestManager getRequestManager() {
		return requestManager;
	}

	/**
	 * Gets the savedData.
	 * @return The savedData.
	 */
	public SavedData getSavedData() {
		return savedData;
	}

	/**
	 * Gets the global data.
	 * @return the global data.
	 */
	public GlobalData getGlobalData() {
		return savedData.getGlobalData();
	}

	/**
	 * Gets the farmingManager.
	 * @return The farmingManager.
	 */

	/**
	 * Gets the monitor.
	 * @return The monitor.
	 */
	public PlayerMonitor getMonitor() {
		return monitor;
	}

	/**
	 * Gets the warningMessages.
	 * @return The warningMessages.
	 */
	public WarningMessages getWarningMessages() {
		return warningMessages;
	}

	/**
	 * Gets the musicPlayer.
	 * @return The musicPlayer.
	 */
	public MusicPlayer getMusicPlayer() {
		return musicPlayer;
	}

	/**
	 * Gets the houseManager.
	 * @return The houseManager.
	 */
	public HouseManager getHouseManager() {
		return houseManager;
	}

	/**
	 * Gets the barcrawlManager.
	 * @return The barcrawlManager.
	 */
	public BarcrawlManager getBarcrawlManager() {
		return barcrawlManager;
	}

	/**
	 * Gets the antiMacroHandler.
	 * @return The antiMacroHandler.
	 */
	public RandomEventManager getAntiMacroHandler() {
		return antiMacroHandler;
	}

	/**
	 * Gets the hunterManager.
	 * @return The hunterManager.
	 */
	public HunterManager getHunterManager() {
		return hunterManager;
	}

	/**
	 * Gets the btreasureTrailManager.
	 * @return the treasureTrailManager
	 */
	public TreasureTrailManager getTreasureTrailManager() {
		return treasureTrailManager;
	}

	/**
	 * Gets the graveManager.
	 * @return the graveManager
	 */
	public GraveManager getGraveManager() {
		return graveManager;
	}

	/**
	 * Gets the audioManager.
	 * @return the audioManager
	 */
	public AudioManager getAudioManager() {
		return audioManager;
	}

	/**
	 * Gets the bankPinManager.
	 * @return the bankPinManager
	 */
	public BankPinManager getBankPinManager() {
		return bankPinManager;
	}

	/**
	 * Gets the achievementDiaryManager.
	 * @return the achievementDiaryManager
	 */
	public AchievementDiaryManager getAchievementDiaryManager() {
		return achievementDiaryManager;
	}

	/**
	 * Gets the ironmanManager.
	 * @return the ironmanManager
	 */
	public IronmanManager getIronmanManager() {
		return ironmanManager;
	}

	public PlunderObjectManager getPlunderObjectManager() {return plunderObjectManager;}

	/**
	 * Gets the emoteManager.
	 * @return the emoteManager.
	 */
	public EmoteManager getEmoteManager() {
		return emoteManager;
	}

	/**
	 * Gets the invisible.
	 * @return the invisible
	 */
	public boolean isInvisible() {
		return invisible;
	}

	/**
	 * Sets the invisible.
	 * @param invisible the invisible to set.
	 */
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	@Override
	public String getUsername() {
		return StringUtils.formatDisplayName(getName());
	}

	public SkillerTasks getSkillTasks() {
		return skillTasks;
	}

	public void setSkillTasks(SkillerTasks skillTasks) {
		this.skillTasks = skillTasks;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", getRights()=" + getRights() + "]";
	}


	public String getCustomState() {
		return customState;
	}

	public void setCustomState(String state)
	{
		this.customState = state;
	}

	public int getArcheryTargets() {
		return archeryTargets;
	}

	public void setArcheryTargets(int archeryTargets) {
		this.archeryTargets = archeryTargets;
	}

	public int getArcheryTotal() {
		return archeryTotal;
	}

	public void setArcheryTotal(int archeryTotal) {
		this.archeryTotal = archeryTotal;
	}

	public BrawlingGlovesManager getBrawlingGlovesManager() { return brawlingGlovesManager;}

	public GrandExchangeRecords getExchangeRecords() { return exchangeRecords; }

	public boolean hasActiveState(String key){
		State state = states.get(key);
		if(state != null && state.getPulse() != null){
			return true;
		}
		return false;
	}

	public State registerState(String key){
		return StateRepository.forKey(key, this);
	}

	public void clearState(String key){
		State state = states.get(key);
		if(state == null) return;
		Pulse pulse = state.getPulse();
		if(pulse != null) {
			pulse.stop();
		}
		states.remove(key);
	}
}
