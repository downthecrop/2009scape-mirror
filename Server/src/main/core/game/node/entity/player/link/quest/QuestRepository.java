package core.game.node.entity.player.link.quest;

import content.data.Quests;
import core.game.node.entity.player.Player;

import core.tools.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;

import static core.api.ContentAPIKt.log;
import static core.api.ContentAPIKt.*;


/**
 * Manages the systems/players quest repository.
 *
 * @author Vexia
 */
public final class QuestRepository {

    /**
     * The static mapping of instanced quests.
     */
    private static final Map<Quests, Quest> QUESTS = new TreeMap<>();

    /**
     * The mapping of quest indexes with related stages.
     */
    private final Map<Integer, Integer> quests = new HashMap<>();

    /**
     * The player instance for this manager.
     */
    private final Player player;

    /**
     * The current syncronized accumulated quest points.
     */
    private int points;

    /**
     * Constructs a new {@code QuestRepository} {@code Object}.
     *
     * @param player the player.
     */
    public QuestRepository(final Player player) {
        this.player = player;
        for (Quest quest : QUESTS.values()) {
            quests.put(quest.getIndex(), 0);
        }
    }

    public void parse(JSONObject questData){
        points = Integer.parseInt( questData.get("points").toString());
        JSONArray questArray = (JSONArray) questData.get("questStages");
        questArray.forEach(quest -> {
            JSONObject q = (JSONObject) quest;
            quests.put(Integer.parseInt( q.get("questId").toString()),Integer.parseInt(q.get("questStage").toString()));
        });
        syncPoints();
    }

    /**
     * Synchronizes the quest tab.
     *
     * @param player The player.
     */
    public void syncronizeTab(Player player) {
        setVarp(player, 101, points);
        int[] config = null;
        for(Quest quest : QUESTS.values()){
            config = quest.getConfig(player,getStage(quest));

            // {questVarpId, questVarbitId, valueToSet}
            if (config.length == 3) {
                // This is to set quests with VARPBIT, ignoring VARP value
                setVarbit(player, config[1], config[2]);
            } else {
                // This is the original VARP quests
                // {questVarpId, valueToSet}
                setVarp(player, config[0], config[1]);
            }

            quest.updateVarps(player);
        }
    }

    /**
     * Sets the stage of a quest.
     *
     * @param quest The quest.
     * @param stage The stage.
     */
    public void setStage(Quest quest, int stage) {
        int oldStage = getStage(quest);
        if(oldStage < stage) {
            quests.put(quest.getIndex(), stage);
        } else {
            log(this.getClass(), Log.WARN,  String.format("Nonmonotonic QuestRepository.setStage call for player \"%s\", quest \"%s\", old stage %d, new stage %d", player.getName(), quest.getQuest(), oldStage, stage));
        }
    }

    /**
     * Sets the stage of a quest, permitting non-monotonic updates.
     *
     * @param quest The quest.
     * @param stage The stage.
     */
    public void setStageNonmonotonic(Quest quest, int stage) {
        quests.put(quest.getIndex(), stage);
    }

    /**
     * Increments the obtained points by the value.
     *
     * @param value the value.
     */
    public void incrementPoints(int value) {
        points += value;
    }

    /**
     * Decrease the points by the value.
     *
     * @param value the value.
     */
    public void dockPoints(int value) { points -= value; }

    /**
     * Syncronizes the quest points.
     */
    public void syncPoints() {
        int points = 0;
        for (Quest quest : QUESTS.values()) {
            if (getStage(quest) >= 100) {
                points += quest.getQuestPoints();
            }
        }
        this.points = points;
    }

    /**
     * Gets the available quest points.
     *
     * @return The availble quest points.
     */
    public int getAvailablePoints() {
        int points = 0;
        for (Quest quest : QUESTS.values()) {
            points += quest.getQuestPoints();
        }
        return points;
    }

    /**
     * Gets the quest for the button id.
     *
     * @param buttonId The button id.
     * @return The quest.
     */
    public Quest forButtonId(int buttonId) {
        for (Quest q : QUESTS.values()) {
            if (q.getButtonId() == buttonId) {
                return q;
            }
        }
        return null;
    }

    /**
     * Gets the quest for the index.
     *
     * @param index The index.
     * @return The quest.
     */
    public Quest forIndex(int index) {
        for (Quest q : QUESTS.values()) {
            if (q.getIndex() == index) {
                return q;
            }
        }
        return null;
    }

    /**
     * Checks if all quests are completed.
     *
     * @return {@code True} if so.
     */
    public boolean hasCompletedAll() {
        return getPoints() >= getAvailablePoints();
    }

    /**
     * Checks if the quest is complete.
     *
     * @param quest The quest.
     * @return {@code True} if so.
     */
    public boolean isComplete(Quests quest) {
        Quest theQuest = getQuest(quest);
        if (theQuest == null) {
            log(this.getClass(), Log.ERR,  "Error can't check if quest is complete for " + quest);
            return false;
        }
        return theQuest.getStage(player) >= 100;
    }

    /**
     * Checks if the quest has at least started.
     *
     * @param quest The quest by id.
     * @return {@code True} if so.
     */
    public boolean hasStarted(Quests quest) {
        Quest theQuest = getQuest(quest);
        if (quest == null) {
            log(this.getClass(), Log.ERR,  "Error can't check if quest is complete for " + quest);
            return false;
        }
        return theQuest.getStage(player) > 0;
    }


    /**
     * Gets the stage of quest by id.
     * @param quest The quest.
     * @return The stage.
     */
    public int getStage(Quests quest) {
        var theQuest = QUESTS.get(quest);
        if (theQuest == null) {
            return 0;
        }
        return getStage(theQuest);
    }

    /**
     * Gets the stage of a quest.
     * @param quest The quest.
     * @return The stage.
     */
    public int getStage(Quest quest) {
        return quests.get(quest.getIndex());
    }

    /**
     * Gets the quest by id.
     * @param quest The quest.
     * @return The quest.
     */
    public Quest getQuest(Quests quest) {
        return QUESTS.get(quest);
    }

    /**
     * Gets the points.
     *
     * @return the points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Registers the quest to the repository.
     *
     * @param quest The quest.
     */
    public static void register(Quest quest) {
        QUESTS.put(quest.getQuest(), quest);
    }

    /**
     * Gets the quest repository.
     *
     * @return the quests.
     */
    public static Map<Quests, Quest> getQuests() {
        return QUESTS;
    }

    public Map<Integer, Integer> getQuestList() {return quests;}

}
