package plugin.quest.learningtheropes;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.content.dialogue.FacialExpression;
import org.crandor.game.content.global.tutorial.TutorialStage;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;

/**
 * @author Ethan Kyle Millard <skype:pumpklins>
 * @since Mon, October 08, 2018 @ 4:19 PM
 */
@InitializablePlugin
public class SirVantDialogue extends DialoguePlugin {

    /**
     * Default Constructor
     */
    public SirVantDialogue() {
        /**
         * Leave this empty
         */
    }

    public SirVantDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new SirVantDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc(FacialExpression.HAPPY, "My Word! You scared me there, friend. I have no idea", "where you came from, but you have fantastic timing.", "You see, I have come across a dragon.");
        setStage(1);
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        switch (stage) {
            case 1:
                player("A dragon?");
                next();
                break;
            case 2:
                npc("Oh yes. It's not usually a problem. After all, I am a", "White Knight! I just haven't really had time to prepare:", "food, runes, legions of other White Knights - those sorts", "of things.");
                next();
                break;
            case 3:
                player ("You didn't prepare for facing a dragon?");
                next();
                break;
            case 4:
                npc ("It's not as of I woke with a dragon-slaying urge! I", "was on my way through Lumbridge when it started", "attacking. It's a rather larger, three-headed variant. Very", "rare.");
                next();
                break;
            case 5:
                player ("So, what can I do?");
                 next();
                 break;
            case 6:
                npc ("You can keep back - it's returning!");
                next();
                break;
            case 7:
                end();
                TutorialStage.load(player, 2, false);
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] {7938};
    }
}
