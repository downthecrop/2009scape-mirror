package content.global.skill.thieving;

import core.game.event.ResourceProducedEvent;
import core.game.node.entity.combat.ImpactHandler;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.tools.RandomFunction;
import core.tools.StringUtils;

import static core.api.ContentAPIKt.*;

/**
 * Represents the pulse used to thieve a stall.
 * @author 'Vexia
 */
public final class StallThiefPulse extends SkillPulse<Scenery> {

	/**
	 * Represents the stealing animation.
	 */
	private static final Animation ANIMATION = new Animation(832);

	/**
	 * Represents the stall being thieved.
	 */
	private final Stall stall;

	/**
	 * Represents the ticks passed.
	 */
	private int ticks;

	/**
	 * Constructs a new {@code StallThiefPulse} {@code Object}.
	 * @param player the player.
	 * @param node the node.
	 * @param stall the stall.
	 */
	public StallThiefPulse(Player player, Scenery node, final Stall stall) {
		super(player, node);
		this.stall = stall;
	}

	@Override
	public void start() {
		player.setAttribute("thieveDelay", GameWorld.getTicks());
		super.start();
	}

	@Override
	public boolean checkRequirements() {
		if (stall == null) {
			return false;
		}
		if (player.inCombat()) {
			player.getPacketDispatch().sendMessage("You cant steal from the market stall during combat!");
			return false;
		}
		if (player.getSkills().getLevel(Skills.THIEVING) < stall.getLevel()) {
			player.getPacketDispatch().sendMessage("You need to be level " + stall.getLevel() + " to steal from the " + node.getName().toLowerCase() + ".");
			return false;
		}
		if (player.getInventory().freeSlots() == 0) {
			player.getPacketDispatch().sendMessage("You don't have enough inventory space.");
			return false;
		}
		if (player.getLocation().isInRegion(10553) && !isQuestComplete(player, "Fremennik Trials") && stall.full_ids.contains(4278)) {
			sendDialogue(player, "The fur trader is staring at you suspiciously. You cannot steal from his stall while he distrusts you.");
			return false;
		}

		if (player.getLocation().isInRegion(10553) && !isQuestComplete(player, "Fremennik Trials") && stall.full_ids.contains(4277)) {
			sendDialogue(player, "The fishmonger is staring at you suspiciously. You cannot steal from his stall while he distrusts you.");
			return false;
		}
		return true;
	}

	@Override
	public boolean hasInactiveNode() {
		if (player.getAttribute("thieveDelay", 0) <= GameWorld.getTicks()) {
			return false;
		}
		return super.hasInactiveNode();
	}

	@Override
	public void animate() {
	}

	@Override
	public boolean reward() {
		if (ticks == 0) {
			player.animate(ANIMATION);
			player.getLocks().lockInteractions(2);
		}
		if (++ticks % 3 != 0) {
			return false;
		}
		final boolean success = success();
		if (success) {
			if (stall == Stall.SILK_STALL) {
				player.getSavedData().getGlobalData().setSilkSteal(System.currentTimeMillis() + 1800000);
			}
			if (node.isActive()) {
				SceneryBuilder.replace(node, node.transform(stall.getEmpty(node.getId())), stall.getDelay());
			}
			final Item item = stall.getRandomLoot();
		    player.getInventory().add(item);
			player.getSkills().addExperience(Skills.THIEVING, stall.getExperience(), true);
			if (item.getId() == 1987) {
				player.getPacketDispatch().sendMessage("You steal grapes from the grape stall.");
				return true;
			}
			if(stall == Stall.CANDLES) {
				return true;
			}
			player.getPacketDispatch().sendMessage("You steal " + (StringUtils.isPlusN(item.getName()) ? "an" : "a") + " " + item.getName().toLowerCase() + " from the " + stall.name().toLowerCase().replace('_',' ') + ".");
			player.dispatch(new ResourceProducedEvent(item.getId(), item.getAmount(), node, 0));
		}
		return true;
	}

	@Override
	public void message(int type) {
		if(stall == Stall.CANDLES) {
			return;
		}
        if (type == 0) {
            player.getPacketDispatch().sendMessage("You attempt to steal some " + stall.msgItem + " from the " + stall.name().toLowerCase().replace('_', ' '));
        }
	}

	/**
	 * Checks if the thief is successful.
	 * @return {@code True} if so.
	 */
	private boolean success() {
		int mod = 0;
		if (RandomFunction.random(15 + mod) < 4) {
			if(stall == Stall.CANDLES) {
				stun(player, 15, false);
				impact(player, 1, ImpactHandler.HitsplatType.NORMAL);
				// Location playerLoc = player.getLocation();
				// forceMove(player, playerLoc, new Location(playerLoc.getX() - 1, playerLoc.getY() - 1),
				// 		0, 4, Direction.SOUTH_WEST, 819, null);
				player.sendMessage("A higher power smites you");
				return false;
			}
			for (NPC npc : RegionManager.getLocalNpcs(player.getLocation(), 8)) {
				if (!npc.getProperties().getCombatPulse().isAttacking() && (npc.getId() == 32 || npc.getId() == 2236)) {
					npc.sendChat("Hey! Get your hands off there!");
					npc.getProperties().getCombatPulse().attack(player);
					return false;
				}
			}
		}
		return true;
	}

}
