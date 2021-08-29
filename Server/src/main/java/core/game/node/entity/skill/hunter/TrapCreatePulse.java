package core.game.node.entity.skill.hunter;

import core.game.node.entity.skill.SkillPulse;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;

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
	}

	@Override
	public boolean checkRequirements() {
		if (player.getHunterManager().getStaticLevel() < trap.getSettings().getLevel()) {
			player.sendMessage("You need a Hunter level of at least " + trap.getSettings().getLevel() + " in order to setup a " + node.getName().toLowerCase() + ".");
			return false;
		}
		if (player.getHunterManager().exceedsTrapLimit(trap)) {
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
		player.getHunterManager().register(trap, node, object);
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
