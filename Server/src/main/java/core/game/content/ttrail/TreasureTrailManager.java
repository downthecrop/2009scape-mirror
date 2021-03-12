package core.game.content.ttrail;

import core.game.node.entity.player.Player;

import core.tools.RandomFunction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.nio.ByteBuffer;

/**
 * Handles the treasure trail of a player.
 * @author Vexia
 */
public final class TreasureTrailManager {

	/**
	 * The ids of the clues.
	 */
	private static final int[] IDS = new int[] { 2677, 2678, 2679, 2680, 2682, 2683, 2684, 2685, 2686, 2687, 2688, 2689, 2690, 2691, 2692, 2693, 2694, 2695, 2696, 2697, 2698, 2699, 2700, 2701, 2702, 2703, 2704, 2705, 2706, 2707, 2708, 2709, 2710, 2711, 2712, 2713, 2716, 2719, 2722, 2723, 2725, 2727, 2729, 2731, 2733, 2735, 2737, 2739, 2741, 2743, 2745, 2747, 2773, 2774, 2776, 2778, 2780, 2782, 2783, 2785, 2786, 2788, 2790, 2792, 2793, 2794, 2796, 2797, 2799, 2801, 2803, 2805, 2807, 2809, 2811, 2813, 2815, 2817, 2819, 2821, 2823, 2825, 2827, 2829, 2831, 2833, 2835, 2837, 2839, 2841, 2843, 2845, 2847, 2848, 2849, 2851, 2853, 2855, 2856, 2857, 2858, 3490, 3491, 3492, 3493, 3494, 3495, 3496, 3497, 3498, 3499, 3500, 3501, 3502, 3503, 3504, 3505, 3506, 3507, 3508, 3509, 3510, 3512, 3513, 3514, 3515, 3516, 3518, 3520, 3522, 3524, 3525, 3526, 3528, 3530, 3532, 3534, 3536, 3538, 3540, 3542, 3544, 3546, 3548, 3550, 3552, 3554, 3556, 3558, 3560, 3562, 3564, 3566, 3568, 3570, 3572, 3573, 3574, 3575, 3577, 3579, 3580, 3582, 3584, 3586, 3588, 3590, 3592, 3594, 3596, 3598, 3599, 3601, 3602, 3604, 3605, 3607, 3609, 3610, 3611, 3612, 3613, 3614, 3615, 3616, 3617, 3618, 7236, 7238, 7239, 7241, 7243, 7245, 7247, 7248, 7249, 7250, 7251, 7252, 7253, 7254, 7255, 7256, 7258, 7260, 7262, 7264, 7266, 7268, 7270, 7272, 7274, 7276, 7278, 7280, 7282, 7284, 7286, 7288, 7290, 7292, 7294, 7296, 7298, 7300, 7301, 7303, 7304, 7305, 7307, 7309, 7311, 7313, 7315, 7317, 10180, 10182, 10184, 10186, 10188, 10190, 10192, 10194, 10196, 10198, 10200, 10202, 10204, 10206, 10208, 10210, 10212, 10214, 10216, 10218, 10220, 10222, 10224, 10226, 10228, 10230, 10232, 10234, 10236, 10238, 10240, 10242, 10244, 10246, 10248, 10250, 10252, 10254, 10256, 10258, 10260, 10262, 10264, 10266, 10268, 10270, 10272, 10274, 10276, 10278, 13010, 13012, 13014, 13016, 13018, 13020, 13022, 13024, 13026, 13028, 13030, 13032, 13034, 13036, 13038, 13040, 13041, 13042, 13044, 13046, 13048, 13049, 13050, 13051, 13053, 13055, 13056, 13058, 13060, 13061, 13063, 13065, 13067, 13068, 13069, 13070, 13071, 13072, 13074, 13075, 13076, 13078, 13079, 13080 };

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The current clue id.
	 */
	private int clueId;

	/**
	 * The clue level.
	 */
	private ClueLevel level;

	/**
	 * The trail stage.
	 */
	private int trailStage;

	/**
	 * The current length of the trail.
	 */
	private int trailLength;

	/**
	 * The amount of completed clues.
	 */
	private final int[] completedClues = new int[3];

