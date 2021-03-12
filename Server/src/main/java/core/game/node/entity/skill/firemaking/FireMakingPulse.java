package core.game.node.entity.skill.firemaking;

import core.game.world.map.zone.ZoneBorders;
import core.game.content.quest.tutorials.tutorialisland.TutorialSession;
import core.game.content.quest.tutorials.tutorialisland.TutorialStage;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.node.object.GameObject;
import core.game.node.object.ObjectBuilder;
import rs09.game.world.GameWorld;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.player.FaceLocationFlag;
import core.tools.RandomFunction;

/**
 * Represents the pulse used to light a log.
 * @author 'Vexia
 */
public final class FireMakingPulse extends SkillPulse<Item> {

	/**
	 * Represents the animation to use.
	 */
	private static final Animation ANIMATION = new Animation(733);

	/**
	 * Represents the tinderbox item.
	 */
	private static final Item TINDERBOX = new Item(590);

	/**
	 * Represents the log being burned.
	 */
	private final Log fire;

	/**
	 * Represents the ground item.
	 */
	private GroundItem groundItem;

	/**
	 * Represents the ticks.
	 */
	private int ticks;

	/**
	 * Constructs a new {@code FireMaking}.
	 * @param player the player.
	 * @param node the node.
	 * @param groundItem the ground item if not null.
	 */
	public FireMakingPulse(Player player, Item node, GroundItem groundItem) {
		super(player, node);
		this.fire = Log.forId(node.getId());
		if (groundItem == null) {
			this.groundItem = new GroundItem(node, player.getLocation(), player);
			player.setAttribute("remove-log", true);
		} else {
			this.groundItem = groundItem;
			player.removeAttribute("remove-log");
		}
	}

	@Override
	public boolean checkRequirements() {
		if (fire == null) {
			return false;
		}
		if (RegionManager.getObject(player.getLocation()) != null || player.getZoneMonitor().isInZone("bank")) {
			player.getPacketDispatch().sendMessage("You can't light a fire here.");
			return false;
		}
		if (!player.getInventory().containsItem(TINDERBOX)) {
			player.getPacketDispatch().sendMessage("You do not have the required items to light this.");
			return false;
		}
		if (player.getSkills().getLevel(Skills.FIREMAKING) < fire.getLevel()) {
			player.getPacketDispatch().sendMessage("You need a firemaking level of " + fire.getLevel() + " to light this log.");
			return false;
		}
		if (player.getAttribute("remove-log", false)) {
			player.removeAttribute("remove-log");
			if (player.getInventory().remove(node)) {
				GroundItemManager.create(groundItem);
			}
		}
		return true;
	}

	@Override
	public void animate() {
	}

	@Override
	public boolean reward() {
		if (getLastFire() >= GameWorld.getTicks()) {
			createFire();
			return true;
		}
		if (ticks == 0) {
			player.animate(ANIMATION);
		}
		if (++ticks % 3 != 0) {
			return false;
		}
		if (ticks % 12 == 0) {
			player.animate(ANIMATION);
		}
		if (!success()) {
			return false;
		}
		createFire();
		return true;
	}

	/**
	 * Creates the fire.
	 */
	public void createFire() {
		if (!groundItem.isActive()) {
			return;
		}
		// GameObject originalOnSpot =
		// RegionManager.getObject(player.getLocation());
		final GameObject object = new GameObject(fire.getFireId(), player.getLocation());
		ObjectBuilder.add(object, fire.getLife(), getAsh(player, fire, object));
		GroundItemManager.destroy(groundItem);
		player.moveStep();
		player.faceLocation(FaceLocationFlag.getFaceLocation(player, object));
		if (TutorialSession.getExtension(player).getStage() == 9) {
			TutorialStage.load(player, 10, false);
		}
		player.getSkills().addExperience(Skills.FIREMAKING,fire.getXp());

		int playerRegion = player.getViewport().getRegion().getId();

		if (fire == Log.MAGIC && playerRegion == 10806) {
			player.getAchievementDiaryManager().finishTask(player,DiaryType.SEERS_VILLAGE,2, 5);
		}
		// Light a campfire from normal logs in Lumbridge Swamp
		if (fire == Log.NORMAL && (playerRegion == 12593 || playerRegion == 12849)) {
			player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 1, 9);
		}
		// Light a willow log fire in Lumbridge Castle courtyard
		if (fire == Log.WILLOW
				&& new ZoneBorders(3216, 3207, 3225, 3233, 0).insideBorder(player)) {
			player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 2, 3);
		}

		setLastFire();
	}

	@Override
	public void message(int type) {
		String name = node.getId() == 3125 ? "bones" : "logs";
		switch (type) {
		case 0:
			player.getPacketDispatch().sendMessage("You attempt to light the " + name + "..");
			break;
		case 1:
			player.getPacketDispatch().sendMessage("The fire catches and the " + name + " begin to burn.");
			break;
		}
	}

	/**
	 * Gets the last firemake.
	 * @return the tick.
	 */
	public int getLastFire() {
		return player.getAttribute("last-firemake", 0);
	}

	/**
	 * Sets the last fire.
	 */
	public void setLastFire() {
		player.setAttribute("last-firemake", GameWorld.getTicks() + 2);
	}

	/**
	 * Gets the ground item ash.
	 * @param object the object.
	 * @return {@code GroundItem} the itemm.
	 */
	public static GroundItem getAsh(final Player player, Log fire, final GameObject object) {
		final GroundItem ash = new GroundItem(new Item(592), object.getLocation(), player);
		ash.setDecayTime(fire.getLife() + 200);
		return ash;
	}

	/**
	 * Checks if the player gets rewarded.
	 * @return {@code True} if so.
	 */
	private boolean success() {
		int level = 1 + player.getSkills().getLevel(Skills.FIREMAKING);
		double req = fire.getLevel();
		double successChance = Math.ceil((level * 50 - req * 15) / req / 3 * 4);
		int roll = RandomFunction.random(99);
		if (successChance >= roll) {
			return true;
		}
		return false;
	}
}