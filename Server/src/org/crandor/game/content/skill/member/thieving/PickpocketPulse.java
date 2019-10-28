package org.crandor.game.content.skill.member.thieving;

import org.crandor.game.content.global.SkillcapePerks;
import org.crandor.game.content.skill.SkillPulse;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.DeathTask;
import org.crandor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.portal.Perks;
import org.crandor.game.node.entity.player.link.audio.Audio;
import org.crandor.game.node.entity.player.link.diary.DiaryType;
import org.crandor.game.node.entity.state.EntityState;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.tools.RandomFunction;

/**
 * Represents the pulse used to pickpocket an npc.
 * @author Vexia
 */
public final class PickpocketPulse extends SkillPulse<NPC> {

	/**
	 * Represents the animation specific to pickpocketing.
	 */
	private static final Animation ANIMATION = new Animation(881);

	/**
	 * Represents the npc animation.
	 */
	private static final Animation NPC_ANIM = new Animation(422);

	/**
	 * Represents the animation specific to pickpocketing.
	 */
	private static final Animation STUN_ANIMATION = new Animation(424);

	/**
	 * Represents the sound to send when failed.
	 */
	private static final Audio SOUND = new Audio(2727, 1, 0);

	/**
	 * Represents the pickpocket type.
	 */
	private final Pickpocket type;

	/**
	 * Represents the tickets to be rewarded.
	 */
	private int ticks;

	/**
	 * Constructs a new {@code PickpocketPulse} {@code Object}.
	 * @param player the player.
	 * @param node the node.
	 * @param type the type.
	 */
	public PickpocketPulse(Player player, NPC node, final Pickpocket type) {
		super(player, node);
		this.type = type;
		this.resetAnimation = false;
	}

	@Override
	public boolean checkRequirements() {
		if (!interactable()) {
			return false;
		}
		if (player.getSkills().getLevel(Skills.THIEVING) < type.getLevel()) {
			player.getPacketDispatch().sendMessage("You need to be a level " + type.getLevel() + " thief in order to pick this pocket.");
			return false;
		}
		player.lock(1);
		player.faceTemporary(node, 2);
		node.getWalkingQueue().reset();
		node.getLocks().lockMovement(1);
		return true;
	}

	@Override
	public void animate() {
	}

	@Override
	public boolean reward() {
		if (ticks == 0) {
			player.animate(ANIMATION);
		}
		if (++ticks % 4 != 0) {
			return false;
		}
		final boolean success = success();
		if (success) {
			if (player.getViewport().getRegion().getId() == 12850 && !player.getAchievementDiaryManager().getDiary(DiaryType.LUMBRIDGE).isComplete(0, 3)) {
				player.getAchievementDiaryManager().updateTask(player, DiaryType.LUMBRIDGE, 0, 3, true);
			}
			if (type == Pickpocket.MARTIN_THE_MASTER_GARDENER && !player.getAchievementDiaryManager().getDiary(DiaryType.LUMBRIDGE).isComplete(1, 6)) {
				player.getAchievementDiaryManager().getDiary(DiaryType.LUMBRIDGE).updateTask(player, 1, 6, true);
			}
		    player.getSkills().addExperience(Skills.THIEVING, type.getExperience(), true);
		    Perks.addDouble(player, type.getRandomLoot(player), true);
		    player.getPacketDispatch().sendMessage(type.getRewardMessage().replace("@name", node.getName().toLowerCase()));
		} else {
			node.animate(NPC_ANIM);
			node.faceTemporary(player, 1);
			node.sendChat(type.getForceMessage());
			player.animate(STUN_ANIMATION);
			player.getAudioManager().send(new Audio(1842));
			player.getStateManager().set(EntityState.STUNNED, 4);
			player.getAudioManager().send(SOUND);
			player.setAttribute("thief-delay", GameWorld.getTicks() + 4);
			player.getImpactHandler().manualHit(player, type.getStunDamage(), HitsplatType.NORMAL);
			player.getPacketDispatch().sendMessage(type.getFailMessage().replace("@name", node.getName().toLowerCase()));
		}
		return true;
	}

	@Override
	public void stop() {
		super.stop();
	}

	@Override
	public void message(int type) {
		switch (type) {
		case 0:
			player.getPacketDispatch().sendMessage(this.type.getStartMessage().replace("@name", node.getName().toLowerCase()));
			break;
		}
	}

	/**
	 * Checks if the npc is interable.
	 * @return {@code True} if so.
	 */
	private boolean interactable() {
		if (DeathTask.isDead(((Entity) node)) || ((NPC) node).isHidden(player) || !node.isActive() || player.getAttribute("thief-delay", 0) > GameWorld.getTicks()) {
			return false;
		} else if (player.inCombat()) {
			player.getPacketDispatch().sendMessage("You can't pickpocket during combat.");
			return false;
		} else if (player.getInventory().freeSlots() == 0) {
			player.getPacketDispatch().sendMessage("You don't have enough inventory space.");
			return false;
		}
		return true;
	}

	/**
	 * Checks if the pickpocket is a success.
	 * @return {@code True} if so.
	 */
	private boolean success() {
		double level = player.getSkills().getLevel(Skills.THIEVING);
		double req = type.getLevel();
		double successChance = Math.ceil((level * 50 - req * 15) / req / 3 * 4);
		if (player.getDetails().getShop().hasPerk(Perks.SLEIGHT_OF_HAND)) {
			successChance += RandomFunction.random(30, 45);
			if (RandomFunction.random(5) == 1) {
				return true;
			}
		}
		if (SkillcapePerks.hasSkillcapePerk(player, SkillcapePerks.THIEVEING)) {
			successChance += 10;
		}
		int roll = RandomFunction.random(99);
		if (RandomFunction.random(12) < 2) {
			return false;
		}
		if (successChance >= roll) {
			return true;
		}
		return false;
	}

}