	/**
	 * Constructs a new {@Code TreasureTrailManager} {@Code
	 * Object}
	 * @param player the player.
	 */
	public TreasureTrailManager(Player player) {
		this.player = player;
	}

	public void parse(JSONObject data){
		JSONArray cc = (JSONArray) data.get("completedClues");
		for(int i = 0; i < cc.size(); i++){
			completedClues[i] = Integer.parseInt(cc.get(i).toString());
		}

		if(data.containsKey("trail")){
			JSONObject trail = (JSONObject) data.get("trail");
			clueId = Integer.parseInt(trail.get("clueId").toString());
			trailLength = Integer.parseInt(trail.get("length").toString());
			trailStage = Integer.parseInt(trail.get("stage").toString());
		}
	}

	/**
	 * Starts a treasure trail.
	 * @param clue the clue.
	 */
	public void startTrail(ClueScrollPlugin clue) {
		setClue(clue);
		trailLength = RandomFunction.random(clue.getLevel().getMinimumLength(), clue.getLevel().getMaximumLength());
		trailStage = 0;
	}

	/**
	 * Clears the trail.
	 */
	public void clearTrail() {
		clueId = 0;
		level = null;
		trailLength = 0;
		trailStage = 0;
	}

	/**
	 * Sets the current clue.
	 * @param clue the clue.
	 */
	public void setClue(ClueScrollPlugin clue) {
		clueId = clue.getClueId();
		level = clue.getLevel();
	}

	/**
	 * Checks if the player has a clue.
	 * @return {@code True} if so.
	 */
	public boolean hasClue() {
		if (player == null) {
			return true;
		}
		if (player.hasItem(ClueLevel.EASY.getCasket()) || player.hasItem(ClueLevel.MEDIUM.getCasket()) || player.hasItem(ClueLevel.HARD.getCasket())) {
			return true;
		}
		for (int id : IDS) {
			if (player.getInventory().contains(id, 1) || player.getBank().contains(id, 1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Increments a trail stage.
	 */
	public void incrementStage() {
		trailStage += 1;
	}

	/**
	 * Checks if the trail is completed.
	 * @return {@code True} if so.
	 */
	public boolean isCompleted() {
		return trailStage != 0 && trailLength != 0 && trailStage >= trailLength;
	}

	/**
	 * Checks if the player has an active trail.
	 * @return the trail.
	 */
	public boolean hasTrail() {
		return clueId != 0 && level != null & trailLength != 0 && trailStage != 0;
	}

	/**
	 * Incremented the clues.
	 */
	public void incrementClues(ClueLevel level) {
		completedClues[level.ordinal()]++;
	}

	/**
	 * Gets the amount of completed clues for the level.
	 * @param level the level.
	 * @return the amount.
	 */
	public int getCompletedClues(ClueLevel level) {
		return completedClues[level.ordinal()];
	}

	/**
	 * Gets the completed clue amount.
	 * @return the completed clues.
	 */
	public int[] getCompletedClues() {
		return completedClues;
	}

	/**
	 * Gets the bclueId.
	 * @return the clueId
	 */
	public int getClueId() {
		return clueId;
	}

	/**
	 * Sets the baclueId.
	 * @param clueId the clueId to set.
	 */
	public void setClueId(int clueId) {
		this.clueId = clueId;
	}

	/**
	 * Gets the blevel.
	 * @return the level
	 */
	public ClueLevel getLevel() {
		return level;
	}

	/**
	 * Sets the balevel.
	 * @param level the level to set.
	 */
	public void setLevel(ClueLevel level) {
		this.level = level;
	}

	/**
	 * Gets the bplayer.
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the btrailLength.
	 * @return the trailLength
	 */
	public int getTrailLength() {
		return trailLength;
	}

	/**
	 * Sets the batrailLength.
	 * @param trailLength the trailLength to set.
	 */
	public void setTrailLength(int trailLength) {
		this.trailLength = trailLength;
	}

	/**
	 * Gets the btrailStage.
	 * @return the trailStage
	 */
	public int getTrailStage() {
		return trailStage;
	}

	/**
	 * Sets the batrailStage.
	 * @param trailStage the trailStage to set.
	 */
	public void setTrailStage(int trailStage) {
		this.trailStage = trailStage;
	}

}
