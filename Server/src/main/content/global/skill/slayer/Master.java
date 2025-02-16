package content.global.skill.slayer;

import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A non-garbage way of representing slayer masters
 * Source for task amounts: <a href="https://web.archive.org/web/20090130000433/http://www.runescape.com/kbase/viewarticle.ws?article_id=2382">...</a>
 * Need to add a source for weights
 * @author ceik
 * @author gregf
 */
public enum Master {
	TURAEL(8273, 3, 0, new int[]{15, 50}, new int[]{0, 0, 0},
			new Task(Tasks.BANSHEE, 8),
			new Task(Tasks.BATS, 7),
			new Task(Tasks.BEARS, 7),
			new Task(Tasks.BIRDS, 6),
			new Task(Tasks.CAVE_BUG, 8),
			new Task(Tasks.CAVE_CRAWLERS, 8),
			new Task(Tasks.CAVE_SLIMES, 8),
			new Task(Tasks.COWS, 8),												
			new Task(Tasks.CRAWLING_HAND, 8),
			new Task(Tasks.DESERT_LIZARDS, 8),
			new Task(Tasks.DOG, 7),
			new Task(Tasks.DWARF, 7),
			new Task(Tasks.GHOSTS, 7),
			new Task(Tasks.GOBLINS, 7),
			new Task(Tasks.ICE_FIENDS, 8),
			new Task(Tasks.KALPHITES, 6),
			new Task(Tasks.MINOTAURS, 7),
			new Task(Tasks.MONKEYS, 6),
			new Task(Tasks.SCORPIONS, 7),
			new Task(Tasks.SKELETONS, 7),
			new Task(Tasks.SPIDERS, 6),			
			new Task(Tasks.WOLVES, 7),
			new Task(Tasks.ZOMBIES, 7)),

	MAZCHNA(8274, 20, 0, new int[]{40, 70}, new int[]{2, 5, 15},
			new Task(Tasks.BANSHEE, 8),
			new Task(Tasks.BATS,7),
			new Task(Tasks.BEARS,6),			
			new Task(Tasks.CATABLEPONS,8),
			new Task(Tasks.CAVE_BUG,8),
			new Task(Tasks.CAVE_CRAWLERS, 8),
			new Task(Tasks.CAVE_SLIMES, 8),
			new Task(Tasks.COCKATRICES, 8),
			new Task(Tasks.CRAWLING_HAND, 8),
			new Task(Tasks.DESERT_LIZARDS, 8),
			new Task(Tasks.DOG,7),
			new Task(Tasks.EARTH_WARRIORS,6),			
			new Task(Tasks.FLESH_CRAWLERS,7),
			new Task(Tasks.GHOSTS,7),			
			new Task(Tasks.GHOULS, 7),
			new Task(Tasks.HILL_GIANTS,7),
			new Task(Tasks.HOBGOBLINS, 7),
			new Task(Tasks.ICE_WARRIOR, 7),
			new Task(Tasks.KALPHITES,6),
			new Task(Tasks.MOGRES, 8),
			new Task(Tasks.PYREFIENDS, 8),
			new Task(Tasks.ROCK_SLUGS,8),
			new Task(Tasks.SHADE,8),
			new Task(Tasks.SKELETONS, 7),			
			new Task(Tasks.VAMPIRES, 6),
			// new Task(Tasks.WALL_BEASTS,7),
			new Task(Tasks.WOLVES, 7),
			new Task(Tasks.ZOMBIES,7)),

	VANNAKA(1597, 40, 0, new int[]{60, 120}, new int[]{4, 20, 60},
			new Task(Tasks.ABERRANT_SPECTRES, 8),
			new Task(Tasks.ANKOU,7),
			new Task(Tasks.BANSHEE,6),
			new Task(Tasks.BASILISKS,8),
			new Task(Tasks.BLUE_DRAGONS,7),
			new Task(Tasks.BLOODVELDS,8),
			new Task(Tasks.BRINE_RATS,7),
			new Task(Tasks.CAVE_BUG,7),
			new Task(Tasks.CAVE_CRAWLERS,7),
			new Task(Tasks.CAVE_SLIMES,7),
			new Task(Tasks.COCKATRICES,8),
			new Task(Tasks.CRAWLING_HAND,6),
			new Task(Tasks.CROCODILES,6, new Integer[]{30, 60}),
			new Task(Tasks.DAGANNOTHS, 7),
			new Task(Tasks.DESERT_LIZARDS,7, new Integer[]{30, 60}),
			new Task(Tasks.DUST_DEVILS,8),
			new Task(Tasks.EARTH_WARRIORS,6, new Integer[]{30, 60}),
			new Task(Tasks.ELVES, 7, new Integer[]{30, 60}),
			//new Task(Tasks.FEVER_SPIDERS,7),
			new Task(Tasks.FIRE_GIANTS,7),
			new Task(Tasks.GHOULS,7),
			new Task(Tasks.GREEN_DRAGONS,6, new Integer[]{30, 60}),
			new Task(Tasks.HARPIE_BUG_SWARMS,8),
			new Task(Tasks.HELLHOUNDS,7),
			new Task(Tasks.HILL_GIANTS,7),
			new Task(Tasks.ICE_GIANTS,7, new Integer[]{30, 60}),
			new Task(Tasks.ICE_WARRIOR,7),
			new Task(Tasks.INFERNAL_MAGES,8),
			new Task(Tasks.JELLIES,8),
			new Task(Tasks.JUNGLE_HORRORS, 8),
			new Task(Tasks.KALPHITES,7),
			// new Task(Tasks.KILLERWATTS,6),
			new Task(Tasks.KURASKS,7),
			new Task(Tasks.LESSER_DEMONS,7),
			new Task(Tasks.MOGRES,7),
			// new Task(Tasks.MOLANISKS,7),
			new Task(Tasks.MOSS_GIANTS,7),
			new Task(Tasks.OGRES,7),
			new Task(Tasks.OTHERWORDLY_BEING,8),
			new Task(Tasks.PYREFIENDS,8),
			new Task(Tasks.ROCK_SLUGS,7),																			
			// new Task(Tasks.SEA_SNAKES,6),
			new Task(Tasks.SHADE,8),
			// new Task(Tasks.SHADOW_WARRIORS, 8),
			new Task(Tasks.TROLLS,7),
			new Task(Tasks.TUROTHS, 8),
			new Task(Tasks.VAMPIRES,7),
			// new Task(Tasks.WALL_BEASTS,6),
			new Task(Tasks.WEREWOLVES,7)),

