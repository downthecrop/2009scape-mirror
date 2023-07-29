package core.game.bots;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Script {

    public ScriptAPI scriptAPI;
    public ArrayList<Item> inventory = new ArrayList<>(20);
    public ArrayList<Item> equipment = new ArrayList<>(20);
    public Map<Integer, Integer> skills = new HashMap<>();
    public ArrayList<String> quests = new ArrayList<>(20);


    public Player bot;

    public boolean running = true;
    public boolean endDialogue = true;

    public void init(boolean isPlayer)
    {
        //bot.init();
        scriptAPI = new ScriptAPI(bot);

        if(!isPlayer) {
            // Skills and quests need to be set before equipment in case equipment has level or quest requirements
            for (Map.Entry<Integer, Integer> skill : skills.entrySet()) {
                setLevel(skill.getKey(), skill.getValue());
            }
            for (String quest : quests) {
                bot.getQuestRepository().setStage(bot.getQuestRepository().getQuest(quest), 100);
            }
            for (Item i : equipment) {
                bot.getEquipment().add(i, true, false);
            }
            bot.getInventory().clear();
            for (Item i : inventory) {
                bot.getInventory().add(i);
            }
        }
    }

    @Override
    public String toString() {
        return bot.getName() + " is a " + this.getClass().getSimpleName() + " at location " + bot.getLocation().toString() + " Current pulse: " + bot.getPulseManager().getCurrent();
    }

    public abstract void tick();

    public void setLevel(int skill, int level) {
        bot.getSkills().setLevel(skill, level);
        bot.getSkills().setStaticLevel(skill, level);
        bot.getSkills().updateCombatLevel();
        bot.getAppearance().sync();
    }

    // This does not get called and all implementations should be removed
    public abstract Script newInstance();
}
