package core.game.node.entity.player.link.skillertasks;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.tools.RandomFunction;

import java.util.ArrayList;

public class SkillerTasks {
	
	public SkillTasks task;
	public int taskAmount;

	public void decreaseTask(Player player, SkillTasks stask) {
		player.getGlobalData().setTaskAmount(taskAmount);
		if (!hasTask() || taskAmount == 0 || !task.getAssignment().equalsIgnoreCase(stask.getAssignment())) {
			return;
		}
		taskAmount--;
		if (taskAmount == 0) {
			if (player.getSkillTasks().getCurrentTask().getDifficulty() == Difficulty.NOVICE) {
				player.getGlobalData().setTaskPoints(player.getGlobalData().getTaskPoints() + 1);
				player.getInventory().add(new Item(995, 10000));
			} else if (player.getSkillTasks().getCurrentTask().getDifficulty() == Difficulty.INTERMEDIATE) {
				player.getGlobalData().setTaskPoints(player.getGlobalData().getTaskPoints() + 3);
				player.getInventory().add(new Item(995, 30000));
			} else if (player.getSkillTasks().getCurrentTask().getDifficulty() ==Difficulty.ADVANCED) {
				player.getGlobalData().setTaskPoints(player.getGlobalData().getTaskPoints() + 7);
				player.getInventory().add(new Item(995, 70000));
			} else if (player.getSkillTasks().getCurrentTask().getDifficulty() ==Difficulty.ELITE) {
				player.getGlobalData().setTaskPoints(player.getGlobalData().getTaskPoints() + 10);
				player.getInventory().add(new Item(995, 100000));
			}
			player.getSkillTasks().setCurrentTask(null);
			player.sendMessage("<col=00FFFF>Your task has been completed! Return to Jack for another assignment!</col>");
			return;
		}
	}

	public SkillTasks getNewTask(Player player, Difficulty tier) {
		final ArrayList<SkillTasks> tasks = new ArrayList<SkillTasks>();
		for (final SkillTasks t : SkillTasks.values()) {
			if (player.getSkills().getExperienceByLevel(t.getSkill()) < t.getLevel()) {
				continue;
			}
			if (t.getDifficulty() == tier)
				tasks.add(t);
		}
		setCurrentTask(tasks.get(RandomFunction.random(tasks.size() - 1)));
		setTaskAmount(task.getAmount());
		return task;
	}
	
	public SkillTasks getCurrentTask() {
		return task;
	}

	public int getTaskAmount() {
		return taskAmount;
	}

	public boolean hasTask() {
		return task != null;
	}

	public boolean isCompleted() {
		return hasTask() && taskAmount == 0;
	}

	public void setCurrentTask(SkillTasks task) {
		this.task = task;
	}

	public void setTaskAmount(int amount) {
		this.taskAmount = amount;
	}


}
