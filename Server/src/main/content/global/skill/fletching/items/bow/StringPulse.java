package content.global.skill.fletching.items.bow;

import core.game.node.entity.player.info.LogType;
import core.game.node.entity.player.info.PlayerMonitor;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.world.map.zone.ZoneBorders;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import content.global.skill.fletching.Fletching;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

import static core.api.ContentAPIKt.amountInInventory;

/**
 * Represents the skill pulse of stringing.
 *
 * @author Ceikry
 */
public class StringPulse extends SkillPulse<Item> {

    /**
     * Represents the string bow.
     */
    private final Fletching.String bow;

    /**
     * The amount.
     */
    private int amount;

    private int initialAmount;
    private int processedAmount;

    /**
     * Constructs a new {@code StringbowPlugin.java} {@code Object}.
     *
     * @param player the player.
     * @param node   the node.
     */
    public StringPulse(Player player, Item node, final Fletching.String bow, int amount) {
        super(player, node);
        this.bow = bow;
        this.amount = amount;
        this.initialAmount = amountInInventory(player, node.getId());
        this.processedAmount = 0;
    }

    @Override
    public boolean checkRequirements() {
        if (getDelay() == 1) {
            setDelay(2);
        }
        if (player.getSkills().getLevel(Skills.FLETCHING) < bow.level) {
            player.getDialogueInterpreter().sendDialogue("You need a fletching level of " + bow.level + " to string this bow.");
            return false;
        }
        if (!player.getInventory().containsItem(new Item(bow.unfinished))) {
            return false;
        }
        if (!player.getInventory().containsItem(new Item(bow.string))) {
            player.getDialogueInterpreter().sendDialogue("You seem to have run out of bow strings.");
            return false;
        }
        animate();
        return true;
    }

    @Override
    public void animate() {
        player.animate(bow.animation);
    }

    @Override
    public boolean reward() {
        if (player.getInventory().remove(new Item(bow.unfinished), new Item(bow.string))) {
            player.getInventory().add(new Item(bow.product));
            player.getSkills().addExperience(Skills.FLETCHING, bow.experience, true);
            player.getPacketDispatch().sendMessage("You add a string to the bow.");
            processedAmount++;
            if (processedAmount > initialAmount) {
                PlayerMonitor.log(player, LogType.DUPE_ALERT, "fletched item (" + player.getName() + ", " + bow.unfinished + "): initialAmount " + initialAmount + ", processedAmount " + processedAmount);
            }

            if (bow == Fletching.String.MAGIC_SHORTBOW
                    && (new ZoneBorders(2721, 3489, 2724, 3493, 0).insideBorder(player)
                    || new ZoneBorders(2727, 3487, 2730, 3490, 0).insideBorder(player))
                    && player.getAttribute("diary:seers:fletch-magic-short-bow", false)) {
                player.getAchievementDiaryManager().finishTask(player, DiaryType.SEERS_VILLAGE, 2, 2);
            }
        }
        if (!player.getInventory().containsItem(new Item(bow.string)) || !player.getInventory().containsItem(new Item(bow.unfinished))) {
            return true;
        }
        amount--;
        return amount == 0;
    }

    @Override
    public void message(int type) {
    }

}