	CHAELDAR(1598, 70, 0, new int[]{110, 170}, new int[]{10, 50, 150},
			new Task(Tasks.ABERRANT_SPECTRES,8),
			new Task(Tasks.ABYSSAL_DEMONS,12),
			new Task(Tasks.BANSHEE, 5),
			new Task(Tasks.BASILISKS,7),
			new Task(Tasks.BLUE_DRAGONS,8),
			new Task(Tasks.BLOODVELDS,8),
			new Task(Tasks.BRINE_RATS,7),
			new Task(Tasks.BRONZE_DRAGONS,11, new Integer[]{30, 60}),
			new Task(Tasks.CAVE_BUG, 5),
			new Task(Tasks.CAVE_CRAWLERS, 5),		
			new Task(Tasks.CAVE_HORRORS,10),
			new Task(Tasks.CAVE_SLIMES,6),		
			new Task(Tasks.COCKATRICES,6),
			new Task(Tasks.CRAWLING_HAND, 5),
			new Task(Tasks.CROCODILES, 5, new Integer[]{30, 60}),
			new Task(Tasks.DAGANNOTHS,11),
			new Task(Tasks.DESERT_LIZARDS, 5, new Integer[]{30, 60}),
			new Task(Tasks.DUST_DEVILS,9),
			new Task(Tasks.ELVES,8, new Integer[]{60, 90}),
			//new Task(Tasks.FEVER_SPIDERS,7),																
			new Task(Tasks.FIRE_GIANTS, 12),
			new Task(Tasks.GARGOYLES,11),
			new Task(Tasks.GREATER_DEMONS,9),
			new Task(Tasks.HARPIE_BUG_SWARMS,6),
			new Task(Tasks.HELLHOUNDS,9),
			new Task(Tasks.IRON_DRAGONS,12, new Integer[]{30, 60}),
			new Task(Tasks.INFERNAL_MAGES,7),
			new Task(Tasks.JELLIES, 10),
			new Task(Tasks.JUNGLE_HORRORS,10),
			new Task(Tasks.KALPHITES,11),
			new Task(Tasks.KURASKS, 12),
			new Task(Tasks.LESSER_DEMONS,9),
			new Task(Tasks.MOGRES,6),
			// new Task(Tasks.MOLANISKS,6),
			//new Task(Tasks.MUTATED_ZYGOMITES,7, new Integer[]{30, 60}),
			new Task(Tasks.NECHRYAELS, 12),
			new Task(Tasks.PYREFIENDS,6),
			new Task(Tasks.ROCK_SLUGS, 5),
			// new Task(Tasks.SHADOW_WARRIORS,8),
			new Task(Tasks.SPIRTUAL_WARRIORS,4),
			new Task(Tasks.SPIRTUAL_RANGERS,4),
			new Task(Tasks.SPIRTUAL_MAGES,4),
			new Task(Tasks.TROLLS,11),
			new Task(Tasks.TUROTHS, 10)),
			// new Task(Tasks.WALL_BEASTS,6, new Integer[]{10, 20}),
			// new Task(Tasks.WARPED_TERROR_BIRD, 5),
			// new Task(Tasks.WARPED_TORTOISE, 5)

