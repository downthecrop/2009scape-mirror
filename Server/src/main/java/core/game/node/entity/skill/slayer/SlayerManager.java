package core.game.node.entity.skill.slayer;

import core.cache.def.impl.NPCDefinition;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.entity.skill.Skills;
import core.tools.RandomFunction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rs09.game.node.entity.skill.slayer.SlayerFlags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the players slayer task.
 * @author Vexia
 *
 */
public final class SlayerManager {

	/**
	 * The player instance.
	 */
	private final Player player;

	/**
	 * The player's slayer flags
	 */
	public final SlayerFlags flags;

	/**
	 * Constructs a new {@Code SlayerManager} {@Code Object}
	 * @param player The player.
	 */
	public SlayerManager(Player player) {
		this.player = player;
		this.flags = new SlayerFlags(player);
	}

	public void parse(JSONObject slayerData){
		Object m = slayerData.get("master");
		if(m != null) {
			flags.setMaster(Master.forId(Integer.parseInt(m.toString())));
		}
		Object t = slayerData.get("taskId");
		if(t != null)
			flags.setTask(Tasks.values()[Integer.parseInt(t.toString())]);
		Object a = slayerData.get("taskAmount");
		if(a != null)
			flags.setTaskAmount(Integer.parseInt(a.toString()));
		Object points = slayerData.get("points");
		if(points != null){
			flags.setPoints(Integer.parseInt(points.toString()));
		}
		Object taskStreak = slayerData.get("taskStreak");
		if(taskStreak != null){
			flags.setTaskStreak(Integer.parseInt(taskStreak.toString()));
		}
		Object la = slayerData.get("learned_rewards");
		if(la != null) {
			JSONArray learnedArray = (JSONArray) slayerData.get("learned_rewards");
			for (int i = 0; i < learnedArray.size(); i++) {
				boolean unlocked = (boolean) learnedArray.get(i);
				switch(i){
					case 0:
						if(unlocked) flags.unlockBroads();
						break;
					case 1:
						if(unlocked) flags.unlockRing();
						break;
					case 2:
						if(unlocked) flags.unlockHelm();
						break;
					default:
						break;
				}
			}
		}
		JSONArray removedTasks = (JSONArray) slayerData.get("removedTasks");
		if(removedTasks != null){
			for(int i = 0; i < removedTasks.size(); i++){
				flags.getRemoved().add(Tasks.values()[Integer.parseInt(removedTasks.get(i).toString())]);
			}
		}
		Object completedTasks = slayerData.get("totalTasks").toString();
		if(completedTasks != null) flags.setCompletedTasks(Integer.parseInt(completedTasks.toString()));
		if(flags.getCompletedTasks() >= 4) flags.flagCanEarnPoints();

		//New system parsing
		if(slayerData.containsKey("equipmentFlags"))
			flags.setEquipmentFlags(Integer.parseInt(slayerData.get("equipmentFlags").toString()));
		if(slayerData.containsKey("taskFlags"))
			flags.setTaskFlags(Integer.parseInt(slayerData.get("taskFlags").toString()));
		if(slayerData.containsKey("rewardFlags"))
			flags.setRewardFlags(Integer.parseInt(slayerData.get("rewardFlags").toString()));

	}

	/**
	 * Called when a hunted creature dies.
	 * @param player The player.
	 * @param npc The NPC. You're currently
	 */
	public void finalizeDeath(Player player, NPC npc) {
		player.getSkills().addExperience(Skills.SLAYER,npc.getSkills().getMaximumLifepoints());
		decrementAmount(1);
		if (!hasTask()) {
			clear();
			flags.setTaskStreak(flags.getTaskStreak() + 1);
			flags.setCompletedTasks(flags.getCompletedTasks() + 1);
			if ((flags.getCompletedTasks() > 4 || flags.canEarnPoints() ) && flags.getMaster() != Master.TURAEL && flags.getPoints() < 64000) {
				int points = flags.getMaster().getTaskPoints()[0];
				if (flags.getTaskStreak() % 10 == 0) {
					points = flags.getMaster().getTaskPoints()[1];
				} else if (flags.getTaskStreak() % 50 == 0) {
					points = flags.getMaster().getTaskPoints()[2];
				}
				flags.incrementPoints(points);
				if (flags.getPoints() > 64000) {
					flags.setPoints(64000);
				}
				player.sendMessages("You've completed " + flags.getTaskStreak() + " tasks in a row and received " + points + " points, with a total of " + flags.getPoints(),"You have completed " + flags.getCompletedTasks() + " tasks in total. Return to a Slayer master.");
			} else if(flags.getCompletedTasks() == 4){
				player.sendMessage("You've completed your task; you will start gaining points on your next task!");
				flags.flagCanEarnPoints();
			} else {
				player.sendMessages("You've completed your task; Complete " + (4 - flags.getCompletedTasks()) + " more task(s) to start gaining points.", "Return to a Slayer master.");
			}
		} else {
			//player.sendMessage("You're assigned to kill " + NPCDefinition.forId((player.getSlayer().getTask().getNpcs()[0])).getName().toLowerCase() + "s; Only " + getAmount() + " more to go.");
		}
	}

