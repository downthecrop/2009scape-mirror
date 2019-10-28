package org.crandor.game.content.skill.member.thieving;

import org.crandor.game.content.skill.SkillPulse;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.portal.Perks;
import org.crandor.game.node.entity.player.link.diary.DiaryType;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.node.object.ObjectBuilder;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.tools.RandomFunction;
import org.crandor.tools.StringUtils;

/**
 * Represents the pulse used to thieve a stall.
 * @author 'Vexia
 */
public final class StallThiefPulse extends SkillPulse<GameObject> {

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
	public StallThiefPulse(Player player, GameObject node, final Stall stall) {
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
		}
		if (++ticks % 3 != 0) {
			return false;
		}
		final boolean success = success();
		if (success) {
			final Item item = stall.getRandomLoot();
			if (stall == Stall.SILK_STALL) {
				player.getSavedData().getGlobalData().setSilkSteal(System.currentTimeMillis() + 1800000);
			}
			if (stall == Stall.TEA_STALL && player.getLocation().withinDistance(new Location(3266, 3413, 0)) && !player.getAchievementDiaryManager().getDiary(DiaryType.VARROCK).isComplete(0, 12)) {
				player.getAchievementDiaryManager().getDiary(DiaryType.VARROCK).updateTask(player, 0, 12, true);
			}
			if (node.isActive()) {
				ObjectBuilder.replace(((GameObject) node), ((GameObject) node).transform(stall.getTemporary()), stall.getDelay());
			}
			Perks.addDouble(player, item, true);
			player.getSkills().addExperience(Skills.THIEVING, stall.getExperience(), true);
			if (item.getId() == 1987) {
				player.getPacketDispatch().sendMessage("You steal grapes from the grape stall.");
				return true;
			}
			player.getPacketDispatch().sendMessage("You steal " + (StringUtils.isPlusN(item.getName()) ? "an" : "a") + " " + item.getName().toLowerCase() + " from the " + node.getName().toLowerCase() + ".");
		}
		return true;
	}

	@Override
	public void message(int type) {
		switch (type) {
		case 0:
			player.getPacketDispatch().sendMessage("You attempt to steal " + stall.getMessage() + "");
			break;
		}
	}

	/**
	 * Checks if the thief is successful.
	 * @return {@code True} if so.
	 */
	private boolean success() {
		int mod = 0;
		if (player.getDetails().getShop().hasPerk(Perks.SLEIGHT_OF_HAND)) {
			mod = 8;
		}
		if (RandomFunction.random(15 + mod) < 4) {
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
