package content.global.skill.hunter;

import core.game.node.entity.skill.SkillPulse;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import org.rs09.consts.Sounds;

import static core.api.ContentAPIKt.*;

/**
 * Handles the trap creating pulse.
 * @author Vexia
 */
public final class TrapCreatePulse extends SkillPulse<Node> {

	/**
	 * The starting location of the trap.
	 */
	private final Location startLocation;

	/**
	 * The ground item of the trap.
	 */
	private GroundItem groundItem;

	/**
	 * The trap type.
	 */
	private final Traps trap;

	/**
	 * The amounts of ticks passed.
	 */
	private int ticks;

	private final HunterManager instance;

	/**
	 * Constructs a new {@code TrapCreatePulse} {@code Object}.
	 * @param player the player.
	 * @param node the node.
	 * @param trap the trap.
	 */
	public TrapCreatePulse(Player player, Node node, Traps trap) {
		super(player, node);
		this.trap = trap;
		this.startLocation = node instanceof GroundItem ? node.getLocation() : player.getLocation();
		this.instance = HunterManager.getInstance(player);
		if (checkRequirements()){
		switch(trap) {
			case BIRD_SNARE:
				playAudio(player, Sounds.HUNTING_SETNOOSE_2646, 40);
				break;
			case BOX_TRAP:
				playAudio(player, Sounds.HUNTING_LAYBOXTRAP_2636, 20);
				break;
			case NET_TRAP:
				lock(player, 3);
				playAudio(player, Sounds.HUNTING_SET_TWITCHNET_2644);
				break;
			case RABBIT_SNARE:
				playAudio(player, Sounds.HUNTING_SETSNARE_2647);
				break;
			case DEAD_FALL:
				lock(player, 6);
				playAudio(player, Sounds.HUNTING_SETDEADFALL_2645, 130);
			}
		}
	}

	@Override
	public boolean checkRequirements() {
		if (player.skills.getStaticLevel(Skills.HUNTER) < trap.getSettings().getLevel()) {
			player.sendMessage("You need a Hunter level of at least " + trap.getSettings().getLevel() + " in order to setup a " + node.getName().toLowerCase() + ".");
			return false;
		}
		if (instance.exceedsTrapLimit(trap)) {
			player.sendMessage(trap.getSettings().getLimitMessage(player));
			return false;
		}
		if (RegionManager.getObject(player.getLocation()) != null) {
			player.sendMessage("You can't lay a trap here.");
			return false;
		}
		if (!player.getLocation().equals(startLocation)) {
			return false;
		}
		if (trap.getSettings().isObjectTrap() && !trap.getSettings().hasItems(player)) {
			return false;
		}
		return true;
	}

	@Override
	public void animate() {
		if (ticks < 1) {
			player.getAnimator().forceAnimation(trap.getSettings().getSetupAnimation());
		}
	}

	@Override
	public boolean reward() {
		if (++ticks % (trap.getSettings().getSetupAnimation().getDefinition().getDurationTicks()) != 0) {
			return false;
		}
		Scenery object = trap.getSettings().buildObject(player, node);
		if (isGroundSetup() || groundItem != null) {
			GroundItemManager.destroy(groundItem);
		}
		if (!trap.getSettings().isObjectTrap()) {
			player.moveStep();
		} else {
			SceneryBuilder.remove(node.asScenery());
		}
		object = SceneryBuilder.add(object);
		instance.register(trap, node, object);
		return true;
	}

	@Override
	public void message(int type) {
		switch (type) {
		case 0:
			setUp();
			player.getPacketDispatch().sendMessage("You begin setting up the trap.");
			break;
		}
	}

	/**
	 * Sets up the trap.
	 */
	private void setUp() {
		player.lock(1);
		player.getWalkingQueue().reset();
		if (trap.getSettings().isObjectTrap()) {

		} else {
			if (!isGroundSetup()) {
				if (player.getInventory().remove((Item) node)) {
					groundItem = new GroundItem((Item) node, player.getLocation(), player);
					GroundItemManager.create(groundItem);
				}
				return;
			}
			groundItem = (GroundItem) node;
		}
	}

	/**
	 * If we're setting it up from the ground.
	 * @return {@code True} if so.
	 */
	public boolean isGroundSetup() {
		return node instanceof GroundItem;
	}

	/**
	 * Gets the trap.
	 * @return The trap.
	 */
	public Traps getTrap() {
		return trap;
	}
}