	/**
	 * Assigns a task to the manager.
	 * @param task the task.
	 * @param master the master.
	 */
	public void assign(Tasks task, final Master master) {
		setMaster(master);
		setTask(task);
		setAmount(getRandomAmount(master.assignment_range));
		if (master == Master.DURADEL) {
			player.getAchievementDiaryManager().finishTask(player, DiaryType.KARAMJA, 2, 8);
		} else if (master == Master.VANNAKA) {
			player.getAchievementDiaryManager().finishTask(player, DiaryType.VARROCK, 1, 14);
		}
		getPlayer().varpManager.get(2502).setVarbit(0, flags.getTaskFlags() >> 4).send(player);
	}

	/**
	 * Method used to assign a new task for a player.
	 * @param master the master to give the task.
	 */
	public void generate(Master master) {
		final List<Master.Task> tasks = new ArrayList<>(10);
		final int[] taskWeightSum = {0};
		master.tasks.stream().filter(task -> canBeAssigned(task.task) && task.task.combatCheck <= player.getProperties().getCurrentCombatLevel()).forEach(task -> {
					taskWeightSum[0] += task.weight;
					tasks.add(task);
		});
		Collections.shuffle(tasks, RandomFunction.RANDOM);
		int rnd = RandomFunction.random(taskWeightSum[0]);
		for(Master.Task task : tasks){
			if(rnd < task.weight)
				assign(task.task,master);
			rnd -= task.weight;
		}
	}

	public boolean canBeAssigned(Tasks task){
		return player.getSkills().getLevel(Skills.SLAYER) >= task.levelReq && !flags.getRemoved().contains(task);
	}

	/**
	 * Clears the task.
	 */
	public void clear() {
		setAmount(0);
	}

	/**
	 * Gets a random amount.
	 * @param ranges the ranges.
	 * @return the amt.
	 */
	private int getRandomAmount(int[] ranges) {
		return RandomFunction.random(ranges[0], ranges[1]);
	}

	/**
	 * Gets the task name.
	 * @return the name.
	 */
	public String getTaskName() {
		Tasks task = flags.getTask();
		if (task.getNpcs() == null) {
			return "no npcs report me";
		}
		if (task.getNpcs().length < 1) {
			return "npc length too small report me";
		}
		return NPCDefinition.forId(task.getNpcs()[0]).getName().toLowerCase();
	}

	/**
	 * Gets the task.
	 * @return The task.
	 */
	public Tasks getTask() {
		return flags.getTask();
	}

	/**
	 * Sets the task.
	 * @param task The task to set.
	 */
	public void setTask(Tasks task){
		flags.setTask(task);
	}

	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the master.
	 * @return The master.
	 */
	public Master getMaster() {
		return flags.getMaster();
	}

	/**
	 * Sets the master.
	 * @param master The master to set.
	 */
	public void setMaster(Master master) {
		flags.setMaster(master);
	}

	/**
	 * Checks if a <b>Player</b> contains a task.
	 * @return {@code True} if so.
	 */
	public boolean hasTask() {
		return getAmount() > 0;
	}

	/**
	 * Method used to check if the task is completed.
	 * @return <code>True</code> if so.
	 */
	public boolean isCompleted() {
		return flags.getTaskAmount() <= 0;
	}

	/**
	 * Gets the amount.
	 * @return The amount.
	 */
	public int getAmount() {
		return flags.getTaskAmount();
	}

	/**
	 * Sets the amount.
	 * @param amount The amount to set.
	 */
	public void setAmount(int amount) {
		flags.setTaskAmount(amount);
	}

	/**
	 * Method used to decrement an amount.
	 * @param amount the amount.
	 */
	public void decrementAmount(int amount) {
		flags.decrementTaskAmount(amount);
		player.varpManager.get(2502).setVarbit(0, flags.getTaskFlags() >> 4).send(player);
	}

	/**
	 * Method used to check if the player has started slayer.
	 * @return {@code True} if so.
	 */
	public boolean hasStarted() {
		return flags.getCompletedTasks() > 0 || flags.getTaskAmount() > 0;
	}

	/**
	 * Gets the slayerPoints.
	 * @return the slayerPoints.
	 */
	public int getSlayerPoints() {
		return flags.getPoints();
	}

	/**
	 * Sets the slayerPoints.
	 * @param slayerPoints the slayerPoints to set
	 */
	public void setSlayerPoints(int slayerPoints) {
		flags.setPoints(slayerPoints);
	}

	/**
	 * Gets the taskCount.
	 * @return the taskCount.
	 */
	public int getTaskCount() {
		return flags.getTaskStreak();
	}

	/**
	 * Sets the taskCount.
	 * @param taskCount the taskCount to set
	 */
	public void setTaskCount(int taskCount) {
		flags.setTaskStreak(taskCount);
	}

	/**
	 * Gets the removed.
	 * @return the removed.
	 */
	public List<Tasks> getRemoved() {
		return flags.getRemoved();
	}

	public int getTotalTasks() {return flags.getCompletedTasks();}

	public boolean isCanEarnPoints(){
		return flags.canEarnPoints();
	}

}
