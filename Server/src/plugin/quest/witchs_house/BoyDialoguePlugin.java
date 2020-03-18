package plugin.quest.witchs_house;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.quest.Quest;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;

/**
 * Created for 2009Scape
 * User: Ethan Kyle Millard
 * Date: March 15, 2020
 * Time: 11:49 AM
 */
@InitializablePlugin
public class BoyDialoguePlugin extends DialoguePlugin {

    public BoyDialoguePlugin() {
    }

    public BoyDialoguePlugin(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new BoyDialoguePlugin(player);
    }

    @Override
    public boolean open(Object... args) {
        final Quest quest = player.getQuestRepository().getQuest("Witch's House");
        if (!quest.isStarted(player)) {
            player("Hello young man.");
            setStage(1);
        }
        npc("Have you gotten my ball back yet?");
        setStage(11);
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        final Quest quest = player.getQuestRepository().getQuest("Witch's House");
        switch(stage) {
            case -1:
                end();
                break;
            case 1:
                sendDialogue("The boy sobs.");
                next();
                break;
            case 2:
                options("What's the matter?", "Well if you're not going to answer then I'll go.");
                next();
                break;
            case 3:
                switch(buttonId) {
                    case 1:
                        player("What's the matter?");
                        setStage(5);
                        break;
                    case 2:
                        player("Well if you're not going to answer then I'll go.");
                        next();
                        break;
                }
                break;
            case 4:
                sendDialogue("The boy sniffs slightly.");
                finish();
                break;
            case 5:
                npc("I've kicked my ball over that hedge, into that garden!", "The old lady who lives there is scary... She's locked the","ball in her wooden shed! Can you get my ball back for", "me please?");
                next();
                break;
            case 6:
                options("Ok, I'll see what I can do.", "Get it back yourself.");
                next();
                break;
            case 7:
                switch(buttonId) {
                    case 1:
                        player("Ok, I'll see what I can do.");
                        setStage(10);
                        break;
                    case 2:
                        player("Get it back yourself.");
                        next();
                        break;
                }
                break;
            case 8:
                npc("You're a meany.");
                next();
                break;
            case 9:
                sendDialogue("The boy starts crying again.");
                finish();
                break;
            case 10:
                npc("Thanks mister!");
                finish();
                quest.start(player);
                break;
            case 11:
                if (!player.getInventory().containsItem(new Item(2407))) {
                    player("Not yet.");
                    next();
                } else {
                    player("Yes I have it here.");
                    finish();//TODO
                }
                break;
            case 12:
                npc("Well it's in the shed in that garden.");
                finish();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] {895};
    }
}