	SUMONA(7780, 85, 35, new int[]{120, 185}, new int[]{12, 60, 180},
			new Task(Tasks.ABERRANT_SPECTRES, 15),
			new Task(Tasks.ABYSSAL_DEMONS, 10),
			new Task(Tasks.AVIANSIES, 10),
			new Task(Tasks.BANSHEE, 15),
			new Task(Tasks.BASILISKS, 15),
			new Task(Tasks.BLACK_DEMONS, 10),
			new Task(Tasks.BLUE_DRAGONS, 5),
			new Task(Tasks.BLOODVELDS, 10),
			new Task(Tasks.CAVE_CRAWLERS, 15),
			new Task(Tasks.CAVE_HORRORS, 15),
			new Task(Tasks.DAGANNOTHS, 10),
			new Task(Tasks.DUST_DEVILS, 15),
			new Task(Tasks.ELVES, 10, new Integer[]{60, 90}),
			new Task(Tasks.FIRE_GIANTS, 10),
			new Task(Tasks.GARGOYLES, 10),
			new Task(Tasks.GREATER_DEMONS, 10),
			new Task(Tasks.HELLHOUNDS, 10),
			new Task(Tasks.IRON_DRAGONS, 7, new Integer[]{30, 60}),
			new Task(Tasks.KALPHITES, 10),
			new Task(Tasks.KURASKS, 15),
			new Task(Tasks.NECHRYAELS, 10),
		 	new Task(Tasks.RED_DRAGONS, 5),
			// new Task(Tasks.SCABARITES, 9, new Integer[]{30, 60}),
			new Task(Tasks.SPIRTUAL_MAGES, 10),
			new Task(Tasks.SPIRTUAL_WARRIORS, 10),
			// new Task(Tasks.TERROR_DOGS, 10, new Integer[]{30, 60}),
			new Task(Tasks.TROLLS, 10),
			new Task(Tasks.TUROTHS, 15)),
			// new Task(Tasks.WARPED_TORTOISE, 15)),

	DURADEL(8275, 100, 50, new int[]{130, 200}, new int[]{15, 75, 225},
			new Task(Tasks.ABERRANT_SPECTRES,7),	
			new Task(Tasks.ABYSSAL_DEMONS,12),
			new Task(Tasks.AVIANSIES, 10),
			new Task(Tasks.BLACK_DEMONS,8),
			new Task(Tasks.BLACK_DRAGONS,9, new Integer[]{40, 80}),
			new Task(Tasks.BLOODVELDS,8),
			new Task(Tasks.DAGANNOTHS,9),
			new Task(Tasks.DARK_BEASTS,11),
			new Task(Tasks.DUST_DEVILS,5),
			new Task(Tasks.FIRE_GIANTS,7),
			new Task(Tasks.GARGOYLES,8),
			new Task(Tasks.GORAKS,9),
			new Task(Tasks.GREATER_DEMONS,9),
			new Task(Tasks.HELLHOUNDS, 10),
			new Task(Tasks.IRON_DRAGONS,5, new Integer[]{40, 80}),
			new Task(Tasks.KALPHITES,9),
			new Task(Tasks.MITHRIL_DRAGONS,9, new Integer[]{4, 8}),
			new Task(Tasks.NECHRYAELS,9),
			// new Task(Tasks.SCABARITES, 9, new Integer[]{40, 80}),
			new Task(Tasks.SKELETAL_WYVERN,7, new Integer[]{40, 80}),
			new Task(Tasks.SPIRTUAL_MAGES,2),
			new Task(Tasks.STEEL_DRAGONS,7, new Integer[]{40, 80}),
			new Task(Tasks.SUQAHS,8, new Integer[]{40, 80}),
			// new Task(Tasks.WARPED_TERROR_BIRD,8),
			new Task(Tasks.WATERFIENDS,2));

	private static final HashMap<Integer,Master> idMap = new HashMap<>();

	static{
		Arrays.stream(Master.values()).forEach(m -> idMap.putIfAbsent(m.npc_id, m));
	}

	final int npc_id;
    final int required_combat;
    final int required_slayer;
	public final int[] default_assignment_range;
	final int[] streakPoints;
	public final List<Task> tasks;
	Master(int npc_id, int required_combat, int required_slayer, int[] default_assignment_range, int[] streakPoints, Task... tasks) {
		this.npc_id = npc_id;
		this.required_combat = required_combat;
		this.required_slayer = required_slayer;
		this.default_assignment_range = default_assignment_range;
		this.streakPoints = streakPoints;
		this.tasks = new ArrayList<>(Arrays.asList(tasks));
	}

	public static Master forId(int id){
		return idMap.get(id);
	}

	public int getNpc(){
		return this.npc_id;
	}

	public int[] getTaskPoints(){
		return streakPoints;
	}

	public boolean hasRequirements(Player player){
		return player.getProperties().getCurrentCombatLevel() >= this.required_combat && player.getSkills().getLevel(Skills.SLAYER) >= this.required_slayer;
	}

	public static boolean hasSameTask(Master master, Player player){
		return master.tasks.stream().filter(task -> task.task == SlayerManager.getInstance(player).getTask()).count() != 0;
	}

	public static class Task{
		public Tasks task;
		public Integer weight;
		public Integer[] task_range;
		public Task(Tasks task, Integer weight){
			this.task = task;
			this.weight = weight;
			this.task_range = new Integer[]{null, null};
		}

		Task(Tasks task, Integer weight, Integer[] task_range){
			this.task = task;
			this.weight = weight;
			this.task_range = task_range;
		}
	}
}
